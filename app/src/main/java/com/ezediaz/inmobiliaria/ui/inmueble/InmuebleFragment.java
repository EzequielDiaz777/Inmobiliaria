package com.ezediaz.inmobiliaria.ui.inmueble;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

//import com.ezediaz.inmobiliaria.;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.model.Tipo;
import com.ezediaz.inmobiliaria.model.Uso;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class InmuebleFragment extends Fragment {
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private FragmentInmuebleBinding binding;
    private InmuebleFragmentViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = new ViewModelProvider(this).get(InmuebleFragmentViewModel.class);
        vm.getMInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                binding.etCodigo.setText(String.valueOf(inmueble.getId()));
                binding.etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                binding.etDireccion.setText(inmueble.getDireccion());
                binding.etPrecio.setText(String.valueOf(inmueble.getPrecio()));
                binding.cbDisponible.setChecked(inmueble.isEstado());
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.icon_inmuebles) // Imagen de marcador de posición
                        .error(R.drawable.icon_logout); // Imagen de error
                // Utiliza Glide para cargar y mostrar la imagen
                Glide.with(getContext())
                        .load(ApiClient.URL+inmueble.getImagenUrl()) // Especifica la URL de la imagen
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Carga la caché para obtener la imagen
                        .apply(options)
                        .into(binding.ivFoto); // Especifica el ImageView donde se mostrará la imagen
            }
        });
        vm.getMDisponible().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean disponible) {
                binding.cbDisponible.setChecked(disponible);
            }
        });
        vm.getMTipo().observe(getViewLifecycleOwner(), new Observer<Tipo>() {
            @Override
            public void onChanged(Tipo tipo) {
                ArrayAdapter<Tipo> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
                binding.spnTipo.setAdapter(tipoAdapter);
                tipoAdapter.add(tipo);
                binding.spnTipo.setSelection(tipoAdapter.getPosition(tipo));
            }
        });
        vm.getMUso().observe(getViewLifecycleOwner(), new Observer<Uso>() {
            @Override
            public void onChanged(Uso uso) {
                ArrayAdapter<Uso> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
                binding.spnUso.setAdapter(tipoAdapter);
                tipoAdapter.add(uso);
                binding.spnUso.setSelection(tipoAdapter.getPosition(uso));
            }
        });
        vm.getMListaTipo().observe(getViewLifecycleOwner(), new Observer<List<Tipo>>() {
            @Override
            public void onChanged(List<Tipo> tipos) {
                ArrayAdapter<Tipo> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
                binding.spnTipo.setAdapter(tipoAdapter);
                tipoAdapter.addAll(tipos);
            }
        });
        vm.getMListaUso().observe(getViewLifecycleOwner(), new Observer<List<Uso>>() {
            @Override
            public void onChanged(List<Uso> usos) {
                ArrayAdapter<Uso> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
                binding.spnUso.setAdapter(tipoAdapter);
                tipoAdapter.addAll(usos);
            }
        });
        vm.getMTextos().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etDireccion.setText(aBoolean ? "" : binding.etDireccion.getText().toString());
                binding.etAmbientes.setText(aBoolean ? "" : binding.etAmbientes.getText().toString());
                binding.etPrecio.setText(aBoolean ? "" : binding.etPrecio.getText().toString());
            }
        });
        vm.getMHabilitar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.cbDisponible.setEnabled(!aBoolean);
                binding.cbDisponible.setChecked(!aBoolean);
                binding.etAmbientes.setEnabled(aBoolean);
                binding.etDireccion.setEnabled(aBoolean);
                binding.etPrecio.setEnabled(aBoolean);
                binding.btnAgregarInmueble.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                binding.btnAgregarFoto.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
        binding.cbDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.cambiarDisponibilidad(binding.cbDisponible.isChecked(), Integer.valueOf(binding.etCodigo.getText().toString()));
            }
        });
        binding.btnAgregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        binding.btnAgregarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inmueble i = new Inmueble();
                i.setEstado(false);
                i.setTipoId(binding.spnTipo.getSelectedItemPosition() + 1);
                i.setUsoId(binding.spnUso.getSelectedItemPosition() + 1);
                vm.agregarInmueble(i, binding.etAmbientes.getText().toString(), binding.etDireccion.getText().toString(), binding.etPrecio.getText().toString(), getView());
            }
        });
        vm.cargarInmueble(getArguments());
        return root;
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        return "data:image/png;base64," + Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
    }

    private Bitmap convertImageUriToBase64(Uri uri) {
        try {
            return getBitmapFromUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);
        } else {
            dispatchSelectPictureIntent();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = null;
            if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                selectedImageUri = data.getData();
            }
            if (selectedImageUri != null) {
                binding.ivFoto.setImageURI(selectedImageUri);
                //vm.(selectedImageUri);
            }
        }
    }

    private void dispatchSelectPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

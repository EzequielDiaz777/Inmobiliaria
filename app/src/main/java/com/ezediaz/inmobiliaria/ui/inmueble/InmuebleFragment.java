package com.ezediaz.inmobiliaria.ui.inmueble;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.model.Tipo;
import com.ezediaz.inmobiliaria.model.Uso;
import com.ezediaz.inmobiliaria.request.ApiClient;
import java.util.List;

public class InmuebleFragment extends Fragment {
    private Intent intent;
    private Uri photoURI;
    private ActivityResultLauncher<Intent> arl;
    private FragmentInmuebleBinding binding;
    private InmuebleFragmentViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = new ViewModelProvider(this).get(InmuebleFragmentViewModel.class);
        abrirGaleria();
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
                        .load(ApiClient.URL + inmueble.getImagenUrl()) // Especifica la URL de la imagen
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
                ArrayAdapter<Tipo> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item);
                binding.spnTipo.setAdapter(tipoAdapter);
                tipoAdapter.add(tipo);
                binding.spnTipo.setSelection(tipoAdapter.getPosition(tipo));
            }
        });
        vm.getMUso().observe(getViewLifecycleOwner(), new Observer<Uso>() {
            @Override
            public void onChanged(Uso uso) {
                ArrayAdapter<Uso> usoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
                binding.spnUso.setAdapter(usoAdapter);
                usoAdapter.add(uso);
                binding.spnUso.setSelection(usoAdapter.getPosition(uso));
            }
        });
        vm.getMListaTipo().observe(getViewLifecycleOwner(), new Observer<List<Tipo>>() {
            @Override
            public void onChanged(List<Tipo> tipos) {
                ArrayAdapter<Tipo> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, tipos);
                binding.spnTipo.setAdapter(tipoAdapter);
            }
        });
        vm.getMListaUso().observe(getViewLifecycleOwner(), new Observer<List<Uso>>() {
            @Override
            public void onChanged(List<Uso> usos) {
                ArrayAdapter<Uso> usoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, usos);
                binding.spnUso.setAdapter(usoAdapter);
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
                binding.etAmbientes.setEnabled(aBoolean);
                binding.etDireccion.setEnabled(aBoolean);
                binding.etPrecio.setEnabled(aBoolean);
                binding.btnAgregarInmueble.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                binding.btnAgregarFoto.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
        vm.getMUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.icon_inmuebles) // Imagen de marcador de posición
                        .error(R.drawable.icon_logout); // Imagen de error
                // Utiliza Glide para cargar y mostrar la imagen
                Glide.with(getContext())
                        .load(uri) // Especifica la URL de la imagen
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Carga la caché para obtener la imagen
                        .apply(options)
                        .into(binding.ivFoto); // Especifica el ImageView donde se mostrará la imagen
                photoURI = uri;
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
                arl.launch(intent);
            }
        });
        binding.btnAgregarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inmueble inmueble = new Inmueble();
                inmueble.setTipoId(binding.spnTipo.getId());
                inmueble.setUsoId(binding.spnUso.getId());
                Log.d("Id de tipo", String.valueOf(inmueble.getTipoId()));
                Log.d("Id de uso", String.valueOf(inmueble.getUsoId()));
                //vm.agregarInmueble(inmueble, binding.etAmbientes.getText().toString(), binding.etDireccion.getText().toString(), binding.etPrecio.getText().toString(), photoURI, binding.getRoot());
            }
        });
        vm.cargarInmueble(getArguments());
        return root;
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult ar) {
                vm.cargarFoto(ar);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
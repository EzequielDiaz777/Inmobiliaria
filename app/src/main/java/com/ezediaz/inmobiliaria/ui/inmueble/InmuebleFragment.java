package com.ezediaz.inmobiliaria.ui.inmueble;
import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
    private ActivityResultLauncher<Intent> arl;
    private Uri photoURI;
    private FragmentInmuebleBinding binding;
    private InmuebleFragmentViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = new ViewModelProvider(this).get(InmuebleFragmentViewModel.class);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        abrirGaleria();
        vm.getMCabecera().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String cabecera) {
                Log.d("cabecera", "NavController.getGraph.findNode.getLabel: " + navController.getGraph().findNode(R.id.nav_inmueble).getLabel());
                navController.getGraph().findNode(R.id.nav_inmueble).setLabel(cabecera);
                Log.d("cabecera", "NavController.getGraph.findNode.getLabel: " + navController.getGraph().findNode(R.id.nav_inmueble).getLabel());
            }
        });
        vm.getMInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                binding.etCodigo.setText(String.valueOf(inmueble.getId()));
                binding.etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                binding.etDireccion.setText(inmueble.getDireccion());
                binding.etPrecio.setText((Utils.formatPrice(inmueble.getPrecio())));
                binding.cbDisponible.setChecked(inmueble.isEstado());
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.cargando_imagen) // Imagen de marcador de posición
                        .error(R.drawable.sin_imagen); // Imagen de error
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
                binding.cbDisponible.setText(disponible ? "Disponible" : "No disponible");
            }
        });
        vm.getMTipo().observe(getViewLifecycleOwner(), new Observer<Tipo>() {
            @Override
            public void onChanged(Tipo tipo) {
                ArrayAdapter<Tipo> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item);
                binding.spnTipo.setAdapter(tipoAdapter);
                tipoAdapter.add(tipo);
                binding.spnTipo.setSelection(tipoAdapter.getPosition(tipo));
                binding.spnTipo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Devuelve true para consumir el evento y evitar que se abra el menú desplegable
                        return true;
                    }
                });
            }
        });
        vm.getMUso().observe(getViewLifecycleOwner(), new Observer<Uso>() {
            @Override
            public void onChanged(Uso uso) {
                ArrayAdapter<Uso> usoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
                binding.spnUso.setAdapter(usoAdapter);
                usoAdapter.add(uso);
                binding.spnUso.setSelection(usoAdapter.getPosition(uso));
                binding.spnUso.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Devuelve true para consumir el evento y evitar que se abra el menú desplegable
                        return true;
                    }
                });
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
                binding.etAmbientes.setFocusable(aBoolean);
                binding.etDireccion.setFocusable(aBoolean);
                binding.etPrecio.setFocusable(aBoolean);
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
                inmueble.setTipoId(binding.spnTipo.getSelectedItemPosition() + 1);
                inmueble.setUsoId(binding.spnUso.getSelectedItemPosition() + 1);
                Log.d("Id de tipo", String.valueOf(inmueble.getTipoId()));
                Log.d("Id de uso", String.valueOf(inmueble.getUsoId()));
                vm.agregarInmueble(inmueble, binding.etAmbientes.getText().toString(), binding.etDireccion.getText().toString(), binding.etPrecio.getText().toString(), photoURI, binding.getRoot());
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
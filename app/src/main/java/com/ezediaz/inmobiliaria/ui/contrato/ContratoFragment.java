package com.ezediaz.inmobiliaria.ui.contrato;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.compose.ui.text.intl.Locale;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.databinding.FragmentContratoBinding;
import com.ezediaz.inmobiliaria.model.Contrato;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.model.Tipo;
import com.ezediaz.inmobiliaria.model.Uso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ContratoFragment extends Fragment {
    private FragmentContratoBinding binding;
    private ContratoFragmentViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContratoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = new ViewModelProvider(this).get(ContratoFragmentViewModel.class);
        vm.getMContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                binding.etCodigoContrato.setText(String.valueOf(contrato.getId()));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy");
                LocalDate fechaLD = LocalDate.parse(contrato.getFechaInicio(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String fechaS = fechaLD.format(formatter);
                binding.etFechaInicio.setText(fechaS);
                fechaLD = LocalDate.parse(contrato.getFechaFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                fechaS = fechaLD.format(formatter);
                binding.etFechaFin.setText(fechaS);
                binding.etMonto.setText(String.valueOf(contrato.getPrecio()));
                binding.etInquilino.setText(contrato.getInquilino().toString());
                binding.etInmueble.setText(contrato.getInmueble().toString());
            }
        });
        binding.btnVerInquilino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.cargarInquilino(v);
            }
        });
        binding.btnVerPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.cargarPagos(v);
            }
        });
        /*binding.btnVerPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("pagos", );
                Navigation.findNavController(v).navigate(R.id.nav_contrato, bundle);
            }
        });
        binding.btnVerPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inmueble contrato = new Inmueble();
                contrato.setTipoId(binding.spnTipo.getSelectedItemPosition() + 1);
                contrato.setUsoId(binding.spnUso.getSelectedItemPosition() + 1);
                vm.agregarInmueble(contrato, binding.etAmbientes.getText().toString(), binding.etDireccion.getText().toString(), binding.etPrecio.getText().toString(), photoURI, binding.getRoot());
            }
        });*/
        vm.cargarContratos(getArguments());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
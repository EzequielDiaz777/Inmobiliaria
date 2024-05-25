package com.ezediaz.inmobiliaria.ui.contrato;
import com.ezediaz.inmobiliaria.ui.inmueble.Utils;
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
                binding.etCodigoContrato.setFocusable(false);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy");
                LocalDate fechaLD = LocalDate.parse(contrato.getFechaInicio(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String fechaS = fechaLD.format(formatter);
                binding.etFechaInicio.setText(fechaS);
                fechaLD = LocalDate.parse(contrato.getFechaFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                fechaS = fechaLD.format(formatter);
                binding.etFechaFin.setText(fechaS);
                binding.etFechaFin.setFocusable(false);
                binding.etMonto.setText(Utils.formatPrice(contrato.getPrecio()));
                binding.etMonto.setFocusable(false);
                binding.etInquilino.setText(contrato.getInquilino().toString());
                binding.etInquilino.setFocusable(false);
                binding.etInmueble.setText(contrato.getInmueble().toString());
                binding.etInmueble.setFocusable(false);
                binding.etFechaInicio.setFocusable(false);
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
        vm.cargarContratos(getArguments());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.ezediaz.inmobiliaria.ui.inmueble;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ezediaz.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.model.Tipo;

import java.util.List;

public class InmuebleFragment extends Fragment {
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
                /*Spinner spinnerUso = binding.spnUso;
                spinnerUso.setEnabled(false);
                ArrayAdapter<String> usoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
                spinnerUso.setAdapter(usoAdapter);
                String uso = inmueble.getUso().getNombre();
                usoAdapter.add(uso);
                spinnerUso.setSelection(usoAdapter.getPosition(uso));
                Spinner spinnerTipo = binding.spnTipo;
                spinnerTipo.setEnabled(false);
                ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
                spinnerTipo.setAdapter(tipoAdapter);
                String tipo = inmueble.getTipo().getNombre();
                tipoAdapter.add(tipo);
                spinnerTipo.setSelection(tipoAdapter.getPosition(tipo));*/
                binding.cbDisponible.setChecked(inmueble.isEstado());
            }
        });
        vm.getMDisponible().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean disponible) {
                binding.cbDisponible.setChecked(disponible);
            }
        });
        vm.getMTipo().observe(getViewLifecycleOwner(), new Observer<List<Tipo>>() {
            @Override
            public void onChanged(List<Tipo> tipos) {
                Spinner spinnerTipo = binding.spnTipo;
                spinnerTipo.setEnabled(false);
                ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
                spinnerTipo.setAdapter(tipoAdapter);
                //String tipo = inmueble.getTipo().getNombre();
                //tipoAdapter.add(tipo);
                //spinnerTipo.setSelection(tipoAdapter.getPosition(tipo));
            }
        });
        vm.getMGuardar().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btnAgregarInmueble.setText(s);
            }
        });
        binding.cbDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.cambiarDisponibilidad(binding.cbDisponible.isChecked(), Integer.valueOf(binding.etCodigo.getText().toString()));
            }
        });
        vm.getMHabilitar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etCodigo.setText("");
                binding.etDireccion.setText("");
                binding.etAmbientes.setText("");
                binding.etPrecio.setText("");
                binding.cbDisponible.setEnabled(!aBoolean);
                binding.cbDisponible.setChecked(!aBoolean);
                binding.etAmbientes.setEnabled(aBoolean);
                binding.etDireccion.setEnabled(aBoolean);
                binding.etPrecio.setEnabled(aBoolean);
                binding.spnTipo.setEnabled(aBoolean);
                binding.spnUso.setEnabled(aBoolean);
            }
        });
        binding.btnAgregarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inmueble i = new Inmueble();
                i.setEstado(false);
                Log.d("salida", "Tipo: " + binding.spnTipo.getSelectedItem() + 1);
                int posicionTipo = binding.spnTipo.getSelectedItemPosition()+1;
                int posicionUso = binding.spnUso.getSelectedItemPosition()+1;
                Log.d("salida", "Posición: " + posicionTipo);
                Log.d("salida", "Posición: " + posicionUso);
                i.setTipoId(posicionTipo);
                i.setUsoId(posicionUso);
                vm.agregarInmueble(i, binding.etAmbientes.getText().toString(), binding.etDireccion.getText().toString(), binding.etPrecio.getText().toString(), getView());
            }
        });
        vm.cargarInmueble(getArguments(), binding.spnTipo, binding.spnUso, binding.btnAgregarInmueble, binding.btnAgregarFoto);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

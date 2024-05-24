package com.ezediaz.inmobiliaria.ui.inquilino;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ezediaz.inmobiliaria.databinding.FragmentInquilinoBinding;
import com.ezediaz.inmobiliaria.model.Inquilino;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InquilinoFragment extends Fragment {
    private FragmentInquilinoBinding binding;
    private InquilinoFragmentViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = new ViewModelProvider(this).get(InquilinoFragmentViewModel.class);
        vm.getMInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
            @Override
            public void onChanged(Inquilino inquilino) {
                binding.etNombreInquilino.setText(inquilino.getNombre());
                binding.etApellidoInquilino.setText(inquilino.getApellido());
                binding.etDNIInquilino.setText(inquilino.getDni());
                binding.etEmailInquilino.setText(inquilino.getEmail());
                binding.etTelefonoInquilino.setText(inquilino.getTelefono());
            }
        });
        vm.cargarInquilino(getArguments());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
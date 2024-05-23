package com.ezediaz.inmobiliaria.ui.perfil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ezediaz.inmobiliaria.databinding.FragmentCambiarPasswordBinding;

public class CambiarPasswordFragment extends Fragment {
    private FragmentCambiarPasswordBinding binding;
    private CambiarPasswordFragmentViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CambiarPasswordFragmentViewModel.class);
        binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnGuardarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.cambiarPassword(binding.etViejaPassword.getText().toString(), binding.etNuevaPassword.getText().toString(), binding.etRepetirNuevaPassword.getText().toString(), getView());
            }
        });
        return root;
    }

}
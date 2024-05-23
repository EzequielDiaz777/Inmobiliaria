package com.ezediaz.inmobiliaria.ui.contrato;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ezediaz.inmobiliaria.databinding.FragmentContratosBinding;
import com.ezediaz.inmobiliaria.model.Contrato;
import java.util.List;

public class ContratosFragment extends Fragment {
    private FragmentContratosBinding binding;
    private ContratosFragmentViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ContratosFragmentViewModel.class);
        vm.getMContratos().observe(getViewLifecycleOwner(), new Observer<List<Contrato>>() {
            @Override
            public void onChanged(List<Contrato> contratos) {
                ContratoAdapter contratoAdapter = new ContratoAdapter(contratos, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(container.getContext(), 1, GridLayoutManager.VERTICAL, false);
                RecyclerView rv = binding.listaDeContratos;
                rv.setLayoutManager(glm);
                rv.setAdapter(contratoAdapter);
            }
        });
        vm.cargarContratos();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

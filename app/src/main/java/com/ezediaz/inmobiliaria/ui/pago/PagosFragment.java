package com.ezediaz.inmobiliaria.ui.pago;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.databinding.FragmentPagosBinding;
import com.ezediaz.inmobiliaria.model.Contrato;
import com.ezediaz.inmobiliaria.model.Pago;
import com.ezediaz.inmobiliaria.ui.inmueble.RecyclerItemClickListener;
import com.ezediaz.inmobiliaria.ui.inmueble.SpaceItemDecoration;

import java.util.List;

public class PagosFragment extends Fragment {
    private FragmentPagosBinding binding;
    private PagosFragmentViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PagosFragmentViewModel.class);
        vm.getMPagos().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> pagos) {
                PagoAdapter pagoAdapter = new PagoAdapter(pagos, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(container.getContext(), 1, GridLayoutManager.VERTICAL, false);
                RecyclerView rv = binding.listaDePagos;
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
                rv.setLayoutManager(glm);
                rv.setAdapter(pagoAdapter);
            }
        });
        vm.cargarPagos(getArguments());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

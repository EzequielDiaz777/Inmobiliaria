package com.ezediaz.inmobiliaria.ui.contrato;
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
import com.ezediaz.inmobiliaria.databinding.FragmentContratosBinding;
import com.ezediaz.inmobiliaria.model.Contrato;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.ui.inmueble.RecyclerItemClickListener;
import com.ezediaz.inmobiliaria.ui.inmueble.SpaceItemDecoration;

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
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
                rv.setLayoutManager(glm);
                rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Obtener el inmueble en la posición del clic
                        Contrato contratoSeleccionado = contratos.get(position);

                        // Crear un Bundle para pasar los datos al siguiente Fragmento
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("desdeVerMas", true); // Enviar el parámetro
                        bundle.putSerializable("contrato", contratoSeleccionado); // Enviar el inmueble seleccionado

                        // Navegar al siguiente Fragmento y pasar el Bundle como argumento
                        Navigation.findNavController(view).navigate(R.id.nav_contrato, bundle);
                    }
                }));
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

package com.ezediaz.inmobiliaria.ui.inmueble;
import android.content.Context;
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
import com.ezediaz.inmobiliaria.databinding.FragmentInmueblesBinding;
import com.ezediaz.inmobiliaria.model.Inmueble;
import java.util.List;

public class InmueblesFragment extends Fragment {
    private FragmentInmueblesBinding binding;
    private InmueblesFragmentViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(InmueblesFragmentViewModel.class);
        vm.getMInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmueble) {
                InmuebleAdapter inmuebleAdapter = new InmuebleAdapter(inmueble, getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(container.getContext(), 1, GridLayoutManager.VERTICAL, false);
                RecyclerView rv = binding.listaDeInmuebles;
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
                rv.setLayoutManager(glm);
                rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Obtener el inmueble en la posición del clic
                        Inmueble inmuebleSeleccionado = inmueble.get(position);

                        // Crear un Bundle para pasar los datos al siguiente Fragmento
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("desdeVerMas", true); // Enviar el parámetro
                        bundle.putSerializable("inmueble", inmuebleSeleccionado); // Enviar el inmueble seleccionado

                        // Navegar al siguiente Fragmento y pasar el Bundle como argumento
                        Navigation.findNavController(view).navigate(R.id.nav_inmueble, bundle);
                    }
                }));
                rv.setAdapter(inmuebleAdapter);
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_inmueble);
            }
        });
        vm.cargarInmuebles();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

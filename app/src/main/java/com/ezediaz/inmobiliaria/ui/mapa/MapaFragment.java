package com.ezediaz.inmobiliaria.ui.mapa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ezediaz.inmobiliaria.R;
import com.google.android.gms.maps.SupportMapFragment;

public class MapaFragment extends Fragment {
    private MapaFragmentViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        vm = new ViewModelProvider(this).get(MapaFragmentViewModel.class);
        vm.getMutableLeerMapa().observe(getViewLifecycleOwner(), new Observer<LeerMapa>() {
            @Override
            public void onChanged(LeerMapa leerMapa) {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.mapa);
                mapFragment.getMapAsync(leerMapa);
            }
        });
        vm.leerMapa();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
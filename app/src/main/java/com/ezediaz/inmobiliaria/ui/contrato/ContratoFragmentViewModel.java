package com.ezediaz.inmobiliaria.ui.contrato;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.model.Contrato;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.model.RealPathUtil;
import com.ezediaz.inmobiliaria.model.Tipo;
import com.ezediaz.inmobiliaria.model.Uso;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Contrato> mContrato;

    public ContratoFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getMContrato() {
        if (mContrato == null) {
            mContrato = new MutableLiveData<>();
        }
        return mContrato;
    }

    public void cargarContratos(Bundle arguments) {
        Contrato contrato = (Contrato) arguments.getSerializable("contrato");
        mContrato.setValue(contrato);
    }

    public void cargarInquilino(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("inquilino", mContrato.getValue().getInquilino());
        Navigation.findNavController(view).navigate(R.id.nav_inquilino, bundle);
    }

    public void cargarPagos(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("pagos", mContrato.getValue().getId());
        Navigation.findNavController(view).navigate(R.id.nav_pagos, bundle);
    }
}


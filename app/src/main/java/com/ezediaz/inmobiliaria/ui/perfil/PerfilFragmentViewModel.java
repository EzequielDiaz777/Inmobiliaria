package com.ezediaz.inmobiliaria.ui.perfil;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezediaz.inmobiliaria.LoginActivity;
import com.ezediaz.inmobiliaria.model.Propietario;
import com.ezediaz.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> mPropietario;
    private MutableLiveData<String> mGuardar;
    private MutableLiveData<Boolean> mHabilitar;
    private MutableLiveData<Integer> mVisible;

    public PerfilFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getMPropietario() {
        if(mPropietario == null){
            mPropietario = new MutableLiveData<>();
        }
        return mPropietario;
    }

    public LiveData<String> getMGuardar() {
        if(mGuardar == null){
            mGuardar = new MutableLiveData<>();
        }
        return mGuardar;
    }

    public LiveData<Boolean> getMHabilitar() {
        if(mHabilitar == null){
            mHabilitar = new MutableLiveData<>();
        }
        return mHabilitar;
    }

    public void editarDatos(String boton, Propietario propietario){
        if(boton.equals("Editar perfil")){
            mGuardar.setValue("Guardar perfil");
            mHabilitar.setValue(true);
        } else {
            mGuardar.setValue("Editar perfil");
            mHabilitar.setValue(false);
            String token = ApiClient.leerToken(getApplication());
            if (token != null) {
                ApiClient.MisEndPoints api = ApiClient.getEndPoints();
                Call<Propietario> call = api.modificarUsuario(token, propietario);
                call.enqueue(new Callback<Propietario>() {
                    @Override
                    public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                        if (response.isSuccessful()) {
                            mPropietario.postValue(response.body());
                            Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "Falla en la actualización", Toast.LENGTH_LONG).show();
                            Log.d("salida", response.message());
                            Log.d("salida", token);
                        }
                    }
                    @Override
                    public void onFailure(Call<Propietario> call, Throwable throwable) {
                        Log.d("salida", "Falla: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    public void miPerfil(){
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.MisEndPoints api = ApiClient.getEndPoints();
            Call<Propietario> call = api.miPerfil(token);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful()) {
                        mPropietario.postValue(response.body());
                    } else {
                        Log.d("salida", "Incorrecto");
                    }
                }
                @Override
                public void onFailure(Call<Propietario> call, Throwable throwable) {
                    Log.d("salida", "Falla: " + throwable.getMessage());
                }
            });
        } else {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
            getApplication().startActivity(intent);
        }
    }
}
package com.ezediaz.inmobiliaria.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezediaz.inmobiliaria.LoginActivity;
import com.ezediaz.inmobiliaria.LoginActivityViewModel;
import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.databinding.ActivityMainBinding;
import com.ezediaz.inmobiliaria.model.Propietario;
import com.ezediaz.inmobiliaria.request.ApiClient;
import com.google.android.gms.common.api.Api;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragmentViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
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
                Call<String> call = api.modificarUsuario(token, propietario);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_LONG).show();
                            sharedPreferences = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nombre completo", propietario.toString());
                            editor.putString("email", propietario.getEmail());
                            editor.apply();
                            ApiClient.guardarToken("Bearer " + response.body(), getApplication());
                            mPropietario.postValue(propietario);
                        } else {
                            Toast.makeText(getApplication(), "Falla en la actualización", Toast.LENGTH_LONG).show();
                            Log.d("salida", response.message());
                            Log.d("salida", token);
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
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
                        mHabilitar.setValue(false);
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
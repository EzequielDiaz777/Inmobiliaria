package com.ezediaz.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListaFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> mInmuebles;
    public ListaFragmentViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<Inmueble>> getMInmuebles(){
        if(mInmuebles == null){
            mInmuebles = new MutableLiveData<>();
        }
        return mInmuebles;
    }

    public void cargarInmuebles(){
        ApiClient.MisEndPoints mep = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication().getApplicationContext());
        Call<List<Inmueble>> call = mep.obtenerInmuebles(token);
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if(response.isSuccessful()){
                    mInmuebles.postValue(response.body());
                } else {
                    Log.d("salida", response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                Log.d("salida", "Falla: " + throwable.getMessage());
            }
        });
    }
}

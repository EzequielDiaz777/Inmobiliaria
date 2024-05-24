package com.ezediaz.inmobiliaria.ui.pago;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezediaz.inmobiliaria.model.Contrato;
import com.ezediaz.inmobiliaria.model.Pago;
import com.ezediaz.inmobiliaria.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PagosFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<List<Pago>> mPagos;
    public PagosFragmentViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<Pago>> getMPagos(){
        if(mPagos == null){
            mPagos = new MutableLiveData<>();
        }
        return mPagos;
    }

    public void cargarPagos(Bundle arguments){
        int idContrato = (int) arguments.getSerializable("pagos");
        ApiClient.MisEndPoints mep = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication().getApplicationContext());
        Call<List<Pago>> call = mep.obtenerPagos(token, idContrato);
        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if(response.isSuccessful()){
                    mPagos.postValue(response.body());
                } else {
                    Log.d("salida", response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Pago>> call, Throwable throwable) {
                Log.d("salida", "Falla: " + throwable.getMessage());
            }
        });
    }
}

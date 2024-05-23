package com.ezediaz.inmobiliaria.ui.contrato;
import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ezediaz.inmobiliaria.model.Contrato;
import com.ezediaz.inmobiliaria.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContratosFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contrato>> mContratos;
    public ContratosFragmentViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<Contrato>> getMContratos(){
        if(mContratos == null){
            mContratos = new MutableLiveData<>();
        }
        return mContratos;
    }

    public void cargarContratos(){
        ApiClient.MisEndPoints mep = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication().getApplicationContext());
        Call<List<Contrato>> call = mep.obtenerContratos(token);
        call.enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if(response.isSuccessful()){
                    response.body().forEach(contrato -> Log.d("salida", contrato.toString()));
                    mContratos.postValue(response.body());
                } else {
                    Log.d("salida", response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable throwable) {
                Log.d("salida", "Falla: " + throwable.getMessage());
            }
        });
    }
}

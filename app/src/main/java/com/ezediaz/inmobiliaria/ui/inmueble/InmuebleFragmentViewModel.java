package com.ezediaz.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.model.Tipo;
import com.ezediaz.inmobiliaria.model.Uso;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble;
    private MutableLiveData<Boolean> mDisponible;
    private MutableLiveData<String> mGuardar;
    private MutableLiveData<Boolean> mHabilitar;

    private MutableLiveData<List<Tipo>> mTipo;
    private MutableLiveData<List<Uso>> mUso;
    private MutableLiveData<Uri> mUri;
    private Context context;

    public InmuebleFragmentViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Inmueble> getMInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }

    public LiveData<Uri> getMUri() {
        if (mUri == null) {
            mUri = new MutableLiveData<>();
        }
        return mUri;
    }

    public LiveData<Boolean> getMDisponible() {
        if (mDisponible == null) {
            mDisponible = new MutableLiveData<>();
        }
        return mDisponible;
    }

    public LiveData<String> getMGuardar() {
        if (mGuardar == null) {
            mGuardar = new MutableLiveData<>();
        }
        return mGuardar;
    }

    public LiveData<Boolean> getMHabilitar() {
        if (mHabilitar == null) {
            mHabilitar = new MutableLiveData<>();
        }
        return mHabilitar;
    }

    public LiveData<List<Tipo>> getMTipo() {
        if (mTipo == null) {
            mTipo = new MutableLiveData<>();
        }
        return mTipo;
    }

    public LiveData<List<Uso>> getMUso() {
        if (mUso == null) {
            mUso = new MutableLiveData<>();
        }
        return mUso;
    }

    public void cargarUri(Uri uri){
        mUri.setValue(uri);
    }

    public void cargarInmueble(Bundle arguments, Spinner spinnerTipo, Spinner spinnerUso, Button botonAI, Button botonAF) {
        Inmueble inmueble = new Inmueble();
        if (arguments != null) {
            inmueble = (Inmueble) arguments.getSerializable("inmueble");
            // Obtener el tipo y uso del inmueble
            Tipo tipoInmueble = inmueble.getTipo();
            Uso usoInmueble = inmueble.getUso();
            // Crear adaptadores para los spinners
            ArrayAdapter<Tipo> tipoAdapter = new ArrayAdapter<>(spinnerTipo.getContext(), android.R.layout.simple_spinner_item);
            ArrayAdapter<Uso> usoAdapter = new ArrayAdapter<>(spinnerUso.getContext(), android.R.layout.simple_spinner_item);
            // Asignar los adaptadores a los spinners
            spinnerTipo.setAdapter(tipoAdapter);
            spinnerUso.setAdapter(usoAdapter);
            // Verificar si el tipo y el uso del inmueble no son nulos
            if (tipoInmueble != null && usoInmueble != null) {
                // Agregar el tipo y el uso del inmueble a los adaptadores
                tipoAdapter.add(tipoInmueble);
                usoAdapter.add(usoInmueble);
                // Establecer la selección en los spinners
                spinnerTipo.setSelection(0);
                spinnerUso.setSelection(0);
            }
            botonAI.setVisibility(View.GONE);
            botonAF.setVisibility(View.GONE);
        } else {
            botonAI.setVisibility(View.VISIBLE);
            botonAF.setVisibility(View.VISIBLE);
            mHabilitar.setValue(true);
            ArrayAdapter<Tipo> tipoAdapter = new ArrayAdapter<>(spinnerTipo.getContext(), android.R.layout.simple_spinner_item);
            ArrayAdapter<Uso> usoAdapter = new ArrayAdapter<>(spinnerUso.getContext(), android.R.layout.simple_spinner_item);
            // Asignar los adaptadores a los spinners
            spinnerTipo.setAdapter(tipoAdapter);
            spinnerUso.setAdapter(usoAdapter);
            String token = ApiClient.leerToken(getApplication());
            if (token != null) {
                ApiClient.MisEndPoints api = ApiClient.getEndPoints();
                Call<List<Tipo>> call = api.obtenerTipos(token);
                call.enqueue(new Callback<List<Tipo>>() {
                    @Override
                    public void onResponse(Call<List<Tipo>> call, Response<List<Tipo>> response) {
                        if (response.isSuccessful()) {
                            response.body().forEach(tipo -> {
                                tipoAdapter.add(tipo);
                                spinnerTipo.setSelection(tipoAdapter.getPosition(tipo));
                            });
                            spinnerTipo.setSelection(0);
                        } else {
                            Toast.makeText(getApplication(), "Falla en el dado de alta del inmueble", Toast.LENGTH_LONG).show();
                            Log.d("salida", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tipo>> call, Throwable throwable) {
                        Log.d("salida", "Falla: " + throwable.getMessage());
                    }
                });
            }
            ApiClient.MisEndPoints api = ApiClient.getEndPoints();
            Call<List<Uso>> call = api.obtenerUsos(token);
            call.enqueue(new Callback<List<Uso>>() {
                @Override
                public void onResponse(Call<List<Uso>> call, Response<List<Uso>> response) {
                    if (response.isSuccessful()) {
                        response.body().forEach(uso -> {
                            usoAdapter.add(uso);
                            spinnerUso.setSelection(usoAdapter.getPosition(uso));
                        });
                        spinnerUso.setSelection(0);
                    } else {
                        Toast.makeText(getApplication(), "Falla en el dado de alta del inmueble", Toast.LENGTH_LONG).show();
                        Log.d("salida", response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Uso>> call, Throwable throwable) {
                    Log.d("salida", "Falla: " + throwable.getMessage());
                }
            });
        }
        mInmueble.setValue(inmueble);
    }

    public void cambiarDisponibilidad(boolean disponible, int id) {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.MisEndPoints api = ApiClient.getEndPoints();
            Call<Void> call = api.inmuebleDisponible(token, id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (disponible) {
                            Toast.makeText(getApplication(), "Inmueble dado de alta", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "Inmueble dado de baja", Toast.LENGTH_LONG).show();
                        }
                        mDisponible.setValue(disponible);
                    } else {
                        Toast.makeText(getApplication(), "Fallo en la actualización del inmueble", Toast.LENGTH_LONG).show();
                        Log.d("salida", response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Log.d("salida", "Falla: " + throwable.getMessage());
                }
            });
        }
    }

    public void agregarInmueble(Inmueble inmueble, String ambientes, String direccion, String precio, View view) {
        if (ambientes.isEmpty() || direccion.isEmpty() || precio.isEmpty()) {
            Toast.makeText(getApplication(), "Debe ingresar todos los datos antes de guardar el inmueble", Toast.LENGTH_LONG).show();
        } else {
            mHabilitar.setValue(false);
            String token = ApiClient.leerToken(getApplication());
            if (token != null) {
                ApiClient.MisEndPoints api = ApiClient.getEndPoints();
                inmueble.setAmbientes(Integer.parseInt(ambientes));
                inmueble.setDireccion(direccion);
                inmueble.setPrecio(Double.valueOf(precio));
                Call<Inmueble> call = api.agregarInmueble(token, inmueble);
                call.enqueue(new Callback<Inmueble>() {
                    @Override
                    public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication(), "Inmueble dado de alta con exito", Toast.LENGTH_LONG).show();
                            NavController navController = Navigation.findNavController(view);
                            navController.navigate(R.id.action_nav_inmueble_to_nav_lista);
                        } else {
                            Toast.makeText(getApplication(), "Falla en el dado de alta del inmueble", Toast.LENGTH_LONG).show();
                            Log.d("salida", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Inmueble> call, Throwable throwable) {
                        Log.d("salida", "Falla: " + throwable.getMessage());
                    }
                });

            }
        }
    }
}


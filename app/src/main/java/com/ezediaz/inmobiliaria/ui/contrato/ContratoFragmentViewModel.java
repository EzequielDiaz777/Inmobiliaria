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
    private MutableLiveData<Inmueble> mInmueble;
    private MutableLiveData<Boolean> mDisponible;
    private MutableLiveData<Boolean> mHabilitar;

    private MutableLiveData<List<Tipo>> mListaTipo;
    private MutableLiveData<List<Uso>> mListaUso;
    private MutableLiveData<Boolean> mTextos;
    private MutableLiveData<Tipo> mTipo;
    private MutableLiveData<Uso> mUso;
    private MutableLiveData<Uri> mUri;

    public ContratoFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getMInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }

    public LiveData<Boolean> getMDisponible() {
        if (mDisponible == null) {
            mDisponible = new MutableLiveData<>();
        }
        return mDisponible;
    }

    public LiveData<Boolean> getMTextos() {
        if (mTextos == null) {
            mTextos = new MutableLiveData<>();
        }
        return mTextos;
    }

    public LiveData<Boolean> getMHabilitar() {
        if (mHabilitar == null) {
            mHabilitar = new MutableLiveData<>();
        }
        return mHabilitar;
    }

    public LiveData<List<Tipo>> getMListaTipo() {
        if (mListaTipo == null) {
            mListaTipo = new MutableLiveData<>();
        }
        return mListaTipo;
    }

    public LiveData<List<Uso>> getMListaUso() {
        if (mListaUso == null) {
            mListaUso = new MutableLiveData<>();
        }
        return mListaUso;
    }

    public LiveData<Tipo> getMTipo() {
        if (mTipo == null) {
            mTipo = new MutableLiveData<>();
        }
        return mTipo;
    }

    public LiveData<Uso> getMUso() {
        if (mUso == null) {
            mUso = new MutableLiveData<>();
        }
        return mUso;
    }

    public LiveData<Uri> getMUri() {
        if (mUri == null) {
            mUri = new MutableLiveData<>();
        }
        return mUri;
    }

    private void cargarTipos() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Call<List<Tipo>> call = api.obtenerTipos(token);
        call.enqueue(new Callback<List<Tipo>>() {
            @Override
            public void onResponse(Call<List<Tipo>> call, Response<List<Tipo>> response) {
                if (response.isSuccessful()) {
                    mListaTipo.setValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Falla en la obtención de los tipos de inmueble", Toast.LENGTH_LONG).show();
                    Log.d("salida", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Tipo>> call, Throwable t) {
                Toast.makeText(getApplication(), "Falla en la obtención de los tipos de inmueble", Toast.LENGTH_LONG).show();
                Log.d("salida", t.getMessage());
            }
        });
    }

    private void cargarUsos() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Call<List<Uso>> call = api.obtenerUsos(token);
        call.enqueue(new Callback<List<Uso>>() {
            @Override
            public void onResponse(Call<List<Uso>> call, Response<List<Uso>> response) {
                if (response.isSuccessful()) {
                    mListaUso.setValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Falla en la obtención de los usos de inmueble", Toast.LENGTH_LONG).show();
                    Log.d("salida", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Uso>> call, Throwable t) {
                Toast.makeText(getApplication(), "Falla en la obtención de los usos de inmueble", Toast.LENGTH_LONG).show();
                Log.d("salida", t.getMessage());
            }
        });
    }

    public void cargarInmueble(Bundle arguments) {
        if (arguments != null) {
            Inmueble inmueble = (Inmueble) arguments.getSerializable("inmueble");
            mTipo.setValue(inmueble.getTipo());
            mUso.setValue(inmueble.getUso());
            mHabilitar.setValue(false);
            mTextos.setValue(false);
            mInmueble.setValue(inmueble);
        } else {
            cargarTipos();
            cargarUsos();
            mHabilitar.setValue(true);
            mTextos.setValue(true);
            mInmueble.setValue(new Inmueble());
        }
    }

    public void cambiarDisponibilidad(boolean disponible, int id) {
        Log.d("salida", String.valueOf(disponible));
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

    public void cargarFoto(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri selectedImageUri = result.getData().getData();
            if (selectedImageUri != null) {
                mUri.setValue(selectedImageUri);
            }
        } else {
            Toast.makeText(getApplication(), "Debe elegirse una imagen antes de dar de guardar el inmueble", Toast.LENGTH_LONG).show();
        }
    }

    public void agregarInmueble(Inmueble inmueble, String ambientes, String direccion, String precio, Uri photoUri, View view) {
        if (ambientes.isEmpty() || direccion.isEmpty() || precio.isEmpty()) {
            Toast.makeText(getApplication(), "Debe ingresar todos los datos antes de guardar el inmueble", Toast.LENGTH_LONG).show();
        } else if (photoUri == null) {
            Toast.makeText(getApplication(), "Debe elegirse una imagen antes de dar de guardar el inmueble", Toast.LENGTH_LONG).show();
        } else {
            String rutaArchivo = RealPathUtil.getRealPath(getApplication(), photoUri);
            File imagen = new File(rutaArchivo);
            String[] parts = imagen.getName().split("\\.");
            String extension2 = parts[1];
            if (!extension2.equals("jpg") && !extension2.equals("png")) {
                Toast.makeText(getApplication(), "La imagen seleccionada debe tener una extensión .jpg o .png", Toast.LENGTH_LONG).show();
            } else {
                String token = ApiClient.leerToken(getApplication());
                if (token != null) {
                    ApiClient.MisEndPoints api = ApiClient.getEndPoints();
                    // Convertir campos a RequestBody
                    RequestBody tipoId = RequestBody.create(MediaType.parse("application/json"), String.valueOf(inmueble.getTipoId()));
                    RequestBody usoId = RequestBody.create(MediaType.parse("application/json"), String.valueOf(inmueble.getUsoId()));
                    RequestBody direccionBody = RequestBody.create(MediaType.parse("application/json"), direccion);
                    RequestBody ambientesBody = RequestBody.create(MediaType.parse("application/json"), ambientes);
                    RequestBody precioBody = RequestBody.create(MediaType.parse("application/json"), precio);
                    RequestBody estado = RequestBody.create(MediaType.parse("application/json"), String.valueOf(inmueble.isEstado()));
                    // Crear MultipartBody.Part para la imagen
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imagen);
                    MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", imagen.getName(), requestFile);
                    // Realizar la llamada a la API
                    Call<Inmueble> call = api.agregarInmueble(token, tipoId, usoId, direccionBody, ambientesBody, precioBody, estado, imagenPart);
                    call.enqueue(new Callback<Inmueble>() {
                        @Override
                        public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplication(), "Inmueble dado de alta con exito", Toast.LENGTH_LONG).show();
                                NavController navController = Navigation.findNavController(view);
                                navController.navigate(R.id.action_nav_inmueble_to_nav_inmuebles);
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
}


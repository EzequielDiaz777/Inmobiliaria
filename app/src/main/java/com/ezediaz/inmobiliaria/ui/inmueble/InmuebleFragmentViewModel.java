package com.ezediaz.inmobiliaria.ui.inmueble;
import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.ezediaz.inmobiliaria.R;
import java.io.File;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.model.RealPathUtil;
import com.ezediaz.inmobiliaria.model.Tipo;
import com.ezediaz.inmobiliaria.model.Uso;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble;
    private MutableLiveData<Boolean> mDisponible;
    private MutableLiveData<Boolean> mNavegarAInmuebles;
    private MutableLiveData<Boolean> mHabilitar;
    private MutableLiveData<String> mCabecera;
    private MutableLiveData<List<Tipo>> mListaTipo;
    private MutableLiveData<List<Uso>> mListaUso;
    private MutableLiveData<Boolean> mTextos;
    private MutableLiveData<Tipo> mTipo;
    private MutableLiveData<Uso> mUso;
    private MutableLiveData<Uri> mUri;

    public InmuebleFragmentViewModel(@NonNull Application application) {
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

    public LiveData<Boolean> getMNavegarAInmuebles() {
        if (mNavegarAInmuebles == null) {
            mNavegarAInmuebles = new MutableLiveData<>();
        }
        return mNavegarAInmuebles;
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

    public LiveData<String> getMCabecera() {
        if (mCabecera == null) {
            mCabecera = new MutableLiveData<>();
        }
        return mCabecera;
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
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<List<Tipo>> call = apiService.obtenerTipos(token);
        call.enqueue(new Callback<List<Tipo>>() {
            @Override
            public void onResponse(Call<List<Tipo>> call, Response<List<Tipo>> response) {
                if (response.isSuccessful()) {
                    mListaTipo.setValue(response.body());
                } else {
                    Log.e("InmuebleViewModel", "Error al obtener tipos de inmueble: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Tipo>> call, Throwable t) {
                Log.e("InmuebleViewModel", "Error al obtener tipos de inmueble: " + t.getMessage());
            }
        });
    }

    private void cargarUsos() {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<List<Uso>> call = apiService.obtenerUsos(token);
        call.enqueue(new Callback<List<Uso>>() {
            @Override
            public void onResponse(Call<List<Uso>> call, Response<List<Uso>> response) {
                if (response.isSuccessful()) {
                    mListaUso.setValue(response.body());
                } else {
                    Log.e("InmuebleViewModel", "Error al obtener usos de inmueble: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Uso>> call, Throwable t) {
                Log.e("InmuebleViewModel", "Error al obtener usos de inmueble: " + t.getMessage());
            }
        });
    }

    public void cargarInmueble(Bundle arguments) {
        if (arguments != null) {
            Inmueble inmueble = (Inmueble) arguments.getSerializable("inmueble");
            mInmueble.setValue(inmueble);
            mDisponible.setValue(inmueble.isEstado());
            mTipo.setValue(inmueble.getTipo());
            mUso.setValue(inmueble.getUso());
            mHabilitar.setValue(false);
            mTextos.setValue(false);
            mCabecera.setValue("Detalles del inmueble");
        } else {
            cargarTipos();
            cargarUsos();
            mHabilitar.setValue(true);
            mTextos.setValue(true);
            mDisponible.setValue(false);
            mInmueble.setValue(new Inmueble());
            mCabecera.setValue("Alta del inmueble");
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

    public void agregarInmueble(Inmueble inmueble, String ambientes, String direccion, String precio, Uri photoUri) {
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
                    String cleanedPrecio = precio.replace("$", "").replace(".", "");
                    try {
                        RequestBody tipoId = RequestBody.create(MediaType.parse("application/json"), String.valueOf(inmueble.getTipoId()));
                        RequestBody usoId = RequestBody.create(MediaType.parse("application/json"), String.valueOf(inmueble.getUsoId()));
                        RequestBody direccionBody = RequestBody.create(MediaType.parse("application/json"), direccion);
                        RequestBody ambientesBody = RequestBody.create(MediaType.parse("application/json"), ambientes);
                        RequestBody precioBody = RequestBody.create(MediaType.parse("application/json"), cleanedPrecio);
                        RequestBody estado = RequestBody.create(MediaType.parse("application/json"), String.valueOf(inmueble.isEstado()));
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imagen);
                        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", imagen.getName(), requestFile);
                        Call<Inmueble> call = api.agregarInmueble(token, tipoId, usoId, direccionBody, ambientesBody, precioBody, estado, imagenPart);
                        call.enqueue(new Callback<Inmueble>() {
                            @Override
                            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplication(), "Inmueble dado de alta con exito", Toast.LENGTH_LONG).show();
                                    mNavegarAInmuebles.setValue(true);
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
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplication(), "El formato del precio es incorrecto", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void corregirPrecio(Editable s, EditText precio, TextWatcher textWatcher){
        String current;
        if (!s.toString().isEmpty()) {
            precio.removeTextChangedListener(textWatcher);

            String cleanString = s.toString().replaceAll("[^\\d]", "");

            if (!cleanString.isEmpty()) {
                double parsed = Double.parseDouble(cleanString) / 100;
                String formatted = formatPrice(parsed);
                current = formatted;
                precio.setText("$"+formatted);
                precio.setSelection(formatted.length()+1);
            }

            precio.addTextChangedListener(textWatcher);
        }
    }

    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat.format(price);
    }
}


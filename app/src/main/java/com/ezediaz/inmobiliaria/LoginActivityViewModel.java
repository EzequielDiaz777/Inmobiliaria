package com.ezediaz.inmobiliaria;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.fragment.app.FragmentActivity;

import com.ezediaz.inmobiliaria.model.Propietario;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Activity activity;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        executor = ContextCompat.getMainExecutor(application);
    }

    public void iniciarAutenticacionBiometrica(Activity activity) {
        this.activity = activity;
        biometricPrompt = new BiometricPrompt((FragmentActivity) activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Handle error
                Log.e("BiometricPrompt", "Authentication error: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Authentication succeeded
                Log.d("BiometricPrompt", "Authentication succeeded");
                // Continuar con el proceso de login
                String email = "diazezequiel777@gmail.com";
                String password = "1234";
                logueo(email, password);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Authentication failed
                Log.e("BiometricPrompt", "Authentication failed");
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Inicia sesión usando tu huella digital")
                .setNegativeButtonText("Usar contraseña")
                .build();

        // Mostrar el diálogo de autenticación biométrica
        biometricPrompt.authenticate(promptInfo);
    }

    public void logueo(String usuario, String clave) {
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Call<String> call = api.login(usuario, clave);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    guardarToken("Bearer " + token);
                    Log.d("salida", "Inicio de sesión exitoso");
                    iniciarMainActivity();
                } else {
                    Toast.makeText(getApplication(), "Email o contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplication(), "Falla en el inicio de sesión", Toast.LENGTH_LONG).show();
                Log.d("Login", "Falla en el inicio de sesión: " + throwable.getMessage());
            }
        });
    }

    public void enviarEmail(String email){
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        if(!email.isEmpty()){
            Log.d("email", email);
            Call<Void> call = api.enviarEmail(email);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Email enviado a su correo para recuperar la contraseña", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("salida", response.message());
                        Toast.makeText(getApplication(), "Email incorrecto o no está registrado", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Toast.makeText(getApplication(), "Falla en la recuperación del email", Toast.LENGTH_LONG).show();
                    Log.d("salida", throwable.getMessage());
                }
            });
        } else {
            Toast.makeText(getApplication(), "Por favor ingrese un email para recuperar la contraseña", Toast.LENGTH_LONG).show();
        }
    }

    private void guardarToken(String token) {
        ApiClient.guardarToken(token, getApplication());
    }

    private void iniciarMainActivity() {
        sharedPreferences = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Propietario> call = api.miPerfil(token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    editor.putString("nombre completo", response.body().toString());
                    editor.putString("email", response.body().getEmail());
                    editor.apply();
                } else {
                    Log.d("salida", response.message());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                Log.d("salida", throwable.getMessage());
            }
        });
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        getApplication().startActivity(intent);
    }

    public void handleBiometricAuthenticationSuccess() {
        // Lógica para manejar el inicio de sesión después de una autenticación biométrica exitosa
        // Authentication succeeded
        Log.d("BiometricPrompt", "Authentication succeeded");
        // Continuar con el proceso de login
        String email = "diazezequiel777@gmail.com";
        String password = "1234";
        logueo(email, password);
    }
}

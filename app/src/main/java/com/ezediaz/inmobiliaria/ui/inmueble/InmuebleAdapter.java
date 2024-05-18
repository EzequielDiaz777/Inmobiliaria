package com.ezediaz.inmobiliaria.ui.inmueble;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolderPepe> {
    private List<Inmueble> listaDeInmuebles;
    private LayoutInflater li;
    public InmuebleAdapter(List<Inmueble> listaDeInmuebles,  LayoutInflater li) {
        this.listaDeInmuebles = listaDeInmuebles;
        this.li = li;
    }

    @NonNull
    @Override
    public ViewHolderPepe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolderPepe(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPepe holder, int position) {
        Inmueble inmueble = listaDeInmuebles.get(position);
        holder.precio.setText(String.valueOf(inmueble.getPrecio()));
        holder.direccion.setText(inmueble.getDireccion());
        //holder.foto.setImageResource(inmueble.getImagenInmueble());
        holder.btnVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String token = ApiClient.leerToken(li.getContext());
                ApiClient.MisEndPoints api = ApiClient.getEndPoints();
                Call<Inmueble> call = api.obtenerInmueble(token, inmueble.getId());
                call.enqueue(new Callback<Inmueble>() {
                    @Override
                    public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                        if(response.isSuccessful()){
                            bundle.putBoolean("desdeVerMas", true); // Enviar el parámetro
                            bundle.putSerializable("inmueble", response.body());
                            Navigation.findNavController(v).navigate(R.id.nav_inmueble, bundle);
                        } else {
                            Log.d("salida", response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Inmueble> call, Throwable throwable) {
                        Log.d("salida", "Falla: " + throwable.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDeInmuebles.size();
    }

    public class ViewHolderPepe extends RecyclerView.ViewHolder {
        TextView precio, direccion;
        //ImageView foto;
        Button btnVerMas;

        public ViewHolderPepe(@NonNull View itemView) {
            super(itemView);
            //foto = itemView.findViewById(R.id.ivImagenInm);
            direccion = itemView.findViewById(R.id.tvDireccionInm);
            precio = itemView.findViewById(R.id.tvPrecioInm);
            btnVerMas = itemView.findViewById(R.id.btnVerMas);
        }
    }
}

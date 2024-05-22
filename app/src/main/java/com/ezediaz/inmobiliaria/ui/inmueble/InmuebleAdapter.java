package com.ezediaz.inmobiliaria.ui.inmueble;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.model.Inmueble;
import com.ezediaz.inmobiliaria.request.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolderPepe> {
    private List<Inmueble> listaDeInmuebles;
    private LayoutInflater li;
    private Context context;

    public InmuebleAdapter(List<Inmueble> listaDeInmuebles, LayoutInflater li) {
        this.listaDeInmuebles = listaDeInmuebles;
        this.li = li;
    }

    @NonNull
    @Override
    public ViewHolderPepe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.item_inmueble, parent, false);
        context = parent.getContext();
        return new ViewHolderPepe(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPepe holder, int position) {
        Inmueble inmueble = listaDeInmuebles.get(position);
        holder.precio.setText(String.valueOf(inmueble.getPrecio()));
        holder.direccion.setText(inmueble.getDireccion());
        // Corregir el formato de la URL de la imagen
        String imageUrl = ApiClient.URL+inmueble.getImagenUrl();
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.icon_inmuebles) // Imagen de marcador de posición
                .error(R.drawable.icon_logout); // Imagen de error
        // Utiliza Glide para cargar y mostrar la imagen
        Glide.with(context)
                .load(imageUrl) // Especifica la URL de la imagen
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Carga la caché para obtener la imagen
                .apply(options)
                .into(holder.foto); // Especifica el ImageView donde se mostrará la imagen
        holder.btnVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("desdeVerMas", true); // Enviar el parámetro
                bundle.putSerializable("inmueble", inmueble);
                Navigation.findNavController(v).navigate(R.id.nav_inmueble, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDeInmuebles.size();
    }

    public class ViewHolderPepe extends RecyclerView.ViewHolder {
        TextView precio, direccion;
        ImageView foto;
        Button btnVerMas;

        public ViewHolderPepe(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.ivImagenInm);
            direccion = itemView.findViewById(R.id.tvDireccionInm);
            precio = itemView.findViewById(R.id.tvPrecioInm);
            btnVerMas = itemView.findViewById(R.id.btnVerMas);
        }
    }
}
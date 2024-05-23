package com.ezediaz.inmobiliaria.ui.contrato;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.model.Contrato;
import com.ezediaz.inmobiliaria.request.ApiClient;

import java.util.List;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.ViewHolderPepe> {
    private List<Contrato> listaDeContratos;
    private LayoutInflater li;
    private Context context;

    public ContratoAdapter(List<Contrato> listaDeContratos, LayoutInflater li) {
        this.listaDeContratos = listaDeContratos;
        this.li = li;
    }

    @NonNull
    @Override
    public ViewHolderPepe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.item_contrato, parent, false);
        context = parent.getContext();
        return new ViewHolderPepe(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPepe holder, int position) {
        Contrato contrato = listaDeContratos.get(position);
        holder.direccion.setText(contrato.getInmueble().getDireccion());
        // Corregir el formato de la URL de la imagen
        String imageUrl = ApiClient.URL+contrato.getInmueble().getImagenUrl();
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
                bundle.putSerializable("contrato", contrato);
                Navigation.findNavController(v).navigate(R.id.nav_contrato, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDeContratos.size();
    }

    public class ViewHolderPepe extends RecyclerView.ViewHolder {
        TextView direccion;
        ImageView foto;
        Button btnVerMas;

        public ViewHolderPepe(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.ivImagenInm);
            direccion = itemView.findViewById(R.id.tvDireccionInm);
            btnVerMas = itemView.findViewById(R.id.btnVerMas);
        }
    }
}
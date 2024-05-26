package com.ezediaz.inmobiliaria.ui.pago;

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
import com.ezediaz.inmobiliaria.model.Pago;
import com.ezediaz.inmobiliaria.request.ApiClient;
import com.ezediaz.inmobiliaria.ui.inmueble.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolderPepe> {
    private List<Pago> listaDePagos;
    private LayoutInflater li;

    public PagoAdapter(List<Pago> listaDePagos, LayoutInflater li) {
        this.listaDePagos = listaDePagos;
        this.li = li;
    }

    @NonNull
    @Override
    public ViewHolderPepe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.item_pago, parent, false);
        return new ViewHolderPepe(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPepe holder, int position) {
        Pago pago = listaDePagos.get(position);
        holder.codigoP.setText(String.valueOf(pago.getId()));
        holder.numeroDP.setText(String.valueOf(pago.getNumeroDePago()));
        holder.codigoC.setText(String.valueOf(pago.getContratoId()));
        holder.importe.setText("$" + Utils.formatPrice(pago.getMonto()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy");
        LocalDate fechaLD = LocalDate.parse(pago.getFecha(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fechaS = fechaLD.format(formatter);
        holder.fecha.setText(fechaS);
    }

    @Override
    public int getItemCount() {
        return listaDePagos.size();
    }

    public class ViewHolderPepe extends RecyclerView.ViewHolder {
        TextView codigoP, numeroDP, codigoC, importe, fecha;

        public ViewHolderPepe(@NonNull View itemView) {
            super(itemView);
            codigoP = itemView.findViewById(R.id.tvCodigoPagoInfo);
            numeroDP = itemView.findViewById(R.id.tvNumeroDePagoInfo);
            codigoC = itemView.findViewById(R.id.tvCodigoDelContratoInfo);
            importe = itemView.findViewById(R.id.tvImporteDelPagoInfo);
            fecha = itemView.findViewById(R.id.tvFechaDePagoInfo);
        }
    }
}
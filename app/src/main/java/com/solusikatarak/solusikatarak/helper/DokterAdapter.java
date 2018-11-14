package com.solusikatarak.solusikatarak.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.models.Dokter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DokterAdapter extends RecyclerView.Adapter<DokterAdapter.MyViewHolder> {
    private List<Dokter> dokterList;
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvID, tvNama, tvEmail;
        public CircleImageView ivFotoDokter;

        public MyViewHolder(View view) {
            super(view);
            tvID = (TextView) view.findViewById(R.id.tvID);
            tvNama = (TextView) view.findViewById(R.id.tvNama);
            tvEmail =(TextView) view.findViewById(R.id.tvEmail);
            ivFotoDokter = (CircleImageView) view.findViewById(R.id.ivFotoDokter);
        }
    }


    public DokterAdapter(List<Dokter> dokterList) {
        this.dokterList = dokterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dokter_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Dokter dokter = dokterList.get(position);

        holder.tvID.setText(dokter.getId()+"");
        holder.tvNama.setText(dokter.getNama());
        holder.tvEmail.setText(dokter.getEmail());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context)
                .load(dokter.getFotoDokterUrl())
//                .apply(requestOptions)
                .into(holder.ivFotoDokter);

    }

    @Override
    public int getItemCount() {
        return dokterList.size();
    }
}

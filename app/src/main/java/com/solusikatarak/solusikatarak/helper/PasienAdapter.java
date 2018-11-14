package com.solusikatarak.solusikatarak.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.models.Pasien;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.solusikatarak.solusikatarak.R.color.belumoperasi;

public class PasienAdapter extends RecyclerView.Adapter<PasienAdapter.MyViewHolder>{
    private List<Pasien> pasienList;
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama, tvNik, tvNomorhp, tvJalan, tvProvinsi, tvKabupaten, tvKecamatan, tvDesa, tvJeniskelamin, tvTtl, tvJadwal, tvStatus;
//        public ImageView ivFotoktp,ivFotobpjs, ivFotomap;
        public CircleImageView ivFotoKtp;
        public ImageButton btDelete;

        public MyViewHolder(View view) {
            super(view);
            tvNama = (TextView) view.findViewById(R.id.tvNama);
            tvNik = (TextView) view.findViewById(R.id.tvNik);
            tvNomorhp =(TextView) view.findViewById(R.id.tvNomorhp);
//            tvJalan =(TextView) view.findViewById(R.id.tvJalan);
//            tvProvinsi =(TextView) view.findViewById(R.id.tvProvinsi);
//            tvKabupaten = (TextView) view.findViewById(R.id.tvKabupaten);
//            tvKecamatan = (TextView) view.findViewById(R.id.tvKecamatan);
//            tvDesa = (TextView) view.findViewById(R.id.tvDesa);
//            tvJeniskelamin =(TextView) view.findViewById(R.id.tvJeniskelamin);
//            tvTtl =(TextView) view.findViewById(R.id.tvTtl);
//            tvJadwal =(TextView) view.findViewById(R.id.tvjadwal);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            ivFotoKtp = (CircleImageView) view.findViewById(R.id.ivFotoKtp);
//            btDelete = (ImageButton) view.findViewById(R.id.btDelete);
//            ivFotobpjs = (ImageView) view.findViewById(R.id.ivFotobpjs);
//            ivFotomap = (ImageView) view.findViewById(R.id.ivFotomap);
        }
    }


    public PasienAdapter(List<Pasien> pasienList) {
        this.pasienList = pasienList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pasien_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Pasien pasien = pasienList.get(position);
        holder.tvNama.setText(pasien.getNama());
        holder.tvNik.setText(pasien.getNIK());
        holder.tvNomorhp.setText(pasien.getNomorhp());

        if (pasien.getStatus() == 0){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.belumoperasi));
            holder.tvStatus.setText("Belum Operasi");
        }else if(pasien.getStatus() == 1){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.checkup));
            holder.tvStatus.setText("Checkup");
        }else if(pasien.getStatus() == 2){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.dashboardrelawan));
            holder.tvStatus.setText("Operasi");
        }else if(pasien.getStatus() == 3){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.operasi));
            holder.tvStatus.setText("Konfirmasi Pembayaran");
        }else if(pasien.getStatus() == 4){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.sudahdibayar));
            holder.tvStatus.setText("Selesai");
        }

//        holder.tvJalan.setText(pasien.getJalan());
//        holder.tvProvinsi.setText(pasien.getProvinsi());
//        holder.tvKabupaten.setText(pasien.getKabupaten());
//        holder.tvKecamatan.setText(pasien.getKecamatan());
//        holder.tvDesa.setText(pasien.getDesa());
//        holder.tvJeniskelamin.setText(pasien.getGender());
//        holder.tvTtl.setText(pasien.getTTL());
//        holder.tvJadwal.setText(pasien.getJadwal());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context)
                .load(pasien.getKtpurl())
//                .apply(requestOptions)
                .into(holder.ivFotoKtp);
//        Glide.with(context)
//                .load(pasien.getBpjsurl())
//                .into(holder.ivFotobpjs);
//        Glide.with(context)
//                .load(pasien.getMapurl())
//                .into(holder.ivFotomap);
    }

    @Override
    public int getItemCount() {
        return pasienList.size();
    }

}

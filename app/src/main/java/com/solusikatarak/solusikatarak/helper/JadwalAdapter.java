package com.solusikatarak.solusikatarak.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.models.Jadwal;

import java.util.List;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.MyViewHolder> {
    private List<Jadwal> jadwalList;
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHari, tvMulai, tvSelesai;

        public MyViewHolder(View view) {
            super(view);
            tvHari = (TextView) view.findViewById(R.id.tvHari);
            tvMulai = (TextView) view.findViewById(R.id.tvMulai);
            tvSelesai =(TextView) view.findViewById(R.id.tvSelesai);
        }
    }


    public JadwalAdapter(List<Jadwal> jadwalList) {
        this.jadwalList = jadwalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jadwal_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Jadwal jadwal = jadwalList.get(position);

        holder.tvHari.setText(jadwal.getHari());
        holder.tvMulai.setText(jadwal.getMulai());
        holder.tvSelesai.setText(jadwal.getSelesai());

    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }
}

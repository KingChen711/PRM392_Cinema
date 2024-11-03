package com.example.prm392_cinema.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.R;

import java.util.List;

public class SeatShowAdapter extends RecyclerView.Adapter<SeatShowAdapter.ViewHolder> {

    private List<String> seatList;

    public SeatShowAdapter(List<String> seatList) {
        this.seatList = seatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String seatName = seatList.get(position);
        holder.txtSeatName.setText(seatName);
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSeatName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSeatName = itemView.findViewById(R.id.itemText);
        }
    }
}

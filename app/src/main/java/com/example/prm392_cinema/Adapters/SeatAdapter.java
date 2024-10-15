package com.example.prm392_cinema.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.Models.Seat;
import com.example.prm392_cinema.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private final List<Seat> seatList;
    public Map<Integer, Boolean> seatIndexToSelected;

    public SeatAdapter(List<Seat> seatList) {
        this.seatList = seatList;
        this.seatIndexToSelected = new HashMap<>();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_item, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        updateSeatView(holder, seat);

        holder.itemView.setOnClickListener(v -> {
            Log.d("Index", seat.getIndex() + "");
            if (seat.isAvailable()) {
                // Đổi trạng thái từ "trống" sang "đã chọn"
                this.seatIndexToSelected.put(seat.getIndex(), true);
                seat.setStatus(Seat.STATUS_SELECTED);
            } else if (seat.isSelected()) {
                // Đổi trạng thái từ "đã chọn" sang "trống"
                this.seatIndexToSelected.remove(seat.getIndex());
                seat.setStatus(Seat.STATUS_AVAILABLE);
            }
            // Cập nhật lại view khi ghế được chọn
            notifyItemChanged(position);
        });
    }

    private void updateSeatView(SeatViewHolder holder, Seat seat) {
        if (seat.isEmpty()) {
            holder.seatImage.setVisibility(View.INVISIBLE);  // Hide seat for empty space
        } else if (seat.isBooked()) {
            holder.seatImage.setImageResource(R.drawable.seat_booked);  // Hình ảnh ghế đã đặt
        } else if (seat.isSelected()) {
            holder.seatImage.setImageResource(R.drawable.seat_selected);  // Hình ảnh ghế đã chọn
        } else {
            holder.seatImage.setImageResource(R.drawable.seat_available);  // Hình ảnh ghế trống
        }
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        ImageView seatImage;

        SeatViewHolder(View itemView) {
            super(itemView);
            seatImage = itemView.findViewById(R.id.seatImage);
        }
    }
}

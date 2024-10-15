package com.example.prm392_cinema;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.Adapters.SeatAdapter;
import com.example.prm392_cinema.Models.Seat;

import java.util.ArrayList;
import java.util.List;

public class Hall1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hall1);

        RecyclerView seatRecyclerView = findViewById(R.id.seatRecyclerView);

        // Setup GridLayoutManager for horizontal and vertical scrolling
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 13); // 10 columns
        seatRecyclerView.setLayoutManager(gridLayoutManager);

        // Adapter setup
        SeatAdapter seatAdapter = new SeatAdapter(getSeats());
        seatRecyclerView.setAdapter(seatAdapter);

        findViewById(R.id.orderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("orderButton", seatAdapter.seatIndexToSelected.toString());
            }
        });
    }

    private List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();

        // Example seat data: '1' is a seat, '0' is an empty space
        String[] seatRows = {
                "1111101111111", // Row 1
                "1111102211111", // Row 2
                "1111101122111", // Row 3
                "1221101111111", // Row 4
                "1112101111111"
        };

        int index = 0;
        for (String row : seatRows) {
            for (char seatType : row.toCharArray()) {
                switch (seatType) {
                    case '0':  // không gian trống
                        seats.add(new Seat(true));
                        index--;
                        break;
                    case '1':  // Ghế trống
                        seats.add(new Seat(Seat.STATUS_AVAILABLE, index));
                        break;
                    case '2':  // Ghế đã đặt
                        seats.add(new Seat(Seat.STATUS_BOOKED, index));
                        break;
                }
                index++;
            }
        }
        return seats;
    }
}
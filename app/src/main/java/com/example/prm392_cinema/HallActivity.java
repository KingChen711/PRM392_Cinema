package com.example.prm392_cinema;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.Adapters.SeatAdapter;
import com.example.prm392_cinema.Models.Seat;
import com.example.prm392_cinema.Services.ApiClient;
import com.example.prm392_cinema.Services.SeatService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HallActivity extends AppCompatActivity {
    private SeatAdapter seatAdapter;
    private RecyclerView seatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hall);

        seatRecyclerView = findViewById(R.id.seatRecyclerView);

        // Setup GridLayoutManager for horizontal and vertical scrolling
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 16); // 16 columns
        seatRecyclerView.setLayoutManager(gridLayoutManager);

        getSeats();
    }

    private void getSeats() {
        List<Seat> seats = new ArrayList<>();

        SeatService apiService = ApiClient.getRetrofitInstance().create(SeatService.class);

        // Call API with a query parameter
        Call<List<SeatService.SeatResponseDto>> call = apiService.getSeats(2);  // Pass `id` as 1
        call.enqueue(new Callback<List<SeatService.SeatResponseDto>>() {
            @Override
            public void onResponse(Call<List<SeatService.SeatResponseDto>> call, Response<List<SeatService.SeatResponseDto>> response) {
                Log.d("callAPI","Done");
                Log.d("callAPI","Done");
                Log.d("callAPI","Done");
                Log.d("callAPI","Done");
                if (response.isSuccessful()) {
                    List<SeatService.SeatResponseDto> rows = response.body();

                    for (SeatService.SeatResponseDto row : rows) {
                        for (SeatService.SeatResponseDto.RowSeat seat : row.rowSeats) {
                            seats.add(new Seat(seat.seatId, seat.seatType, seat.price, seat.isSeat, seat.name, seat.isSold, seat.colIndex, seat.seatIndex));
                        }
                    }
                }
                Log.d("callAPI","Done");
                // Adapter setup
                seatAdapter = new SeatAdapter(seats);
                seatRecyclerView.setAdapter(seatAdapter);

                findViewById(R.id.orderButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("orderButton", seatAdapter.seatIndexToSelected.toString());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<SeatService.SeatResponseDto>> call, Throwable t) {
                // Handle the error
                Log.d("callAPI", t.getMessage());
            }
        });
    }
}
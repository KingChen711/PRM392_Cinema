package com.example.prm392_cinema;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.Adapters.FabAdapter;
import com.example.prm392_cinema.Adapters.SeatAdapter;
import com.example.prm392_cinema.Models.Fab;
import com.example.prm392_cinema.Models.Seat;
import com.example.prm392_cinema.Services.ApiClient;
import com.example.prm392_cinema.Services.FabService;
import com.example.prm392_cinema.Services.SeatService;
import com.example.prm392_cinema.Stores.HallScreen;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HallActivity extends AppCompatActivity {
    private SeatAdapter seatAdapter;
    private FabAdapter fabAdapter;
    private RecyclerView seatRecyclerView, fabRecyclerView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hall);

        ((TextView) findViewById(R.id.hallTitle)).setText("CHỌN GHẾ - RẠP " + HallScreen.hallNumber);

        GradientDrawable backgroundNormalExplain = (GradientDrawable) findViewById(R.id.normalExplain).getBackground();
        backgroundNormalExplain.setColor(Color.rgb(255, 255, 255));
        backgroundNormalExplain.setStroke(6, Color.GREEN);

        GradientDrawable backgroundVipExplain = (GradientDrawable) findViewById(R.id.vipExplain).getBackground();
        backgroundVipExplain.setColor(Color.rgb(255, 255, 255));
        backgroundVipExplain.setStroke(6, Color.RED);

        GradientDrawable backgroundSelectedExplain = (GradientDrawable) findViewById(R.id.selectedExplain).getBackground();
        backgroundSelectedExplain.setColor(Color.rgb(243, 234, 40));
        backgroundSelectedExplain.setStroke(6, Color.rgb(243, 234, 40));

        GradientDrawable backgroundBookedExplain = (GradientDrawable) findViewById(R.id.bookedExplain).getBackground();
        backgroundBookedExplain.setColor(Color.rgb(187, 187, 187));
        backgroundBookedExplain.setStroke(6, Color.rgb(187, 187, 187));

        seatRecyclerView = findViewById(R.id.seatRecyclerView);

        // Setup GridLayoutManager for horizontal and vertical scrolling
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 16); // 16 columns
        seatRecyclerView.setLayoutManager(gridLayoutManager);

        getSeats();


        fabRecyclerView = findViewById(R.id.fabRecyclerView);
        fabRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getFabs();
    }

    private void getSeats() {
        List<Seat> seats = new ArrayList<>();

        SeatService apiService = ApiClient.getRetrofitInstance().create(SeatService.class);

        // Call API with a query parameter
        Call<List<SeatService.SeatResponseDto>> call = apiService.getSeats(HallScreen.showTimeId);  // Pass `id` as 1
        call.enqueue(new Callback<List<SeatService.SeatResponseDto>>() {
            @Override
            public void onResponse(Call<List<SeatService.SeatResponseDto>> call, Response<List<SeatService.SeatResponseDto>> response) {
                Log.d("callAPI", "Done");
                Log.d("callAPI", "Done");
                Log.d("callAPI", "Done");
                Log.d("callAPI", "Done");
                if (response.isSuccessful()) {
                    List<SeatService.SeatResponseDto> rows = response.body();

                    for (SeatService.SeatResponseDto row : rows) {
                        for (SeatService.SeatResponseDto.RowSeat seat : row.rowSeats) {
                            seats.add(new Seat(seat.seatId, seat.seatType, seat.price, seat.isSeat, seat.name, seat.isSold, seat.colIndex, seat.seatIndex));
                        }
                    }
                }
                Log.d("callAPI", "Done");
                // Adapter setup
                seatAdapter = new SeatAdapter(seats);
                seatRecyclerView.setAdapter(seatAdapter);

                findViewById(R.id.orderButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Integer> selectedSeats = seatAdapter.getSelectedSeatId();

                        if(selectedSeats.isEmpty())
                        {
                            Toast.makeText(HallActivity.this, "Cần chọn ít nhất 1 ghế.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        HallScreen.orderFabDto = fabAdapter.getOrderFabDto();
                        HallScreen.listSeatId = selectedSeats;
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

    @Override
    protected void onStop() {
        super.onStop();
        HallScreen.orderFabDto = null;
        HallScreen.listSeatId = new ArrayList<>();
    }

    private void getFabs() {
        List<Fab> fabs = new ArrayList<>();

        FabService apiService = ApiClient.getRetrofitInstance().create(FabService.class);

        // Call API with a query parameter
        Call<FabService.GetFabsResponseDto> call = apiService.getFabs();  // Pass `id` as 1
        call.enqueue(new Callback<FabService.GetFabsResponseDto>() {
            @Override
            public void onResponse(Call<FabService.GetFabsResponseDto> call, Response<FabService.GetFabsResponseDto> response) {
                Log.d("callAPI", "Done");
                Log.d("callAPI", "Done");
                Log.d("callAPI", "Done");
                Log.d("callAPI", "Done");
                if (response.isSuccessful()) {
                    FabService.GetFabsResponseDto res = response.body();

                    for (FabService.FabDto fab : res.result.fABList) {
                        fabs.add(new Fab(fab.foodId,fab.name,fab.description,fab.price));
                    }
                }
                Log.d("callAPI", "Done");
                // Adapter setup
                fabAdapter = new FabAdapter(fabs);
                fabRecyclerView.setAdapter(fabAdapter);

                findViewById(R.id.orderButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("orderButton", seatAdapter.seatIndexToSelected.toString());
                    }
                });
            }

            @Override
            public void onFailure(Call<FabService.GetFabsResponseDto> call, Throwable t) {
                // Handle the error
                Log.d("callAPI", t.getMessage());
            }
        });
    }
}
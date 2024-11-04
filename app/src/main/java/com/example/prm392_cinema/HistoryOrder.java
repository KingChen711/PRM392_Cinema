package com.example.prm392_cinema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.Adapters.OrderAdapter;
import com.example.prm392_cinema.Services.ApiClient;
import com.example.prm392_cinema.Services.BookingService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryOrder extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<BookingService.BookingDetailAllDTO> orderList;
    private ProgressBar progressBar; // Loading indicator
    String userId;
    String  currentOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        recyclerViewOrders = findViewById(R.id.orderRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        userId = "1";
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList, this);
        recyclerViewOrders.setAdapter(orderAdapter);

        if (userId != null) {
            Log.d("callAPI", "userId:" + userId);
            loadData(userId);
        }

    }

    private void loadData(String userId) {
        BookingService apiService = ApiClient.getRetrofitInstance().create(BookingService.class);

        // Show the loading indicator
        progressBar.setVisibility(View.VISIBLE);

        Call<BookingService.ResAllDTO> call = apiService.getBookings(userId);
        call.enqueue(new Callback<BookingService.ResAllDTO>() {
            @Override
            public void onResponse(Call<BookingService.ResAllDTO> call, Response<BookingService.ResAllDTO> response) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                Log.d("callAPI", "Done");
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body().result);
                    orderAdapter.notifyDataSetChanged();
                } else {
                    // Handle the case where result is null or response is not successful
                    Log.d("callAPI", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<BookingService.ResAllDTO> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                Log.d("callAPI", t.getMessage());
                // Show a user-friendly error message
                Toast.makeText(HistoryOrder.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



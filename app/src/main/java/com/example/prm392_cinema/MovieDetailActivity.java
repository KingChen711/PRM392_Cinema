package com.example.prm392_cinema;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.Adapters.DateAdapter;
import com.example.prm392_cinema.Adapters.FabAdapter;
import com.example.prm392_cinema.Models.Fab;
import com.example.prm392_cinema.Models.Showtime;
import com.example.prm392_cinema.Services.ApiClient;
import com.example.prm392_cinema.Services.FabService;
import com.example.prm392_cinema.Services.MovieService;
import com.example.prm392_cinema.Utils.DateGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_detail);

        recyclerView = findViewById(R.id.datesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getMovies();
        getShowtimes(this);
    }

    private void getMovies() {
        MovieService apiService = ApiClient.getRetrofitInstance().create(MovieService.class);

        // Call API with a query parameter
        Call<MovieService.MovieDto> call = apiService.getMovieDetail(1);  // Pass `id` as 1
        call.enqueue(new Callback<MovieService.MovieDto>() {
            @Override
            public void onResponse(Call<MovieService.MovieDto> call, Response<MovieService.MovieDto> response) {
                if (!response.isSuccessful()) return;

                MovieService.MovieDto res = response.body();

                if (res == null) return;

                ((TextView) findViewById(R.id.title)).setText(res.title);
                ((TextView) findViewById(R.id.description)).setText(res.description);
                ((TextView) findViewById(R.id.release)).setText("Phát hành: " + res.releaseDate);
                ((TextView) findViewById(R.id.duration)).setText("Thời lượng: " + res.duration + " phút");
                ((TextView) findViewById(R.id.rating)).setText(res.rating + "");
                ((TextView) findViewById(R.id.genre)).setText("Thể loại: " + res.genre);
            }

            @Override
            public void onFailure(Call<MovieService.MovieDto> call, Throwable t) {
                // Handle the error
                Log.d("callAPI", t.getMessage());
            }
        });
    }

    private void getShowtimes(Context context) {
        List<Showtime> showtimes = new ArrayList<>();

        MovieService apiService = ApiClient.getRetrofitInstance().create(MovieService.class);

        // Call API with a query parameter
        Call<MovieService.GetShowtimesResponse> call = apiService.getShowtimes(1);  // Pass `id` as 1
        call.enqueue(new Callback<MovieService.GetShowtimesResponse>() {
            @Override
            public void onResponse(Call<MovieService.GetShowtimesResponse> call, Response<MovieService.GetShowtimesResponse> response) {
                if (!response.isSuccessful()) return;
                MovieService.GetShowtimesResponse res = response.body();

                for (MovieService.ShowtimeDto showtime : res.result.data) {
                    showtimes.add(new Showtime(showtime.getShowtimeId(), showtime.getMovieId(), showtime.getHallId(), showtime.getSeatPrice(), showtime.getShowDate(), showtime.hall.hallName));
                }

                List<DateGroup> dateGroups = DateGroup.groupShowtimesByDate(showtimes);
                DateAdapter dateAdapter = new DateAdapter(context, dateGroups);
                recyclerView.setAdapter(dateAdapter);
            }

            @Override
            public void onFailure(Call<MovieService.GetShowtimesResponse> call, Throwable t) {
                // Handle the error
                Log.d("callAPI", t.getMessage());
            }
        });
    }
}
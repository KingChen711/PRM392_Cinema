package com.example.prm392_cinema.Services;

import com.example.prm392_cinema.Models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("/api/movies")
    Call<List<Movie>> getMovies(
            @Query("genre") String genre,
            @Query("language") String language
    );

}

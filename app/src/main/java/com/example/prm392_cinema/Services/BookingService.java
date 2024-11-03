package com.example.prm392_cinema.Services;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface BookingService {
    @GET("/api/Booking/{bookingId}")
    Call<BookingDetailDTO> getBookingDetail(
            @Path("bookingId") String bookingId
    );

    @PUT("/api/Booking")
    Call<BookingDetailDTO> updateBookingStatus(
            @Query("bookingId") String bookingId,
            @Query("status") String status
    );

     public class BookingDetailDTO {
        public int bookingId;
        public String userName;
        public String hallName;
        public String movieName;
        public String showDate;
        public String bookingDate;
        public String[] seatNames;
        public List<FabDetail> fabDetails;
        public String status;
        public int totalPrice;
    }

    public class FabDetail {
        public String foodName;
        public int amount;
        public int price;
    }
}

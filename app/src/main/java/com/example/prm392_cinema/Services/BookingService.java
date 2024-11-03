package com.example.prm392_cinema.Services;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface BookingService {
    @GET("/api/Booking/{bookingId}")
    Call<BookingDetailDTO> getBookingDetail(
            @Path("bookingId") String bookingId
    );

    @POST("/api/Booking/create-booking")
    Call<CreateBookingResponseDto> createBooking(@Body CreateBookingDto dto);

    @PUT("/api/Booking")
    Call<BookingDetailDTO> updateBookingStatus(
            @Query("bookingId") String bookingId,
            @Query("status") String status
    );

    public class CreateBookingDto {
        public int userId;
        public int showTimeId;
        public List<Integer> listSeatId;
        public int totalPrice;

        public CreateBookingDto(int userId, int showTimeId, List<Integer> listSeatId, int totalPrice) {
            this.userId = userId;
            this.showTimeId = showTimeId;
            this.listSeatId = listSeatId;
            this.totalPrice = totalPrice;
        }
    }

    public class CreateBookingResponseDto {
        public CreateBookingResponseResultDto result;
    }

    public class CreateBookingResponseResultDto {
        public CreateBookingResponseResultDataDto data;
    }

    public class CreateBookingResponseResultDataDto {
        public int bookingId;
    }

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

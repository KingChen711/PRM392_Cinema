package com.example.prm392_cinema;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_cinema.Adapters.FabAdapter;
import com.example.prm392_cinema.Payment.Api.CreateOrder;
import com.example.prm392_cinema.Services.BookingService;

import org.json.JSONObject;

import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderPaymentActivity extends AppCompatActivity {
    Button btnPay;
    TextView userName, hallName, movieName, showDate, bookingDate, seatNames, status, totalPrice;

    private RecyclerView recyclerView;
    private FabAdapter fabDetailAdapter;
    private List<BookingService.FabDetail> fabDetailList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        String orderId = getIntent().getStringExtra("orderId");


//        if (orderId != null) {
//            loadOrderDetails(orderId);
//        } else {
//            Toast.makeText(this, "Order ID không hợp lệ!", Toast.LENGTH_SHORT).show();
//        }


        btnPay = findViewById(R.id.buttonThanhToan);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

//        Intent intent = getIntent();
//
//        txtQuantity.setText(intent.getStringExtra("quantity"));
//
//        Double total = intent.getDoubleExtra("total", (double) 0);
//        String totalString = String.format("%.0f", total);

//        txtTotal.setText("1000");
//        txtTotal.setText(Double.toString(total));


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder(10000+"");

//                    lblZpTransToken.setVisibility(View.VISIBLE);
                    String code = data.getString("return_code");
                    Toast.makeText(getApplicationContext(), "Tiếp tục với ZALO Pay", Toast.LENGTH_LONG).show();

                    if (code.equals("1")) {
                        String token = data.getString("zp_trans_token");
                        ZaloPaySDK.getInstance().payOrder(OrderPaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(OrderPaymentActivity.this).setTitle("Payment Success").setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).setNegativeButton("Cancel", null).show();
                                    }

                                });
                                IsLoading();

                                Intent intentSuccess = new Intent(OrderPaymentActivity.this, PaymentNotification.class);
                                intentSuccess.putExtra("result", "Thanh toán thành công");
                                startActivity(intentSuccess);
                            }

                            @Override
                            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                new AlertDialog.Builder(OrderPaymentActivity.this).setTitle("User Cancel Payment").setMessage(String.format("zpTransToken: %s \n", zpTransToken)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setNegativeButton("Cancel", null).show();
                                Toast.makeText(getApplicationContext(), "Thanh toán thất bại", Toast.LENGTH_LONG).show();
//                                Intent intentCanceled = new Intent(OrderPaymentActivity.this, PaymentNotification.class);
//                                intentCanceled.putExtra("result", "Thanh toán thất bại");
//                                startActivity(intentCanceled);
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                new AlertDialog.Builder(OrderPaymentActivity.this).setTitle("Payment Fail").setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setNegativeButton("Cancel", null).show();
                                Toast.makeText(getApplicationContext(), "Thanh toán thất bại", Toast.LENGTH_LONG).show();
//                                Intent intentCanceled = new Intent(OrderPaymentActivity.this, PaymentNotification.class);
//                                intentCanceled.putExtra("result", "Thanh toán thất bại");
//                                startActivity(intentCanceled);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void IsDone() {
        btnPay.setVisibility(View.VISIBLE);
    }

    private void IsLoading() {
        btnPay.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}

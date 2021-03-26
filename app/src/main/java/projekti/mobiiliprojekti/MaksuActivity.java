package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigDecimal;

import dk.mobilepay.sdk.Country;
import dk.mobilepay.sdk.MobilePay;
import dk.mobilepay.sdk.ResultCallback;
import dk.mobilepay.sdk.model.FailureResult;
import dk.mobilepay.sdk.model.Payment;
import dk.mobilepay.sdk.model.SuccessResult;

public class MaksuActivity extends AppCompatActivity {

    boolean isMobilePayInstalled;
    int MOBILEPAY_PAYMENT_REQUEST_CODE = 1337;
    Button maksuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maksu);
        MobilePay.getInstance().init("APPFI0000000000", Country.FINLAND);
        isMobilePayInstalled = MobilePay.getInstance().isMobilePayInstalled(getApplicationContext());
        maksuButton = findViewById(R.id.buttonMaksu);

        maksuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payNow();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == MOBILEPAY_PAYMENT_REQUEST_CODE) {
            MobilePay.getInstance().handleResult(resultCode, data, new ResultCallback() {
                @Override
                public void onSuccess(SuccessResult successResult) {
                    Toast.makeText(getApplicationContext(),"Maksu suoritettu!",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(FailureResult failureResult) {
                    Toast.makeText(getApplicationContext(),"Maksu epäonnistui", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(String s) {
                    Toast.makeText(getApplicationContext(),"Maksu peruutettu",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void payNow() {
        if (isMobilePayInstalled) {
            Payment payment = new Payment();
            payment.setProductPrice(BigDecimal.valueOf(10.0));
            payment.setOrderId("63577888445");

            Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(payment);
            startActivityForResult(paymentIntent, MOBILEPAY_PAYMENT_REQUEST_CODE);
        }
        else {
            Intent intent = MobilePay.getInstance().createDownloadMobilePayIntent(getApplicationContext());
            startActivity(intent);
        }
    }
}
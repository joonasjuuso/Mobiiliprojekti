package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class varmistusActivity extends AppCompatActivity  {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();
    Button checkBtn;
    boolean IS_EMAIL_VERIFIED = false;
    private int count = 0;
    private Handler handler = new Handler();
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varmistus);
        Intent i = new Intent(this, LoginActivity.class);
        checkBtn = (Button) findViewById(R.id.checkBtn);
        Log.e("Tag","oncreate");
        checkBtn.setOnClickListener(View -> {
            backtoLogin();
        });
        content();
    }

    public void backtoLogin() {
        handler.removeCallbacks(runnable);
        Intent setIntent = new Intent(varmistusActivity.this, LoginActivity.class);
        startActivity(setIntent);
        finish();
    }

    public boolean checkVerificationStatus() {
        currentUser.reload();
        Log.e("Tag", "Verifying");
        Log.e("Tag", String.valueOf(currentUser.isEmailVerified()));
        if (currentUser.isEmailVerified()) {
            IS_EMAIL_VERIFIED = true;
            Toast.makeText(getApplicationContext(), "Email verification complete!", Toast.LENGTH_LONG).show();
            Intent setIntent = new Intent(varmistusActivity.this, LoginActivity.class);
            startActivity(setIntent);
            finish();
            return true;
        }
        return false;
    }

    //Ajastus jolla varmennus tsekataan 2 sekunnin välein
    public void content() {
        count++;
        refresh(2000);
    }
    private void refresh(int milliseconds) {

        runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, milliseconds);
        if(checkVerificationStatus()) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        Intent setIntent = new Intent(this, LoginActivity.class);
        startActivity(setIntent);
        finish();
    }
}
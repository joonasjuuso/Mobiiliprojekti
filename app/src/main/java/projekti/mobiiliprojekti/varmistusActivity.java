package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class varmistusActivity extends AppCompatActivity  {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();
    Button checkBtn;
    boolean IS_EMAIL_VERIFIED = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varmistus);
        checkBtn = (Button) findViewById(R.id.checkBtn);
        Log.e("Tag","oncreate");
        checkBtn.setOnClickListener(View -> {
            startCheck();
        });
    }

    public void startCheck() {
        currentUser.reload();
        Log.e("Tag", "Verifying");
        Log.e("Tag", String.valueOf(currentUser.isEmailVerified()));
        if (currentUser.isEmailVerified()) {
            IS_EMAIL_VERIFIED = true;
            Toast.makeText(getApplicationContext(), "Sähköposti varmennettu!", Toast.LENGTH_LONG).show();
            Intent mokkiIntent = new Intent(this, Mokki_List.class);
            startActivity(mokkiIntent);
            finish();
        }
    }
}
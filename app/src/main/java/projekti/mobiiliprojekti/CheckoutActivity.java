package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckoutActivity extends AppCompatActivity {

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();

    private Button korttiBtn;
    private Button mobilepayBtn;

    private int summa;
    private String vuokraaja;
    private String[] paivamaarat = {};
    private String vuokraOtsikko;
    private String lisahommat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mobilepayBtn = findViewById(R.id.toggleMobilepay);
        korttiBtn = findViewById(R.id.toggleKortti);

        mobilepayBtn.setOnClickListener(v -> {
            Intent mobilepayIntent = new Intent(this,MaksuActivity.class);
            mobilepayIntent.putExtra("summa",summa);
            startActivity(mobilepayIntent);
        });

        korttiBtn.setOnClickListener(v -> {
          //TODO  Intent korttiIntent = new Intent();
        });
    }

    private String getName() {
       
        return
    }
}
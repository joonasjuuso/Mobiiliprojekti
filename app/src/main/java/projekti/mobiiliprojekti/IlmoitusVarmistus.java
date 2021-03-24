package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class IlmoitusVarmistus extends AppCompatActivity {

    private String eOtsikko;
    private String eKuvaus;
    private String eHinta;

    private Button bTakaisinIlmoitukseen;
    private Button bAsetaVuokralle;

    DatabaseReference dbMokki;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilmoitus_varmistus);

        dbMokki = FirebaseDatabase.getInstance().getReference("Mökit");

        bTakaisinIlmoitukseen = findViewById(R.id.bTakaisinIlmoitukseen);
        bAsetaVuokralle = findViewById(R.id.bAsetaVuokralle);

        Intent varmistaIntent = getIntent();
        eOtsikko = varmistaIntent.getStringExtra("eOtsikko");
        eKuvaus = varmistaIntent.getStringExtra("eKuvaus");
        eHinta = varmistaIntent.getStringExtra("eHinta");

        TextView sOtsikko = findViewById(R.id.sOtsikko);
        TextView sKuvaus = findViewById(R.id.sKuvaus);
        TextView sHinta = findViewById(R.id.sHinta);

        sOtsikko.setText(eOtsikko);
        sKuvaus.setText(eKuvaus);
        sHinta.setText(eHinta);

        bTakaisinIlmoitukseen.setOnClickListener( view -> {
            Intent takaisinIlmoitukseen = new Intent(this, LaitaVuokralle.class);
            startActivity(takaisinIlmoitukseen);
        });


        bAsetaVuokralle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMokki();
            }
        });
    }

    private void addMokki()
    {

            eOtsikko = dbMokki.push().getKey();

            MokkiItem mokki = new MokkiItem(eOtsikko, eKuvaus, eHinta);
            dbMokki.child(eOtsikko).setValue(mokki);


    }
}
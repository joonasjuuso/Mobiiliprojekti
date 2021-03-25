package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class IlmoitusVarmistus extends AppCompatActivity {

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();

    private String eOtsikko;
    private String eHinta;
    private String eOsoite;
    private String eHuoneet;
    private String eNeliot;
    private String eLammitys;
    private String eVesi;
    private String eSauna;
    private String eKuvaus;
    private String eOtsikkoID;
    private String eVuokraaja;
    //private String eOmistaja;

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
        eHinta = varmistaIntent.getStringExtra("eHinta");
        eOsoite = varmistaIntent.getStringExtra("eOsoite");
        eHuoneet = varmistaIntent.getStringExtra("eHuoneet");
        eNeliot = varmistaIntent.getStringExtra("eNeliot");
        eLammitys = varmistaIntent.getStringExtra("eLammitys");
        eVesi = varmistaIntent.getStringExtra("eVesi");
        eSauna = varmistaIntent.getStringExtra("eSauna");
        eKuvaus = varmistaIntent.getStringExtra("eKuvaus");

        TextView sOtsikko = findViewById(R.id.sOtsikko);
        TextView sHinta = findViewById(R.id.sHinta);
        TextView sOsoite = findViewById(R.id.sOsoite);
        TextView sHuoneet = findViewById(R.id.sHuoneet);
        TextView sNeliot = findViewById(R.id.sNeliot);
        TextView sLammitys = findViewById(R.id.sLammitys);
        TextView sVesi = findViewById(R.id.sVesi);
        TextView sSauna = findViewById(R.id.sSauna);
        TextView sKuvaus = findViewById(R.id.sKuvaus);

        sOtsikko.setText(eOtsikko);
        sHinta.setText(eHinta);
        sOsoite.setText(eOsoite);
        sHuoneet.setText(eHuoneet);
        sNeliot.setText(eNeliot);
        sLammitys.setText(eLammitys);
        sVesi.setText(eVesi);
        sSauna.setText(eSauna);
        sKuvaus.setText(eKuvaus);

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
        eVuokraaja = currentUser.getDisplayName();

        eOtsikkoID = eOtsikko;

        eOtsikkoID = dbMokki.push().getKey();

        MokkiItem mokki = new MokkiItem(eOtsikko, eHinta, eOsoite, eHuoneet, eNeliot, eLammitys, eVesi, eSauna, eKuvaus, eOtsikkoID, eVuokraaja/*, eOmistaja*/);
        dbMokki.child(eOtsikkoID).setValue(mokki);
    }
}
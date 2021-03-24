package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LaitaVuokralle extends AppCompatActivity {

    private Button bTakaisinMokkiListaan;
    private Button bAsetaVuokralle;

    private EditText EditOtsikko;
    private EditText EditKuvaus;
    private EditText EditHinta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laita_vuokralle);

        bTakaisinMokkiListaan = findViewById(R.id.bTakaisinMokkiListaan);
        bAsetaVuokralle = findViewById(R.id.bAsetaVuokralle);

        bTakaisinMokkiListaan.setOnClickListener(View -> {
            Intent takaisinMokkiListaan = new Intent(this, Mokki_List.class);
            startActivity(takaisinMokkiListaan);
        });


        bAsetaVuokralle =  findViewById(R.id.bAsetaVuokralle);
        bAsetaVuokralle.setOnClickListener(view -> {
            Intent varmistaIntent = new Intent(this, IlmoitusVarmistus.class);
            EditOtsikko =  findViewById(R.id.EditOtsikko);
            String eOtsikko = EditOtsikko.getText().toString();

            EditKuvaus = findViewById(R.id.EditKuvaus);
            String eKuvaus = EditKuvaus.getText().toString();

            EditHinta = findViewById(R.id.EditHinta);
            String eHinta = EditHinta.getText().toString();
            varmistaIntent.putExtra("eOtsikko", eOtsikko);
            varmistaIntent.putExtra("eKuvaus", eKuvaus);
            varmistaIntent.putExtra("eHinta", eHinta);
            startActivity(varmistaIntent);
        });

    }
}
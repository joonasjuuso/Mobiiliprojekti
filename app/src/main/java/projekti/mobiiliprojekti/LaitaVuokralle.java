package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LaitaVuokralle extends AppCompatActivity {

    private Button bTakaisinMokkiListaan;
    private Button bAsetaVuokralle;

    private EditText EditOtsikko;
    private EditText EditHinta;
    private EditText EditOsoite;
    private Spinner EditHuoneet;
    private EditText EditNeliot;
    private EditText EditLammitys;
    private Spinner EditVesi;
    private Spinner EditSauna;
    private EditText EditKuvaus;


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

            EditHinta = findViewById(R.id.EditHinta);
            String eHinta = EditHinta.getText().toString();

            EditOsoite = findViewById(R.id.EditOsoite);
            String eOsoite = EditOsoite.getText().toString();

            EditHuoneet = findViewById(R.id.HuoneMaaraSpinner);
            //EditHuoneet.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            String eHuoneet = EditHuoneet.getSelectedItem().toString();

            EditNeliot = findViewById(R.id.EditNelioMaara);
            String eNeliot = EditNeliot.getText().toString();

            EditLammitys = findViewById(R.id.EditLammitys);
            String eLammitys = EditLammitys.getText().toString();

            EditVesi = findViewById(R.id.VesiSpinner);
            //EditVesi.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            String eVesi = EditVesi.getSelectedItem().toString();

            EditSauna = findViewById(R.id.SaunaSpinner);
            //EditSauna.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            String eSauna = EditSauna.getSelectedItem().toString();

            EditKuvaus = findViewById(R.id.EditKuvaus);
            String eKuvaus = EditKuvaus.getText().toString();


            varmistaIntent.putExtra("eOtsikko", eOtsikko);
            varmistaIntent.putExtra("eHinta", eHinta);
            varmistaIntent.putExtra("eOsoite", eOsoite);
            varmistaIntent.putExtra("eHuoneet", eHuoneet);
            varmistaIntent.putExtra("eNeliot", eNeliot);
            varmistaIntent.putExtra("eLammitys", eLammitys);
            varmistaIntent.putExtra("eVesi", eVesi);
            varmistaIntent.putExtra("eSauna", eSauna);
            varmistaIntent.putExtra("eKuvaus", eKuvaus);

            startActivity(varmistaIntent);
        });

    }
}
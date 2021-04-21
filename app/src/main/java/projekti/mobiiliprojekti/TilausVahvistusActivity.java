package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class TilausVahvistusActivity extends AppCompatActivity {

    private TextView tOtsikko;
    private TextView tOsoite;
    private TextView tNimi;
    private TextView tNro;
    private TextView tSposti;
    private TextView tPaivat;
    private TextView tOrderID;

    private String nimi;
    private String osoite;
    private String otsikko;
    private String nro;
    private String sposti;
    private String orderID;
    private ArrayList<String> paivat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilaus_vahvistus);

        paivat = new ArrayList<String>();
        tSposti = findViewById(R.id.textViewTilausSahkoposti);
        tNimi = findViewById(R.id.textViewTilausMyyjä);
        tOsoite = findViewById(R.id.textViewTilausOsoite);
        tNro = findViewById(R.id.textViewTilausPuhNro);
        tOtsikko = findViewById(R.id.textViewTilausOtsikko);
        tPaivat = findViewById(R.id.textViewTilausPaivat);
        tOrderID = findViewById(R.id.textViewTilausNumero);

        Intent onnistuiIntent = getIntent();
        sposti = onnistuiIntent.getStringExtra("vuokraPosti");
        nimi = onnistuiIntent.getStringExtra("vuokranantaja");
        otsikko = onnistuiIntent.getStringExtra("otsikko");
        nro = onnistuiIntent.getStringExtra("vuokraNro");
        osoite = onnistuiIntent.getStringExtra("osoite");
        orderID = onnistuiIntent.getStringExtra("orderID");
        paivat = onnistuiIntent.getStringArrayListExtra("paivat");

        tOrderID.setText("Tilausnumero " + orderID);
        tSposti.setText(sposti);
        tNimi.setText(nimi);
        tOsoite.setText("Mökin osoite on " + osoite);
        tOtsikko.setText("Mökki: " + otsikko);
        tPaivat.setText("Varaamanne päivät: " + paivat.toString());
        if(nro.equals("123")) {
            tNro.setVisibility(View.GONE);
            Log.d("tag","if-yes");
        }
        else {
            tNro.setText(nro);
        }

    }
}
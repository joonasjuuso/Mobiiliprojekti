package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MokkiNakyma extends AppCompatActivity {

    Button bTakaisinMokkilistaan;
    Button bVuokraa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki_nakyma);

        Intent intent = getIntent();
        MokkiItem mokkiItem = intent.getParcelableExtra("Mokki");

        //int MokkiImage = mokkiItem.getMokkiImage();
        String MokkiOtsikko = mokkiItem.getOtsikko();
        String MokkiHinta = mokkiItem.getHinta();
        String MokkiOsoite = mokkiItem.getOsoite();
        String MokkiHuoneet = mokkiItem.getHuoneMaara();
        String MokkiNelio = mokkiItem.getNelioMaara();
        String MokkiLammitys = mokkiItem.getLammitys();
        String MokkiVesi = mokkiItem.getVesi();
        String MokkiSauna = mokkiItem.getSauna();
        String MokkiKuvaus = mokkiItem.getKuvaus();
        String OtsikkoID = mokkiItem.getOtsikkoID();
        String Vuokraaja = mokkiItem.getVuokraaja();
        //String Mokkiomistaja = mokkiItem.getOmistaja();

        //ImageView imageViewMokki = findViewById(R.id.ImageMokkiNakyma);
        //imageViewMokki.setImageResource(MokkiImage);

        TextView textViewOtsikko = findViewById(R.id.OtsikkoMokkiNakyma);
        textViewOtsikko.setText(MokkiOtsikko);

        TextView textViewHinta = findViewById(R.id.HintaMokkiNakyma);
        textViewHinta.setText(MokkiHinta);

        TextView textViewOsoite = findViewById(R.id.OsoiteMokkiNakyma);
        textViewOsoite.setText(MokkiOsoite);

        TextView textViewHuoneet = findViewById(R.id.HuoneetMokkiNakyma);
        textViewHuoneet.setText(MokkiHuoneet);

        TextView textViewNeliot = findViewById(R.id.NeliotMokkiNakyma);
        textViewNeliot.setText(MokkiNelio);

        TextView textViewLammitys = findViewById(R.id.LammitysMokkiNakyma);
        textViewLammitys.setText(MokkiLammitys);

        TextView textViewVesi = findViewById(R.id.VesiMokkiNakyma);
        textViewVesi.setText(MokkiVesi);

        TextView textViewSauna = findViewById(R.id.SaunaMokkiNakyma);
        textViewSauna.setText(MokkiSauna);

        TextView textViewKuvaus = findViewById(R.id.KuvausMokkiNakyma);
        textViewKuvaus.setText(MokkiKuvaus);




        bTakaisinMokkilistaan = findViewById(R.id.bTakaisinMokkiListaan);
        bVuokraa = findViewById(R.id.bVuokraa);

        bTakaisinMokkilistaan.setOnClickListener(View ->{
            Intent mokkiIntent = new Intent(this,Mokki_List.class);
            startActivity(mokkiIntent);
        });

    }

    public void onClick_Takaisin(View view) {
        Intent i = new Intent(this, Mokki_List.class);
        startActivity(i);
    }
}

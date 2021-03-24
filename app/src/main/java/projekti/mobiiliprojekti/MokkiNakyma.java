package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MokkiNakyma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki_nakyma);

        Intent intent = getIntent();
        MokkiItem mokkiItem = intent.getParcelableExtra("Mokki");

        int MokkiImage = mokkiItem.getMokkiImage();
        String MokkiOtsikko = mokkiItem.getOtsikko();
        String MokkiKuvaus = mokkiItem.getKuvaus();
        String MokkiHinta = mokkiItem.getHinta();

        ImageView imageViewMokki = findViewById(R.id.ImageMokkiNakyma);
        imageViewMokki.setImageResource(MokkiImage);

        TextView textViewOtsikko = findViewById(R.id.OtsikkoMokkiNakyma);
        textViewOtsikko.setText(MokkiOtsikko);

        TextView textViewKuvaus = findViewById(R.id.KuvausMokkiNakyma);
        textViewKuvaus.setText(MokkiKuvaus);

        TextView textViewHinta = findViewById(R.id.HintaMokkiNakyma);
        textViewHinta.setText(MokkiHinta);
    }

    public void onClick_Takaisin(View view) {
        Intent i = new Intent(this, Mokki_List.class);
        startActivity(i);
    }
}

package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MokkiNakyma extends AppCompatActivity {

    Button bTakaisinMokkilistaan;
    private Button bVuokraa;
    private Button bMuokkaa;
    private ImageView ImageViewDelete;
    Button bChat;
    private Context mContext;

    private String setVisibility = "";

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef =  storage.getReference();
    private DatabaseReference fbDatabaseRef;
    private String deleteKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki_nakyma);

        fbDatabaseRef = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/");

        Intent intent = getIntent();
        MokkiItem mokkiItem = intent.getParcelableExtra("Mokki");

        String MokkiImage = mokkiItem.getMokkiImage();
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
        String VuokraajaID = mokkiItem.getVuokraajaID();
        //String Mokkiomistaja = mokkiItem.getOmistaja();

        ImageView imageViewMokki = findViewById(R.id.ImageMokkiNakyma);
        Picasso.get().load(mokkiItem.getMokkiImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(imageViewMokki);

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

        bVuokraa = findViewById(R.id.bVuokraa);
        bMuokkaa = findViewById(R.id.bMuokkaa);
        ImageViewDelete = findViewById(R.id.ImageViewDelete);

        //bVuokraa.setVisibility(View.GONE);
        bMuokkaa.setVisibility(View.GONE);
        ImageViewDelete.setVisibility(View.GONE);


        setVisibility = intent.getStringExtra("setVisibility");
        if(setVisibility.matches("omatMokit")){
            bVuokraa.setVisibility(View.GONE);
            bMuokkaa.setVisibility(View.VISIBLE);
            ImageViewDelete.setVisibility(View.VISIBLE);
        }else if(setVisibility.matches("kaikkiMokit")){
            bVuokraa.setVisibility(View.VISIBLE);
            bMuokkaa.setVisibility(View.GONE);
            //ImageViewDelete.setVisibility(View.GONE);
        }
        Log.d("Tag",setVisibility);


        deleteKey = intent.getStringExtra("deleteKey");
        ImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbDatabaseRef.child(deleteKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MokkiNakyma.this, "Mokki poistettu", Toast.LENGTH_LONG).show();
                        StorageReference imageRef = storage.getReferenceFromUrl(MokkiImage);
                        imageRef.delete();
                        ImageViewDelete.setVisibility(View.GONE);
                        bMuokkaa.setVisibility(View.GONE);
                        bChat.setVisibility(View.GONE);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MokkiNakyma.this, "Ei onnistunut", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



        bTakaisinMokkilistaan = findViewById(R.id.bTakaisinMokkiListaan);
        bChat = findViewById(R.id.chatBtn);

        bTakaisinMokkilistaan.setOnClickListener(View ->{
            Intent mokkiIntent = new Intent(this,Mokki_List.class);
            startActivity(mokkiIntent);
        });
        bChat.setOnClickListener(View -> {
            Intent chatIntent = new Intent(this,ChatActivity.class);
            chatIntent.putExtra("ID",VuokraajaID);
            chatIntent.putExtra("name",Vuokraaja);
            startActivity(chatIntent);
        });

    }

    public void onClick_Takaisin(View view) {
        Intent i = new Intent(this, Mokki_List.class);
        startActivity(i);
    }

}

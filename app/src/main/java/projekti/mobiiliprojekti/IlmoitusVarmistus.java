package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class IlmoitusVarmistus extends AppCompatActivity {

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();

    private DatabaseReference mDataBaseref;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef =  storage.getReference();

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
    private String MokkiKuva;
    private String eID;
    //private String eOmistaja;

    private Uri mImageUri;

    File file = new File (String.valueOf(mImageUri));

    private ImageView sImageUpload;

    DatabaseReference dbMokki;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilmoitus_varmistus);

        dbMokki = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/");

        Button bTakaisinIlmoitukseen = findViewById(R.id.bTakaisinIlmoitukseen);
        Button bAsetaVuokralle = findViewById(R.id.bAsetaVuokralle);


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
        sImageUpload = findViewById(R.id.ImageViewUpload);

        sOtsikko.setText(eOtsikko);
        sHinta.setText(eHinta);
        sOsoite.setText(eOsoite);
        sHuoneet.setText(eHuoneet);
        sNeliot.setText(eNeliot);
        sLammitys.setText(eLammitys);
        sVesi.setText(eVesi);
        sSauna.setText(eSauna);
        sKuvaus.setText(eKuvaus);

        if(currentUser!=null) {
            storageRef.child("Mökkien kuvia/" + currentUser.getUid()).getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Glide.with(getApplicationContext()).load(uri.toString()).into(sImageUpload);
                        MokkiKuva = uri.toString();
                    })
                    .addOnFailureListener(e -> sImageUpload.setImageResource(R.mipmap.ic_launcher));
        } else if(currentUser==null) {
            sImageUpload.setImageResource(R.mipmap.ic_launcher);
        }


        bTakaisinIlmoitukseen.setOnClickListener(view -> {
            Intent takaisinIlmoitukseen = new Intent(this, LaitaVuokralle.class);
            startActivity(takaisinIlmoitukseen);
        });


        bAsetaVuokralle.setOnClickListener(v -> addMokki());
        //getUrlAsync();

    }

    private void addMokki()
    {
        eVuokraaja = currentUser.getDisplayName();
        eID = currentUser.getUid();
        eOtsikkoID = eVuokraaja + eOtsikko;

        eOtsikkoID = dbMokki.push().getKey();

        MokkiItem mokki = new MokkiItem(MokkiKuva, eOtsikko, eHinta, eOsoite, eHuoneet, eNeliot, eLammitys,
                eVesi, eSauna, eKuvaus, eOtsikkoID, eVuokraaja, eID);

        dbMokki.child(eOtsikkoID).setValue(mokki);
    }

    private void getUrlAsync(String date)
    {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("Mökkien kuvia/" + currentUser.getDisplayName());
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUri) {
                MokkiKuva = String.valueOf(downloadUri);

                Glide.with(getApplicationContext()).load(downloadUri.toString()).into(sImageUpload);
                //return date;
            }
        });
    }

}
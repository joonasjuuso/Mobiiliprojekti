package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef =  storage.getReference();

    private Button korttiBtn;
    private Button mobilepayBtn;
    private TextView nameText;
    private TextView hintaText;
    private TextView paivaText;
    private TextView lisaText;
    private TextView otsikkoText;
    private TextView osoiteText;
    private ImageView takaisinBtn;
    private ImageView menuImage;

    private String summa;
    private String vuokraaja;
    private String vuokraOtsikko;
    private String lisahommat;
    private ArrayList<String> paivat;
    private String osoite;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mobilepayBtn = findViewById(R.id.toggleMobilepay);
        //korttiBtn = findViewById(R.id.toggleKortti);

        hintaText = findViewById(R.id.textViewHintaMaarat);
        nameText = findViewById(R.id.textViewVuokraajaNimi);
        otsikkoText = findViewById(R.id.textViewMökki);
        paivaText = findViewById(R.id.textViewPaivaMaarat);
        osoiteText = findViewById(R.id.textViewOsoiteMaarat);
        takaisinBtn = findViewById(R.id.goBack);
        menuImage = findViewById(R.id.profiiliKuva);

        Intent fromMokki = getIntent();
        vuokraaja = fromMokki.getStringExtra("name");
        summa = fromMokki.getStringExtra("hinta");
        vuokraOtsikko = fromMokki.getStringExtra("otsikko");
        osoite = fromMokki.getStringExtra("osoite");
        paivat = fromMokki.getStringArrayListExtra("paivat");
        Log.d("tag", String.valueOf(paivat));

        hintaText.setText(summa);
        nameText.setText(vuokraaja);
        otsikkoText.setText(vuokraOtsikko);
        paivaText.setText(String.valueOf(paivat));
        osoiteText.setText(osoite);

        mobilepayBtn.setOnClickListener(v -> {
            Intent mobilepayIntent = new Intent(this,MaksuActivity.class);
            mobilepayIntent.putExtra("summa",summa);
            startActivity(mobilepayIntent);
        });

        //korttiBtn.setOnClickListener(v -> {
          //TODO  Intent korttiIntent = new Intent();
        //});

        takaisinBtn.setOnClickListener(v -> {
            finish();
        });


        storageRef.child("ProfilePictures/"+currentUser.getUid()).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(getApplicationContext()).load(uri.toString()).circleCrop().into(menuImage);
                })
                .addOnFailureListener(e -> {
                    menuImage.setImageResource(R.mipmap.ic_launcher);
                });
    }
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void onClick_Usermenu(View view) {
        PopupMenu popup = new PopupMenu(this, menuImage);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.user:
                    Intent profiiliIntent = new Intent(this,ProfiiliActivity.class);
                    startActivity(profiiliIntent);
                    break;
                case R.id.chat:
                    Intent chatIntent = new Intent(this,ChatActivity.class);
                    startActivity(chatIntent);
                    finish();
                    break;
                case R.id.logout:
                    mauth.signOut();
                    Intent signOutIntent = new Intent(this, LoginActivity.class);
                    startActivity(signOutIntent);
                    finish();
                    break;
            }
            return false;
        });
        if(currentUser != null) {
            popup.inflate(R.menu.menu_list);
            if (currentUser.getDisplayName() != null) {
                popup.getMenu().findItem(R.id.user).setTitle(currentUser.getDisplayName());
            } else {
                popup.getMenu().findItem(R.id.user).setTitle(currentUser.getEmail());
            }
            popup.show();
        }
    }

}
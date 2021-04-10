package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.io.FileOutputStream;
import java.io.IOException;

public class LaitaVuokralle extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();
    //private FirebaseStorage storage = FirebaseStorage.getInstance();

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef =  storage.getReference();
    private final StorageReference mokkiRef = storageRef.child("Mökkien kuvia");

    private ImageView ImageViewUpload;

    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laita_vuokralle);

        Button bTakaisinMokkiListaan = findViewById(R.id.bTakaisinMokkiListaan);
        Button bAsetaVuokralle = findViewById(R.id.bAsetaVuokralle);


        Button bChooseimage = findViewById(R.id.bChooseImage);
        Button bUploadImage = findViewById(R.id.bUpload);
        ImageViewUpload = findViewById(R.id.ImageViewUpload);
        ProgressBar uploadImageProgressBar = findViewById(R.id.UploadImageProgressBar);


        bChooseimage.setOnClickListener(v -> OpenImageChooser());

        bUploadImage.setOnClickListener(v -> uploadImage());

        bTakaisinMokkiListaan.setOnClickListener(View -> {
            Intent takaisinMokkiListaan = new Intent(this, Mokki_List.class);
            startActivity(takaisinMokkiListaan);
        });

        bAsetaVuokralle.setOnClickListener(view -> AsetaVuokralle());

    }

    private void AsetaVuokralle()
    {
        Intent varmistaIntent = new Intent(this, IlmoitusVarmistus.class);

        EditText editOtsikko = findViewById(R.id.EditOtsikko);
        String mOtsikko = editOtsikko.getText().toString();

        EditText editHinta = findViewById(R.id.EditHinta);
        String eHinta = editHinta.getText().toString();

        EditText editOsoite = findViewById(R.id.EditOsoite);
        String eOsoite = editOsoite.getText().toString();

        Spinner editHuoneet = findViewById(R.id.HuoneMaaraSpinner);
        String eHuoneet = editHuoneet.getSelectedItem().toString();

        EditText editNeliot = findViewById(R.id.EditNelioMaara);
        String eNeliot = editNeliot.getText().toString();

        EditText editLammitys = findViewById(R.id.EditLammitys);
        String eLammitys = editLammitys.getText().toString();

        Spinner editVesi = findViewById(R.id.VesiSpinner);
        String eVesi = editVesi.getSelectedItem().toString();

        Spinner editSauna = findViewById(R.id.SaunaSpinner);
        String eSauna = editSauna.getSelectedItem().toString();

        EditText editKuvaus = findViewById(R.id.EditKuvaus);
        String eKuvaus = editKuvaus.getText().toString();

        varmistaIntent.putExtra("eOtsikko", mOtsikko);
        varmistaIntent.putExtra("eHinta", eHinta);
        varmistaIntent.putExtra("eOsoite", eOsoite);
        varmistaIntent.putExtra("eHuoneet", eHuoneet);
        varmistaIntent.putExtra("eNeliot", eNeliot);
        varmistaIntent.putExtra("eLammitys", eLammitys);
        varmistaIntent.putExtra("eVesi", eVesi);
        varmistaIntent.putExtra("eSauna", eSauna);
        varmistaIntent.putExtra("eKuvaus", eKuvaus);

        startActivity(varmistaIntent);
    }

    private void OpenImageChooser()
    {
        Intent chooseImageIntent = new Intent();
        chooseImageIntent.setType("image/*");
        chooseImageIntent.setAction(chooseImageIntent.ACTION_GET_CONTENT);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try
            {
                BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri), null, options);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                ImageViewUpload.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (mImageUri != null) {

            mokkiRef.child("/" + currentUser.getUid()).putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getApplicationContext(),"Kuva Lisätty!",Toast.LENGTH_LONG).show();
                storageRef.child("Mökkien kuvia/"+currentUser.getUid()).getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            Glide.with(getApplicationContext()).load(uri.toString()).into(ImageViewUpload);
                        });
            })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Virhe kuvan päivityksessä",Toast.LENGTH_LONG).show())
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    });
        }
    }

}
package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class LaitaVuokralle extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();


    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef =  storage.getReference();
    private final StorageReference mokkiRef = storageRef.child("Mökkien kuvia/");

    private ImageView ImageViewUpload;
    private Button bUploadImage;
    private Button bChooseimage;

    private Uri mImageUri;
    private ProgressBar uploadImageProgressBar;

    private EditText editOtsikko;
    private String mOtsikko;
    private String UID ;

    boolean filled = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laita_vuokralle);

        Button bTakaisinMokkiListaan = findViewById(R.id.bTakaisinMokkiListaan);
        Button bAsetaVuokralle = findViewById(R.id.bAsetaVuokralle);


        bChooseimage = findViewById(R.id.bChooseImage);
        bUploadImage = findViewById(R.id.bUpload);
        ImageViewUpload = findViewById(R.id.ImageViewUpload);
        uploadImageProgressBar = findViewById(R.id.UploadImageProgressBar);

        bUploadImage.setVisibility(View.GONE);


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

        editOtsikko = findViewById(R.id.EditOtsikko);
        mOtsikko = editOtsikko.getText().toString();

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
        varmistaIntent.putExtra("eUID", UID);

        if(mOtsikko.matches("")&& eHinta.matches("") && eOsoite.matches("")
                 && eNeliot.matches("") && eLammitys.matches("")
                && eKuvaus.matches("")) {
            Toast.makeText(this, "Lisää vaadittavat tiedot mökistäsi", Toast.LENGTH_LONG).show();
            filled = false;
            filled = true;
        }else if(filled == true){
            startActivity(varmistaIntent);
            filled = true;
        }
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
            try {
                BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri), null, options);
                int imageWidth = options.outWidth;
                int imageHeight = options.outHeight;
                if (imageHeight >= 1) {
                    if (imageWidth >= 1) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                        ImageViewUpload.setImageBitmap(bitmap);
                        bUploadImage.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Virhe kuvan kanssa, syötä toinen kuva", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Virhe kuvan kanssa, syötä toinen kuva", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getfileExtension(Uri uri)
    {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    private void uploadImage() {
        if (getfileExtension(mImageUri).equals("jpg") || getfileExtension(mImageUri).equals("png") || getfileExtension(mImageUri).equals("jpeg")) {
            Log.e("Tag", getfileExtension(mImageUri));
            if (mImageUri != null) {

                UID = UUID.randomUUID().toString();

                mokkiRef.child("/" + currentUser.getUid() + UID).putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                uploadImageProgressBar.setProgress(0);
                            }
                        }, 5000);
                        mokkiRef.child("Mökkien kuvia/"  + currentUser.getUid() + UID).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                   Picasso.get().load(uri).into(ImageViewUpload);
                                });
                        Toast.makeText(getApplicationContext(), "Kuva lisätty", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Virhe kuvan latauksessa", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                uploadImageProgressBar.setProgress((int)progress);
                                bUploadImage.setVisibility(View.GONE);
                            }
                        });
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Virheellinen tiedostomuoto",Toast.LENGTH_SHORT).show();
        }
    }
}
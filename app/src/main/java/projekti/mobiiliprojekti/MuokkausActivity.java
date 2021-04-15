package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class MuokkausActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef =  storage.getReference();
    private final StorageReference mokkiRef = storageRef.child("Mökkien kuvia/");

    private DatabaseReference dbMokki;

    private EditText editOtsikko;
    private EditText editHinta;
    private EditText editOsoite;
    private Spinner editHuoneet;
    private EditText editNeliot;
    private EditText editLammitys;
    private Spinner editVesi;
    private Spinner editSauna;
    private  EditText editKuvaus;

    private ImageView ImageViewUpload;
    //private Button bUploadImage;
    private Button bChooseimage;
    private Button bTakaisinMokkiListaan;

    private Button bHyvaksy;

    private Uri mImageUri;
    private ProgressBar uploadImageProgressBar;

    private String eOtsikko;
    private String eHinta;
    private String eOsoite;
    private String eHuoneet;
    private String eNeliot;
    private String eLammitys;
    private String eVesi;
    private String eSauna;
    private String eKuvaus;
    private String UID;
    private String eID;
    private String eVuokraaja;
    private String eOtsikkoID;
    private String MokkiKuva;
    private String muokkaaKey;

    private String muokkausKuva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muokkaus);

        dbMokki = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/");


        bHyvaksy = findViewById(R.id.bHyväksy);
        //bHyvaksy.setVisibility(View.GONE);
        uploadImageProgressBar = findViewById(R.id.UploadImageProgressBar);

        editOtsikko = findViewById(R.id.EditOtsikko);
        eOtsikko = editOtsikko.getText().toString();
        //eOtsikko = takaisinIlmoitukseen.getStringExtra("eOtsikko");
        editOtsikko.setText(eOtsikko);

        ImageViewUpload = findViewById(R.id.ImageViewUpload);
        Picasso.get().load(MokkiKuva).into(ImageViewUpload);

        editHinta = findViewById(R.id.EditHinta);
        eHinta = editHinta.getText().toString();
        //eHinta = takaisinIlmoitukseen.getStringExtra("eHinta");
        editHinta.setText(eHinta);

        editOsoite = findViewById(R.id.EditOsoite);
        eOsoite = editOsoite.getText().toString();
        //eOsoite = takaisinIlmoitukseen.getStringExtra("eOsoite");
        editOsoite.setText(eOsoite);

        editHuoneet = findViewById(R.id.HuoneMaaraSpinner);
        eHuoneet = editHuoneet.getSelectedItem().toString();

        editNeliot = findViewById(R.id.EditNelioMaara);
        eNeliot = editNeliot.getText().toString();
        //eNeliot = takaisinIlmoitukseen.getStringExtra("eNeliot");
        editNeliot.setText(eNeliot);

        editLammitys = findViewById(R.id.EditLammitys);
        eLammitys = editLammitys.getText().toString();
        //eLammitys = takaisinIlmoitukseen.getStringExtra("eLammitys");
        editLammitys.setText(eLammitys);

        editVesi = findViewById(R.id.VesiSpinner);
        eVesi = editVesi.getSelectedItem().toString();

        editSauna = findViewById(R.id.SaunaSpinner);
        eSauna = editSauna.getSelectedItem().toString();

        editKuvaus = findViewById(R.id.EditKuvaus);
        eKuvaus = editKuvaus.getText().toString();
        //eKuvaus = takaisinIlmoitukseen.getStringExtra("eKuvaus");
        editKuvaus.setText(eKuvaus);

        Intent muokkausIntent = getIntent();
        eOtsikko = muokkausIntent.getStringExtra("eOtsikko");
        MokkiKuva = muokkausIntent.getStringExtra("eKuva");
        eHinta = muokkausIntent.getStringExtra("eHinta");
        eOsoite = muokkausIntent.getStringExtra("eOsoite");
        eHuoneet = muokkausIntent.getStringExtra("eHuoneet");
        eNeliot = muokkausIntent.getStringExtra("eNeliot");
        eLammitys = muokkausIntent.getStringExtra("eLammitys");
        eVesi = muokkausIntent.getStringExtra("eVesi");
        eSauna = muokkausIntent.getStringExtra("eSauna");
        eKuvaus = muokkausIntent.getStringExtra("eKuvaus");
        muokkaaKey = muokkausIntent.getStringExtra("muokkaaKey");


        editOtsikko.setText(eOtsikko);
        editHinta.setText(eHinta);
        editOsoite.setText(eOsoite);
        editNeliot.setText(eNeliot);
        editLammitys.setText(eLammitys);
        editKuvaus.setText(eKuvaus);
        Picasso.get().load(MokkiKuva).fit().centerCrop().into(ImageViewUpload);

        bTakaisinMokkiListaan = findViewById(R.id.bTakaisinMokkiListaan);
        bTakaisinMokkiListaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MuokkausActivity.this, Mokki_List.class);
                startActivity(intent);
            }
        });

        bChooseimage = findViewById(R.id.bChooseImage);
        bChooseimage.setVisibility(View.GONE);

        bChooseimage.setOnClickListener(v -> OpenImageChooser());
        bHyvaksy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadImage();
                updateMokki();
            }
        });

        checkText();
    }

    private void updateMokki()
    {
        HashMap hashmap = new HashMap();
        hashmap.put("otsikko", eOtsikko);
        hashmap.put("hinta", eHinta);
        hashmap.put("huoneMaara", eHuoneet);
        hashmap.put("osoite", eOsoite);
        hashmap.put("lammitys", eLammitys);
        hashmap.put("sauna", eSauna);
        hashmap.put("vesi", eVesi);
        hashmap.put("kuvaus", eKuvaus);
        hashmap.put("nelioMaara", eNeliot);
        //hashmap.put("mokkiImage", muokkausKuva);

        dbMokki.child(muokkaaKey).updateChildren(hashmap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MuokkausActivity.this, "JEE", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MuokkausActivity.this, Mokki_List.class);
                startActivity(intent);
            }
        });
    }

    private void OpenImageChooser()
    {
        StorageReference imageRef = storage.getReferenceFromUrl(MokkiKuva);

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
                    //bHyvaksy.setVisibility(View.VISIBLE);
                    if (imageWidth >= 1) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                        ImageViewUpload.setImageBitmap(bitmap);
                    }
                    else {
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
                Log.d("osoiteString",eOtsikko);

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
                                    Picasso.get().load(uri).fit().centerCrop().into(ImageViewUpload);
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
                                if(progress == 100){
                                    //AsetaVuokralle();
                                    //asetaMokki();
                                    updateMokki();
                                }
                            }
                        });
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Virheellinen tiedostomuoto",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkText() {
        editOtsikko.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                eOtsikko = editOtsikko.getText().toString();
            }
        });

        editHinta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                eHinta = editHinta.getText().toString();
            }
        });

        editOsoite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                eOsoite = editOsoite.getText().toString();
            }
        });

        editHuoneet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    eHuoneet = editHuoneet.getSelectedItem().toString();
                }else if(position == 1){
                    eHuoneet = editHuoneet.getSelectedItem().toString();
                }
                else if(position == 2){
                    eHuoneet = editHuoneet.getSelectedItem().toString();
                }
                else if(position == 3){
                    eHuoneet = editHuoneet.getSelectedItem().toString();
                }
                else if(position == 4){
                    eHuoneet = editHuoneet.getSelectedItem().toString();
                }
                else if(position == 5){
                    eHuoneet = editHuoneet.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editNeliot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                eNeliot = editNeliot.getText().toString();
            }
        });

        editLammitys.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                eLammitys = editLammitys.getText().toString();
            }
        });

        editVesi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    eVesi = editVesi.getSelectedItem().toString();
                }else if(position == 1){
                    eVesi = editVesi.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editSauna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    eSauna = editSauna.getSelectedItem().toString();
                }else if(position == 1){
                    eSauna = editSauna.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editKuvaus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                eKuvaus = editKuvaus.getText().toString();
            }
        });

    }
}
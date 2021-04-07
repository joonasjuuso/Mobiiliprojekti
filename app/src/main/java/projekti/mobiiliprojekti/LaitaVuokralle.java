package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;

public class LaitaVuokralle extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef =  storage.getReference();
    private StorageReference mokkiRef;

    private Button bChooseimage;
    private Button bUploadImage;
    private ImageView ImageViewUpload;
    private ProgressBar UploadImageProgressBar;

    private Uri mImageUri;

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

        /*
        if(currentUser != null) {
             mokkiRef = storageRef.child(currentUser.getDisplayName() + "/" + "Mökkien kuvat");
        }
         */
        bTakaisinMokkiListaan = findViewById(R.id.bTakaisinMokkiListaan);
        bAsetaVuokralle = findViewById(R.id.bAsetaVuokralle);


        bChooseimage = findViewById(R.id.bChooseImage);
        bUploadImage = findViewById(R.id.bUpload);
        ImageViewUpload = findViewById(R.id.ImageViewUpload);
        UploadImageProgressBar = findViewById(R.id.UploadImageProgressBar);

        bChooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageChooser();
            }
        });

        bUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        bTakaisinMokkiListaan.setOnClickListener(View -> {
            Intent takaisinMokkiListaan = new Intent(this, Mokki_List.class);
            startActivity(takaisinMokkiListaan);
        });

        bAsetaVuokralle.setOnClickListener(view -> {
            AsetaVuokralle();
        });

    }

    private void AsetaVuokralle()
    {
        Intent varmistaIntent = new Intent(this, IlmoitusVarmistus.class);

        EditOtsikko =  findViewById(R.id.EditOtsikko);
        String eOtsikko = EditOtsikko.getText().toString();

        EditHinta = findViewById(R.id.EditHinta);
        String eHinta = EditHinta.getText().toString();

        EditOsoite = findViewById(R.id.EditOsoite);
        String eOsoite = EditOsoite.getText().toString();

        EditHuoneet = findViewById(R.id.HuoneMaaraSpinner);
        String eHuoneet = EditHuoneet.getSelectedItem().toString();

        EditNeliot = findViewById(R.id.EditNelioMaara);
        String eNeliot = EditNeliot.getText().toString();

        EditLammitys = findViewById(R.id.EditLammitys);
        String eLammitys = EditLammitys.getText().toString();

        EditVesi = findViewById(R.id.VesiSpinner);
        String eVesi = EditVesi.getSelectedItem().toString();

        EditSauna = findViewById(R.id.SaunaSpinner);
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
    }

    private void OpenImageChooser()
    {
        Intent chooseImageIntent = new Intent();
        chooseImageIntent.setType("image/*");
        chooseImageIntent.setAction(chooseImageIntent.ACTION_GET_CONTENT);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                ImageViewUpload.setImageBitmap(bitmap);

                String filename = "bitmap.png";
                //Bitmap bmp;
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //stream.close();
                //bitmap.recycle();

                Intent varmistaIntent = new Intent(this, IlmoitusVarmistus.class);
                varmistaIntent.putExtra("eKuva", filename);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (mImageUri != null) {
            //ProgressDialog progressDialog = new ProgressDialog(this);
            //progressDialog.setTitle("Lataus käynnissä...");
            //progressDialog.show();

            mokkiRef.child("/"+currentUser.getUid()).putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Kuva Lisätty!",Toast.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Virhe kuvan päivityksessä",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            //progressDialog.setMessage("Ladattu " + (int)progress + "%");
                        }
                    });
        }
    }
}
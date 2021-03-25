package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfiiliActivity extends AppCompatActivity {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef =  storage.getReference();
    private StorageReference profileRef = storageRef.child("ProfilePictures");
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    private ImageView profiiliKuva;

    TextView txtNimi;
    TextView txtEmail;
    TextView txtSalasana;
    ImageView imageView;
    Button lataaButton;

    boolean PASSWORD_CHANGE = false;
    boolean EMAIL_CHANGE = false;

    EditText edtText;
    Button submitButton;
    Button cancelButton;
    String newStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);
        txtNimi = findViewById(R.id.textNiminayta);
        txtEmail = findViewById(R.id.textEmail);
        txtSalasana = findViewById(R.id.textPass);
        lataaButton = findViewById(R.id.buttonLataa);
        profiiliKuva = findViewById(R.id.profiiliKuva);

        imageView = findViewById(R.id.imageAvatar);
        txtEmail.setText(currentUser.getEmail());
        txtNimi.setText(currentUser.getDisplayName());
        imageView.setImageResource(R.mipmap.ic_launcher);
        if(currentUser.getDisplayName() != null) {
            txtNimi.setText("Hei " + currentUser.getDisplayName());
        }



        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMAIL_CHANGE = true;
                reAuthenticate();
            }
        });
        txtSalasana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PASSWORD_CHANGE = true;
                reAuthenticate();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                lataaButton.setVisibility(View.VISIBLE);
            }
        });
        lataaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Valitse kuva..."),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Lataus käynnissä...");
            progressDialog.show();

            profileRef.child("/"+currentUser.getUid()).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Profiilikuva päivitetty!",Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Virhe kuvan päivityksessä",Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Ladattu " + (int)progress + "%");
                }
            });
        }
    }

    private void setEmail() {
        Log.e("Tag","setEmail");
        AlertDialog alert = new AlertDialog.Builder(this).create();
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alert.setMessage("Syötä uusi sähköposti:");
        alert.setTitle("Sähköpostin muutos");

        alert.setView(edittext);

        alert.setButton(AlertDialog.BUTTON_POSITIVE,"Muuta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentUser.updateEmail(edittext.getText().toString());
                Toast.makeText(getApplicationContext(),"Email updated!",Toast.LENGTH_SHORT).show();
                EMAIL_CHANGE = false;
            }
        });

        alert.setButton(AlertDialog.BUTTON_NEGATIVE,"Palaa takaisin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.show();
        Log.e("Tag","FinishedEmail");

    }
    private void setPass() {
        Log.e("Tag","setPass");
        AlertDialog alert = new AlertDialog.Builder(this).create();
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setMessage("Syötä uusi salasana:");
        alert.setTitle("Salasanan muutos");

        alert.setView(edittext);

        alert.setButton(AlertDialog.BUTTON_POSITIVE,"Muuta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentUser.updatePassword(edittext.getText().toString());
                Toast.makeText(getApplicationContext(),"Password updated!",Toast.LENGTH_SHORT).show();
                PASSWORD_CHANGE = false;
            }
        });

        alert.setButton(AlertDialog.BUTTON_NEGATIVE,"Palaa takaisin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.show();
        Log.e("Tag","FinishedPass");
    }

    public void onClick_Usermenu(View view) {
        PopupMenu popup = new PopupMenu(this, profiiliKuva);
        popup.setOnMenuItemClickListener(item -> {
            final Intent intent;
            switch (item.getItemId()) {
                case R.id.user:
                    Log.d("TAGI", "0");
                    finish();
                    startActivity(getIntent());
                    break;
                case R.id.logout:
                    Log.d("TAGI", "1");
                    mauth.signOut();
                    Intent signOutIntent = new Intent(this,AloitusLogin.class);
                    startActivity(signOutIntent);
                    finish();
                    break;
            }
            return false;
        });
        popup.inflate(R.menu.profiilimenu_list);
        popup.show();
    }
    private void reAuthenticate() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog alert = new AlertDialog.Builder(this).create();
        final EditText newEmail = new EditText(this);
        newEmail.setHint("Sähköposti");
        newEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(newEmail);
        final EditText newPass = new EditText(this);
        newPass.setHint("Salasana");
        newPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPass);

        alert.setMessage("Varmista kirjautumalla uudestaan");
        alert.setTitle("Varmistus");

        alert.setView(layout);
        alert.setButton(AlertDialog.BUTTON_POSITIVE,"Tunnistaudu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(newEmail.getText().toString(),newPass.getText().toString());
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.e("Tag", "User re-authenticated");
                            if(PASSWORD_CHANGE == true) {
                                setPass();
                            }
                            else if (EMAIL_CHANGE == true) {
                                setEmail();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Wrong email / password",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,"Palaa takaisin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.show();
    }
}
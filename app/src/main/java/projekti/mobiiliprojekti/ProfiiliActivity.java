package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfiiliActivity extends AppCompatActivity {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();

    ImageView profiiliKuva;

    TextView txtNimi;
    TextView txtEmail;
    TextView txtSalasana;
    ImageView imageView;

    EditText edtText;
    Button submitButton;
    Button cancelButton;
    String newStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);
        txtNimi = (TextView) findViewById(R.id.textNiminayta);
        txtEmail = (TextView) findViewById(R.id.textEmail);
        txtSalasana = (TextView) findViewById(R.id.textPass);
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
                reAuthenticate();
            }
        });
        txtSalasana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAuthenticate();
            }
        });
    }

    private void setEmail() {
        Log.e("Tag","setEmail");
        AlertDialog alert = new AlertDialog.Builder(this).create();
        final EditText edittext = new EditText(this);
        alert.setMessage("Syötä uusi sähköposti:");
        alert.setTitle("Sähköpostin muutos");

        alert.setView(edittext);

        alert.setButton(AlertDialog.BUTTON_POSITIVE,"Muuta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentUser.updateEmail(edittext.getText().toString());
                verifyEmail();
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
        alert.setMessage("Syötä uusi salasana:");
        alert.setTitle("Salasanan muutos");

        alert.setView(edittext);

        alert.setButton(AlertDialog.BUTTON_POSITIVE,"Muuta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentUser.updatePassword(edittext.getText().toString());
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



    private void verifyEmail() {
        Intent varmistusIntent = new Intent(this,varmistusActivity.class);
        startActivity(varmistusIntent);
        finish();
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
        newEmail.setHint("EmaiL");
        layout.addView(newEmail);
        final EditText newPass = new EditText(this);
        newPass.setHint("Password");
        layout.addView(newPass);

        alert.setMessage("Varmista kirjautumalla uudestaan");
        alert.setTitle("Varmistus");

        alert.setView(layout);
        alert.setButton(AlertDialog.BUTTON_POSITIVE,"Tunnistaudu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential("email","password"/*newEmail.getText().toString(),newPass.getText().toString()*/);
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("Tag","User re-authenticated");
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
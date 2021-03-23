package projekti.mobiiliprojekti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfiiliActivity extends AppCompatActivity {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();

    TextView txtNimi;
    TextView txtEmail;
    TextView txtSalasana;

    EditText edtText;
    Button submitButton;
    Button cancelButton;
    String newStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);
        txtNimi = (TextView) findViewById(R.id.textName);
        txtEmail = (TextView) findViewById(R.id.textEmail);
        txtSalasana = (TextView) findViewById(R.id.textPass);

        txtEmail.setText(currentUser.getEmail());
        txtNimi.setText(currentUser.getDisplayName());

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmail();
            }
        });
        txtSalasana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPass();
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
}
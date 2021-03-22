package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AloitusLogin extends AppCompatActivity {

    Button kirjauduBtn;
    Button jatkaBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aloitus_login);
        kirjauduBtn = (Button) findViewById(R.id.buttonKirjaudu_aloitus);
        jatkaBtn = (Button) findViewById(R.id.buttonJatka);


        kirjauduBtn.setOnClickListener(view -> {

            Intent kirjauduIntent = new Intent(this,LoginActivityV2.class);
            startActivity(kirjauduIntent);

        });

        jatkaBtn.setOnClickListener(view -> {
            Intent mokkiIntent = new Intent(this,Mokki_List.class);
            startActivity(mokkiIntent);
        });

    }
}
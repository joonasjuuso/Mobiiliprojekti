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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AloitusLogin extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    Button tiliBtn;
    Button kirjauduBtn;
    Button jatkaBtn;
    String email;
    String password;
    long maxid;
    int i=0;
    FirebaseDatabase database = com.google.firebase.database.FirebaseDatabase.getInstance("https://mokkidata-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference reference = database.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aloitus_login);
        edtEmail = (EditText) findViewById(R.id.editTextSahkoposti_aloitus);
        edtPassword = (EditText) findViewById(R.id.editTextSalasana_aloitus);
        tiliBtn = (Button) findViewById(R.id.buttonTili);
        kirjauduBtn = (Button) findViewById(R.id.buttonKirjaudu_aloitus);
        jatkaBtn = (Button) findViewById(R.id.buttonJatka);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid=(dataSnapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                // System.out.println("onTextChanged"+s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                // System.out.println("beforeTextChanged"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = edtEmail.getText().toString();
            }
        });
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                // System.out.println("onTextChanged"+s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                // System.out.println("beforeTextChanged"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                password = edtPassword.getText().toString();
            }
        });

        tiliBtn.setOnClickListener(view -> {
            Intent registerIntent = new Intent(this,LoginActivity.class);
            registerIntent.putExtra("Email",email);
            registerIntent.putExtra("Password",password);
            startActivity(registerIntent);
            finish();
        });

        kirjauduBtn.setOnClickListener(view -> {

            Intent mokkiIntent = new Intent(this,Mokki_List.class);
            startActivity(mokkiIntent);

        });
    }
}
package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    Button button;
    EditText edtEmail;
    EditText edtPass;
    private String email;
    private String password;
    long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseDatabase database = com.google.firebase.database.FirebaseDatabase.getInstance("https://mokkidata-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference reference = database.getReference().child("Users");
        button = (Button) findViewById(R.id.buttonKirjaudu);
        edtEmail = (EditText) findViewById(R.id.editTextUser);
        edtPass = (EditText) findViewById(R.id.editTextPass);
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

        edtPass.addTextChangedListener(new TextWatcher() {
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
                password = edtPass.getText().toString();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxid++;
                reference.child(String.valueOf(maxid)).child("Email").setValue(email);
                reference.child(String.valueOf(maxid)).child("Password").setValue(password);
            }
        });
    }


}
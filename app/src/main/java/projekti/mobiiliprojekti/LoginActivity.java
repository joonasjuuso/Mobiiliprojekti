package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase database = com.google.firebase.database.FirebaseDatabase.getInstance("https://mokkidata-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference reference = database.getReference().child("Users");
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
        button = (Button) findViewById(R.id.buttonKirjaudu);
        edtEmail = (EditText) findViewById(R.id.editTextPass);
        edtPass = (EditText) findViewById(R.id.editTextUser);
        reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists())
                   maxid=(dataSnapshot.getChildrenCount());

               String checkEmail;
               //for(DataSnapshot data: dataSnapshot.getChildren()) {

                  // email == dataSnapshot.child("Users").child(String.valueOf(i)).child("Email").getValue()
                      // Log.e("Printline","Same email");
                      // Log.e("Print",email);
                   //checkEmail = (String) dataSnapshot.child(String.valueOf(i)).child("Email").getValue();

                   /*if(checkEmail == email) {
                       Log.e("Print","Same email");
                   }*/
                   //if(dataSnapshot.child(String.valueOf(i)).child("Email").exists()) {
                       //Log.e("Print","Same email");
                   //}
                   /*else {
                       Log.e("yes", (String) dataSnapshot.child(String.valueOf(3)).child("Email").getValue());
                       return;
                   }*/
                   
                   // Tämä toimii jos määrittää i:n tilalle jonkin numeron
                   // else {
                   //Log.e("yes", (String) dataSnapshot.child("Users").child(String.valueOf(i)).child("Email").getValue());
                      // return;
                    // }
               //}

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

        button.setOnClickListener(view -> {
            writeUser(reference.push().getKey(),email,password);
            //reference.child(String.valueOf(maxid)).child("Email").setValue(email);
            //reference.child(String.valueOf(maxid)).child("Password").setValue(password);
        });
    }

    public void writeUser(String userId, String email, String password) {
        Users user = new Users(email,password);
        Query queryToGetData = reference.orderByChild("Email").equalTo(email);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> userChildren = snapshot.getChildren();

                for (DataSnapshot user : userChildren) {
                    Users u = user.getValue(Users.class);

                    if (u.email.equalsIgnoreCase(email)) {
                        Log.e("Print", "Email exists");
                    } else {
                        reference.child(userId).setValue(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
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

    private static final int RC_SIGN_IN = 123;
    EditText edtEmail;
    EditText edtPassword;
    Button tiliBtn;
    Button kirjauduBtn;
    Button jatkaBtn;
    String email;
    String password;
    long maxid;
    int i=0;

    private FirebaseAuth mAuth;
    private String mCustomToken;
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
        mAuth = FirebaseAuth.getInstance();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

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
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void startSignIn() {
        // [START sign_in_custom]
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(AloitusLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_custom]
    }
    private void updateUI(FirebaseUser user) {

    }
}
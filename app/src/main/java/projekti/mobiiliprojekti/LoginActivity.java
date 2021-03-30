package projekti.mobiiliprojekti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    private EditText editEmail, editPassword;
    private TextView textTervehdys;
    private Intent mokkiIntent, varmistusIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.edittext_email);
        editPassword = findViewById(R.id.edittext_password);
        textTervehdys = findViewById(R.id.textview_tervehdys);
        varmistusIntent = new Intent(this, varmistusActivity.class);
        mokkiIntent = new Intent(this, Mokki_List.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())  {
                        Log.d("TAG", "creatUserWithGoogle:success");
                        startActivity(mokkiIntent);
                        finish();
                    }else {
                        Log.d("TAG", "creatUserWithGoogle:failed");}
                }
            });
        }

    public void clickGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void clickLuoTunnus(View view) {

        if(currentUser != null) {
            Log.d("TAG", "kirjaudutaan ulos ekaksi ;)");
            mAuth.signOut();
            Intent i = new Intent(this, LuoTunnus.class);
            startActivity(i);
            finish();
        } else {
            Log.d("TAG", "moikka tyhjä käyttäjä, luodaan uusi");
            Intent i = new Intent(this, LuoTunnus.class);
            startActivity(i);
            finish();
        }
    }

    public void clickKirjaudu(View view) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        updateUI(currentUser);

        if (email.matches("") || password.matches("")) {
            Log.d("TAG", "tyhjä salis tai tyhjä email");
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())  {
                        Log.d("TAG", "creatUserWithEmail:success");
                        if(currentUser != null) {
                            if (!currentUser.isEmailVerified()) {
                                Log.d("TAG", "emaili ei ole vahvistettu");
                                startActivity(varmistusIntent);
                                finish();
                            }else { Log.d("TAG", "emaili on vahvistettu");
                                startActivity(mokkiIntent);
                                finish();
                            }
                        }else {
                            Log.d("TAG", "user ei null ja emaili on vahvistettu");
                            startActivity(mokkiIntent);
                            finish();
                        }
                    }else {
                        Log.d("TAG", "creatUserWithEmail:failed");
                        Toast.makeText(getApplicationContext(),
                                "Tarkasta Sähköposti tai salasana", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void updateUI(FirebaseUser user) {
        if (currentUser != null) {
            currentUser.reload();
            Log.d("TAG", "user ei ole null");
            if (!currentUser.isEmailVerified()) {
                Log.d("TAG", "emaili ei ole vahvistettu ;)");
            }
            String tervehdys = "Terve " + currentUser.getEmail();
            String email = currentUser.getEmail();

            textTervehdys.setText(tervehdys);
            editEmail.setText(email);
        } else {
            Log.d("TAG", "user on null");
        }
    }
}
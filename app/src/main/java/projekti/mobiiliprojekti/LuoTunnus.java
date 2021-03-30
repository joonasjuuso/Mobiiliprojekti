package projekti.mobiiliprojekti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LuoTunnus extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;
    private EditText editEmail, editPassword, editConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luotunnus_layout);

        Log.d("TAG", "MOI LUOTUNNUS ACTIVI");
        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        editConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        mAuth = FirebaseAuth.getInstance();
        currentUser  = mAuth.getCurrentUser();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void clickSignUp(View view) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Salasanat eivät täsmää", Toast.LENGTH_SHORT).show();

        } else if (email.matches("") || password.matches("")) {
            Log.d("TAG", "virhe");

        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())  {
                        Toast.makeText(getApplicationContext(), "Käyttäjä luotu", Toast.LENGTH_SHORT).show();
                        currentUser = mAuth.getCurrentUser();
                        currentUser.reload();

                        if(!currentUser.isEmailVerified()) {
                            vahvistaEmail();
                        }
                        //FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                    } else {
                        Toast.makeText(getApplicationContext(), "Käyttäjän luonnissa virhe", Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }
            });
        }
    }
    private void vahvistaEmail() {
        currentUser = mAuth.getCurrentUser();
        Intent varmistusIntent = new Intent(this, varmistusActivity.class);
        if(null != currentUser) {
            currentUser.reload();
            if(currentUser.getEmail()!=null) {
                if(!currentUser.isEmailVerified()) {
                    /* Send Verification Email */
                    currentUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            /* Check Success */
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Verification Email Sent To: " + currentUser.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                                startActivity(varmistusIntent);
                                finish();
                            } else {
                                Log.e("TAG", "sendEmailVerification", task.getException());
                                Toast.makeText(getApplicationContext(),
                                        "Failed To Send Verification Email!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if(currentUser.isEmailVerified()) {
                    Toast.makeText(getApplicationContext(), "Nykyinen käyttäjä on vahvistettu", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
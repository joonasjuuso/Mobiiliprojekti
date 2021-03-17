package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText edtText;
    String message = "";
    private Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mokkidata-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtText = (EditText) findViewById(R.id.editText);

        edtText.addTextChangedListener(new TextWatcher() {
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
                message = edtText.getText().toString();
            }
        });

}

        public void onClick(View view) {
            DatabaseReference myRef = database.getReference("Ville");
            myRef.setValue(message);
        }
}
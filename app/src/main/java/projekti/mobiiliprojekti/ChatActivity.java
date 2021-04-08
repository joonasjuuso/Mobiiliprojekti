package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatActivity extends AppCompatActivity {

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();

    private ImageView profiiliKuva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        profiiliKuva = findViewById(R.id.profiiliKuva);
    }

    public void onClick_Usermenu(View view) {
        PopupMenu popup = new PopupMenu(this, profiiliKuva);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.user:
                    Intent profileIntent = new Intent(this,ProfiiliActivity.class);
                    startActivity(profileIntent);
                    finish();
                    break;
                case R.id.chat:
                    finish();
                    startActivity(getIntent());
                    break;
                case R.id.logout:
                    mauth.signOut();
                    Intent signOutIntent = new Intent(this, LoginActivity.class);
                    startActivity(signOutIntent);
                    finish();
                    break;
            }
            return false;
        });
        popup.inflate(R.menu.profiilimenu_list);
        popup.show();
    }
}
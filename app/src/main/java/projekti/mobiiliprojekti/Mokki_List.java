package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Mokki_List extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private ArrayList<MokkiItem> mMokkiItem;

    private RecyclerView mRecyclerView;
    private MokkiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef =  storage.getReference();

    ImageView profiiliKuva;
    Button bLaitaVuokralle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki__list);
        drawerLayout = findViewById(R.id.drawer_layout);
        profiiliKuva = findViewById(R.id.profiiliKuva);
        createMokkiItem();
        buildRecyclerView();

        if(currentUser!=null) {
            storageRef.child("ProfilePictures/" + currentUser.getUid()).getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Glide.with(getApplicationContext()).load(uri.toString()).circleCrop().into(profiiliKuva);
                    })
                    .addOnFailureListener(e -> {
                        profiiliKuva.setImageResource(R.mipmap.ic_launcher);
                    });


            if(currentUser.getDisplayName() == null) {
                Log.d("TAG", "moro " + currentUser.getDisplayName());
            } else { Log.d("TAG", "EI OO NULL"); }


        } else if(currentUser==null) {
            profiiliKuva.setImageResource(R.mipmap.ic_launcher);
        }

        bLaitaVuokralle = findViewById(R.id.bLaitaVuokralle);
        bLaitaVuokralle.setOnClickListener(view -> {
            Intent vuokraaIntent = new Intent(this, LaitaVuokralle.class);
            startActivity(vuokraaIntent);
        });
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, LoginActivity.class);
        startActivity(setIntent);
        finish();
    }

    //Manuaalisesti täytettävä
    public void createMokkiItem()
    {
        mMokkiItem = new ArrayList<>();


    }

    public void buildRecyclerView()
    {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MokkiAdapter(mMokkiItem);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //käytetään position atribuuttia jolla mökkejä targetataan.
        //siirrytään tarkempaan mökki näkymään
        mAdapter.setOnItemClickListener(new MokkiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intentMokkiNakyma = new Intent(Mokki_List.this, MokkiNakyma.class);
                intentMokkiNakyma.putExtra("Mokki", mMokkiItem.get(position));
                startActivity(intentMokkiNakyma);
            }
        });
    }

    //Vasemman vetolaatikon metoodeja
    public void onClick_Drawermenu(View view) {
        openDrawermenu(drawerLayout);
    }

    public void closeDrawermenu(View view) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    private static void openDrawermenu(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    //Oikeanpuolen menu hommelit
    public void onClick_Usermenu(View view) {
        if(currentUser == null){
            Intent kirjauduIntent = new Intent(this, LoginActivity.class);
            startActivity(kirjauduIntent);
            finish();
        }

        PopupMenu popup = new PopupMenu(this, profiiliKuva);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.user:
                    Intent intent = new Intent(this, ProfiiliActivity.class);
                    startActivity(intent);
                    break;
                case R.id.chat:
                    Intent chatIntent = new Intent(this,ChatActivity.class);
                    startActivity(chatIntent);
                    break;
                case R.id.logout:
                    Intent signOutIntent = new Intent(this,LoginActivity.class);
                    mauth.signOut();
                    startActivity(signOutIntent);
                    finish();
                    break;
            }
            return false;
        });
        if(currentUser != null) {
            popup.inflate(R.menu.menu_list);
            if (currentUser.getDisplayName() != null) {
                popup.getMenu().findItem(R.id.user).setTitle(currentUser.getDisplayName());
            } else {
                popup.getMenu().findItem(R.id.user).setTitle(currentUser.getEmail());
            }
            popup.show();
        }
    }
}
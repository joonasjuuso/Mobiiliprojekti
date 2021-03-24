package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Mokki_List extends AppCompatActivity {


    private DrawerLayout drawerLayout;

    private ArrayList<MokkiItem> mMokkiItem;

    private RecyclerView mRecyclerView;
    private MokkiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();

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

        bLaitaVuokralle = findViewById(R.id.bLaitaVuokralle);
        bLaitaVuokralle.setOnClickListener(view -> {
            Intent vuokraaIntent = new Intent(this, LaitaVuokralle.class);
            startActivity(vuokraaIntent);
        });

        profiiliKuva.setOnClickListener(View -> {
            Intent profiiliIntent = new Intent(this,ProfiiliActivity.class);
            startActivity(profiiliIntent);
            finish();
        });
    }




    //Manuaalisesti täytettävä
    public void createMokkiItem()
    {
        mMokkiItem = new ArrayList<>();

        mMokkiItem.add(new MokkiItem( "Line 1", "Line 20", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 22", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 24", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 26", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 28", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 30", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 32", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 34", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 36", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 38", "1000"));
        mMokkiItem.add(new MokkiItem( "Line 1", "Line 40", "1000"));
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

    //Vasemman vetolaatikon metoodeja avaamiseen ja sulkemiseen
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

    public void onClick_Usermenu(View view) {
        PopupMenu popup = new PopupMenu(this, profiiliKuva);
        popup.setOnMenuItemClickListener(item -> {
            final Intent intent;
            switch (item.getItemId()) {
                case R.id.user:
                    Log.d("TAGI", "0");
                    intent = new Intent(Mokki_List.this, ProfiiliActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    Log.d("TAGI", "1");
                    break;
            }
            return false;
        });
        popup.inflate(R.menu.menu_list);
        popup.show();
    }

}
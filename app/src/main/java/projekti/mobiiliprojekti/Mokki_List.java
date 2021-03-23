package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Mokki_List extends AppCompatActivity {


    DrawerLayout drawerLayout;
    CalendarView calendarView;

    private ArrayList<MokkiItem> mMokkiItem;

    private RecyclerView mRecyclerView;
    private MokkiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki__list);
        drawerLayout = findViewById(R.id.drawer_layout);

        createMokkiItem();
        buildRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.user);
        item.setTitle(currentUser.getDisplayName());
        return super.onPrepareOptionsMenu(menu);
    }


    //Manuaalisesti täytettävä
    public void createMokkiItem()
    {
        mMokkiItem = new ArrayList<>();
        mMokkiItem.add(new MokkiItem(R.drawable.majava_mokki, "asd", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel urna porttitor, sodales neque sit amet, consequat quam. Aenean quam lectus, efficitur eget ipsum nec, mollis iaculis diam. Morbi vestibulum pharetra libero, ut pretium dui gravida nec. Etiam gravida purus enim, imperdiet congue sem suscipit nec. Mauris id urna laoreet, aliquet purus sed, maximus ante. Aliquam erat volutpat. Integer vel congue quam, sed sodales lectus. Mauris vulputate diam sed diam vestibulum finibus.\n" +
                "\n" +
                "Sed non sapien id metus tristique commodo. Maecenas sit amet imperdiet ante, quis elementum diam. Aenean pulvinar porttitor arcu. Sed sit amet finibus dui. Cras eget commodo massa. Maecenas pellentesque et risus eu tristique. Sed et hendrerit nisl. Integer suscipit pulvinar bibendum. Quisque gravida risus eu sollicitudin scelerisque.\n" +
                "\n" +
                "Mauris a eros id orci luctus semper. Praesent suscipit imperdiet ante id suscipit. Vestibulum venenatis mollis erat, facilisis dictum leo lobortis hendrerit. Curabitur finibus nunc sit amet mauris mollis vehicula. Aliquam convallis placerat nunc in vulputate. Duis augue turpis, semper quis dictum non, imperdiet eget tortor. Morbi eget ligula eu purus efficitur iaculis. Sed porttitor neque vel velit ullamcorper convallis. Morbi lacinia fringilla maximus. Nunc magna metus, ultrices in ex nec, condimentum molestie nibh. Donec nec venenatis quam, nec auctor nunc.\n" +
                "\n" +
                "Sed eget nunc quam. Ut pulvinar, purus eget facilisis porta, turpis elit pellentesque arcu, quis aliquam odio arcu tincidunt turpis. Quisque in erat eu justo interdum faucibus quis id ipsum. Aliquam interdum accumsan est. Sed volutpat tempor nibh, id sollicitudin magna consequat sit amet. Integer eu suscipit arcu. Nunc porttitor elit eu enim pulvinar, eget facilisis velit dignissim. Mauris tincidunt diam ac mollis faucibus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Mauris id porta libero. Vivamus tincidunt vitae libero sed imperdiet. Maecenas finibus a elit nec venenatis.\n" +
                "\n" +
                "Fusce elit nisi, ullamcorper aliquet justo ut, molestie ullamcorper est. Praesent non est auctor, aliquet dolor sed, bibendum dui. Aenean nulla velit, sagittis quis interdum eget, condimentum viverra odio. Nulla sit amet justo elementum, ullamcorper velit et, maximus elit. Nam porttitor, neque eget porta pharetra, sapien est ultrices urna, ac ornare risus sem quis nulla. Vivamus a augue ac est fringilla mattis ut id diam. Nulla scelerisque turpis eget dapibus dignissim. Duis rhoncus nibh elementum, congue metus vel, consectetur neque. Cras a dolor semper, ultricies dolor at, cursus augue. Praesent mattis dui nisl, a ultricies lorem efficitur id.", "1"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 3", "Line 4", "10"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 4", "Line 6", "100"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 6", "Line 8", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 8", "Line 10", "10000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 12", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 14", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 16", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 18", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 20", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 22", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 24", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 26", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 28", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 30", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 32", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 34", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 36", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 38", "1000"));
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 40", "1000"));
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

    public void openMenu(View view) {
        openDrawer(drawerLayout);
    }

    public void closeMenu(View view) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


}
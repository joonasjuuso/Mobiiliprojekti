package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Mokki_List extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki__list);


        //Manuaalinen  mökkien täyttö tällä hetkellä
        ArrayList<MokkiItem> mokki_scroll_list = new ArrayList<>();
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "asd", "Line 2"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 3", "Line 4"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 4", "Line 6"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 6", "Line 8"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 8", "Line 10"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 12"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 14"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 16"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 18"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 20"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 22"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 24"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 26"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 28"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 30"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 32"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 34"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 36"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 38"));
        mokki_scroll_list.add(new MokkiItem(R.drawable.ic_launcher_background, "Line 1", "Line 40"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MokkiAdapter(mokki_scroll_list);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
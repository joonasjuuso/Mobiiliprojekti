package projekti.mobiiliprojekti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class Mokki_List extends AppCompatActivity {

    private ArrayList<MokkiItem> mMokkiItem;

    private RecyclerView mRecyclerView;
    private MokkiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki__list);

        createMokkiItem();
        buildRecyclerView();
    }

    public void createMokkiItem()
    {
        mMokkiItem = new ArrayList<>();
        mMokkiItem.add(new MokkiItem(R.drawable.ic_launcher_background, "asd", "Line 2", "1"));
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

        mAdapter.setOnItemClickListener(new MokkiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Mokki_List.this, MokkiNakyma.class);
                intent.putExtra("Mokki", mMokkiItem.get(position));

                startActivity(intent);
            }
        });
    }
}
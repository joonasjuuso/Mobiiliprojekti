package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static projekti.mobiiliprojekti.R.id.ImageViewDelete;

public class Mokki_List extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private RecyclerView fbRecyclerView;

    private DatabaseReference fbDatabaseRef;
    private List<MokkiItem> mMokkiItem;
    private MokkiAdapterV2 mAdapter;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mauth.getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef =  storage.getReference();

    private ValueEventListener fbDbListener;

    ImageView profiiliKuva;
    private Button bLaitaVuokralle;
    private  Button bNaytaKaikkienMokit;
    private Button bOmatMokit;

    ImageView ImageViewDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki__list);

        drawerLayout = findViewById(R.id.drawer_layout);
        profiiliKuva = findViewById(R.id.profiiliKuva);


        fbRecyclerView = findViewById(R.id.recyclerView);
        fbRecyclerView.setHasFixedSize(true);
        fbRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fbDatabaseRef = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/");

        mMokkiItem = new ArrayList<>();

        //mAdapter = new MokkiAdapterV2(Mokki_List.this, mMokkiItem);
        //fbRecyclerView.setAdapter(mAdapter);


        bLaitaVuokralle = findViewById(R.id.bVuokraa);
        bLaitaVuokralle.setOnClickListener(view -> {
            Intent vuokraaIntent = new Intent(this, LaitaVuokralle.class);
            startActivity(vuokraaIntent);
        });

        bOmatMokit = findViewById(R.id.bOmatMökit);
        if( currentUser.getDisplayName() != null) {
            bOmatMokit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    naytaOmatMokit();

                }
            });
        }

        bNaytaKaikkienMokit = findViewById(R.id.bNaytaKaikkienMokit);
        bNaytaKaikkienMokit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naytaKaikkiMokit();
            }
        });


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

        naytaKaikkiMokit();

    }

    private void naytaOmatMokit()
    {
        mMokkiItem.clear();

        Intent intent = new Intent(this, MokkiNakyma.class);

        Query query = fbDatabaseRef.orderByChild("vuokraaja").equalTo(currentUser.getDisplayName());

        fbDbListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    MokkiItem mokkiItem = postSnapshot.getValue(MokkiItem.class);
                    mokkiItem.setKey(postSnapshot.getKey());
                    mMokkiItem.add(mokkiItem);
                }


                mAdapter = new MokkiAdapterV2(Mokki_List.this, mMokkiItem);

                fbRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                mAdapter.setOnItemClickListener(new MokkiAdapterV2.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        mMokkiItem.get(position);
                        intent.putExtra("Mokki", mMokkiItem.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        deleteMokki(position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Mokki_List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void naytaKaikkiMokit()
    {
        mMokkiItem.clear();

        Intent intent = new Intent(this, MokkiNakyma.class);

        fbDbListener = fbDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    MokkiItem mokkiItem = postSnapshot.getValue(MokkiItem.class);
                    mokkiItem.setKey(postSnapshot.getKey());
                    mMokkiItem.add(mokkiItem);
                }



                mAdapter = new MokkiAdapterV2(Mokki_List.this, mMokkiItem);

                fbRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                mAdapter.setOnItemClickListener(new MokkiAdapterV2.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        mMokkiItem.get(position);
                        intent.putExtra("Mokki", mMokkiItem.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        //deleteMokki(position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Mokki_List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //@Override
    private void deleteMokki(int position)
    {
        MokkiItem selectedItem = mMokkiItem.get(position);
        String selectedKey = selectedItem.getKey();
        //fbDatabaseRef.child(selectedKey).removeValue();
        //mAdapter.notifyItemRemoved(position);

        StorageReference imageRef = storage.getReferenceFromUrl(selectedItem.getMokkiImage());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fbDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(Mokki_List.this, "Mökki poistettu", Toast.LENGTH_SHORT).show();
                //fbRecyclerView.setAdapter(mAdapter);
                //mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRemoved(position);
                naytaOmatMokit();
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
        else if(currentUser == null) {
            Intent kirjauduIntent = new Intent(this, LoginActivity.class);
            startActivity(kirjauduIntent);
            finish();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        fbDatabaseRef.removeEventListener(fbDbListener);
    }
}
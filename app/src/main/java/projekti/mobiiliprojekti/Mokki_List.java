package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class Mokki_List extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private RecyclerView fbRecyclerView;

    private DatabaseReference fbDatabaseRef;
    private final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private List<MokkiItem> mMokkiItem;
    private MokkiAdapterV2 mAdapter;
    private final Handler handler = new Handler();
    private Runnable runnable;

    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef =  storage.getReference();

    private DatabaseReference dbVarmistamatonMokki;
    private DatabaseReference fbVuokratutRef;

    private ValueEventListener fbDbListener;

    ImageView profiiliKuva;
    private Button bLaitaVuokralle;
    private  Button bNaytaKaikkienMokit;
    private Button bOmatMokit;
    private Button bVuokratut;
    private EditText editSearch;
    private String imageString;
    private boolean NEW_USER = false;

    private final String omatMokit = "omatMokit";
    private final String kaikkiMokit = "KaikkiMokit";
    private final String vuokratutMokit = "vuokratutMokit";

    private String dates;
    private List<String> dateList;
    private List<Integer> dateListInt;
    private Date currentDate;
    private SimpleDateFormat dateFormat;

    //suosikkitoiminnolle
    private DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference();
    private ImageButton bSuosikit;
    private String suosikkiMokit = "suosikkiMokit";

    //TODO: Vuokranantajalle näkymä tilauksista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki__list);

        //dbDates = FirebaseDatabase.getInstance().getReference("Varmistamattomat mökit/" + currentUser.getUid());
        dateList = new ArrayList<>();
        dateListInt = new ArrayList<>();

        drawerLayout = findViewById(R.id.drawer_layout);
        profiiliKuva = findViewById(R.id.profiiliKuva);


        fbRecyclerView = findViewById(R.id.recyclerView);
        fbRecyclerView.setHasFixedSize(true);
        fbRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fbDatabaseRef = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/");
        fbVuokratutRef = FirebaseDatabase.getInstance().getReference().child("Invoices").child(currentUser.getUid());

        mMokkiItem = new ArrayList<>();

        mAdapter = new MokkiAdapterV2(Mokki_List.this, mMokkiItem);
        fbRecyclerView.setAdapter(mAdapter);

        userRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    Users newUser = new Users(currentUser.getUid(),"", "", currentUser.getEmail());
                    userRef.child(currentUser.getUid()).setValue(newUser);
                    Log.d("Tag","new user");
                    NEW_USER = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef.child(currentUser.getUid()).child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    storageRef.child("ProfilePictures/" + currentUser.getUid()).getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                imageString = uri.toString();
                                Log.d("Tag",imageString);
                                if(!snapshot.getValue().equals(imageString)) {
                                    Contacts newContact = new Contacts(currentUser.getUid(),imageString);
                                    userRef.child(currentUser.getUid()).removeValue();
                                    userRef.child(currentUser.getUid()).setValue(newContact);
                                    Log.d("Tag","Image addeasdasdd");
                                }
                                else {
                                    return;
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userRef.child(currentUser.getUid()).child("sahkoposti").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    userRef.child(currentUser.getUid()).child("sahkoposti").setValue(currentUser.getUid());
                }
                if(snapshot.exists()) {
                    if(!snapshot.getValue().equals(currentUser.getEmail())) {
                    userRef.child(currentUser.getUid()).child("sahkoposti").setValue(currentUser.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        bLaitaVuokralle = findViewById(R.id.bVuokraa);
        bLaitaVuokralle.setOnClickListener(view -> {
            if(currentUser.getUid() != null){
                Intent vuokraaIntent = new Intent(this, LaitaVuokralle.class);
                startActivity(vuokraaIntent);
                //dbVarmistamatonMokki.removeValue();
            }
        });

        //Search funtkio
        editSearch = findViewById(R.id.editTextSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });




        //mokkien nayttoa vaihtavat napit
        bOmatMokit = findViewById(R.id.bOmatMökit);
        bOmatMokit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naytaOmatMokit();
            }
        });

        bNaytaKaikkienMokit = findViewById(R.id.bNaytaKaikkienMokit);
        bNaytaKaikkienMokit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                naytaKaikkiMokit();
            }
        });

        bVuokratut = findViewById(R.id.bVuokratut);
        bVuokratut.setOnClickListener(v -> {
            naytaVuokratutMokit();
        });

        bSuosikit = findViewById(R.id.bSuosikit);
        bSuosikit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavorites();
            }
        });

        if(currentUser!=null) {
            storageRef.child("ProfilePictures/" + currentUser.getUid()).getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Glide.with(getApplicationContext()).load(uri.toString()).circleCrop().into(profiiliKuva);
                        imageString = uri.toString();
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
        content();
    }

    //Search funtkio
    private void filter(String text)
    {
        ArrayList<MokkiItem> filteredList = new ArrayList<>();

        for(MokkiItem mokki : mMokkiItem)
        {
            if(mokki.getOtsikko().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(mokki);
            }
        }

        mAdapter.filteredList(filteredList);
    }

    public void content() {
        refresh(5000);
        Log.d("tag","content");
        if(NEW_USER == true) {
            syotaNumero();
            handler.removeCallbacks(runnable);
        }
    }

    private void refresh(int milliseconds) {

        runnable = () -> content();
        handler.postDelayed(runnable, milliseconds);
    }

    public interface MyCallback {
        void onCallback(String numero);
    }

    private void readData(MyCallback myCallback) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String numero = dataSnapshot.child(currentUser.getUid()).child("numero").getValue().toString();
                myCallback.onCallback(numero);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void syotaNumero() {
        readData(new MyCallback() {
            @Override
            public void onCallback(String numero) {
                if(numero.equals("")){
                    NEW_USER = true;
                }
            }
        });
        if(NEW_USER == true) {
            Log.d("tag", "alertdialog");
            AlertDialog alert = new AlertDialog.Builder(this).create();
            final EditText edittext = new EditText(this);
            edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
            alert.setMessage("Hei! Lisääthän puhelinnumerosi profiiliin, jotta sinuun voidaan ottaa yhteyttä" +
                    "myös sitä kautta");
            alert.setTitle("Puhelinnumeron lisäys");

            alert.setView(edittext);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "Lisää numero", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userRef.child(currentUser.getUid()).child("numero").setValue(edittext.getText().toString());
                    Toast.makeText(getApplicationContext(), "Puhelinnumero lisätty!", Toast.LENGTH_SHORT).show();
                }
            });

            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Palaa takaisin", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userRef.child(currentUser.getUid()).child("numero").setValue("123");
                    NEW_USER = false;
                    alert.dismiss();
                }
            });
            alert.show();
        }
        }

    private void naytaVuokratutMokit() {
        if( currentUser != null) {
            mMokkiItem.clear();
            Log.d("tag","vuokratutmokit");
            Intent intent = new Intent(this, MokkiNakyma.class);

            fbVuokratutRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Log.d("tag", postSnapshot.child("asiakas").getValue().toString());
                        if (postSnapshot.child("asiakas").getValue().toString().equals(currentUser.getUid())) {
                            String vKey = postSnapshot.child("mokkiID").getValue().toString();
                            String vDates = postSnapshot.child("paivamaarat").getValue().toString();
                            Log.d("tag", vKey);
                            fbDatabaseRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot postSnapshot2 : snapshot.getChildren()) {
                                        if (postSnapshot2.child("otsikkoID").getValue().toString().equals(vKey)) {
                                            MokkiItem v_MokkiItem = postSnapshot2.getValue(MokkiItem.class);
                                            v_MokkiItem.setKey(vKey);
                                            mMokkiItem.add(v_MokkiItem);

                                            mAdapter = new MokkiAdapterV2(Mokki_List.this, mMokkiItem);

                                            fbRecyclerView.setAdapter(mAdapter);
                                            mAdapter.notifyDataSetChanged();

                                            mAdapter.setOnItemClickListener(new MokkiAdapterV2.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                    mMokkiItem.get(position);
                                                    MokkiItem selectedItem = mMokkiItem.get(position);
                                                    String selectedKey = selectedItem.getKey();
                                                    intent.putExtra("Mokki", mMokkiItem.get(position));
                                                    intent.putExtra("setVisibility", vuokratutMokit);
                                                    intent.putExtra("deleteKey", selectedKey);
                                                    intent.putExtra("dates",vDates);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onDeleteClick(int position) {
                                                    deleteMokki(position); }
                                                @Override
                                                public void addFavoriteButtonClick(int position) { addFavoriteClick(position); }
                                                @Override
                                                public void removeFavoriteButtonClick(int position) { removeFavoriteClick(position); }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Mokki_List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            if(currentUser.getDisplayName() == null)
            {
                Toast.makeText(this, "Kirjaudu sisään nähdäksesi ilmoittamasi mökit", Toast.LENGTH_SHORT).show();
            }
            else if(currentUser == null)
            {
            naytaKaikkiMokit();
            }
        }
    }

    private void naytaOmatMokit()
    {
        if( currentUser != null) {
            mMokkiItem.clear();

            Intent intent = new Intent(this, MokkiNakyma.class);

            Query query = fbDatabaseRef.orderByChild("vuokraaja").equalTo(currentUser.getDisplayName());

            fbDbListener = query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
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
                            MokkiItem selectedItem = mMokkiItem.get(position);
                            String selectedKey = selectedItem.getKey();
                            intent.putExtra("Mokki", mMokkiItem.get(position));
                            intent.putExtra("setVisibility", omatMokit);
                            intent.putExtra("deleteKey", selectedKey);
                            startActivity(intent);
                        }

                        @Override
                        public void onDeleteClick(int position) {
                            deleteMokki(position);
                        }
                        @Override
                        public void addFavoriteButtonClick(int position) { addFavoriteClick(position); }
                        @Override
                        public void removeFavoriteButtonClick(int position) { removeFavoriteClick(position); }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Mokki_List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            if(currentUser.getDisplayName() == null)
            {
                Toast.makeText(this, "Kirjaudu sisään nähdäksesi ilmoittamasi mökit", Toast.LENGTH_SHORT).show();
            }
        }else if(currentUser == null)
        {
            naytaKaikkiMokit();
        }
    }

    private void naytaKaikkiMokit()
    {
        mMokkiItem.clear();

        Intent intent = new Intent(this, MokkiNakyma.class);

        currentDate = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = dateFormat.format(currentDate);
        String newDate = null;

        if(formattedDate.charAt(4) == '0') {
            newDate = formattedDate.substring(0, 4) + formattedDate.substring(2 + 3);
        }


        int currentdateINT = Integer.parseInt(newDate);

        fbDbListener = fbDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    MokkiItem mokkiItem = postSnapshot.getValue(MokkiItem.class);
                    dates = mokkiItem.getmDates().toString().replaceAll("\\[", "").replaceAll("\\(", "")
                            .replaceAll("\\]", "").replaceAll("\\)", "")
                            .replaceAll("/", "").replaceAll(":", "");

                    Log.d("mDates", dates);
                    dateList = Arrays.asList(dates.split("\\s*,\\s*"));
                    for(String s : dateList){
                        dateListInt.add(Integer.valueOf(s));
                        Log.d("dateListInt", String.valueOf(dateListInt));
                    }

                    Log.d("currentDateInt", String.valueOf(currentdateINT));

                    for(int x : dateListInt){
                        if(x >= currentdateINT){
                            mokkiItem.setKey(postSnapshot.getKey());
                            mMokkiItem.add(mokkiItem);
                            Log.d("x", String.valueOf(x));
                            break;
                        }
                    }

                    Log.d("dateList", String.valueOf(dateList));
                    Log.d("formattedDate", String.valueOf(formattedDate));

                    dateListInt.clear();
                }



                mAdapter = new MokkiAdapterV2(Mokki_List.this, mMokkiItem);

                fbRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                mAdapter.setOnItemClickListener(new MokkiAdapterV2.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        mMokkiItem.get(position);
                        intent.putExtra("Mokki", mMokkiItem.get(position));
                        intent.putExtra("setVisibility", kaikkiMokit);
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        //deleteMokki(position);
                    }
                    @Override
                    public void addFavoriteButtonClick(int position) { addFavoriteClick(position); }
                    @Override
                    public void removeFavoriteButtonClick(int position) { removeFavoriteClick(position); }
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

        fbDatabaseRef.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Mokki_List.this, "Mökki poistettu", Toast.LENGTH_SHORT).show();
                //
                StorageReference imageRef = storage.getReferenceFromUrl(selectedItem.getMokkiImage());
                imageRef.delete();
                naytaOmatMokit();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Mokki_List.this, "Ei onnistunut", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addFavoriteClick(int position) {
        MokkiItem selectedItem = mMokkiItem.get(position);
        String key = selectedItem.getKey();
        //favoriteRef = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/" + key + "/suosikki/").child("Lisännyt");
        favoriteRef = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/" + key + "/suosikki/").child(currentUser.getDisplayName());
        favoriteRef.setValue(currentUser.getUid());
    }

    private void removeFavoriteClick(int position)
    {
        MokkiItem selectedItem = mMokkiItem.get(position);
        String key = selectedItem.getKey();
        favoriteRef = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/" + key + "/suosikki/").child(currentUser.getDisplayName());
        favoriteRef.removeValue();
    }

    private void checkIfFavorite()
    {
        //työn alla

    }

    public void openFavorites() {

        if( currentUser != null) {
            mMokkiItem.clear();

            Intent intent = new Intent(this, MokkiNakyma.class);

            Query query = fbDatabaseRef.orderByChild("suosikki/" + currentUser.getDisplayName()).equalTo(currentUser.getUid());

            fbDbListener = query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
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
                            MokkiItem selectedItem = mMokkiItem.get(position);
                            String selectedKey = selectedItem.getKey();
                            intent.putExtra("Mokki", mMokkiItem.get(position));
                            intent.putExtra("setVisibility", omatMokit);
                            intent.putExtra("deleteKey", selectedKey);
                            startActivity(intent);
                        }

                        @Override
                        public void onDeleteClick(int position) { deleteMokki(position); }
                        @Override
                        public void addFavoriteButtonClick(int position) { addFavoriteClick(position); }
                        @Override
                        public void removeFavoriteButtonClick(int position) { removeFavoriteClick(position); }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Mokki_List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            if(currentUser.getDisplayName() == null)
            {
                Toast.makeText(this, "Kirjaudu sisään nähdäksesi suosikkisi", Toast.LENGTH_SHORT).show();
            }
        }
        else if(currentUser == null)
        {
            naytaKaikkiMokit();
        }
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
            handler.removeCallbacks(runnable);
            finish();
        }

        PopupMenu popup = new PopupMenu(this, profiiliKuva);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.user:
                    Intent intent = new Intent(this, ProfiiliActivity.class);
                    startActivity(intent);
                    handler.removeCallbacks(runnable);
                    break;
                case R.id.msg:
                    Log.d("Tag","Valikko painettu");
                    Intent chatIntent = new Intent(this,ChatActivity.class);
                    startActivity(chatIntent);
                    handler.removeCallbacks(runnable);
                    break;
                case R.id.logout:
                    Intent signOutIntent = new Intent(this,LoginActivity.class);
                    mauth.signOut();
                    startActivity(signOutIntent);
                    handler.removeCallbacks(runnable);
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
            handler.removeCallbacks(runnable);
            finish();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        fbDatabaseRef.removeEventListener(fbDbListener);
    }
}
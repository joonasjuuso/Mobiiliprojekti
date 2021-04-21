package projekti.mobiiliprojekti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MokkiNakyma extends AppCompatActivity {

    Button bTakaisinMokkilistaan;
    private Button bVuokraa;
    private Button bMuokkaa;
    private ImageView ImageViewDelete;
    Button bChat;
    private Context mContext;

    private String setVisibility = "";

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef =  storage.getReference();
    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mauth.getCurrentUser();
    private DatabaseReference fbDatabaseRef;
    private String deleteKey;

    private String MokkiImage;
    private String MokkiOtsikko;
    private String MokkiHinta;
    private String MokkiOsoite;
    private String MokkiHuoneet;
    private String MokkiNelio;
    private String MokkiLammitys;
    private String MokkiVesi;
    private String MokkiSauna;
    private String MokkiKuvaus;
    private String OtsikkoID;
    private String Vuokraaja;
    private String VuokraajaID;
    private String mDates;

    private List<String> splitDates;
    private List<String> splitString;
    private List<String> dateList;
    private String selectedYear;
    private String selectedMonth;
    private String selectedDay;
    private String selectedDate;
    private String replace;

    private TextView textViewSelectedDates;

    private CalendarView calendarDates;

    //KALENTERIPASKAA
    List<Date> listDates;
    List<Date> tmpDates;
    List<Date> highlightedDates;
    ArrayList<String> stringDates;
    ArrayList<String> tmpStringDates;
    ArrayList<String> tmpStringDates2;
    ArrayList<String> finalStringDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mokki_nakyma);

        fbDatabaseRef = FirebaseDatabase.getInstance().getReference("Vuokralla olevat mökit/");

        Intent intent = getIntent();
        MokkiItem mokkiItem = intent.getParcelableExtra("Mokki");

        MokkiImage = mokkiItem.getMokkiImage();
        MokkiOtsikko = mokkiItem.getOtsikko();
        MokkiHinta = mokkiItem.getHinta();
        MokkiOsoite = mokkiItem.getOsoite();
        MokkiHuoneet = mokkiItem.getHuoneMaara();
        MokkiNelio = mokkiItem.getNelioMaara();
        MokkiLammitys = mokkiItem.getLammitys();
        MokkiVesi = mokkiItem.getVesi();
        MokkiSauna = mokkiItem.getSauna();
        MokkiKuvaus = mokkiItem.getKuvaus();
        OtsikkoID = mokkiItem.getOtsikkoID();
        Vuokraaja = mokkiItem.getVuokraaja();
        VuokraajaID = mokkiItem.getVuokraajaID();
        mDates = mokkiItem.getmDates();

        //String Mokkiomistaja = mokkiItem.getOmistaja();

        ImageView imageViewMokki = findViewById(R.id.ImageMokkiNakyma);
        Picasso.get().load(mokkiItem.getMokkiImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(imageViewMokki);

        TextView textViewOtsikko = findViewById(R.id.OtsikkoMokkiNakyma);
        textViewOtsikko.setText(MokkiOtsikko);

        TextView textViewHinta = findViewById(R.id.HintaMokkiNakyma);
        textViewHinta.setText(MokkiHinta);

        TextView textViewOsoite = findViewById(R.id.OsoiteMokkiNakyma);
        textViewOsoite.setText(MokkiOsoite);

        TextView textViewHuoneet = findViewById(R.id.HuoneetMokkiNakyma);
        textViewHuoneet.setText(MokkiHuoneet);

        TextView textViewNeliot = findViewById(R.id.NeliotMokkiNakyma);
        textViewNeliot.setText(MokkiNelio);

        TextView textViewLammitys = findViewById(R.id.LammitysMokkiNakyma);
        textViewLammitys.setText(MokkiLammitys);

        TextView textViewVesi = findViewById(R.id.VesiMokkiNakyma);
        textViewVesi.setText(MokkiVesi);

        TextView textViewSauna = findViewById(R.id.SaunaMokkiNakyma);
        textViewSauna.setText(MokkiSauna);

        TextView textViewDates = findViewById(R.id.textViewDates);

        if(mDates != null){
            replace = mDates.replaceAll("\\[", "").replaceAll("\\(", "")
                    .replaceAll("\\]", "").replaceAll("\\)", "")
                    .replaceAll(" ", "");
            //textViewDates.setText(replace);
            Log.d("dfg", mDates);
            Log.e("asd", replace);
            splitDates = Arrays.asList(replace.split(",", -1));
        }

        //Log.d("dfg", mDates);
        //Log.e("asd", replace);


        //calendarDates = findViewById(R.id.date_pick_calendar);
        textViewSelectedDates = findViewById(R.id.textViewSelectedDates);

        TextView textViewKuvaus = findViewById(R.id.KuvausMokkiNakyma);
        textViewKuvaus.setText(MokkiKuvaus);

        bVuokraa = findViewById(R.id.bVuokraa);
        bMuokkaa = findViewById(R.id.bMuokkaa);
        bChat = findViewById(R.id.chatBtn);
        ImageViewDelete = findViewById(R.id.ImageViewDelete);

        //bVuokraa.setVisibility(View.GONE);
        bMuokkaa.setVisibility(View.GONE);
        ImageViewDelete.setVisibility(View.GONE);


        setVisibility = intent.getStringExtra("setVisibility");
        if(setVisibility.matches("omatMokit")){
            bVuokraa.setVisibility(View.GONE);
            bMuokkaa.setVisibility(View.VISIBLE);
            ImageViewDelete.setVisibility(View.VISIBLE);
            bChat.setVisibility(View.GONE);
            textViewDates.setVisibility(View.GONE);
            calendarDates.setVisibility(View.GONE);
        }else if(setVisibility.matches("kaikkiMokit")){
            bVuokraa.setVisibility(View.VISIBLE);
            bMuokkaa.setVisibility(View.GONE);
            textViewDates.setVisibility(View.VISIBLE);
            calendarDates.setVisibility(View.VISIBLE);
            //ImageViewDelete.setVisibility(View.GONE);
        }
        Log.d("Tag",setVisibility);


        deleteKey = intent.getStringExtra("deleteKey");
        ImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbDatabaseRef.child(deleteKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MokkiNakyma.this, "Mokki poistettu", Toast.LENGTH_LONG).show();
                        StorageReference imageRef = storage.getReferenceFromUrl(MokkiImage);
                        imageRef.delete();
                        ImageViewDelete.setVisibility(View.GONE);
                        bMuokkaa.setVisibility(View.GONE);
                        bChat.setVisibility(View.GONE);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MokkiNakyma.this, "Ei onnistunut", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        bTakaisinMokkilistaan = findViewById(R.id.bTakaisinMokkiListaan);

        bTakaisinMokkilistaan.setOnClickListener(View ->{
            Intent mokkiIntent = new Intent(this,Mokki_List.class);
            startActivity(mokkiIntent);
        });
        bChat.setOnClickListener(View -> {
            Intent chatIntent = new Intent(this,ChatActivity.class);
            chatIntent.putExtra("ID",VuokraajaID);
            chatIntent.putExtra("name",Vuokraaja);
            startActivity(chatIntent);
        });

        //KALENTERI
        listDates = new ArrayList<>();
        highlightedDates = new ArrayList<>();
        tmpDates = new ArrayList<>();
        stringDates = new ArrayList<>();
        tmpStringDates = new ArrayList<>();
        tmpStringDates2 = new ArrayList<>();
        finalStringDates = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (String s : splitDates) {
            ParsePosition pos = new ParsePosition(0);
            Log.d("TAG", "s = " + s);
            Date date = sdf.parse(s, pos);
            listDates.add(date);
            if(!s.endsWith(":1")) {
                highlightedDates.add(date);
            }
        }
        Log.d("TAG", "splitDates = " + splitDates.toString());
        Log.d("TAG", "highligtatut = " + highlightedDates.toString());

        Collections.sort(listDates);
        //TEXTVIEWIN JÄRKKÄILY
        /*

        Log.d("TAG", "sortattu listDates = " + listDates.toString());
        for(Date d : listDates) {
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            String str = fmt.format(d);
            stringDates.add(str);
        }
        String tmpStr = stringDates.get(0);
        String tmpStr2 = stringDates.get(stringDates.size() - 1);
        textViewDates.setText(tmpStr + " - " + tmpStr2);

         */

        CalendarPickerView datePicker = findViewById(R.id.kalenteri);
        Calendar firstDay = Calendar.getInstance();
        Date tmpDate = listDates.get(0);
        firstDay.add(Calendar.DATE, - 1);
        firstDay.setTime(tmpDate);

        Calendar lastDay = Calendar.getInstance();
        Date tmpDate2 = listDates.get(listDates.size() - 1);
        lastDay.setTime(tmpDate2);
        lastDay.add(Calendar.DATE, + 1);

        datePicker.init(firstDay.getTime(), lastDay.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        datePicker.highlightDates(highlightedDates);

        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                //VALITAAN DATET
                tmpDates = datePicker.getSelectedDates();
                Log.d("TAG", "listDates = " + tmpDates);

                //VALITUT DATET STRINGEIKSI
                for(Date i : tmpDates) {
                    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                    String str = fmt.format(i);
                    tmpStringDates.add(str);
                }

                Log.d("TAG", "tmpStringDates selected dates = " + tmpStringDates.toString());

                //VÄLIAIKASEEN LISTAAN VAPAAT PÄIVÄT
                for (String s : splitDates) {
                    String tmpStr = s;
                    tmpStr = tmpStr.substring(0, tmpStr.length() - 2);
                    tmpStringDates2.add(tmpStr);

                    //VERRATAAN VARATTUJA PÄIVIÄ(tmpStringDates) VAPAISIIN (tmpStringDates2)
                    for (String s2 : tmpStringDates) {
                        if(s2.equals(tmpStr)) {
                            tmpStringDates2.remove(tmpStr);
                            tmpStr = tmpStr + ":1";
                            finalStringDates.add(tmpStr);
                        }
                    }
                }
                //tmpStringDates.clear();
                for(String s : tmpStringDates2) {
                    if (!s.endsWith(":1")) {
                        String tmpStr = s + ":0";
                        finalStringDates.add(tmpStr);
                    }
                }

                Log.d("TAG", "splitDates  = " + splitDates.toString());
                Log.d("TAG", "tmpStringDates2  = " + tmpStringDates2.toString());
                Log.d("TAG", "tmpStringDates  = " + tmpStringDates.toString());
                Log.d("TAG", "finalStringDates  = " + finalStringDates.toString());

                Log.d("TAG", "otsikkoid = " + OtsikkoID);

                HashMap hashMap = new HashMap();
                hashMap.put("mDates", finalStringDates.toString());
                Log.d("TAG", "hashmap = " + hashMap);
                fbDatabaseRef.child(OtsikkoID).updateChildren(hashMap);

                tmpStringDates.clear();
                tmpStringDates2.clear();
                finalStringDates.clear();
            }
            @Override
            public void onDateUnselected(Date date) {

            }
        });

        Log.d("TAG", "date = " + listDates);

            bVuokraa.setOnClickListener(v -> {

                Intent vuokraIntent = new Intent(this, CheckoutActivity.class);
                vuokraIntent.putExtra("name", Vuokraaja);
                vuokraIntent.putExtra("hinta",MokkiHinta);
                vuokraIntent.putExtra("otsikko",MokkiOtsikko);
                vuokraIntent.putExtra("osoite",MokkiOsoite);
                vuokraIntent.putStringArrayListExtra("paivat", (ArrayList<String>) stringDates);
                startActivity(vuokraIntent);
        });

        bMuokkaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muokkaaIlmoitusta();
            }
        });

        //setDates();


    }
/*
    private void setDates()
    {

        dateList = new ArrayList<String>();

        calendarDates.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = String.valueOf(year);
                selectedMonth = String.valueOf(month + 1);
                selectedDay = String.valueOf(dayOfMonth);

                selectedDate = selectedDay + "/" + selectedMonth + "/" + selectedYear;

                if(!dateList.contains(selectedDate)){
                    if(splitDates.contains(selectedDate)){
                        dateList.add(selectedDate);
                    }
                }else if(dateList.contains(selectedDate)){
                    dateList.remove(selectedDate);
                }

                StringBuilder builder = new StringBuilder();
                for(String s : dateList){
                    builder.append(s).append(" ");
                }
                textViewSelectedDates.setText(builder.toString());

                Log.d("datelist", String.valueOf(dateList));
                Log.d("splitdate", String.valueOf(splitDates));
            }
        });
    }

 */

    public void onClick_Takaisin(View view) {
        Intent i = new Intent(this, Mokki_List.class);
        startActivity(i);
    }

    private void muokkaaIlmoitusta()
    {
        Intent muokkaaIntent = new Intent(this, MuokkausActivity.class);

        //Toast.makeText(this, "Mökki lisätty vuokralle", Toast.LENGTH_LONG).show();

        muokkaaIntent.putExtra("eKuva", MokkiImage);
        muokkaaIntent.putExtra("eOtsikko", MokkiOtsikko);
        muokkaaIntent.putExtra("eHinta", MokkiHinta);
        muokkaaIntent.putExtra("eOsoite", MokkiOsoite);
        muokkaaIntent.putExtra("eHuoneet", MokkiHuoneet);
        muokkaaIntent.putExtra("eNeliot", MokkiNelio);
        muokkaaIntent.putExtra("eLammitys", MokkiLammitys);
        muokkaaIntent.putExtra("eVesi", MokkiVesi);
        muokkaaIntent.putExtra("eSauna", MokkiSauna);
        muokkaaIntent.putExtra("eKuvaus", MokkiKuvaus);
        muokkaaIntent.putExtra("muokkaaKey", deleteKey);
        muokkaaIntent.putExtra("dates", String.valueOf(splitDates));
        //muokkaaIntent.putExtra("eUID", UID);


        startActivity(muokkaaIntent);
    }

}

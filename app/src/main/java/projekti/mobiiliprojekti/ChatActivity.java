package projekti.mobiiliprojekti;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity
{
    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;

    private TextView userName, userLastSeen;
    private ImageView userImage;

    private Toolbar ChatToolBar;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ImageButton SendMessageButton, SendFilesButton;
    private EditText MessageInputText;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;


    private String saveCurrentTime, saveCurrentDate;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.d("Tag","onCreate");

        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        messageReceiverID = "5fN3ZduK8vfFYqBQZuA6v5DpNWY2";


        //messageReceiverID = getIntent().getExtras().get("visit_user_id").toString();
       // messageReceiverName = getIntent().getExtras().get("visit_user_name").toString();
        //messageReceiverImage = getIntent().getExtras().get("visit_image").toString();


        IntializeControllers();


        userName.setText(messageReceiverName);
        //Picasso.get().load(messageReceiverImage).placeholder(R.drawable.ic_account_box).into(userImage);


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMessage();
            }
        });

    }




    private void IntializeControllers()
    {


        userName = (TextView) findViewById(R.id.receiverName);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_btn);
        SendFilesButton = (ImageButton) findViewById(R.id.send_files_btn);
        MessageInputText = (EditText) findViewById(R.id.input_message);

        messageAdapter = new MessageAdapter(messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        RootRef.child("Messages").child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messageAdapter.notifyDataSetChanged();

                        userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    private void SendMessage()
    {
        Log.d("Tag","sendmessage");
        String messageText = MessageInputText.getText().toString();
        String msgType = "text";
        String name;
        if(mAuth.getCurrentUser().getDisplayName() == null) {
            name = mAuth.getCurrentUser().getEmail();
        }
        else {
            name = mAuth.getCurrentUser().getDisplayName();
        }

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "first write your message...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("Tag","Else");
            String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                    .child(messageSenderID).child(messageReceiverID).push();
            Log.d("Tag","reference");
            String messagePushID = RootRef.child("Messages")
                    .child(messageSenderID).child(messageReceiverID).push().getKey();

            Messages newMessage = new Messages(messageSenderID,messageText, msgType,messageReceiverID,messagePushID,saveCurrentTime,saveCurrentDate,name);

            Map<String, Object> postValues = newMessage.toMap();
            Map<String, Object> messageBodyDetails = new HashMap<>();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, postValues);
            messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, postValues);
            Log.d("Tag","map");
            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {

                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    MessageInputText.setText("");
                    Log.d("Tag","Updating");
                }
            });
            Log.d("Tag","updatechildren");
        }
    }
}


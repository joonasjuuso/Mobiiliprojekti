package projekti.mobiiliprojekti;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{

    private List<Contacts> userContactsList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();


    public ContactsAdapter (List<Contacts> userContactsList)
    {
        this.userContactsList = userContactsList;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public ImageView receiverImage;
        public TextView receiverName;

        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            receiverImage = (ImageView) itemView.findViewById(R.id.users_profile_image);
            receiverName = (TextView) itemView.findViewById(R.id.user_profile_name);

        }
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.users_display_layout, viewGroup, false);

        mAuth = FirebaseAuth.getInstance();
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsAdapter.ContactsViewHolder contactsViewHolder, int i)
    {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Contacts contacts = userContactsList.get(i);

        String fromUserID = contacts.getName();
        String fromUserImage = contacts.getImage();

        /*storageRef.child("Users/"+fromUserID).getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString()).placeholder(R.drawable.ic_account_box).into(messageViewHolder.receiverProfileImage);
            })
            .addOnFailureListener(e -> {

            });*/



        contactsViewHolder.receiverImage.setVisibility(View.GONE);
        contactsViewHolder.receiverName.setVisibility(View.GONE);


        if (fromUserID != null)
        {
            contactsViewHolder.receiverName.setVisibility(View.VISIBLE);
            contactsViewHolder.receiverImage.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount()
    {
        return userContactsList.size();
    }
}

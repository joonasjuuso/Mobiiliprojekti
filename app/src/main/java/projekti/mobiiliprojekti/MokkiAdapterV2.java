package projekti.mobiiliprojekti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MokkiAdapterV2 extends RecyclerView.Adapter<MokkiAdapterV2.MokkiviewHolderV2>
{
    private Context mContext;
    private List<MokkiItem> mMokkiList;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }

    public MokkiAdapterV2(Context context, List<MokkiItem> mokkiItems)
    {
        mContext = context;
        mMokkiList = mokkiItems;

    }

    @Override
    public void onBindViewHolder(@NonNull MokkiviewHolderV2 holder, int position) {
        MokkiItem mokkiItemCurrent = mMokkiList.get(position);
        holder.textViewOtsikko.setText(mokkiItemCurrent.getOtsikko());
        holder.textViewHinta.setText(mokkiItemCurrent.getHinta());
        Picasso.with(mContext).load(mokkiItemCurrent.getMokkiImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageViewKuva);
    }

    @Override
    public int getItemCount() {
        return mMokkiList.size();
    }

    public static class MokkiviewHolderV2 extends RecyclerView.ViewHolder
    {
        public TextView textViewOtsikko;
        public TextView textViewHinta;
        public ImageView imageViewKuva;
        public ImageView imageViewDelete;

        public MokkiviewHolderV2(View itemView, OnItemClickListener listener)
        {
            super(itemView);

            textViewOtsikko = itemView.findViewById(R.id.TextViewOtsikko);
            textViewHinta = itemView.findViewById(R.id.TextViewHinta);
            imageViewKuva = itemView.findViewById(R.id.imageViewMokkiKuva);
            imageViewDelete = itemView.findViewById(R.id.ImageViewDelete);

            imageViewDelete.setVisibility(View.INVISIBLE);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public  MokkiviewHolderV2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.mokki_item, parent, false);
        MokkiviewHolderV2 mvh = new MokkiviewHolderV2(v, mOnItemClickListener);
        return mvh;
    }
}

package projekti.mobiiliprojekti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MokkiAdapter extends RecyclerView.Adapter<MokkiAdapter.MokkiViewHolder> {
    private ArrayList<MokkiItem> mMokki_scroll_list;

    public static class MokkiViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextViewOtsikko;
        public TextView mTextViewKuvaus;

        public MokkiViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextViewOtsikko = itemView.findViewById(R.id.TextViewOtsikko);
            mTextViewKuvaus = itemView.findViewById(R.id.TextViewKuvaus);
        }

    }

    public MokkiAdapter(ArrayList<MokkiItem> mokki_scroll_list)
    {
        mMokki_scroll_list = mokki_scroll_list;
    }

    @Override
    public MokkiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mokki_item, parent, false);
        MokkiViewHolder mvh = new MokkiViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MokkiViewHolder holder, int position) {
        MokkiItem currentMokki = mMokki_scroll_list.get(position);

        holder.mImageView.setImageResource(currentMokki.getMokkiImage());
        holder.mTextViewOtsikko.setText(currentMokki.getOtsikko());
        holder.mTextViewKuvaus.setText(currentMokki.getKuvaus());
    }

    @Override
    public int getItemCount() {
        return mMokki_scroll_list.size();
    }
}
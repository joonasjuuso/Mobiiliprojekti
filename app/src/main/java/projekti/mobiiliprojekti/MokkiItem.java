package projekti.mobiiliprojekti;

import android.os.Parcel;
import android.os.Parcelable;

public class MokkiItem implements Parcelable {
    //private int mMokkiImage;
    private String mOtsikko;
    private String mKuvaus;
    private String mHinta;

    public MokkiItem(/*int MokkiImage,*/ String otsikko, String kuvaus, String hinta)
    {
        //mMokkiImage = MokkiImage;
        mOtsikko = otsikko;
        mKuvaus = kuvaus;
        mHinta = hinta;
    }

    protected MokkiItem(Parcel in) {
        //mMokkiImage = in.readInt();
        mOtsikko = in.readString();
        mKuvaus = in.readString();
        mHinta = in.readString();
    }

    public static final Creator<MokkiItem> CREATOR = new Creator<MokkiItem>() {
        @Override
        public MokkiItem createFromParcel(Parcel in) {
            return new MokkiItem(in);
        }

        @Override
        public MokkiItem[] newArray(int size) {
            return new MokkiItem[size];
        }
    };

    /*public int getMokkiImage()
    {
        return mMokkiImage;
    }*/
    public String getOtsikko()
    {
        return mOtsikko;
    }
    public String getKuvaus()
    {
        return mKuvaus;
    }
    public String getHinta() { return mHinta; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(mMokkiImage);
        dest.writeString(mOtsikko);
        dest.writeString(mKuvaus);
        dest.writeString(mHinta);
    }
}

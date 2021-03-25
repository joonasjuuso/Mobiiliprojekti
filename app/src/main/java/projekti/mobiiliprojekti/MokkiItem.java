package projekti.mobiiliprojekti;

import android.os.Parcel;
import android.os.Parcelable;

public class MokkiItem implements Parcelable {
    //private int mMokkiImage;
    private String mOtsikko;
    private String mHinta;
    private String mOsoite;
    private String mHuoneMaara;
    private String mNelioMaara;
    private String mLammitys;
    private String mVesi;
    private String mSauna;
    private String mKuvaus;
    private String mOtsikkoID;
    //private String mOmistaja;



    public MokkiItem(/*int MokkiImage,*/ String otsikko, String hinta, String osoite, String huoneMaara,
                                         String nelioMaara, String lammitys, String vesi, String sauna, String kuvaus, String otsikkoID/*, String omistaja*/)
    {
        //mMokkiImage = MokkiImage;
        mOtsikko = otsikko;
        mHinta = hinta;
        mOsoite = osoite;
        mHuoneMaara = huoneMaara;
        mNelioMaara = nelioMaara;
        mLammitys = lammitys;
        mVesi = vesi;
        mSauna = sauna;
        mKuvaus = kuvaus;
        mOtsikkoID = otsikkoID;
        //mOmistaja = omistaja;
    }

    protected MokkiItem(Parcel in) {
        //mMokkiImage = in.readInt();
        mOtsikko = in.readString();
        mHinta = in.readString();
        mOsoite = in.readString();
        mHuoneMaara = in.readString();
        mNelioMaara = in.readString();
        mLammitys = in.readString();
        mVesi = in.readString();
        mSauna = in.readString();
        mKuvaus = in.readString();
        mOtsikkoID = in.readString();
        //mOmistaja = in.readString();
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
    public String getHinta() { return mHinta; }
    public String getOsoite() { return mOsoite; }
    public String getHuoneMaara() { return mHuoneMaara; }
    public String getNelioMaara() { return mNelioMaara; }
    public String getLammitys() { return mLammitys; }
    public String getVesi() { return mVesi; }
    public String getSauna() { return mSauna; }
    public String getKuvaus()
    {
        return mKuvaus;
    }
    public String getOtsikkoID() { return mOtsikkoID; }
    //public String getOmistaja() { return mOmistaja; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(mMokkiImage);
        dest.writeString(mOtsikko);
        dest.writeString(mHinta);
        dest.writeString(mOsoite);
        dest.writeString(mHuoneMaara);
        dest.writeString(mNelioMaara);
        dest.writeString(mLammitys);
        dest.writeString(mVesi);
        dest.writeString(mSauna);
        dest.writeString(mKuvaus);
        dest.writeString(mOtsikkoID);
        //dest.writeString(mOmistaja);
    }
}

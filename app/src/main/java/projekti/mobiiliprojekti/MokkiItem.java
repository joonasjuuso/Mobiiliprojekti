package projekti.mobiiliprojekti;

import android.os.Parcel;
import android.os.Parcelable;

public class MokkiItem implements Parcelable{
    private String mMokkiImage;
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
    private String mVuokraaja;
    //private String mOmistaja;

    public MokkiItem()
    {

    }

    public MokkiItem(String MokkiImage, String otsikko, String hinta, String osoite, String huoneMaara,
                     String nelioMaara, String lammitys, String vesi, String sauna, String kuvaus, String otsikkoID,
                     String vuokraaja /*, String omistaja*/)
    {
        mMokkiImage = MokkiImage;
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
        mVuokraaja = vuokraaja;
        //mOmistaja = omistaja;
    }

    protected MokkiItem(Parcel in) {
        mMokkiImage = in.readString();
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
        mVuokraaja = in.readString();
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

    public String getMokkiImage() { return mMokkiImage; }
    public String getOtsikko() { return mOtsikko; }
    public String getHinta() { return mHinta; }
    public String getOsoite() { return mOsoite; }
    public String getHuoneMaara() { return mHuoneMaara; }
    public String getNelioMaara() { return mNelioMaara; }
    public String getLammitys() { return mLammitys; }
    public String getVesi() { return mVesi; }
    public String getSauna() { return mSauna; }
    public String getKuvaus() { return mKuvaus; }
    public String getOtsikkoID() { return mOtsikkoID; }
    public String getVuokraaja() { return mVuokraaja; }
    //public String getOmistaja() { return mOmistaja; }


    public void setMokkiImage(String mokkiImage) { this.mMokkiImage = mokkiImage; }
    public void setOtsikko(String otsikko ) { this.mOtsikko = otsikko; }
    public void setHinta(String hinta ) { this.mHinta = hinta; }
    public void setOsoite(String osoite ) { this.mOsoite = osoite; }
    public void setHuoneMaara(String huoneMaara ) { this.mHuoneMaara = huoneMaara; }
    public void setNelioMaara(String nelioMaara ) { this.mNelioMaara = nelioMaara; }
    public void setLammitys(String lammitys ) { this.mLammitys = lammitys; }
    public void setVesi(String vesi ) { this.mVesi = vesi; }
    public void setSauna(String sauna ) { this.mSauna = sauna; }
    public void setKuvaus(String kuvaus ) { this.mKuvaus = kuvaus; }
    public void setOtsikkoID(String otsikkoID ) { this.mOtsikkoID = otsikkoID; }
    public void setVuokraaja(String vuokraaja ) { this.mVuokraaja = vuokraaja; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMokkiImage);
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
        dest.writeString(mVuokraaja);
        //dest.writeString(mOmistaja);
    }

    @Override
    public String toString()
    {
        return "MokkiItem{" +
                "vuokraaja='" + mVuokraaja + '/' + '}';
    }
}

package projekti.mobiiliprojekti;

public class MokkiItem {
    private int mMokkiImage;
    private String mOtsikko;
    private String mKuvaus;

    public MokkiItem(int MokkiImage, String otsikko, String kuvaus)
    {
        mMokkiImage = MokkiImage;
        mOtsikko = otsikko;
        mKuvaus = kuvaus;


    }

    public int getMokkiImage()
    {
        return mMokkiImage;
    }
    public String getOtsikko()
    {
        return mOtsikko;
    }
    public String getKuvaus()
    {
        return mKuvaus;
    }
}

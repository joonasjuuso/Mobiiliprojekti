package projekti.mobiiliprojekti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Invoices {
    private String orderID;
    private String vuokraaja;
    private String vuokranAntaja;
    private ArrayList<String> paivamaarat;
    private String hinta;
    private String otsikko;
    private String osoite;
    private String vuokraajaID;
    private String numero;
    private String sahkoposti;

    public Invoices() {

    }


    public Invoices(String messagePushID, String vuokraOtsikko, String vuokraOsoite, String uid, String vuokraaja, String vuokraID, String posti, String nro, ArrayList<String> paivat, String summa) {
        this.setOrderID(messagePushID);
        this.setOtsikko(vuokraOtsikko);
        this.setOsoite(vuokraOsoite);
        this.setVuokraaja(uid);
        this.setVuokranAntaja(vuokraaja);
        this.setVuokraajaID(vuokraID);
        this.setSahkoposti(posti);
        this.setNumero(nro);
        this.setPaivamaarat(paivat);
        this.setHinta(summa);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("orderID", getOrderID());
        result.put("otsikko", getOtsikko());
        result.put("osoite",getOsoite());
        result.put("asiakas", getVuokraaja());
        result.put("vuokranantaja", getVuokranAntaja());
        result.put("vuokranantajaID",getVuokraajaID());
        result.put("paivamaarat", getPaivamaarat());
        result.put("hinta", getHinta());
        return result;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String nro) {
        this.numero = nro;
    }
    public String getSahkoposti() {
        return sahkoposti;
    }
    public void setSahkoposti(String sposti) {
        this.sahkoposti = sposti;
    }

    public String getVuokraajaID() {
        return vuokraajaID;
    }
    public void setVuokraajaID(String vuokraID) {
        this.vuokraajaID = vuokraID;
    }
    public String getOrderID() {
        return orderID;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String vuokraOsoite) {
        this.osoite = vuokraOsoite;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getVuokraaja() {
        return vuokraaja;
    }

    public void setVuokraaja(String vuokraaja) {
        this.vuokraaja = vuokraaja;
    }

    public String getVuokranAntaja() {
        return vuokranAntaja;
    }

    public void setVuokranAntaja(String vuokranAntaja) {
        this.vuokranAntaja = vuokranAntaja;
    }

    public ArrayList<String> getPaivamaarat() {
        return paivamaarat;
    }

    public void setPaivamaarat(ArrayList<String> paivamaarat) {
        this.paivamaarat = paivamaarat;
    }

    public String getHinta() {
        return hinta;
    }

    public void setHinta(String hinta) {
        this.hinta = hinta;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }
}

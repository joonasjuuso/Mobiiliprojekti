package projekti.mobiiliprojekti;

import android.app.Application;

public class setDeleteVisible extends Application {
    private boolean visibility = false;

    public boolean getVisibility(){ return visibility; }

    public void setVisibility(boolean visible){ visibility = visible; }

}

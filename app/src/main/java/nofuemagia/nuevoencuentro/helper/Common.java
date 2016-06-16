package nofuemagia.nuevoencuentro.helper;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Tano on 16/06/2016.
 */
public class Common {

    public static void ShowOkMessage(View v, int mensaje) {
        final Snackbar snackBar = Snackbar.make(v, mensaje, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }
}

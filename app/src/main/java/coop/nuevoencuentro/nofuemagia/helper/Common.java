package coop.nuevoencuentro.nofuemagia.helper;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Tano on 16/06/2016.
 */
public class Common {

    public static final String PREFERENCES = "NOFUEMAGIA_NUEVOENCUENTRO";
    public static final String YA_REGISTRADO = "YA_REGISTRADO";

    public static final String PLAY_URL = "https://play.google.com/store/apps/details?id=coop.nuevoencuentro&hl=es";
    public static final String EMAIL = "EMAIL";
    public static final String NOMBRE = "NOMBRE";
    public static final String FBID = "FBID";
    public static final String FB_REG = "FB_REG";
    public static final String PRIMER_NOMBRE = "PRIMER_NOMBRE";
    public static String REGISTRAR_URL = "http://nofuemagia.ueuo.com/Nuevo/backend/usuarios/crearUsuario.php";

    public static String imagenURL = "http://nofuemagia.ueuo.com/Nuevo/imagenes/";

    public static String urlActividades = "http://nofuemagia.ueuo.com/Nuevo/backend/actividades/listActividades.php?fid=-2";

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

    public static void ShowMessage(View v, int mensaje) {
        final Snackbar snackBar = Snackbar.make(v, mensaje, Snackbar.LENGTH_SHORT);
        snackBar.show();
    }
}

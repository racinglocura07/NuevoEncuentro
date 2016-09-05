package coop.nuevoencuentro.nofuemagia.dos;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.squareup.picasso.Picasso;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Feriantes;

/**
 * Created by Tanoo on 1/9/2016.
 * Nuevo Encuentro
 * No Fue Magiaø
 */
public class DetalleActivity2 extends AppCompatActivity {
    public static final String IMAGEN_FULL = "IMAGEN_FULL";
    public static final String TITULO = "TITULO";
    public static final String DESC = "DESC";
    public static final String ITEM = "ITEM";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle2);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        Bundle args = getIntent().getExtras();

        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        TextView locationTitulo = (TextView) findViewById(R.id.location_titulo);
        TextView plcaeLocation = (TextView) findViewById(R.id.place_location);
        LinearLayout llFeriantes = (LinearLayout) findViewById(R.id.ll_feriantes);

        ImageView placePicutre = (ImageView) findViewById(R.id.image);

        String urlImagen;
        String titulo;
        String desc;

        final Feriantes feriante = args.getParcelable(ITEM);
        if (feriante == null) {
            urlImagen = args.getString(IMAGEN_FULL);
            titulo = args.getString(TITULO);
            desc = args.getString(DESC);

            llFeriantes.setVisibility(View.GONE);
            locationTitulo.setVisibility(View.GONE);
            plcaeLocation.setVisibility(View.GONE);
        } else {
            urlImagen = Common.imagenURL + "feriante-" + feriante.idFeriante + ".jpg";
            titulo = feriante.nombre;
            desc = feriante.descripcion;

            ImageButton mailBtn = (ImageButton) findViewById(R.id.mail_button_detalle);
            mailBtn.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_envelope).colorRes(R.color.partido_medio));
            mailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mandarMail(feriante.mail);
                }
            });
            if (TextUtils.isEmpty(feriante.mail))
                mailBtn.setVisibility(View.GONE);

            ImageButton faceBtn = (ImageButton) findViewById(R.id.facebook_button_detalle);
            faceBtn.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_facebook_official).colorRes(R.color.partido_medio));
            faceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getFacebookPageURL(feriante.facebook);
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    startActivity(facebookIntent);
                }
            });
            if (TextUtils.isEmpty(feriante.facebook))
                faceBtn.setVisibility(View.GONE);

            ImageButton llamarBtn = (ImageButton) findViewById(R.id.llamar_button_detalle);
            llamarBtn.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_phone).colorRes(R.color.partido_medio));
            llamarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + feriante.telefono));
                        if (ActivityCompat.checkSelfPermission(DetalleActivity2.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);
                    } catch (ActivityNotFoundException activityException) {
                        Common.ShowOkMessage(view, R.string.error_llamando);
                    }
                }
            });
            if (TextUtils.isEmpty(feriante.telefono))
                llamarBtn.setVisibility(View.GONE);

            locationTitulo.setText("Ubicación y Rubro");
            plcaeLocation.setText("Comuna " + feriante.comuna + " - " + feriante.rubro);
        }

        collapsingToolbar.setTitle(titulo);
        placeDetail.setText(desc);
        Picasso.with(this).load(urlImagen).into(placePicutre);

    }

    public String getFacebookPageURL(String pagina) {
        String FACEBOOK_URL = "https://www.facebook.com/" + pagina;

        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + pagina;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private void mandarMail(String mail) {
        String asunto = "";
        String cuerpo = "";

        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + asunto + "&body=" + cuerpo + "&to=" + "\"" + mail);
        testIntent.setData(data);
        startActivity(testIntent);
    }
}

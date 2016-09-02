package coop.nuevoencuentro.nofuemagia.dos;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import coop.nuevoencuentro.nofuemagia.R;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by Tanoo on 1/9/2016.
 * Nuevo Encuentro
 * No Fue Magiaø
 */
public class DetalleActivity2 extends AppCompatActivity {
    public static final String IMAGEN_FULL = "IMAGEN_FULL";
    public static final String TITULO = "TITULO";
    public static final String DESC = "DESC";

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
        String urlImagen = args.getString(IMAGEN_FULL);
        String titulo = args.getString(TITULO);
        String desc = args.getString(DESC);

        collapsingToolbar.setTitle(titulo);

        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(desc);

        ImageView placePicutre = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(args.getString(IMAGEN_FULL)).into(placePicutre);
    }
}

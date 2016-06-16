package nofuemagia.nuevoencuentro.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import nofuemagia.nuevoencuentro.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    public static final String IMAGEN_FULL = "IMAGEN_FULL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        Bundle args = getIntent().getExtras();

        ImageViewTouch ivFull = (ImageViewTouch) findViewById(R.id.iv_full);
        ivFull.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        Picasso.with(this).load(args.getInt(IMAGEN_FULL)).into(ivFull);
    }


}

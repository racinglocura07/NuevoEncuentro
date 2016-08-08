package coop.nuevoencuentro.nofuemagia.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.fragments.ActividadesAdminFragment;

/**
 * Created by jlionti on 29/07/2016. No Fue Magia
 */
public class AdminActivity extends AppCompatActivity {

    public static final String ESTALLER = "ESTALLER";
    public static final String NOTICIAS = "NOTICIAS";
    public static final String BOLSON = "BOLSON";

    public static final String TITULO = "TITULO";
    public static final String DESCRIPCION = "DESCRIPCION";
    public static final String IMAGEN_URL = "IMAGEN_URL";
    public static final String CUANDO = "CUANDO";
    public static final String REPITE = "REPITE";
    public static final String ID = "ID";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);
        
        Bundle args = getIntent().getExtras();
        ActividadesAdminFragment actividadesAdmin = (ActividadesAdminFragment) Fragment.instantiate(this, ActividadesAdminFragment.class.getName(), args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_admin_container, actividadesAdmin)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}

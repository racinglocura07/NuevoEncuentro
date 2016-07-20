package coop.nuevoencuentro.nofuemagia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import coop.nuevoencuentro.nofuemagia.adapters.PantallaPrincipalAdapter;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasFragment;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import cz.msebera.android.httpclient.Header;
import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.fcm.Util;
import coop.nuevoencuentro.nofuemagia.fragments.ActividadesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ComprasComunitariasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ContactoFragment;
import coop.nuevoencuentro.nofuemagia.fragments.TalleresFragment;
import coop.nuevoencuentro.nofuemagia.fragments.UbicacionFragment;
import coop.nuevoencuentro.nofuemagia.sync.SyncUtils;


public class PantallaPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String IDACTIVIDAD = "IDACTIVIDAD";

    private FragmentManager fragmentManager;
    private SharedPreferences preferences;
    private ActionBar abar;


    private ViewPager viewPager;

    private ComprasComunitariasFragment comprasFragment;
    private NoticiasFragment noticiasFragment;
    private ActividadesFragment actividadesFragment;
    private TalleresFragment talleresFragment;
    private ContactoFragment contactoFragment;
    private UbicacionFragment ubicacionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        abar = getSupportActionBar();

        preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

//        viewPager = (ViewPager) findViewById(R.id.vpager_principal);
//        viewPager.setAdapter(new PantallaPrincipalAdapter(getSupportFragmentManager()));

        assert abar != null;
        assert fab != null;
        assert drawer != null;
        assert navigationView != null;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_noticias).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_newspaper_o).colorRes(R.color.partido));
        menu.findItem(R.id.nav_actividades).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_book).colorRes(R.color.partido));
        menu.findItem(R.id.nav_talleres).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_users).colorRes(R.color.partido));
        menu.findItem(R.id.nav_compras_comunitarias).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_shopping_bag).colorRes(R.color.partido));
        menu.findItem(R.id.nav_contacto).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_info).colorRes(R.color.partido));
        menu.findItem(R.id.nav_ubicacion).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.partido));
        menu.findItem(R.id.nav_compartir).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_hand_peace_o).colorRes(R.color.partido));
        menu.findItem(R.id.nav_salir).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_close).colorRes(R.color.partido));

        SyncUtils.CreateSyncAccount(this);

        if (preferences.getBoolean(Common.FB_REG, false)) {
            String nombre = preferences.getString(Common.NOMBRE, null);
            String email = preferences.getString(Common.EMAIL, null);
            String id = preferences.getString(Common.FBID, null);
            String primer = preferences.getString(Common.PRIMER_NOMBRE, null);

            if (!preferences.getBoolean(Common.YA_REGISTRADO, false))
                sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken(), id, nombre, email);

            View headerView = navigationView.getHeaderView(0);

            TextView tvNombre = (TextView) headerView.findViewById(R.id.tv_nav_nombre);
            tvNombre.setText(getString(R.string.bienvenida, primer));


            noticiasFragment = new NoticiasFragment();
            actividadesFragment = new ActividadesFragment();
            talleresFragment = new TalleresFragment();
            comprasFragment = new ComprasComunitariasFragment();
            contactoFragment = new ContactoFragment();
            ubicacionFragment = new UbicacionFragment();


            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_container, noticiasFragment).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
        } else {
            Loguearse();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Profile.getCurrentProfile() == null) {
            Loguearse();
        }
    }

    private void sendRegistrationToServer(String token, String fid, String nombre, String email) {
        if (token == null || token.equals("") || fid == null || fid.equals("") || nombre == null || nombre.equals(""))
            return;

        RequestParams params = new RequestParams();
        params.put("registrationId", token);
        params.put("facebookId", fid);
        params.put("nombreApellido", nombre);
        params.put("email", email);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(25000 * 10);
        client.setTimeout(25000 * 10);
        client.post(Common.REGISTRAR_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (!response.getBoolean("error")) {
                        SharedPreferences preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Common.YA_REGISTRADO, true);
                        editor.apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            System.out.println("Confi");
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        int id = item.getItemId();

        abar.setTitle(item.getTitle());

        if (id == R.id.nav_noticias) {
            fragment = noticiasFragment;
        } else if (id == R.id.nav_actividades) {
            fragment = actividadesFragment;
        } else if (id == R.id.nav_talleres) {
            fragment = talleresFragment;
        } else if (id == R.id.nav_compras_comunitarias) {
            fragment = comprasFragment;
        } else if (id == R.id.nav_contacto) {
            fragment = contactoFragment;
        } else if (id == R.id.nav_ubicacion) {
            fragment = ubicacionFragment;
        } else if (id == R.id.nav_compartir) {
            CompartirApp();
        } else if (id == R.id.nav_salir) {
            LoginManager.getInstance().logOut();
            Loguearse();
            return true;
        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CompartirApp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.compartir_asunto));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Common.PLAY_URL);

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.compartir_titulo)));
    }

    private void Loguearse() {
        finish();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }
}

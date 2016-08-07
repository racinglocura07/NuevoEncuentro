package coop.nuevoencuentro.nofuemagia.activities;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.fragments.ActividadesAdminFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ActividadesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ComprasComunitariasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ContactoFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.TalleresFragment;
import coop.nuevoencuentro.nofuemagia.fragments.UbicacionFragment;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.sync.SyncUtils;
import cz.msebera.android.httpclient.Header;


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
    private NavigationView navigationView;

    private AsyncHttpClient client;

    private boolean mEsAdmin;
    private boolean mTieneAdmin;

    public AsyncHttpClient GetAsynk(){
        return client;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        mEsAdmin = false;
        mTieneAdmin = false;

        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        abar = getSupportActionBar();

        preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);

        client = new AsyncHttpClient();
        client.setConnectTimeout(25000 * 10);
        client.setTimeout(25000 * 10);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

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

            boolean registrado = preferences.getBoolean(Common.YA_REGISTRADO, false);
            if (!registrado)
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

            String ultima = preferences.getString(Common.ULTIMA, Common.NOTICIAS);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String abrirDonde = extras.getString(Common.ABRIR_DONDE);
                AbrirEnFragment(abrirDonde == null ? ultima : abrirDonde);
            } else {
                AbrirEnFragment(ultima);
            }


            CheckEsAdmin(id);
            RateApp();

        } else {
            Loguearse();
        }
    }

    private void RateApp() {

        final String RATE_NO_MOSTRAR = "RATE_NO_MOSTRAR";
        String RATE_VECES = "RATE_VECES";
        String RATE_FECHA = "RATE_FECHA";

        int DAYS_UNTIL_PROMPT = 3;
        int LAUNCHES_UNTIL_PROMPT = 7;

        if (preferences.getBoolean(RATE_NO_MOSTRAR, false)) { return ; }

        final SharedPreferences.Editor editor = preferences.edit();

        long launch_count = preferences.getLong(RATE_VECES, 0) + 1;
        editor.putLong(RATE_VECES, launch_count);

        Long date_firstLaunch = preferences.getLong(RATE_FECHA, 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong(RATE_FECHA, date_firstLaunch);
        }

        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                AlertDialog.Builder buider = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setMessage(R.string.calificando_la_aplicaci_n_nos_ayudas_a_mejorarla)
                        .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_star).colorRes(R.color.partido))
                        .setTitle(R.string.calificar_app)
                        .setNegativeButton("No, Gracias!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putBoolean(RATE_NO_MOSTRAR, true);
                                editor.apply();
                            }
                        })
                        .setPositiveButton("Calificar ahora!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putBoolean(RATE_NO_MOSTRAR, true);
                                editor.apply();
                                mostrarMarket();
                            }
                        });

                buider.show();
            }
        }

        editor.apply();
    }

    public void mostrarMarket(){
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Imposible abrir la tienda", Toast.LENGTH_LONG).show();
        }
    }

    private void CheckEsAdmin(String id) {

        RequestParams params = new RequestParams();
        params.put("facebookId", id);
        client.post(Common.ESADMIN_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println(response);
                    if (!response.getBoolean("error")) {
                        mEsAdmin = response.getBoolean("esAdmin");
                        invalidateOptionsMenu();
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


    private void AbrirEnFragment(String abrirDonde) {
        if (abrirDonde == null)
            abrirDonde = Common.NOTICIAS;

        switch (abrirDonde) {
            case Common.NOTICIAS:
                fragmentManager.beginTransaction().replace(R.id.main_container, noticiasFragment, Common.NOTICIAS).commit();
                navigationView.getMenu().getItem(0).setChecked(true);
                mTieneAdmin = true;
                break;
            case Common.ACTIVIDADES:
                fragmentManager.beginTransaction().replace(R.id.main_container, actividadesFragment, Common.ACTIVIDADES).commit();
                navigationView.getMenu().getItem(1).setChecked(true);
                mTieneAdmin = true;
                break;
            case Common.TALLERES:
                fragmentManager.beginTransaction().replace(R.id.main_container, talleresFragment, Common.TALLERES).commit();
                navigationView.getMenu().getItem(2).setChecked(true);
                mTieneAdmin = true;
                break;
            case Common.BOLSONES:
                fragmentManager.beginTransaction().replace(R.id.main_container, comprasFragment, Common.BOLSONES).commit();
                navigationView.getMenu().getItem(3).setChecked(true);
                mTieneAdmin = false;
                break;
            case Common.CONTACTO:
                fragmentManager.beginTransaction().replace(R.id.main_container, contactoFragment, Common.CONTACTO).commit();
                navigationView.getMenu().getItem(4).setChecked(true);
                mTieneAdmin = false;
                break;
            case Common.MICOMUNA:
                fragmentManager.beginTransaction().replace(R.id.main_container, ubicacionFragment, Common.MICOMUNA).commit();
                navigationView.getMenu().getItem(5).setChecked(true);
                mTieneAdmin = false;
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!preferences.getBoolean(Common.FB_REG, false)) {
            Loguearse();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            String tag = getSupportFragmentManager().findFragmentById(R.id.main_container).getTag();
            preferences.edit().putString(Common.ULTIMA, tag).apply();
        } catch (Exception ex) {
            //System.err.println(ex.getMessage());
        }

        super.onDestroy();
    }

    private void sendRegistrationToServer(String token, String fid, String nombre, String email) {
        if (token == null || token.equals("") || fid == null || fid.equals("") || nombre == null || nombre.equals(""))
            return;

        RequestParams params = new RequestParams();
        params.put("registrationId", token);
        params.put("facebookId", fid);
        params.put("nombreApellido", nombre);
        params.put("email", email);

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
            //super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
        MenuItem miAdmin = menu.findItem(R.id.action_admin);

        miAdmin.setVisible(mEsAdmin && mTieneAdmin);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent conf = new Intent(this, ConfiguracionActivity.class);
            startActivity(conf);
            return true;
        } else if (id == R.id.action_admin) {

            Bundle args = new Bundle();
            if (getSupportFragmentManager().findFragmentByTag(Common.ACTIVIDADES) != null) {
                args.putBoolean(ActividadesAdminFragment.ESTALLER, false);
                args.putBoolean(ActividadesAdminFragment.NOTICIAS, false);
            } else if (getSupportFragmentManager().findFragmentByTag(Common.TALLERES) != null) {
                args.putBoolean(ActividadesAdminFragment.ESTALLER, true);
                args.putBoolean(ActividadesAdminFragment.NOTICIAS, false);
            } else if (getSupportFragmentManager().findFragmentByTag(Common.NOTICIAS) != null) {
                args.putBoolean(ActividadesAdminFragment.ESTALLER, false);
                args.putBoolean(ActividadesAdminFragment.NOTICIAS, true);
            } else {
                Toast.makeText(this, "Opcion no valida en esta seccion", Toast.LENGTH_LONG).show();
                return true;
            }

            Intent admin = new Intent(this, AdminActivity.class);
            admin.putExtras(args);
            startActivity(admin);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        int id = item.getItemId();
        String tag = null;

        abar.setTitle(item.getTitle());

        if (id == R.id.nav_noticias) {
            fragment = noticiasFragment;
            tag = Common.NOTICIAS;
            mTieneAdmin = true;
        } else if (id == R.id.nav_actividades) {
            fragment = actividadesFragment;
            tag = Common.ACTIVIDADES;
            mTieneAdmin = true;
        } else if (id == R.id.nav_talleres) {
            fragment = talleresFragment;
            tag = Common.TALLERES;
            mTieneAdmin = true;
        } else if (id == R.id.nav_compras_comunitarias) {
            fragment = comprasFragment;
            tag = Common.BOLSONES;
            mTieneAdmin = false;
        } else if (id == R.id.nav_contacto) {
            fragment = contactoFragment;
            tag = Common.CONTACTO;
            mTieneAdmin = false;
        } else if (id == R.id.nav_ubicacion) {
            fragment = ubicacionFragment;
            tag = Common.MICOMUNA;
            mTieneAdmin = false;
        } else if (id == R.id.nav_compartir) {
            CompartirApp();
        } else if (id == R.id.nav_salir) {
            LoginManager.getInstance().logOut();
            Loguearse();
            return true;
        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment, tag).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);

        invalidateOptionsMenu();

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

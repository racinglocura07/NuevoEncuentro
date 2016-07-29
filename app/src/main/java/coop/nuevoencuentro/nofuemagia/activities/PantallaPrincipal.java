package coop.nuevoencuentro.nofuemagia.activities;

import android.app.PendingIntent;
import android.content.Context;
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

    private MenuItem miAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

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


        } else {
            Loguearse();
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
                        boolean esAdmin = response.getBoolean("esAdmin");
                        miAdmin.setVisible(esAdmin);
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
                break;
            case Common.ACTIVIDADES:
                fragmentManager.beginTransaction().replace(R.id.main_container, actividadesFragment, Common.ACTIVIDADES).commit();
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
            case Common.TALLERES:
                fragmentManager.beginTransaction().replace(R.id.main_container, talleresFragment, Common.TALLERES).commit();
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
            case Common.BOLSONES:
                fragmentManager.beginTransaction().replace(R.id.main_container, comprasFragment, Common.BOLSONES).commit();
                navigationView.getMenu().getItem(3).setChecked(true);
                break;
            case Common.CONTACTO:
                fragmentManager.beginTransaction().replace(R.id.main_container, contactoFragment, Common.CONTACTO).commit();
                navigationView.getMenu().getItem(4).setChecked(true);
                break;
            case Common.MICOMUNA:
                fragmentManager.beginTransaction().replace(R.id.main_container, ubicacionFragment, Common.MICOMUNA).commit();
                navigationView.getMenu().getItem(5).setChecked(true);
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

        miAdmin = menu.findItem(R.id.action_admin);

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
            } else if (getSupportFragmentManager().findFragmentByTag(Common.TALLERES) != null) {
                args.putBoolean(ActividadesAdminFragment.ESTALLER, true);
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
        } else if (id == R.id.nav_actividades) {
            fragment = actividadesFragment;
            tag = Common.ACTIVIDADES;
        } else if (id == R.id.nav_talleres) {
            fragment = talleresFragment;
            tag = Common.TALLERES;
        } else if (id == R.id.nav_compras_comunitarias) {
            fragment = comprasFragment;
            tag = Common.BOLSONES;
        } else if (id == R.id.nav_contacto) {
            fragment = contactoFragment;
            tag = Common.CONTACTO;
        } else if (id == R.id.nav_ubicacion) {
            fragment = ubicacionFragment;
            tag = Common.MICOMUNA;
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

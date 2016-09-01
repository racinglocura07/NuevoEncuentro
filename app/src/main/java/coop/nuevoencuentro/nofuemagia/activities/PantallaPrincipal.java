package coop.nuevoencuentro.nofuemagia.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.fragments.ActividadesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ComprasComunitariasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ContactoFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.TalleresFragment;
import coop.nuevoencuentro.nofuemagia.fragments.TwitterFragment;
import coop.nuevoencuentro.nofuemagia.fragments.UbicacionFragment;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.helper.CustomRequest;
import coop.nuevoencuentro.nofuemagia.model.Actividades;
import coop.nuevoencuentro.nofuemagia.sync.SyncUtils;

//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.JsonHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

//import cz.msebera.android.httpclient.Header;
//import cz.msebera.android.httpclient.HttpHeaders;


public class PantallaPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private SharedPreferences preferences;

    private ComprasComunitariasFragment comprasFragment;
    private TwitterFragment twitterFragment;
    private NoticiasFragment noticiasFragment;
    private ActividadesFragment actividadesFragment;
    private TalleresFragment talleresFragment;
    private ContactoFragment contactoFragment;
    private UbicacionFragment ubicacionFragment;
    private NavigationView navigationView;

    //private AsyncHttpClient client;

    private boolean mEsAdmin;
    private boolean mTieneAdmin;

    private RequestQueue mRequestQueue;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout col;

    public RequestQueue GetRequest() {
        return mRequestQueue;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_pantalla_principal);


        mEsAdmin = false;
        mTieneAdmin = false;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);

        /*client = new AsyncHttpClient();
        client.setConnectTimeout(25000 * 10);
        client.setTimeout(25000 * 10);*/

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        col = (CollapsingToolbarLayout) findViewById(R.id.col_toolbar);

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
        menu.findItem(R.id.nav_twitter).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_twitter).colorRes(R.color.partido));
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
            twitterFragment = new TwitterFragment();
            contactoFragment = new ContactoFragment();
            ubicacionFragment = new UbicacionFragment();

            fragmentManager = getSupportFragmentManager();

            if (savedInstanceState == null)
                CheckBundle();


            CheckEsAdmin(id);
            RateApp();

        } else {
            Loguearse();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void CheckBundle() {
        String ultima = preferences.getString(Common.ULTIMA, Common.NOTICIAS);
        Bundle extras = getIntent().getExtras();
        System.out.println("exttas " + extras);
        System.out.println("ultima " + ultima);
        if (extras != null) {
            String abrirDonde = extras.getString(Common.ABRIR_DONDE);
            AbrirEnFragment(abrirDonde == null ? ultima : abrirDonde);
        } else {
            AbrirEnFragment(ultima);
        }
    }

    private void RateApp() {

        final String RATE_NO_MOSTRAR = "RATE_NO_MOSTRAR";
        String RATE_VECES = "RATE_VECES";
        String RATE_FECHA = "RATE_FECHA";

        int DAYS_UNTIL_PROMPT = 3;
        int LAUNCHES_UNTIL_PROMPT = 7;

        if (preferences.getBoolean(RATE_NO_MOSTRAR, false)) {
            return;
        }

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

    public void mostrarMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Imposible abrir la tienda", Toast.LENGTH_LONG).show();
        }
    }

    private void CheckEsAdmin(String id) {
        preferences.edit().putBoolean(Common.ES_ADMIN, false).apply();

        final Map<String, String> params = new HashMap<>();
        params.put("facebookid", id);

        String url = Common.ESADMIN_URL;
        CustomRequest checkAdmin = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getBoolean("error")) {
                        mEsAdmin = response.getBoolean("esAdmin");
                        invalidateOptionsMenu();
                        preferences.edit().putBoolean(Common.ES_ADMIN, mEsAdmin).apply();
                    }
                } catch (JSONException e) {
                    preferences.edit().putBoolean(Common.ES_ADMIN, false).apply();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(checkAdmin);
    }


    private void AbrirEnFragment(String abrirDonde) {
        if (abrirDonde == null)
            abrirDonde = Common.NOTICIAS;

        switch (abrirDonde) {
            case Common.NOTICIAS:
                fragmentManager.beginTransaction().replace(R.id.main_container, noticiasFragment, Common.NOTICIAS).commit();
                navigationView.getMenu().getItem(0).setChecked(true);
                mTieneAdmin = true;
                appBar.setExpanded(true);
                break;
            case Common.ACTIVIDADES:
                fragmentManager.beginTransaction().replace(R.id.main_container, actividadesFragment, Common.ACTIVIDADES).commit();
                navigationView.getMenu().getItem(1).setChecked(true);
                mTieneAdmin = true;
                appBar.setExpanded(true);
                break;
            case Common.TALLERES:
                fragmentManager.beginTransaction().replace(R.id.main_container, talleresFragment, Common.TALLERES).commit();
                navigationView.getMenu().getItem(2).setChecked(true);
                mTieneAdmin = true;
                appBar.setExpanded(true);
                break;
            case Common.BOLSONES:
                fragmentManager.beginTransaction().replace(R.id.main_container, comprasFragment, Common.BOLSONES).commit();
                navigationView.getMenu().getItem(3).setChecked(true);
                mTieneAdmin = true;
                appBar.setExpanded(false);
                break;
            case Common.TWITTER:
                fragmentManager.beginTransaction().replace(R.id.main_container, twitterFragment, Common.TWITTER).commit();
                navigationView.getMenu().getItem(4).setChecked(true);
                mTieneAdmin = false;
                appBar.setExpanded(false);
                break;
            case Common.CONTACTO:
                fragmentManager.beginTransaction().replace(R.id.main_container, contactoFragment, Common.CONTACTO).commit();
                navigationView.getMenu().getItem(5).setChecked(true);
                mTieneAdmin = false;
                appBar.setExpanded(false);
                break;
            case Common.MICOMUNA:
                fragmentManager.beginTransaction().replace(R.id.main_container, ubicacionFragment, Common.MICOMUNA).commit();
                navigationView.getMenu().getItem(6).setChecked(true);
                mTieneAdmin = false;
                appBar.setExpanded(false);
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
        GrabarUltimo();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        GrabarUltimo();
        super.onPause();
    }

    private void GrabarUltimo() {
        try {
            String tag = getSupportFragmentManager().findFragmentById(R.id.main_container).getTag();
            preferences.edit().putString(Common.ULTIMA, tag).apply();
        } catch (Exception ex) {
            //System.err.println(ex.getMessage());
        }
    }

    private void sendRegistrationToServer(String token, String fid, String nombre, String email) {
        if (token == null || token.equals("") || fid == null || fid.equals("") || nombre == null || nombre.equals(""))
            return;

        final Map<String, String> params = new HashMap<>();
        params.put("registrationId", token);
        params.put("facebookId", fid);
        params.put("nombreApellido", nombre);
        params.put("email", email);

        String url = Common.REGISTRAR_URL;
        CustomRequest registrar = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getBoolean("error")) {
                        System.out.println("REGISTRADO!");
                        preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);
                        preferences.edit().putBoolean(Common.YA_REGISTRADO, true).apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    preferences.edit().putBoolean(Common.YA_REGISTRADO, false).apply();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                preferences.edit().putBoolean(Common.YA_REGISTRADO, false).apply();
            }
        });

        mRequestQueue.add(registrar);
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
        miAdmin.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_plus).colorRes(R.color.blanco).actionBarSize());
        miAdmin.setVisible(mEsAdmin && mTieneAdmin);

        MenuItem miNotif = menu.findItem(R.id.action_notif);
        miNotif.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_paper_plane).colorRes(R.color.blanco).actionBarSize());
        miNotif.setVisible(mEsAdmin);

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
                args.putBoolean(AdminActivity.ESTALLER, false);
                args.putBoolean(AdminActivity.NOTICIAS, false);
                args.putBoolean(AdminActivity.BOLSON, false);
            } else if (getSupportFragmentManager().findFragmentByTag(Common.TALLERES) != null) {
                args.putBoolean(AdminActivity.ESTALLER, true);
                args.putBoolean(AdminActivity.NOTICIAS, false);
                args.putBoolean(AdminActivity.BOLSON, false);
            } else if (getSupportFragmentManager().findFragmentByTag(Common.NOTICIAS) != null) {
                args.putBoolean(AdminActivity.ESTALLER, false);
                args.putBoolean(AdminActivity.NOTICIAS, true);
                args.putBoolean(AdminActivity.BOLSON, false);
            } else if (getSupportFragmentManager().findFragmentByTag(Common.BOLSONES) != null) {
                args.putBoolean(AdminActivity.ESTALLER, false);
                args.putBoolean(AdminActivity.NOTICIAS, false);
                args.putBoolean(AdminActivity.BOLSON, true);
            } else {
                Toast.makeText(this, "Opcion no valida en esta seccion", Toast.LENGTH_LONG).show();
                return true;
            }

            Intent admin = new Intent(this, AdminActivity.class);
            admin.putExtras(args);
            startActivity(admin);
            return true;
        } else if (id == R.id.action_notif) {
            EnviarNotificacion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void EnviarNotificacion() {

        List<Actividades> lista = Actividades.GetAll();
        lista.add(0, new Actividades(-1, "--Ninguna--"));
        lista.add(1, new Actividades(-2, "--FORMULARIO--"));
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        View dialogView = View.inflate(this, R.layout.dialog_notificacion, null);// LayoutInflater.from(this).inflate(R.layout.dialog_nombre, null, false);
        final TextInputEditText etTitulo = (TextInputEditText) dialogView.findViewById(R.id.et_titulo_notif);
        final TextInputEditText etMensaje = (TextInputEditText) dialogView.findViewById(R.id.et_desc_notif);
        final Spinner spActividades = (Spinner) dialogView.findViewById(R.id.sp_actividades);
        spActividades.setAdapter(spinnerArrayAdapter);


        new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.app_name)
                .setCancelable(true)
                .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_paper_plane).actionBarSize().color(R.color.colorAccent))
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Map<String, String> params = new HashMap<>();
                        params.put("titulo", etTitulo.getText().toString().trim());
                        params.put("cuerpo", etMensaje.getText().toString().trim());
                        params.put("idActividad", ((Actividades) spActividades.getSelectedItem()).idActividad + "");

                        String url = Common.ENVIARNOTIFICACION;
                        CustomRequest registrar = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    final boolean error = response.getBoolean("error");
                                    String title = error ? "Error" : "Nuevo Encuentro";
                                    String msg = response.getString("mensaje");
                                    Drawable icono = new IconDrawable(PantallaPrincipal.this, error ? FontAwesomeIcons.fa_warning : FontAwesomeIcons.fa_hand_peace_o);

                                    new AlertDialog.Builder(PantallaPrincipal.this)
                                            .setTitle(title)
                                            .setMessage(msg)
                                            .setIcon(icono)
                                            .setPositiveButton("Ok", null)
                                            .show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                preferences.edit().putBoolean(Common.YA_REGISTRADO, false).apply();
                            }
                        });

                        mRequestQueue.add(registrar);
                    }
                })
                .show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //CheckEsAdmin(preferences.getString(Common.FBID, null));

        Fragment fragment = null;

        setTitle(item.getTitle());

        int id = item.getItemId();
        String tag = null;

        if (id == R.id.nav_noticias) {
            fragment = noticiasFragment;
            tag = Common.NOTICIAS;
            mTieneAdmin = true;
            appBar.setExpanded(true, true);
        } else if (id == R.id.nav_actividades) {
            fragment = actividadesFragment;
            tag = Common.ACTIVIDADES;
            appBar.setExpanded(true, true);
            mTieneAdmin = true;
        } else if (id == R.id.nav_talleres) {
            fragment = talleresFragment;
            appBar.setExpanded(true, true);
            tag = Common.TALLERES;
            mTieneAdmin = true;
        } else if (id == R.id.nav_compras_comunitarias) {
            fragment = comprasFragment;
            tag = Common.BOLSONES;
            appBar.setExpanded(false, true);
            mTieneAdmin = true;
        } else if (id == R.id.nav_twitter) {
            fragment = twitterFragment;
            tag = Common.TWITTER;
            appBar.setExpanded(false, true);
            mTieneAdmin = false;
        } else if (id == R.id.nav_contacto) {
            fragment = contactoFragment;
            tag = Common.CONTACTO;
            appBar.setExpanded(false, true);
            mTieneAdmin = false;
        } else if (id == R.id.nav_ubicacion) {
            fragment = ubicacionFragment;
            appBar.setExpanded(false, true);
            tag = Common.MICOMUNA;
            mTieneAdmin = false;
        } else if (id == R.id.nav_compartir) {
            appBar.setExpanded(false, true);
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

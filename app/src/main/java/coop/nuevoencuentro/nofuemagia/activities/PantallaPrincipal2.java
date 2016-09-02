package coop.nuevoencuentro.nofuemagia.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
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
import coop.nuevoencuentro.nofuemagia.dos.FijoViewPager;
import coop.nuevoencuentro.nofuemagia.dos.PrincipalAdapter2;
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

/**
 * Created by Tanoo on 1/9/2016.
 * Nuevo Encuentro
 * No Fue Magiaø
 */
public class PantallaPrincipal2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RequestQueue mRequestQueue;
    private FijoViewPager viewPager;
    private DrawerLayout drawer;

    private boolean mTieneAdmin;
    private boolean mEsAdmin;

    private SharedPreferences preferences;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal2);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        mEsAdmin = false;
        mTieneAdmin = false;

        preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_principal2);
        setSupportActionBar(toolbar);

        viewPager = (FijoViewPager) findViewById(R.id.viewpager_principal2);
        PrincipalAdapter2 adapter = new PrincipalAdapter2(getSupportFragmentManager());
        adapter.addFragment(new NoticiasFragment(), "Noticias");
        adapter.addFragment(new ActividadesFragment(), "Actividades");
        adapter.addFragment(new TalleresFragment(), "Talleres");
        adapter.addFragment(new ComprasComunitariasFragment(), "Bolsones");
        adapter.addFragment(new TwitterFragment(), "Twitter");
        adapter.addFragment(new ContactoFragment(), "Contacto");
        adapter.addFragment(new UbicacionFragment(), "Ubicacion");
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(10);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        navigationView.getMenu().getItem(0).setChecked(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

            if (savedInstanceState == null)
                CheckBundle();

            CheckEsAdmin(id);
            RateApp();

        } else {
            Loguearse();
        }

    }

    public RequestQueue GetRequest() {
        return mRequestQueue;
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
                                    Drawable icono = new IconDrawable(PantallaPrincipal2.this, error ? FontAwesomeIcons.fa_warning : FontAwesomeIcons.fa_hand_peace_o);

                                    new AlertDialog.Builder(PantallaPrincipal2.this)
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

    private void CheckBundle() {
        Map<String, ?> all = preferences.getAll();
        if (all.get(Common.ULTIMA) instanceof String) {
            preferences.edit().remove(Common.ULTIMA).apply();
        }

        int ultima = preferences.getInt(Common.ULTIMA, 0);
        Bundle extras = getIntent().getExtras();
        System.out.println("exttas " + extras);
        System.out.println("ultima " + ultima);
        if (extras != null) {
            int abrirDonde = extras.getInt(Common.ABRIR_DONDE, -1);
            viewPager.setCurrentItem(abrirDonde == -1 ? ultima : abrirDonde);
            navigationView.getMenu().getItem(abrirDonde == -1 ? ultima : abrirDonde).setChecked(true);
        } else {
            viewPager.setCurrentItem(ultima);
            navigationView.getMenu().getItem(ultima).setChecked(true);
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
            preferences.edit().putInt(Common.ULTIMA, viewPager.getCurrentItem()).apply();
        } catch (Exception ex) {
            //System.err.println(ex.getMessage());
        }
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setTitle(item.getTitle());
        item.setChecked(true);

        int id = item.getItemId();
        if (id == R.id.nav_noticias) {
            viewPager.setCurrentItem(0);
            mTieneAdmin = true;
        } else if (id == R.id.nav_actividades) {
            viewPager.setCurrentItem(1);
            mTieneAdmin = true;
        } else if (id == R.id.nav_talleres) {
            viewPager.setCurrentItem(2);
            mTieneAdmin = true;
        } else if (id == R.id.nav_compras_comunitarias) {
            viewPager.setCurrentItem(3);
            mTieneAdmin = true;
        } else if (id == R.id.nav_twitter) {
            viewPager.setCurrentItem(4);
            mTieneAdmin = false;
        } else if (id == R.id.nav_contacto) {
            viewPager.setCurrentItem(5);
            mTieneAdmin = false;
        } else if (id == R.id.nav_ubicacion) {
            viewPager.setCurrentItem(6);
            mTieneAdmin = false;
        } else if (id == R.id.nav_compartir) {
            CompartirApp();
        } else if (id == R.id.nav_salir) {
            LoginManager.getInstance().logOut();
            Loguearse();
        }

        drawer.closeDrawer(GravityCompat.START);
        invalidateOptionsMenu();

        return true;
    }

    public int fragmentActual() {
        return viewPager.getCurrentItem();
    }
}

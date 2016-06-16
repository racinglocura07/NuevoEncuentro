package nofuemagia.nuevoencuentro.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import nofuemagia.nuevoencuentro.R;
import nofuemagia.nuevoencuentro.fcm.Util;
import nofuemagia.nuevoencuentro.fragments.ActividadesFragment;
import nofuemagia.nuevoencuentro.fragments.ComprasComunitariasFragment;
import nofuemagia.nuevoencuentro.fragments.ContactoFragment;
import nofuemagia.nuevoencuentro.fragments.TalleresFragment;
import nofuemagia.nuevoencuentro.fragments.UbicacionFragment;
import nofuemagia.nuevoencuentro.model.Usuarios;


public class PantallaPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final int REQUEST_LOGIN_CODE = 0x0010;
    private FragmentManager fragmentManager;
    private Picasso mPicasso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        assert fab != null;
        assert drawer != null;
        assert navigationView != null;

        mPicasso = Picasso.with(this);


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
        menu.findItem(R.id.nav_actividades).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_book).colorRes(R.color.partido));
        menu.findItem(R.id.nav_talleres).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_users).colorRes(R.color.partido));
        menu.findItem(R.id.nav_compras_comunitarias).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_shopping_bag).colorRes(R.color.partido));
        menu.findItem(R.id.nav_contacto).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_info).colorRes(R.color.partido));
        menu.findItem(R.id.nav_ubicacion).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.partido));
        menu.findItem(R.id.nav_compartir).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_hand_peace_o).colorRes(R.color.partido));
        menu.findItem(R.id.nav_salir).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_close).colorRes(R.color.partido));

        //SyncUtils.CreateSyncAccount(this);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_container, new ActividadesFragment()).commit();

        if (!isLoggedIn()) {
            Loguearse();
            return;
        }

        Profile profile = Profile.getCurrentProfile();

        View headerView = navigationView.getHeaderView(0);

        TextView tvNombre = (TextView) headerView.findViewById(R.id.tv_nav_nombre);
        tvNombre.setText(getString(R.string.bienvenida, profile.getFirstName()));

        SharedPreferences preferences = getSharedPreferences(Util.PREFERENCES, MODE_PRIVATE);
        if (!preferences.getBoolean(Util.YA_REGISTRADO, false))
            sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken(), profile.getId());

    }

    private byte[] GenerateJson(String registrationId, String facebookId) {
        try {
            JSONObject datos = new JSONObject();
            datos.put("registrationId", registrationId);
            datos.put("facebookId", facebookId);

            return datos.toString().getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    private void sendRegistrationToServer(String token, String fid) {
        RequestParams params = new RequestParams();
        params.put("registrationId",token);
        params.put("facebookId",fid);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(25000 * 10);
        client.setTimeout(25000 * 10);
        client.post(Util.REGISTRAR_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
                System.out.println("onSuccess! " + new String(responseBytes));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
                System.out.println("onFailure! uno " + statusCode + throwable.getLocalizedMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("onFailure! dos " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("onSuccess! " + responseString);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN_CODE && !isLoggedIn()) {
            Toast.makeText(this, R.string.iniciar_sesion, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_actividades) {
            fragment = new ActividadesFragment();

        } else if (id == R.id.nav_talleres) {
            fragment = new TalleresFragment();
        } else if (id == R.id.nav_compras_comunitarias) {
            fragment = new ComprasComunitariasFragment();
        } else if (id == R.id.nav_contacto) {
            fragment = new ContactoFragment();
        } else if (id == R.id.nav_ubicacion) {

            //GoogleMapOptions options = new GoogleMapOptions();
            //fragment = UbicacionFragment.newInstance(options);
            fragment = new UbicacionFragment();

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
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Util.PLAY_URL);

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.compartir_titulo)));
    }

    private void Loguearse() {

        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login, REQUEST_LOGIN_CODE);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public Picasso getInstancePicasso() {
        return mPicasso;
    }
}

package coop.nuevoencuentro.nofuemagia.activities;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by Tano on 21/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class ConfiguracionActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        try {
            tvVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            
        }

        SwitchCompat tbNoticia = (SwitchCompat) findViewById(R.id.tb_noticia);
        tbNoticia.setOnCheckedChangeListener(this);
        tbNoticia.setTag(Common.RECIBIR_NOTICIA);
        tbNoticia.setChecked(preferences.getBoolean(Common.RECIBIR_NOTICIA, false));

        SwitchCompat tbActividad = (SwitchCompat) findViewById(R.id.tb_actividad);
        tbActividad.setOnCheckedChangeListener(this);
        tbActividad.setTag(Common.RECIBIR_ACTIVIDAD);
        tbActividad.setChecked(preferences.getBoolean(Common.RECIBIR_ACTIVIDAD, false));

        SwitchCompat tbTaller = (SwitchCompat) findViewById(R.id.tb_taller);
        tbTaller.setOnCheckedChangeListener(this);
        tbTaller.setTag(Common.RECIBIR_BOLSON);
        tbTaller.setChecked(preferences.getBoolean(Common.RECIBIR_BOLSON, true));

        SwitchCompat tbMensajes = (SwitchCompat) findViewById(R.id.tb_mensajes);
        tbMensajes.setOnCheckedChangeListener(this);
        tbMensajes.setTag(Common.RECIBIR_MENSAJES);
        tbMensajes.setChecked(preferences.getBoolean(Common.RECIBIR_MENSAJES, true));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        preferences.edit().putBoolean(compoundButton.getTag().toString(), b).apply();
    }
}

package coop.nuevoencuentro.nofuemagia.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Locale;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import cz.msebera.android.httpclient.Header;

/**
 * Created by jlionti on 29/07/2016. No Fue Magia
 */
public class ActividadesAdminFragment extends Fragment {

    private static final int SELECT_PICTURE = 0x1212;
    public static final String ESTALLER = "ESTALLER";
    public static final String NOTICIAS = "NOTICIAS";

    private TextInputEditText etNombre;
    private TextInputEditText etDescripcion;
    private IconTextView itvCamera;
    private TextInputEditText etCuando;
    private Spinner spRepite;
    private CheckBox cbNotificar;
    private Button btnGuardar;

    private Calendar calendar;
    private ImageView ivCargada;

    private Uri mSelectedImage;
    private boolean esTaller;
    private boolean esNoticia;
    private ProgressBar pbGuardar;
    private TextInputEditText etLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actividades_admin, container, false);

        Bundle args = getArguments();
        esTaller = args.getBoolean(ActividadesAdminFragment.ESTALLER);
        esNoticia = args.getBoolean(ActividadesAdminFragment.NOTICIAS);

        calendar = Calendar.getInstance();
        mSelectedImage = null;

        etNombre = (TextInputEditText) v.findViewById(R.id.et_titulo);
        etDescripcion = (TextInputEditText) v.findViewById(R.id.et_descripcion);
        etLink = (TextInputEditText) v.findViewById(R.id.et_link);
        itvCamera = (IconTextView) v.findViewById(R.id.itv_camara_actividades);
        ivCargada = (ImageView) v.findViewById(R.id.iv_cargada_actividad);
        etCuando = (TextInputEditText) v.findViewById(R.id.et_cuando);
        spRepite = (Spinner) v.findViewById(R.id.sp_repite);
        cbNotificar = (CheckBox) v.findViewById(R.id.cb_notificar);
        btnGuardar = (Button) v.findViewById(R.id.btn_grabar_actividad);
        pbGuardar = (ProgressBar) v.findViewById(R.id.pb_crear_actividad);

        if (esTaller && !esNoticia)
            getActivity().setTitle(R.string.nuevo_taller);
        else if (!esTaller && !esNoticia)
            getActivity().setTitle(R.string.nueva_actividad);
        else if (!esTaller && esNoticia) {
            getActivity().setTitle(R.string.nueva_noticia);
            etCuando.setVisibility(View.GONE);
            spRepite.setVisibility(View.GONE);
            etLink.setVisibility(View.VISIBLE);
        }


        etCuando.setKeyListener(null);
        etCuando.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {

                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            etCuando.setText(String.format(Locale.getDefault(), "%d/%d/%d %d:", dayOfMonth, monthOfYear + 1, year, hourOfDay) + (minute == 0 ? "00" : minute));
                                        }
                                    }, 18, 0, true);
                            timePickerDialog.show();

                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        itvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/jpg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Elegir una imagen"), SELECT_PICTURE);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Grabar();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            mSelectedImage = selectedImageUri;
            ivCargada.setImageURI(selectedImageUri);
            Toast.makeText(getContext(), selectedImageUri.getPath(), Toast.LENGTH_LONG).show();
        }
    }

    private void HabilitarVistas(boolean habilitar) {
        pbGuardar.setVisibility(habilitar ? View.GONE : View.VISIBLE);
        etNombre.setEnabled(habilitar);
        etDescripcion.setEnabled(habilitar);
        itvCamera.setEnabled(habilitar);
        etCuando.setEnabled(habilitar);
        spRepite.setEnabled(habilitar);
        cbNotificar.setEnabled(habilitar);
        btnGuardar.setEnabled(habilitar);
        etLink.setEnabled(habilitar);

        if (!habilitar) {
            etNombre.setError(null);
            etLink.setError(null);
            etDescripcion.setError(null);
            etCuando.setError(null);
        }
    }

    private void Grabar() {

        if (mSelectedImage == null) {
            Snackbar.make(itvCamera, "Hay que seleccionar una imagen", Snackbar.LENGTH_LONG).show();
            return;
        }

        boolean cancel = false;

        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String cuando = etCuando.getText().toString();
        String link = etLink.getText().toString().trim();
        int repite = spRepite.getSelectedItemPosition();

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(descripcion)) {
            etDescripcion.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(link) && esNoticia) {
            etLink.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(cuando) && !esNoticia) {
            etCuando.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (!cancel) {
            try {
                HabilitarVistas(false);
                AsyncHttpClient client = new AsyncHttpClient();
                client.setConnectTimeout(25000 * 10);
                client.setTimeout(25000 * 10);

                RequestParams params = new RequestParams();
                params.put("descripcion", descripcion);
                params.put("imagen_img", getActivity().getContentResolver().openInputStream(mSelectedImage));
                if (!esNoticia) {
                    params.put("nombre", nombre);
                    params.put("cuando", cuando);
                    params.put("repeticion", repite);
                    params.put("esTallerCel", esTaller ? "true" : "false");
                    params.put("notifica", cbNotificar.isChecked() ? "true" : "false");
                } else {
                    params.put("titulo", nombre);
                    params.put("link", link);
                }


                client.post(esNoticia ? Common.AGREGARNOTICIA : Common.AGREGARACTIVIDAD, params, new JsonHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(getContext(), responseString, Toast.LENGTH_LONG).show();
                        HabilitarVistas(true);
                        System.out.println(responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            final boolean error = response.getBoolean("error");
                            String title = error ? "Error" : "Nuevo Encuentro";
                            String msg = response.getString("mensaje");
                            Drawable icono = new IconDrawable(getContext(), error ? FontAwesomeIcons.fa_warning : FontAwesomeIcons.fa_hand_peace_o);

                            new AlertDialog.Builder(getContext())
                                    .setTitle(title)
                                    .setMessage(msg)
                                    .setIcon(icono)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (!error)
                                                getActivity().finish();
                                            else
                                                HabilitarVistas(true);
                                        }
                                    })
                                    .show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception ex) {
                Snackbar.make(itvCamera, "Error: " + ex.getMessage(), Snackbar.LENGTH_LONG).show();
                HabilitarVistas(true);
            }
        } else {
            HabilitarVistas(true);
        }

    }

}

package coop.nuevoencuentro.nofuemagia.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.JsonHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//import com.squareup.picasso.LruCache;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.activities.AdminActivity;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import cz.msebera.android.httpclient.Header;
//import cz.msebera.android.httpclient.Header;

/**
 * Created by jlionti on 29/07/2016. No Fue Magia
 */
public class ActividadesAdminFragment extends Fragment {

    private static final int SELECT_PICTURE = 0x1212;

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
    private ProgressBar pbGuardar;
    private TextInputEditText etLink;
    private boolean esTaller;
    private boolean esNoticia;
    private boolean esBolson;

    private boolean editando;
    private int id;
    private String imagenURL;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actividades_admin, container, false);

        Bundle args = getArguments();
        esTaller = args.getBoolean(AdminActivity.ESTALLER);
        esNoticia = args.getBoolean(AdminActivity.NOTICIAS);
        esBolson = args.getBoolean(AdminActivity.BOLSON);

        editando = args.getString(AdminActivity.TITULO, null) != null;

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

        if (esTaller && !esNoticia && !esBolson)
            getActivity().setTitle(R.string.nuevo_taller);
        else if (!esTaller && !esNoticia && !esBolson)
            getActivity().setTitle(R.string.nueva_actividad);
        else if (!esTaller && esNoticia && !esBolson) {
            getActivity().setTitle(R.string.nueva_noticia);
            etCuando.setVisibility(View.GONE);
            spRepite.setVisibility(View.GONE);
            etLink.setVisibility(View.VISIBLE);
        } else if (!esTaller && !esNoticia && esBolson) {
            getActivity().setTitle(R.string.nueva_bolson);
            etNombre.setVisibility(View.GONE);
            etDescripcion.setVisibility(View.GONE);
            etCuando.setVisibility(View.GONE);
            itvCamera.setVisibility(View.GONE);
            ivCargada.setVisibility(View.GONE);
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

        if (editando) {
            String nombre = args.getString(AdminActivity.TITULO, null);
            String desc = args.getString(AdminActivity.DESCRIPCION, null);
            imagenURL = args.getString(AdminActivity.IMAGEN_URL, null);
            String cuando = args.getString(AdminActivity.CUANDO, null);
            int repite = args.getInt(AdminActivity.REPITE, 0);
            id = args.getInt(AdminActivity.ID, 0);

            etNombre.setText(nombre);
            etDescripcion.setText(desc);
            etCuando.setText(cuando.replace("-", "/"));
            Picasso.with(getContext()).load(imagenURL).into(ivCargada);
            spRepite.setSelection(repite, true);
        }

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
            if (!esBolson && !editando) {
                Snackbar.make(itvCamera, "Hay que seleccionar una imagen", Snackbar.LENGTH_LONG).show();
                return;
            }
        }

        boolean cancel = false;

        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String cuando = etCuando.getText().toString();
        String link = etLink.getText().toString().trim();
        int repite = spRepite.getSelectedItemPosition();

        if (TextUtils.isEmpty(nombre) && !esBolson) {
            etNombre.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(descripcion) && !esBolson) {
            etDescripcion.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(link) && esNoticia && esBolson) {
            etLink.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(cuando) && !esNoticia && !esBolson) {
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
                params.put("notifica", cbNotificar.isChecked() ? "true" : "false");

                if (esNoticia && !esTaller && !esBolson) {
                    params.put("titulo", nombre);
                    params.put("descripcion", descripcion);
                    params.put("link", link);
                    if (!editando)
                        params.put("imagen_img", getActivity().getContentResolver().openInputStream(mSelectedImage));
                    else if (editando && mSelectedImage != null)
                        params.put("imagen_img", getActivity().getContentResolver().openInputStream(mSelectedImage));
                } else if (!esNoticia && !esBolson) {
                    params.put("nombre", nombre);
                    params.put("descripcion", descripcion);
                    if (!editando)
                        params.put("imagen_img", getActivity().getContentResolver().openInputStream(mSelectedImage));
                    else if (editando && mSelectedImage != null)
                        params.put("imagen_img", getActivity().getContentResolver().openInputStream(mSelectedImage));
                    params.put("cuando", cuando);
                    params.put("repeticion", repite);
                    params.put("esTallerCel", esTaller ? "true" : "false");
                } else if (!esNoticia && !esTaller && esBolson) {
                    params.put("link", link);
                }

                if (editando && !esNoticia)
                    params.put("idActividad", id);


                String url = null;
                if (!esBolson && !esNoticia && !editando)
                    url = Common.AGREGAR_ACTIVIDAD;
                else if (!esBolson && !esNoticia && editando)
                    url = Common.EDITAR_ACTIVIDAD;
                else if (!esTaller && !esBolson && esNoticia && !editando)
                    url = Common.AGREGAR_NOTICIA;
                else if (!esTaller && !esBolson && esNoticia && editando)
                    url = Common.EDITAR_NOTICIA;
                else if (!esTaller && esBolson && !esNoticia)
                    url = Common.AGREGARBOLSON;


                client.post(url, params, new JsonHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(getContext(), responseString, Toast.LENGTH_LONG).show();
                        HabilitarVistas(true);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            final boolean error = response.getBoolean("error");
                            String title = error ? "Error" : "Nuevo Encuentro";
                            String msg = response.getString("mensaje");
                            Drawable icono = new IconDrawable(getContext(), error ? FontAwesomeIcons.fa_warning : FontAwesomeIcons.fa_hand_peace_o);

                            if (editando)
                                Picasso.with(getContext()).invalidate(imagenURL);

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

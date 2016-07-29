package coop.nuevoencuentro.nofuemagia.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actividades_admin, container, false);

        Bundle args = getArguments();
        esTaller = args.getBoolean(ActividadesAdminFragment.ESTALLER);

        calendar = Calendar.getInstance();
        mSelectedImage = null;

        etNombre = (TextInputEditText) v.findViewById(R.id.et_titulo);
        etDescripcion = (TextInputEditText) v.findViewById(R.id.et_descripcion);
        itvCamera = (IconTextView) v.findViewById(R.id.itv_camara_actividades);
        ivCargada = (ImageView) v.findViewById(R.id.iv_cargada_actividad);
        etCuando = (TextInputEditText) v.findViewById(R.id.et_cuando);
        spRepite = (Spinner) v.findViewById(R.id.sp_repite);
        cbNotificar = (CheckBox) v.findViewById(R.id.cb_notificar);
        btnGuardar = (Button) v.findViewById(R.id.btn_grabar_actividad);


        etCuando.setKeyListener(null);
        etCuando.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            etCuando.setText(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
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

    private void Grabar() {

        if (mSelectedImage == null) {
            Snackbar.make(itvCamera, "Hay que seleccionar una imagen", Snackbar.LENGTH_LONG).show();
            return;
        }

        boolean cancel = false;

        String nombre = etNombre.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String cuando = etCuando.getText().toString();
        int repite = spRepite.getSelectedItemPosition();

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(descripcion)) {
            etDescripcion.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (TextUtils.isEmpty(cuando)) {
            etCuando.setError(getString(R.string.obligatorio));
            cancel = true;
        }

        if (!cancel) {
            try {
                AsyncHttpClient client = new AsyncHttpClient();
                client.setConnectTimeout(25000 * 10);
                client.setTimeout(25000 * 10);

                RequestParams params = new RequestParams();
                params.put("nombre", nombre);
                params.put("descripcion", descripcion);
                params.put("imagen_img", getActivity().getContentResolver().openInputStream(mSelectedImage));
                params.put("cuando", cuando);
                params.put("repeticion", repite);
                params.put("esTallerCel", esTaller ? "true" : "false");
                params.put("notifica", cbNotificar.isChecked() ? "true" : "false");

                client.post(Common.AGREGARACTIVIDAD, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println(statusCode + " - " + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        System.out.println(statusCode + " - " + responseString);
                    }
                });
            } catch (Exception ex) {
                Snackbar.make(itvCamera, "Error: " + ex.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }

    }

}

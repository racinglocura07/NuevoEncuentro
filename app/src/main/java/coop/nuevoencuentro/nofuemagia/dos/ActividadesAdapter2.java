package coop.nuevoencuentro.nofuemagia.dos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.activities.AdminActivity;
import coop.nuevoencuentro.nofuemagia.activities.FullscreenActivity;
import coop.nuevoencuentro.nofuemagia.activities.PantallaPrincipal;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Actividades;
import cz.msebera.android.httpclient.Header;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by Tanoo on 1/9/2016.
 * Nuevo Encuentro
 * No Fue Magiaø
 */
public class ActividadesAdapter2 extends RecyclerView.Adapter<ActividadesAdapter2.ViewHolder> {

    private final boolean mEsTaller;
    private final List<Actividades> mDataset;
    private Context mContext;

    public ActividadesAdapter2(Context _c, boolean esTaller) {
        mDataset = Actividades.GetAll(esTaller);
        mContext = _c;
        mEsTaller = esTaller;
    }

    public boolean haveUpdate() {
        return mDataset.size() == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Actividades item = mDataset.get(position);

        holder.item = item;
        holder.tvTitulo.setText(item.nombre);
        holder.tvDescripcion.setText(item.descripcion);
        holder.ivImagen.setTag(item);

        String imagenUrl = Common.imagenURL + (mEsTaller ? "taller-" : "actividad-") + item.idActividad + ".jpg";

        Picasso.with(mContext).load(imagenUrl).noFade().into(holder.ivImagen, new Callback() {
            @Override
            public void onSuccess() {
                holder.tvCargando.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.tvCargando.setText(R.string.error_cargando);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void mandarMail(Actividades item) {
        String asunto = "Consulta sobre " + item.nombre;
        String cuerpo = "";

        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + asunto + "&body=" + cuerpo + "&to=" + "\"comuna10@encuentrocapital.com.ar");
        testIntent.setData(data);
        mContext.startActivity(testIntent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageViewTouch ivImagen;
        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        private final IconTextView tvCargando;
        private Actividades item;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_actividades2, parent, false));

            SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
            boolean esAdmin = preferences.getBoolean(Common.ES_ADMIN, false);

            ivImagen = (ImageViewTouch) itemView.findViewById(R.id.card_image);
            ivImagen.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String imagenUrl = Common.imagenURL + "actividad-" + item.idActividad + ".jpg";

                    Bundle args = new Bundle();
                    args.putString(FullscreenActivity.IMAGEN_FULL, imagenUrl);

                    Intent intent = new Intent(mContext, FullscreenActivity.class);
                    intent.putExtras(args);
                    return false;
                }
            });
            ivImagen.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
            tvTitulo = (TextView) itemView.findViewById(R.id.card_title);
            tvDescripcion = (TextView) itemView.findViewById(R.id.card_text);
            tvCargando = (IconTextView) itemView.findViewById(R.id.tv_cargando_actividad);

            ImageButton itvBorrar = (ImageButton) itemView.findViewById(R.id.borrar_button);
            itvBorrar.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_trash).colorRes(R.color.partido_medio));

            ImageButton itvEditar = (ImageButton) itemView.findViewById(R.id.editar_button);
            itvEditar.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_edit).colorRes(R.color.partido_medio));
            if (esAdmin) {
                itvEditar.setVisibility(View.VISIBLE);
                itvEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle args = new Bundle();
                        args.putBoolean(AdminActivity.ESTALLER, mEsTaller);
                        args.putBoolean(AdminActivity.NOTICIAS, false);
                        args.putBoolean(AdminActivity.BOLSON, false);

                        args.putString(AdminActivity.TITULO, item.nombre);
                        args.putString(AdminActivity.DESCRIPCION, item.descripcion);
                        args.putString(AdminActivity.IMAGEN_URL, Common.imagenURL + (mEsTaller ? "taller-" : "actividad-") + item.idActividad + ".jpg");
                        args.putString(AdminActivity.CUANDO, item.cuando);
                        args.putInt(AdminActivity.REPITE, item.repeticion);
                        args.putInt(AdminActivity.ID, item.idActividad);

                        Intent adIntent = new Intent(mContext, AdminActivity.class);
                        adIntent.putExtras(args);
                        mContext.startActivity(adIntent);
                    }
                });

                itvBorrar.setVisibility(View.VISIBLE);
                itvBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(mContext)
                                .setCancelable(true)
                                .setIcon(new IconDrawable(mContext, FontAwesomeIcons.fa_trash).colorRes(R.color.partido))
                                .setTitle(R.string.eliminar_titulo)
                                .setMessage(mContext.getResources().getString(R.string.eliminar, item.nombre))
                                .setNegativeButton("No!", null)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        RequestParams params = new RequestParams();
                                        params.put("notifica", "false");
                                        params.put("idActividad", item.idActividad);

                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.setTimeout(20 * 1000);
                                        client.setConnectTimeout(20 * 1000);
                                        client.post(Common.BORRAR_ACTIVIDAD, params, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);

                                                try {
                                                    final boolean error = response.getBoolean("error");
                                                    String title = error ? "Error" : "Nuevo Encuentro";
                                                    String msg = response.getString("mensaje");
                                                    Drawable icono = new IconDrawable(mContext, error ? FontAwesomeIcons.fa_warning : FontAwesomeIcons.fa_hand_peace_o);

                                                    new AlertDialog.Builder(mContext)
                                                            .setTitle(title)
                                                            .setMessage(msg)
                                                            .setIcon(icono)
                                                            .setPositiveButton("Ok", null)
                                                            .show();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }
                                }).show();
                    }
                });
            }

            final ImageButton publicar = (ImageButton) itemView.findViewById(R.id.favorite_button);
            publicar.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_facebook).colorRes(R.color.partido_medio));
            publicar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
                    if (!preferences.getBoolean(Common.ES_FB, false)) {
                        Snackbar.make(v, R.string.iniciar_sesion_fb, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    Snackbar.make(v, R.string.cargando, Snackbar.LENGTH_LONG).show();
                    publicar.setEnabled(false);
                    Actividades usada = (Actividades) ivImagen.getTag();
                    String imagenUrl = Common.imagenURL + (mEsTaller ? "taller-" : "actividad-") + usada.idActividad + ".jpg";

                    Picasso.with(mContext)
                            .load(imagenUrl)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    SharePhoto photo = new SharePhoto.Builder()
                                            .setBitmap(bitmap)
                                            .build();


                                    if (ShareDialog.canShow(SharePhotoContent.class)) {

                                        ShareHashtag hash = new ShareHashtag.Builder()
                                                .setHashtag("#NuevoEncuentro")
                                                .build();

                                        SharePhotoContent content = new SharePhotoContent.Builder()
                                                .addPhoto(photo)
                                                .setShareHashtag(hash)
                                                .build();


                                        ShareDialog shareDialog = new ShareDialog((PantallaPrincipal) mContext);
                                        shareDialog.show(content);
                                        publicar.setEnabled(true);

                                    } else
                                        Common.ShowOkMessage(v, R.string.tener_instalado);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    Common.ShowOkMessage(v, R.string.error_flyer);
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    Common.ShowMessage(v, R.string.obteniendo_flyer);
                                }
                            });
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_search).colorRes(R.color.partido_medio));
            shareImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Actividades usada = (Actividades) ivImagen.getTag();
                    String imagenUrl = Common.imagenURL + (mEsTaller ? "taller-" : "actividad-") + usada.idActividad + ".jpg";

                    Bundle args = new Bundle();
                    args.putString(DetalleActivity2.IMAGEN_FULL, imagenUrl);
                    args.putString(DetalleActivity2.TITULO, usada.nombre);
                    args.putString(DetalleActivity2.DESC, usada.descripcion);

                    Intent full = new Intent(mContext, DetalleActivity2.class);
                    full.putExtras(args);
                    mContext.startActivity(full);
                }
            });

            ImageButton consultaButton = (ImageButton) itemView.findViewById(R.id.consulta_button);
            consultaButton.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_envelope).colorRes(R.color.partido_medio));
            consultaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mandarMail(item);
                }
            });
        }
    }

}

package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.activities.AdminActivity;
import coop.nuevoencuentro.nofuemagia.activities.FullscreenActivity;
import coop.nuevoencuentro.nofuemagia.activities.PantallaPrincipal;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Actividades;
//import cz.msebera.android.httpclient.Header;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ActividadesViewHolder> {

    private final boolean mEsTaller;
    private final List<Actividades> mDataset;
    private Context mContext;

//    public ActividadesAdapter(Context _c, List<Actividades> actividades) {
//        mDataset = actividades;
//        mContext = _c;
//        mPP = (PantallaPrincipal) _c;
//    }

    public ActividadesAdapter(Context _c, boolean esTaller) {
        mDataset = Actividades.GetAll(esTaller);
        mContext = _c;
        mEsTaller = esTaller;
    }

    public boolean haveUpdate() {
        return mDataset.size() == 0;
    }

    @Override
    public ActividadesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividades, parent, false);
        return new ActividadesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ActividadesViewHolder holder, int position) {
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

    public class ActividadesViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        private final ImageView ivImagen;
        private final IconTextView tvCargando;

        private Actividades item;

        public ActividadesViewHolder(View itemView) {
            super(itemView);

            SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
            boolean esAdmin = preferences.getBoolean(Common.ES_ADMIN, false);

            View hover = View.inflate(mContext, R.layout.hover_item_actividades, null);
            //LayoutInflater.from(mContext).inflate(R.layout.hover_item_actividades, null);// (ViewGroup) itemView.getRootView());
            tvTitulo = (TextView) itemView.findViewById(R.id.tv_titulo_item_actividades);
            tvTitulo.setBackgroundResource(mEsTaller ? R.color.talleres : R.color.actividades);
            tvDescripcion = (TextView) itemView.findViewById(R.id.tv_descripcion_item_actividades);
            ivImagen = (ImageView) itemView.findViewById(R.id.iv_item_actividad);
            tvCargando = (IconTextView) itemView.findViewById(R.id.tv_cargando_actividad);

            IconTextView itvEditar = (IconTextView) hover.findViewById(R.id.editar_actividad);
            IconTextView itvBorrar = (IconTextView) hover.findViewById(R.id.borrar_actividad);
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
//                blur.addChildAppearAnimator(hover, R.id.editar_actividad, Techniques.BounceInUp, 1200);
//                blur.addChildDisappearAnimator(hover, R.id.editar_actividad, Techniques.SlideOutRight, 1200);

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

                                        /*RequestParams params = new RequestParams();
                                        params.put("notifica", "false");
                                        params.put("idActividad", item.idActividad);

                                        AsyncHttpClient client = ((PantallaPrincipal) mContext).GetAsynk();
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
                                        });*/

                                    }
                                }).show();
                    }
                });
//                blur.addChildAppearAnimator(hover, R.id.borrar_actividad, Techniques.BounceInUp, 1200);
//                blur.addChildDisappearAnimator(hover, R.id.borrar_actividad, Techniques.SlideOutRight, 1200);
            }

//            blur.setHoverView(hover);
//            blur.enableZoomBackground(true);
//            blur.setBlurDuration(1000);
//
//            blur.addChildAppearAnimator(hover, R.id.ver_actividad, Techniques.BounceInDown, 1200);
//            blur.addChildDisappearAnimator(hover, R.id.ver_actividad, Techniques.SlideOutLeft, 1200);
//
//            blur.addChildAppearAnimator(hover, R.id.fb_actividad, Techniques.BounceInUp, 1200);
//            blur.addChildDisappearAnimator(hover, R.id.fb_actividad, Techniques.SlideOutRight, 1200);

            IconTextView verActividad = (IconTextView) hover.findViewById(R.id.ver_actividad);
            verActividad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Actividades usada = (Actividades) ivImagen.getTag();
                    String imagenUrl = Common.imagenURL + (mEsTaller ? "taller-" : "actividad-") + usada.idActividad + ".jpg";

                    Bundle args = new Bundle();
                    args.putString(FullscreenActivity.IMAGEN_FULL, imagenUrl);

                    Intent full = new Intent(mContext, FullscreenActivity.class);
                    full.putExtras(args);
                    mContext.startActivity(full);

                }
            });

            final IconTextView publicar = (IconTextView) hover.findViewById(R.id.fb_actividad);
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
        }
    }
}

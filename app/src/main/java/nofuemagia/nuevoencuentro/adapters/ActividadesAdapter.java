package nofuemagia.nuevoencuentro.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

import nofuemagia.nuevoencuentro.activities.FullscreenActivity;
import nofuemagia.nuevoencuentro.activities.PantallaPrincipal;
import nofuemagia.nuevoencuentro.helper.Common;
import nofuemagia.nuevoencuentro.model.Actividad;
import nofuemagia.nuevoencuentro.R;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ActividadesViewHolder> {


    private final ArrayList<Actividad> mDataset;
    private final PantallaPrincipal mPP;
    private Context mContext;

    public ActividadesAdapter(Context _c, ArrayList<Actividad> actividades) {
        mDataset = actividades;
        mContext = _c;
        mPP = (PantallaPrincipal) _c;
    }

    @Override
    public ActividadesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividades, parent, false);
        return new ActividadesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ActividadesViewHolder holder, int position) {
        Actividad item = mDataset.get(position);

        holder.tvTitulo.setText(item.Titulo);
        holder.tvDescripcion.setText(item.Descripcion);
        holder.ivImagen.setTag(item);

        mPP.getInstancePicasso().load(item.Imagen).into(holder.ivImagen);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ActividadesViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        private final ImageView ivImagen;
        private final BlurLayout blur;

        public ActividadesViewHolder(View itemView) {
            super(itemView);

            View hover = LayoutInflater.from(mContext).inflate(R.layout.hover_item_actividades, null);
            tvTitulo = (TextView) itemView.findViewById(R.id.tv_titulo_item_actividades);
            tvDescripcion = (TextView) itemView.findViewById(R.id.tv_descripcion_item_actividades);
            ivImagen = (ImageView) itemView.findViewById(R.id.iv_item_actividad);
            blur = (BlurLayout) itemView.findViewById(R.id.blur_actividad);

            blur.setHoverView(hover);
            blur.enableZoomBackground(true);
            blur.setBlurDuration(1000);

            blur.addChildAppearAnimator(hover, R.id.ver_actividad, Techniques.BounceInDown, 1200);
            blur.addChildDisappearAnimator(hover, R.id.ver_actividad, Techniques.SlideOutLeft, 1200);

            blur.addChildAppearAnimator(hover, R.id.fb_actividad, Techniques.BounceInUp, 1200);
            blur.addChildDisappearAnimator(hover, R.id.fb_actividad, Techniques.SlideOutRight, 1200);

            IconTextView verActividad = (IconTextView) hover.findViewById(R.id.ver_actividad);
            verActividad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Actividad usada = (Actividad) ivImagen.getTag();

                    Bundle args = new Bundle();
                    args.putInt(FullscreenActivity.IMAGEN_FULL, usada.Imagen);

                    Intent full = new Intent(mContext, FullscreenActivity.class);
                    full.putExtras(args);
                    mContext.startActivity(full);

                }
            });

            IconTextView publicar = (IconTextView) hover.findViewById(R.id.fb_actividad);
            publicar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Actividad usada = (Actividad) ivImagen.getTag();

                    Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), usada.Imagen);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
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
                        //shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                        //ShareDialog.show((PantallaPrincipal) mContext, content);
                    } else
                        Common.ShowOkMessage(v, R.string.tener_instalado);


                }
            });
        }
    }
}

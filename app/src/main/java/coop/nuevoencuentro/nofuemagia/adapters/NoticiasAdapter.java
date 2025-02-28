package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Noticias;

/**
 * Created by Tano on 18/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.ViewHolder> {

    private final List<Noticias> mDataset;
    private Context mContext;

    public NoticiasAdapter(Context _c) {
        mDataset = Noticias.GetAll();
        mContext = _c;
    }

    public boolean haveUpdate() {
        return mDataset.size() == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticias, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Noticias item = mDataset.get(position);

        holder.item = item;
        holder.tvTitulo.setText(item.titulo);
        holder.tvDescripcion.setText(item.descripcion);
        holder.ivImagen.setTag(item);

        String imagenUrl = Common.imagenURL + "noticia-" + item.idNoticia + ".jpg";
        Picasso.with(mContext).load(imagenUrl).noFade().into(holder.ivImagen, new Callback() {
            @Override
            public void onSuccess() {
                holder.tvCargando.setVisibility(View.GONE);
                holder.tvIrImagen.setVisibility(View.VISIBLE);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        private final ImageView ivImagen;
        private final IconTextView tvCargando;
        private final IconTextView tvIrImagen;

        private Noticias item;

        public ViewHolder(View itemView) {
            super(itemView);

            tvIrImagen = (IconTextView) itemView.findViewById(R.id.tv_ir_noticia);
            tvIrImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link.trim()));
                    mContext.startActivity(browserIntent);
                }
            });

            tvTitulo = (TextView) itemView.findViewById(R.id.tv_titulo_noticia);
            tvDescripcion = (TextView) itemView.findViewById(R.id.tv_desc_noticia);
            ivImagen = (ImageView) itemView.findViewById(R.id.iv_noticia);
            tvCargando = (IconTextView) itemView.findViewById(R.id.tv_cargando_noticias);

            /*View v = itemView;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link.trim()));
                    mContext.startActivity(browserIntent);
                }
            });*/


        }
    }
}

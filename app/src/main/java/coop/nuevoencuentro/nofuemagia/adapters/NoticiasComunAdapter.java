package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.model.Noticias;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasComunAdapter extends RecyclerView.Adapter<NoticiasComunAdapter.ViewHolder> {

    private final List<Noticias> mDataset;
    private Context mContext;

    public NoticiasComunAdapter(Context _c) {
        mDataset = Noticias.GetAll();
        mContext = _c;
    }

    public boolean haveUpdate() {
        return mDataset.size() == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticias_comun, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Noticias item = mDataset.get(position);

        holder.tvTitulo.setText(item.titulo);
        holder.tvDescripcion.setText(item.descripcion);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        private final TextView tvIrImagen;

        private Noticias item;

        public ViewHolder(View itemView) {
            super(itemView);

            tvIrImagen = (TextView) itemView.findViewById(R.id.tv_ir_noticia_comun);
            tvIrImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link.trim()));
                    mContext.startActivity(browserIntent);
                }
            });

            tvTitulo = (TextView) itemView.findViewById(R.id.tv_titulo_noticia);
            tvDescripcion = (TextView) itemView.findViewById(R.id.tv_desc_noticia);

        }
    }
}
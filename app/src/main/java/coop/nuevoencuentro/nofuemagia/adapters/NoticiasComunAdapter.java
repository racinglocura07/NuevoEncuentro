package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Noticias;
import coop.nuevoencuentro.nofuemagia.xml.XMLNuestrasVoces;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasComunAdapter extends RecyclerView.Adapter<NoticiasComunAdapter.ViewHolder> {

    private List<XMLNuestrasVoces> mDataset;
    private Context mContext;

    public NoticiasComunAdapter(Context _c) {
        mContext = _c;
    }

    public boolean haveUpdate() {
        return mDataset == null || mDataset.size() == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticias_comun, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        XMLNuestrasVoces item = mDataset.get(position);

        Spanned titulo;
        Spanned desc;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            titulo = Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY);
            desc = Html.fromHtml(item.description, Html.FROM_HTML_MODE_LEGACY);
        } else {
            titulo = Html.fromHtml(item.title);
            desc = Html.fromHtml(item.description);
        }

        holder.item = item;
        holder.tvTitulo.setText(titulo);
        holder.tvDescripcion.setText(desc);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setItems(List<XMLNuestrasVoces> items) {
        this.mDataset = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        //private final TextView tvIrImagen;

        private XMLNuestrasVoces item;

        public ViewHolder(View itemView) {
            super(itemView);

            Button btnIrNoticia = (Button) itemView.findViewById(R.id.btn_leer);
            btnIrNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (item.link.contains("pagina12"))
                        item.link = item.link.replace("www", "m");


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link.trim()));
                    mContext.startActivity(browserIntent);
                }
            });

            Button btnCompartir = (Button) itemView.findViewById(R.id.btn_compartir);
            btnCompartir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (item.link.contains("pagina12"))
                        item.link = item.link.replace("www", "m");

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");

                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, item.title);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, item.link);

                    mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getString(R.string.compartir_titulo)));
                }
            });

            tvTitulo = (TextView) itemView.findViewById(R.id.tv_titulo_noticia_comun);
            tvDescripcion = (TextView) itemView.findViewById(R.id.tv_desc_noticia_comun);

        }
    }
}
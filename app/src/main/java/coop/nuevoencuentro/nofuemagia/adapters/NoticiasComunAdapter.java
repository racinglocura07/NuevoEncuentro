package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.xml.RSSItems;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasComunAdapter extends RecyclerView.Adapter<NoticiasComunAdapter.ViewHolder> {

    private List<RSSItems> mDataset;
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
        RSSItems item = mDataset.get(position);

        String creador = item.getCreador();
        if (creador == null)
            creador = "";

        Spanned titulo;
        Spanned desc;
        Spanned crea;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            titulo = Html.fromHtml(item.getTitulo(), Html.FROM_HTML_MODE_LEGACY);
            desc = Html.fromHtml(item.getDescripcion(), Html.FROM_HTML_MODE_LEGACY);
            crea = Html.fromHtml(creador, Html.FROM_HTML_MODE_LEGACY);
        } else {
            titulo = Html.fromHtml(item.getTitulo());
            desc = Html.fromHtml(item.getDescripcion());
            crea = Html.fromHtml(creador);
        }


        holder.item = item;
        holder.tvTitulo.setText(titulo);
        holder.tvDescripcion.setText(desc);
        holder.tvFecha.setText(crea + item.getFechaPublicado());
        if (TextUtils.isEmpty(desc))
            holder.tvDescripcion.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public void setItems(List<RSSItems> items) {
        this.mDataset = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        private final TextView tvFecha;
        //private final TextView tvIrImagen;

        private RSSItems item;

        public ViewHolder(View itemView) {
            super(itemView);

            Button btnIrNoticia = (Button) itemView.findViewById(R.id.btn_leer);
            btnIrNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String link = item.getLink().trim();
                    if (link.contains("pagina12"))
                        link = link.replace("www", "m");


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    mContext.startActivity(browserIntent);
                }
            });

            Button btnCompartir = (Button) itemView.findViewById(R.id.btn_compartir);
            btnCompartir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String link = item.getLink().trim();
                    if (link.contains("pagina12"))
                        link = link.replace("www", "m");

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");

                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, item.getTitulo());
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);

                    mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getString(R.string.compartir_titulo)));
                }
            });

            tvTitulo = (TextView) itemView.findViewById(R.id.tv_titulo_noticia_comun);
            tvDescripcion = (TextView) itemView.findViewById(R.id.tv_desc_noticia_comun);
            tvFecha = (TextView) itemView.findViewById(R.id.tv_fecha_noticia_comun);

        }
    }
}
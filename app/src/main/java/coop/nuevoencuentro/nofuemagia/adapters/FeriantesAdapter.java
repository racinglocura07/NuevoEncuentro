package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.dos.DetalleActivity2;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Feriantes;

/**
 * Created by Tanoo on 4/9/2016.
 * Nuevo Encuentro
 * No Fue Magiaø
 */
public class FeriantesAdapter extends RecyclerView.Adapter<FeriantesAdapter.ViewHolder> {

    private List<Feriantes> mDataset;
    private Context mContext;

    public FeriantesAdapter(Context _c) {
        mDataset = Feriantes.GetAll();
        mContext = _c;
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
        Feriantes item = mDataset.get(position);

        holder.item = item;
        holder.tvTitulo.setText(item.nombre);
        holder.ivImagen.setTag(item);

        String imagen = Common.imagenURL + "feriante-" + item.idFeriante + ".jpg";
        Picasso.with(mContext).load(imagen).noFade().into(holder.ivImagen, new Callback() {
            @Override
            public void onSuccess() {
                holder.tvCargando.setVisibility(View.GONE);
                holder.tvTitulo.setVisibility(View.VISIBLE);
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

        private final ImageView ivImagen;
        private final TextView tvTitulo;
        private final TextView tvCargando;

        private Feriantes item;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_feriantes, parent, false));

            SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
            boolean esAdmin = preferences.getBoolean(Common.ES_ADMIN, false);

            ivImagen = (ImageView) itemView.findViewById(R.id.feriantes_imagen);
            tvTitulo = (TextView) itemView.findViewById(R.id.feriantes_titulo);
            tvTitulo.setVisibility(View.GONE);
            tvCargando = (TextView) itemView.findViewById(R.id.feriantes_cargando);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetalleActivity2.class);
                    intent.putExtra(DetalleActivity2.ITEM, item);
                    context.startActivity(intent);
                }
            });

        }
    }
}

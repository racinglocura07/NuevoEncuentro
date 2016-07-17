package coop.nuevoencuentro.nofuemagia.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.ActividadesAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Actividades;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ActividadesFragment extends Fragment {

    private ActividadesAdapter adapter;
    private RecyclerView recList;
    private ProgressBar pbLista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actividades, container, false);

        recList = (RecyclerView) v.findViewById(R.id.list_actividades);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        pbLista = (ProgressBar) v.findViewById(R.id.pb_actividades);
        pbLista.setVisibility(View.VISIBLE);

        adapter = new ActividadesAdapter(getContext(), false, pbLista);
        if (adapter.haveUpdate()) {
            Common.SincronizarActividades(new AsyncHttpClient(), this);
        } else
            recList.setAdapter(adapter);

        return v;
    }

    public void recargar() {
        adapter = new ActividadesAdapter(getContext(), true, pbLista);
        recList.setAdapter(adapter);
    }
}

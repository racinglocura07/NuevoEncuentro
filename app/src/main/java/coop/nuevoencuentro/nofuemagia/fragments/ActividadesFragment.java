package coop.nuevoencuentro.nofuemagia.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.ActividadesAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ActividadesFragment extends Fragment {

    private ActividadesAdapter adapter;
    private RecyclerView recList;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actividades, container, false);

        swipe = (SwipeRefreshLayout) v.findViewById(R.id.srl_actividades);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                Common.SincronizarActividades(new AsyncHttpClient(), ActividadesFragment.this);
            }
        });

        recList = (RecyclerView) v.findViewById(R.id.list_actividades);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        adapter = new ActividadesAdapter(getContext(), false);
        if (adapter.haveUpdate()) {
            swipe.setRefreshing(true);
            Common.SincronizarActividades(new AsyncHttpClient(), this);
        } else
            recList.setAdapter(adapter);

        return v;
    }


    public void recargar() {
        swipe.setRefreshing(false);
        adapter = new ActividadesAdapter(getContext(), false);
        if (recList != null)
            recList.setAdapter(adapter);
    }
}

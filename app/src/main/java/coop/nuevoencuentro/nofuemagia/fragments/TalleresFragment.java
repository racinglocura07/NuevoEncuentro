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
public class TalleresFragment extends Fragment {

    private ActividadesAdapter adapter;
    private RecyclerView recList;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_talleres, container, false);

        swipe = (SwipeRefreshLayout) v.findViewById(R.id.srl_talleres);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recList.setAdapter(null);
                swipe.setRefreshing(true);
                Common.SincronizarActividades(new AsyncHttpClient(), TalleresFragment.this);
            }
        });

        recList = (RecyclerView) v.findViewById(R.id.list_talleres);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        adapter = new ActividadesAdapter(getContext(), true);
        if (adapter.haveUpdate()) {
            swipe.post(new Runnable() {
                @Override
                public void run() {
                    swipe.setRefreshing(true);
                }
            });
            Common.SincronizarActividades(new AsyncHttpClient(), this);
        } else
            recList.setAdapter(adapter);

        return v;
    }

    public void recargar() {
        swipe.setRefreshing(false);
        adapter = new ActividadesAdapter(getContext(), true);
        recList.setAdapter(adapter);
    }
}

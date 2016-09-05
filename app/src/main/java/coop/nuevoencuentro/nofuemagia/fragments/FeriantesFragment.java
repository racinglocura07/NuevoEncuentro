package coop.nuevoencuentro.nofuemagia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.FeriantesAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by Tanoo on 4/9/2016.
 * Nuevo Encuentro
 * No Fue Magiaø
 */
public class FeriantesFragment extends Fragment {
    private FeriantesAdapter adapter;
    private RecyclerView recList;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feriantes, container, false);

        swipe = (SwipeRefreshLayout) v.findViewById(R.id.srl_feriantes);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recList.setAdapter(null);
                swipe.setRefreshing(true);
                Common.SincronizarFeriantes(FeriantesFragment.this);
            }
        });

        recList = (RecyclerView) v.findViewById(R.id.list_feriantes);
        recList.setHasFixedSize(true);

        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recList.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recList.setLayoutManager(new GridLayoutManager(getActivity(), 1));


        adapter = new FeriantesAdapter(getContext());
        if (adapter.haveUpdate()) {
            swipe.post(new Runnable() {
                @Override
                public void run() {
                    swipe.setRefreshing(true);
                }
            });
            Common.SincronizarFeriantes(this);
        } else
            recList.setAdapter(adapter);

        return v;
    }


    public void recargar() {
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        });
        adapter = new FeriantesAdapter(getContext());
        if (recList != null)
            recList.setAdapter(adapter);
    }
}

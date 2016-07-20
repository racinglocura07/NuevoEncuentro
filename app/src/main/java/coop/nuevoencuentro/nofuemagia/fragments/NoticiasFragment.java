package coop.nuevoencuentro.nofuemagia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Noticias;

/**
 * Created by Tano on 18/07/2016.
 */
public class NoticiasFragment extends Fragment {

    private NoticiasAdapter adapter;
    private RecyclerView recList;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noticias, container, false);


        swipe = (SwipeRefreshLayout) v.findViewById(R.id.srl_noticias);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Common.SincronizarNoticias(new AsyncHttpClient(), NoticiasFragment.this);
                swipe.setRefreshing(false);
            }
        });

        recList = (RecyclerView) v.findViewById(R.id.list_noticias);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        adapter = new NoticiasAdapter(getContext());
        if (adapter.haveUpdate()) {
            swipe.setRefreshing(true);
            Common.SincronizarNoticias(new AsyncHttpClient(), this);
        } else
            recList.setAdapter(adapter);

        return v;
    }

    public void recargar() {
        swipe.setRefreshing(false);
        adapter = new NoticiasAdapter(getContext());
        if (recList != null)
            recList.setAdapter(adapter);
    }
}

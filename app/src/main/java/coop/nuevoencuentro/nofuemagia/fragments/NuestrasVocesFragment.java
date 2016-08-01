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

import com.loopj.android.http.AsyncHttpClient;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasAdapter;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasComunAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NuestrasVocesFragment extends Fragment {

    public static final CharSequence TITLE = "Nuestras Voces";
    private SwipeRefreshLayout swipe;
    private RecyclerView recList;
    private NoticiasComunAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noticias, container, false);

        swipe = (SwipeRefreshLayout) v.findViewById(R.id.srl_noticias);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BuscarNoticias();
            }
        });

        recList = (RecyclerView) v.findViewById(R.id.list_noticias);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        adapter = new NoticiasComunAdapter(getContext());
        BuscarNoticias();

        return v;
    }

    private void BuscarNoticias() {

    }

}

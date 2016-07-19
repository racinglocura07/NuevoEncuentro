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
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by Tano on 18/07/2016.
 */
public class NoticiasFragment extends Fragment {

    private NoticiasAdapter adapter;
    private RecyclerView recList;
    private ProgressBar pbLista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noticias, container, false);

        recList = (RecyclerView) v.findViewById(R.id.list_noticias);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        pbLista = (ProgressBar) v.findViewById(R.id.pb_noticias);
        pbLista.setVisibility(View.VISIBLE);

        adapter = new NoticiasAdapter(getContext());
        if (adapter.haveUpdate()) {
            pbLista.setVisibility(View.VISIBLE);
            Common.SincronizarNoticias(new AsyncHttpClient(), this);
        } else
            recList.setAdapter(adapter);
        pbLista.setVisibility(View.GONE);

        return v;
    }

    public void recargar() {
        pbLista.setVisibility(View.GONE);
        adapter = new NoticiasAdapter(getContext());
        if (recList != null)
            recList.setAdapter(adapter);
    }
}

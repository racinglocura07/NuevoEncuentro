package coop.nuevoencuentro.nofuemagia.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.ActividadesAdapter;
import coop.nuevoencuentro.nofuemagia.model.Actividades;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class TalleresFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_talleres, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.list_talleres);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        ActividadesAdapter adapter = new ActividadesAdapter(getContext(), true);
        recList.setAdapter(adapter);

        return v;
    }
}

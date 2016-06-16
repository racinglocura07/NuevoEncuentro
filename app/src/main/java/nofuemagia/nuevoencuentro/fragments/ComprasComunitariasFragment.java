package nofuemagia.nuevoencuentro.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nofuemagia.nuevoencuentro.R;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ComprasComunitariasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_compras_comunitarias, container, false);
        return v;
    }
}

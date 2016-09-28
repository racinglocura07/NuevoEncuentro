package coop.nuevoencuentro.nofuemagia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasPageAdapter;

//import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by Tano on 18/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noticias, container, false);

        ViewPager vpPager = (ViewPager) v.findViewById(R.id.viewpager_noticias);
        vpPager.setOffscreenPageLimit(0);
        TabLayout tabs = (TabLayout) v.findViewById(R.id.tabs_noticias);

        if (savedInstanceState == null) {
            NoticiasPageAdapter noticiasAdapter = new NoticiasPageAdapter(getContext(), getChildFragmentManager());
            vpPager.setAdapter(noticiasAdapter);
            vpPager.setOffscreenPageLimit(3);
            tabs.setupWithViewPager(vpPager);
        }

        return v;
    }


}

package coop.nuevoencuentro.nofuemagia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasAdapter;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasPageAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by Tano on 18/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasFragment extends Fragment {


    private NoticiasPageAdapter noticiasAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noticias, container, false);


        ViewPager vpPager = (ViewPager) v.findViewById(R.id.viewpager_noticias);
        TabLayout tabs = (TabLayout) v.findViewById(R.id.tabs_noticias);

        noticiasAdapter = new NoticiasPageAdapter(getContext(), getActivity().getSupportFragmentManager());
        vpPager.setAdapter(noticiasAdapter);
        tabs.setupWithViewPager(vpPager);

        return v;
    }


}

package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import coop.nuevoencuentro.nofuemagia.fragments.NoticiasImagenFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasSinImagenFragment;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasPageAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 3;
    private final Context mContext;

    public NoticiasPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();

        switch (position) {
            case 0:
                return Fragment.instantiate(mContext, NoticiasImagenFragment.class.getName());
            case 1:
                args.putBoolean(NoticiasSinImagenFragment.ESPAGINA, false);
                return Fragment.instantiate(mContext, NoticiasSinImagenFragment.class.getName(), args);
            case 2:
                args.putBoolean(NoticiasSinImagenFragment.ESPAGINA, true);
                return Fragment.instantiate(mContext, NoticiasSinImagenFragment.class.getName(), args);
            default:
                return Fragment.instantiate(mContext, NoticiasImagenFragment.class.getName());
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Novedades";
            case 1:
                return "Nuestras Voces";
            case 2:
                return "Página 12";
            default:
                return null;
        }
    }
}

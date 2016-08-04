package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import coop.nuevoencuentro.nofuemagia.fragments.NoticiasImagenFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasSinImagenFragment;
import coop.nuevoencuentro.nofuemagia.fragments.PaginaFragment;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasPageAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 3;
    private final Context mContext;


    private final NoticiasImagenFragment nuestraComuna;
    private final NoticiasSinImagenFragment nuestrasVoces;
    private final NoticiasSinImagenFragment paginas12;

    public NoticiasPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        Bundle argspagina = new Bundle();
        argspagina.putBoolean(NoticiasSinImagenFragment.ESPAGINA, true);

        Bundle argsnuestras = new Bundle();
        argsnuestras.putBoolean(NoticiasSinImagenFragment.ESPAGINA, false);

        nuestraComuna = (NoticiasImagenFragment) Fragment.instantiate(mContext, NoticiasImagenFragment.class.getName());
        nuestrasVoces = (NoticiasSinImagenFragment) Fragment.instantiate(mContext, NoticiasSinImagenFragment.class.getName(), argsnuestras);
        paginas12 = (NoticiasSinImagenFragment) Fragment.instantiate(mContext, NoticiasSinImagenFragment.class.getName(), argspagina);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return nuestraComuna;
            case 1:
                return nuestrasVoces;
            case 2:
                return paginas12;
            default:
                return nuestraComuna;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return "Novedades";
            case 1:
                return "Nuestras Voces";
            case 2: // Fragment # 1 - This will show SecondFragment
                return "Página 12";
            default:
                return null;
        }
    }
}

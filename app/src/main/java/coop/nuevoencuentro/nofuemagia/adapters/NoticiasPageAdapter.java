package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import coop.nuevoencuentro.nofuemagia.fragments.NoticiasImagenFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasSinImagenFragment;
import coop.nuevoencuentro.nofuemagia.helper.Common;

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
                args.putString(NoticiasSinImagenFragment.QUE_NOTICIA, Common.COMUNIDAD_BSAS);
                return Fragment.instantiate(mContext, NoticiasSinImagenFragment.class.getName(), args);
            case 1:
                args.putString(NoticiasSinImagenFragment.QUE_NOTICIA, Common.NUESTAS_VOCES);
                return Fragment.instantiate(mContext, NoticiasSinImagenFragment.class.getName(), args);
            case 2:
                args.putString(NoticiasSinImagenFragment.QUE_NOTICIA, Common.PAGINA_12);
                return Fragment.instantiate(mContext, NoticiasSinImagenFragment.class.getName(), args);
            default:
                args.putString(NoticiasSinImagenFragment.QUE_NOTICIA, Common.COMUNIDAD_BSAS);
                return Fragment.instantiate(mContext, NoticiasImagenFragment.class.getName(), args);
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Comunidad BS AS";
            case 1:
                return "Nuestras Voces";
            case 2:
                return "Página 12";
            default:
                return null;
        }
    }
}

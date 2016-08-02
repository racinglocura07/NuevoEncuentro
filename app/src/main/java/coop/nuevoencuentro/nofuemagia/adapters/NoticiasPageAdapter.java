package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import coop.nuevoencuentro.nofuemagia.fragments.NuestrasNoticiasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NuestrasVocesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.PaginaFragment;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasPageAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 3;
    private final Context mContext;


    private final Fragment nuestraComuna;
    private final Fragment nuestrasVoces;
    private final Fragment paginas12;

    public NoticiasPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        nuestraComuna = Fragment.instantiate(mContext, NuestrasNoticiasFragment.class.getName());
        nuestrasVoces = Fragment.instantiate(mContext, NuestrasVocesFragment.class.getName());
        paginas12 = Fragment.instantiate(mContext, PaginaFragment.class.getName());
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
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return NuestrasNoticiasFragment.TITLE;
            case 1:
                return NuestrasVocesFragment.TITLE;
            case 2: // Fragment # 1 - This will show SecondFragment
                return PaginaFragment.TITLE;
            default:
                return null;
        }
    }
}

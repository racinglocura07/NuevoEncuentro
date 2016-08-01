package coop.nuevoencuentro.nofuemagia.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import coop.nuevoencuentro.nofuemagia.fragments.NuestrasNoticiasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NuestrasVocesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.PaginaFragment;

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
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return Fragment.instantiate(mContext, NuestrasNoticiasFragment.class.getName());
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return Fragment.instantiate(mContext, NuestrasVocesFragment.class.getName());
            case 2: // Fragment # 1 - This will show SecondFragment
                return Fragment.instantiate(mContext, PaginaFragment.class.getName());
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

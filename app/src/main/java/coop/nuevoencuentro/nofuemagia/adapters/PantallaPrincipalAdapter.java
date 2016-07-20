package coop.nuevoencuentro.nofuemagia.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import coop.nuevoencuentro.nofuemagia.fragments.ActividadesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ComprasComunitariasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ContactoFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.TalleresFragment;
import coop.nuevoencuentro.nofuemagia.fragments.UbicacionFragment;


/**
 * Created by jlionti on 20/07/2016. No Fue Magia
 */
public class PantallaPrincipalAdapter extends FragmentPagerAdapter {


    private NoticiasFragment noticiasFragment;
    private ActividadesFragment actividadesFragment;
    private TalleresFragment talleresFragment;
    private ComprasComunitariasFragment comprasFragment;
    private ContactoFragment contactoFragment;
    private UbicacionFragment ubicacionFragment;

    public PantallaPrincipalAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        noticiasFragment = new NoticiasFragment();
        actividadesFragment = new ActividadesFragment();
        talleresFragment = new TalleresFragment();
        comprasFragment = new ComprasComunitariasFragment();
        contactoFragment = new ContactoFragment();
        ubicacionFragment = new UbicacionFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return noticiasFragment;
            case 1:
                return actividadesFragment;
            case 3:
                return talleresFragment;
            case 4:
                return comprasFragment;
            case 5:
                return contactoFragment;
            case 6:
                return ubicacionFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 7;
    }
}
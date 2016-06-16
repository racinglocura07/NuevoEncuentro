package nofuemagia.nuevoencuentro.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nofuemagia.nuevoencuentro.R;
import nofuemagia.nuevoencuentro.adapters.ActividadesAdapter;
import nofuemagia.nuevoencuentro.model.Actividad;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ActividadesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actividades, container, false);


        RecyclerView recList = (RecyclerView) v.findViewById(R.id.list_actividades);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ArrayList<Actividad> actividades = new ArrayList<>();
        actividades.add(new Actividad("Apoyo Escolar\nMartes 17.30hs", "Clases de apoyo libres y gratuitas para todos y todas!", R.mipmap.apoyo));
        actividades.add(new Actividad("Reunion\nJueves 19hs", "¡Traé ropa cómoda y, si tenés, colchoneta!", R.mipmap.yoga));
        actividades.add(new Actividad("Plenario\nSabado por medio 15hs", "Clases de apoyo libres y gratuitas para todos y todas!", R.mipmap.apoyo));
        actividades.add(new Actividad("Compras Comunitarias\nSabado por medio", "Fiedos, salsa de tomate, tapas de empanadas, aceite, yerba, azucar y arroz!", R.mipmap.compras_comunitarias));
        actividades.add(new Actividad("Bolsones\nSabado por medio", "Bolson de verdura orgánica, fruta y tierra!", R.mipmap.bolsones));
        /*actividades.add(new Actividad("Yoga - Lunes 19hs", "¡Traé ropa cómoda y, si tenés, colchoneta!", R.mipmap.yoga));
        actividades.add(new Actividad("Apoyo Escolar - Martes 17.30hs", "Clases de apoyo libres y gratuitas para todos y todas!", R.mipmap.apoyo));
        actividades.add(new Actividad("Salsa - Martes 19hs", "Cupos limitados", R.mipmap.salsa));*/

        ActividadesAdapter adapter = new ActividadesAdapter(getContext(), actividades);
        recList.setAdapter(adapter);

        return v;
    }
}

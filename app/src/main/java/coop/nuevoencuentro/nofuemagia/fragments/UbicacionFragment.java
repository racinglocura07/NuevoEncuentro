package coop.nuevoencuentro.nofuemagia.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.model.Local;

/**
 * Created by jlionti on 13/06/2016. No Fue Magia
 */
public class UbicacionFragment extends SupportMapFragment implements OnMapReadyCallback {

    public UbicacionFragment() {

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_TERRAIN);
        options.mapToolbarEnabled(true);
        newInstance(options);
        getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Local local10 = new Local(-34.624252, -58.509026, "Comuna 10 - Calderón de la barca 1674", 10);
        LatLng lat = new LatLng(local10.getLatitude(), local10.getLongitude());

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(12));
        map.moveCamera(CameraUpdateFactory.newLatLng(lat));

        MarkerOptions options = new MarkerOptions().position(lat);
        options.title(local10.getNombre());

        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
        map.addMarker(options);

        ArrayList<LatLng> limites = new ArrayList<>();
        limites.add(new LatLng(-34.63661536, -58.47130895));
        limites.add(new LatLng(-34.62249032, -58.47768188));
        limites.add(new LatLng(-34.62462688, -58.48274589));
        limites.add(new LatLng(-34.61440265, -58.49555612));
        limites.add(new LatLng(-34.60910462, -58.50019097));
        limites.add(new LatLng(-34.61313115, -58.50710034));
        limites.add(new LatLng(-34.61673367, -58.5130012));
        limites.add(new LatLng(-34.62024774, -58.51694942));
        limites.add(new LatLng(-34.61148877, -58.52857947));
        limites.add(new LatLng(-34.61503839, -58.53025317));
        limites.add(new LatLng(-34.6157271, -58.53051066));
        limites.add(new LatLng(-34.63391413, -58.52930903));
        limites.add(new LatLng(-34.63472627, -58.5283649));
        limites.add(new LatLng(-34.63377289, -58.52149844));
        limites.add(new LatLng(-34.63315494, -58.5150826));
        limites.add(new LatLng(-34.63582089, -58.50761533));
        limites.add(new LatLng(-34.63587385, -58.50673556));
        limites.add(new LatLng(-34.63663301, -58.50576997));
        limites.add(new LatLng(-34.63808069, -58.51031899));
        limites.add(new LatLng(-34.63998736, -58.5097611));
        limites.add(new LatLng(-34.64579535, -58.50225091));
        limites.add(new LatLng(-34.64341218, -58.49707961));
        limites.add(new LatLng(-34.64482443, -58.49523425));
        limites.add(new LatLng(-34.6369508, -58.47851872));
        limites.add(new LatLng(-34.63880452, -58.47613692));
        limites.add(new LatLng(-34.63728624, -58.47351909));

        PolygonOptions poligono = new PolygonOptions();
        poligono.addAll(limites);
        poligono.fillColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryTrans));
        poligono.strokeColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        poligono.strokeWidth(2);
        map.addPolygon(poligono);
    }
}

package coop.nuevoencuentro.nofuemagia.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mGoogleMap;

    public UbicacionFragment() {

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_TERRAIN);
        options.mapToolbarEnabled(true);
        newInstance(options);
        getMapAsync(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (mGoogleMap != null)
                configurarMapa();
        }
    }

    private void configurarMapa() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        ArrayList<Local> locales = new ArrayList<>();
        locales.add(new Local(-34.629094, -58.389907, "Av. Brasil 1760 - Comuna 1", 1));
        locales.add(new Local(-34.615980, -58.383730, "Santiago del Estero 638 - Comuna 1", 1));
        locales.add(new Local(-34.609781, -58.380060, "Bernardo de Irigoyen 118 - El Hornero", 1));
        locales.add(new Local(-34.594424, -58.405480, "Charcas 2761 - Comuna 2", 2));
        locales.add(new Local(-34.614138, -58.398282, "Pichincha 394 - Comuna 3", 3));
        locales.add(new Local(-34.601329, -58.410150, "Zelaya 3175 - Comuna 3", 3));
        locales.add(new Local(-34.640800, -58.397807, "Luna 382 - Comuna 4", 4));
        locales.add(new Local(-34.635104, -58.356598, "Necochea 1235 - Comuna 4", 4));
        locales.add(new Local(-34.654883, -58.402524, "Iguazú 1554 - Comuna 4", 4));
        locales.add(new Local(-34.636392, -58.367703, "Pinzón 1106 - Comuna 4", 4));
        locales.add(new Local(-34.623521, -58.411677, "Sánchez de Loria 1149 - Comuna 5", 5));
        locales.add(new Local(-34.601917, -58.415693, "Mario Bravo 656 - Comuna 5", 5));
        locales.add(new Local(-34.608170, -58.442434, "Av. Díaz Vélez 5480 - Comuna 6", 6));
        locales.add(new Local(-34.619111, -58.456554, "Av. Tte. Gral. Donato Álvarez 567 - Comuna 6", 6));
        locales.add(new Local(-34.635248, -58.461369, "Pedernera 533 - Comuna 7", 7));
        locales.add(new Local(-34.633555, -58.463246, "Av. Varela 336 - Comuna 7", 7));
        locales.add(new Local(-34.679209, -58.476953, "Av. Riestra 6029 - Comuna 8", 8));
        locales.add(new Local(-34.645830, -58.500447, "Corvalan 802 - Comuna 9", 9));
        locales.add(new Local(-34.624252, -58.509026, "Pedro Calderón de la Barca 1674 - Comuna 10", 10));
        locales.add(new Local(-34.603066, -58.500199, "Tinogasta 3627 - Comuna 11", 11));
        locales.add(new Local(-34.567670, -58.486932, "Galvan 3068 - Comuna 12", 12));
        locales.add(new Local(-34.554097, -58.484539, "Estomba 3904 - Comuna 12", 12));
        locales.add(new Local(-34.579526, -58.450525, "Av. Federico Lacroze 3381 - Comuna 13", 13));
        locales.add(new Local(-34.593177, -58.428436, "Av. Raúl Scalabrini Ortiz 1276 - Comuna 14", 14));
        locales.add(new Local(-34.587411, -58.439832, "Fitz Roy 1327 - Comuna 14", 14));
        locales.add(new Local(-34.600795, -58.462055, "Cucha Cucha 2399 - Comuna 15", 15));
        locales.add(new Local(-34.598732, -58.444102, "Padilla 829 - Comuna 15", 15));


        Bitmap ima = resizeMapIcons("ne512", 50, 50);
        for (Local local : locales) {
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(local.getLatitude(), local.getLongitude())).title(local.getNombre()).icon(BitmapDescriptorFactory.fromBitmap(ima)));
        }


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
        mGoogleMap.addPolygon(poligono);

        Local local10 = new Local(-34.624252, -58.509026, "Comuna 10 - Calderón de la barca 1674", 10);
        LatLng lat = new LatLng(local10.getLatitude(), local10.getLongitude());

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        configurarMapa();
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "mipmap", getActivity().getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
}

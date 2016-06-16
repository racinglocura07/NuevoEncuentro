package nofuemagia.nuevoencuentro.model;

/**
 * Created by jlionti on 15/06/2016. No Fue Magia
 */
public class Local {
    public double latitude;
    public double longitude;
    public String Nombre;
    public int comuna;

    public Local(double latitude, double longitude, String nombre, int comuna) {
        this.latitude = latitude;
        this.longitude = longitude;
        Nombre = nombre;
        this.comuna = comuna;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getNombre() {
        return Nombre;
    }
}

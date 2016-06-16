package nofuemagia.nuevoencuentro.model;


import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
@Table(name = "caracteristica", id = BaseColumns._ID)
public class Caracteristica extends Model {

    public static final String NOMBRE = "nombre";
    public static final String CANTIDAD = "cantidad";

    @Column(name = NOMBRE)
    private String nombre;

    @Column(name = CANTIDAD)
    private String cantidad;

    public Caracteristica() {
        super();
    }

    public Caracteristica(String nombre, String cantidad) {
        super();
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}

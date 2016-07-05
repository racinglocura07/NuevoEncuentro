package coop.nuevoencuentro.nofuemagia.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
@Table(name = "producto",  id = BaseColumns._ID)
public class Producto extends Model {

    public static final String NOMBRE = "nombre";
    public static final String DESCRIPCION = "descripcion";
    public static final String PRECIO = "precio";
    public static final String DIASEMANA = "diaSemanaPedido";
    public static final String INTERVALOPEDIDO = "intervaloSemanaPedido";

    @Column(name = NOMBRE)
    private String nombre;

    @Column(name = DESCRIPCION)
    private String descripcion;

    @Column(name = PRECIO)
    private String precio;

    @Column(name = DIASEMANA)
    private int diaSemanaPedido;

    @Column(name = INTERVALOPEDIDO)
    private int intervaloSemanaPedido;

    public Producto() {
        super();
    }

    public Producto(String nombre, String descripcion, String precio, int diaSemanaPedido, int intervaloSemanaPedido) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.diaSemanaPedido = diaSemanaPedido;
        this.intervaloSemanaPedido = intervaloSemanaPedido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getDiaSemanaPedido() {
        return diaSemanaPedido;
    }

    public void setDiaSemanaPedido(int diaSemanaPedido) {
        this.diaSemanaPedido = diaSemanaPedido;
    }

    public int getIntervaloSemanaPedido() {
        return intervaloSemanaPedido;
    }

    public void setIntervaloSemanaPedido(int intervaloSemanaPedido) {
        this.intervaloSemanaPedido = intervaloSemanaPedido;
    }
}

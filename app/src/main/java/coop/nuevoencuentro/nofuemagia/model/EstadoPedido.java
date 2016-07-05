package coop.nuevoencuentro.nofuemagia.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
@Table(name = "estado",  id = BaseColumns._ID)
public class EstadoPedido extends Model {

    public static final String NOMBRE = "nombre";
    public static final String CODIGO = "codigo";

    @Column(name = NOMBRE)
    private String nombre;

    @Column(name = CODIGO)
    private int codigo;

    public EstadoPedido() {
        super();
    }

    public EstadoPedido(String nombre, int codigo) {
        super();
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}

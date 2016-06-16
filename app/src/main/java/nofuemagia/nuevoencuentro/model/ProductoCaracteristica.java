package nofuemagia.nuevoencuentro.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
@Table(name = "productoCaracteristica",  id = BaseColumns._ID)
public class ProductoCaracteristica extends Model {

    public static final String PRODUCTO = "productoId";
    public static final String CARACTERISTICA = "caracteristicaId";

    @Column(name = PRODUCTO)
    private Producto producto;

    @Column(name = CARACTERISTICA)
    private Caracteristica caracteristica;

    public ProductoCaracteristica() {
        super();
    }

    public ProductoCaracteristica(Producto producto, Caracteristica caracteristica) {
        super();
        this.producto = producto;
        this.caracteristica = caracteristica;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }
}

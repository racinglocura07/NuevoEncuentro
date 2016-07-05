package coop.nuevoencuentro.nofuemagia.model;


import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */

@Table(name = "compra",  id = BaseColumns._ID)
public class Pedido extends Model {

    public static final String FECHA = "fechaPedido";
    public static final String PRODUCTO = "productoId";
    public static final String ESTADO = "estadoId";
    public static final String USUARIO = "usuarioId";

    @Column(name = FECHA)
    private Date fechaPedido;

    @Column(name = PRODUCTO)
    private Producto producto;

    @Column(name = ESTADO)
    private EstadoPedido estado;

    @Column(name = USUARIO)
    private Usuarios usuarioId;

    public Pedido() {
        super();
    }

    public Pedido(Date fechaPedido, Producto producto, EstadoPedido estado, Usuarios usuarioId) {
        super();
        this.fechaPedido = fechaPedido;
        this.producto = producto;
        this.estado = estado;
        this.usuarioId = usuarioId;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Usuarios getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuarios usuarioId) {
        this.usuarioId = usuarioId;
    }
}

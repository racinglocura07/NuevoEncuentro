package nofuemagia.nuevoencuentro.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
@Table(name = "Actividades", id = "idActividad")
public class Actividades extends Model {

    @Column(name = "idActividad")
    public int idActividad;

    @Column(name = "nombre")
    public String nombre;

    @Column(name = "descripcion")
    public String descripcion;

    @Column(name = "cuando")
    public int cuando;

    @Column(name = "repeticion")
    public int repeticion;

    public Actividades() {
        super();
    }

    public Actividades(int idActividad, String nombre, String descripcion, int cuando, int repeticion) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cuando = cuando;
        this.repeticion = repeticion;
    }

    public static List<Actividades> GetAll() {
        return new Select()
                .from(Actividades.class)
                .execute();
    }
}

package coop.nuevoencuentro.nofuemagia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
@Table(name = "Actividades", id = "idActividad")
public class Actividades extends Model {

    @Column(name = "idActividad")
    @Expose
    public int idActividad;

    @Column(name = "nombre")
    @Expose
    public String nombre;

    @Column(name = "descripcion")
    @Expose
    public String descripcion;

    @Column(name = "cuando")
    @Expose
    public String cuando;

    @Column(name = "repeticion")
    @Expose
    public int repeticion;

    @Column(name = "esTaller")
    @Expose
    public boolean esTaller;

    public Actividades() {
        super();
    }

    public Actividades(int idActividad, String nombre, String descripcion, String cuando, int repeticion, boolean esTaller) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cuando = cuando;
        this.repeticion = repeticion;
        this.esTaller = esTaller;
    }

    public static List<Actividades> GetAll(boolean esTaller) {
        return new Select()
                .from(Actividades.class)
                .where("esTaller = ?", esTaller)
                .orderBy("idActividad DESC")
                .execute();
    }

    public static List<Actividades> GetAll() {
        return new Select()
                .from(Actividades.class)
                .orderBy("idActividad DESC")
                .execute();
    }
}

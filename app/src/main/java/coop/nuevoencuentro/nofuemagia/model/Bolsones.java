package coop.nuevoencuentro.nofuemagia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Tano on 17/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
@Table(name = "Bolsones", id = "idBolson")
public class Bolsones extends Model {

    @Column(name = "idBolson")
    @Expose
    public int idBolson;

    @Column(name = "link")
    @Expose
    public String link;

    @Column(name = "esSeco")
    @Expose
    public boolean esSeco;

    @Column(name = "creadoEl")
    @Expose
    public String creadoEl;

    public Bolsones() {
    }

    public static Bolsones getLast(boolean seco) {
        return new Select().from(Bolsones.class).where("esSeco = ?", seco).orderBy("idBolson DESC").executeSingle();
    }

    public static List<Bolsones> GetAll() {
        return new Select()
                .from(Actividades.class)
                .orderBy("idBolson DESC")
                .execute();
    }

    public void setIdBolson(int idBolson) {
        this.idBolson = idBolson;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCreadoEl(String creadoEl) {
        this.creadoEl = creadoEl;
    }

    public boolean isEsSeco() {
        return esSeco;
    }

    public void setEsSeco(boolean esSeco) {
        this.esSeco = esSeco;
    }
}

package coop.nuevoencuentro.nofuemagia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by Tano on 17/07/2016.
 */
@Table(name = "Bolsones", id = "idBolson")
public class Bolsones extends Model {

    @Column(name = "idBolson")
    public int idBolson;

    @Column(name = "link")
    public String link;

    @Column(name = "creadoEl")
    public String creadoEl;

    public Bolsones() {
    }

    public static Bolsones getLast() {
        return new Select().all().from(Bolsones.class).orderBy("idBolson DESC").executeSingle();
    }

    public int getIdBolson() {
        return idBolson;
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

    public String getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(String creadoEl) {
        this.creadoEl = creadoEl;
    }
}

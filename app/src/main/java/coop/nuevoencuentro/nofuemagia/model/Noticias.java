package coop.nuevoencuentro.nofuemagia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Tano on 18/07/2016.
 */

@Table(name = "Noticias", id = "idNoticia")
public class Noticias extends Model {

    @Column(name = "idNoticia")
    @Expose
    public int idNoticia;

    @Column(name = "titulo")
    @Expose
    public String titulo;

    @Column(name = "descripcion")
    @Expose
    public String descripcion;

    @Column(name = "link")
    @Expose
    public String link;

    public Noticias() {
        super();
    }

    public Noticias(int idNoticia, String titulo, String descripcion, String link) {
        this.idNoticia = idNoticia;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.link = link;
    }

    public static List<Noticias> GetAll() {
        return new Select().all()
                .from(Noticias.class)
                .orderBy("idNoticia DESC")
                .execute();
    }
}

package coop.nuevoencuentro.nofuemagia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Tano on 18/07/2016.
 */

@Table(name = "Noticias", id = "idNoticia")
public class Noticias extends Model {

    @Column(name = "idNoticia")
    public int idNoticia;

    @Column(name = "titulo")
    public String titulo;

    @Column(name = "descripcion")
    public String descripcion;

    @Column(name = "link")
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

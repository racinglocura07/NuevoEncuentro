package nofuemagia.nuevoencuentro.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
@Table(name = "usuario", id = BaseColumns._ID)
public class Usuarios extends Model {

    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
    public static final String MAIL = "mail";
    public static final String TELEFONO = "telefono";
    public static final String FACEBOOKID = "facebookId";

    @Column(name = NOMBRE)
    private String nombre;

    @Column(name = APELLIDO)
    private String apellido;

    @Column(name = MAIL)
    private String mail;

    @Column(name = TELEFONO)
    private String telefono;

    @Column(name = FACEBOOKID)
    private String facebookId;

    public Usuarios() {
        super();
    }

    public Usuarios(String nombre, String apellido, String mail, String telefono, String facebookId) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.telefono = telefono;
        this.facebookId = facebookId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}

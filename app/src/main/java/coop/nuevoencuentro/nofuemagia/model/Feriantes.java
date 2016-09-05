package coop.nuevoencuentro.nofuemagia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Tanoo on 4/9/2016.
 * Nuevo Encuentro
 * No Fue Magiaø
 */

@Table(name = "Feriantes", id = "idFeriante")
public class Feriantes extends Model implements Parcelable {

    public static final Creator<Feriantes> CREATOR = new Creator<Feriantes>() {
        @Override
        public Feriantes createFromParcel(Parcel in) {
            return new Feriantes(in);
        }

        @Override
        public Feriantes[] newArray(int size) {
            return new Feriantes[size];
        }
    };
    @Column(name = "idFeriante")
    @Expose
    public int idFeriante;
    @Column(name = "nombre")
    @Expose
    public String nombre;
    @Column(name = "descripcion")
    @Expose
    public String descripcion;
    @Column(name = "mail")
    @Expose
    public String mail;
    @Column(name = "facebook")
    @Expose
    public String facebook;
    @Column(name = "telefono")
    @Expose
    public String telefono;
    @Column(name = "comuna")
    @Expose
    public int comuna;
    @Column(name = "rubro")
    @Expose
    public String rubro;

    public Feriantes() {
        super();
    }

    public Feriantes(int idFeriante, String nombre, String descripcion, String mail, String facebook, String telefono, int comuna, String rubro) {
        this.idFeriante = idFeriante;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.mail = mail;
        this.facebook = facebook;
        this.telefono = telefono;
        this.comuna = comuna;
        this.rubro = rubro;
    }

    protected Feriantes(Parcel in) {
        idFeriante = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        mail = in.readString();
        facebook = in.readString();
        telefono = in.readString();
        comuna = in.readInt();
        rubro = in.readString();
    }

    public static List<Feriantes> GetAll() {
        return new Select()
                .from(Feriantes.class)
                .orderBy("idFeriante DESC")
                .execute();
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idFeriante);
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeString(mail);
        parcel.writeString(facebook);
        parcel.writeString(telefono);
        parcel.writeInt(comuna);
        parcel.writeString(rubro);
    }
}

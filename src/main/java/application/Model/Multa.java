package application.Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Multa implements Serializable {
    private int id;

    private double precio;

    private LocalDate fecha;

    private Coche coche;

    public Multa() {
    }

    public Multa(int id, Coche coche, double precio, LocalDate fecha) {
        this.id = id;
        this.precio = precio;
        this.fecha = fecha;
        this.coche = coche;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    @Override
    public String toString() {
        return "Multa{" +
                ", precio=" + getPrecio() +
                ", fecha=" + getFecha() +
                ", matricula=" + getCoche().getMatricula() +
                '}';
    }
}

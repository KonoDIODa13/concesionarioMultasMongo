package application.Model;

public class Coche {
    private int id;
    private String matricula;
    private String marca;
    private String modelo;
    private String tipo;

    public Coche(int id, String matricula, String marca, String modelo, String tipo) {
        this.id = id;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Coche{" +
                "matricula='" + matricula + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}

package application.DAO;

import application.Model.Coche;

import java.util.List;

public interface CocheDAOimpl {
     void conectarBD();

     void desconectarBD();

     List<Coche> getCoches();

     void insertarCoche(Coche coche);

     void modificarCoche(Coche nuevoCoche, Coche antiguoCoche);

     void eliminarCoche(Coche coche);

    Coche buscarCoche(int id);
}

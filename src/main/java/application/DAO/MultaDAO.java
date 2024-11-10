package application.DAO;

import application.Connection.ConnectionDB;
import application.Model.Coche;
import application.Model.Multa;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MultaDAO implements MultaDAOImpl {
    MongoClient con;
    Gson gson = new Gson();
    MongoCollection<Document> collection = null;

    public MultaDAO() {
        conectarBD();

    }

    public void conectarBD() {
        try {
            con = ConnectionDB.conectar();

            MongoDatabase database = con.getDatabase("concesionarioMultas");

            database.createCollection("multa");

            System.out.println("Coleccion creada Satisfactoriamente.\n");

            collection = database.getCollection("multa");

        } catch (Exception exception) {
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }


    @Override
    public void desconectarBD() {
        ConnectionDB.desconectar(con);
    }

    @Override
    public void insertarMulta(Multa multa) {
        collection.insertOne(new Document("id", multa.getId()).append("precio", multa.getPrecio())
                .append("fecha", multa.getFecha().toString()).append("id_coche", multa.getCoche().getId())
        );
    }

    @Override
    public void modificarMulta(Multa nuevaMulta, Multa antiguaMulta) {
        collection.updateOne(new Document("id", antiguaMulta.getId()),
                new Document("$set", new Document("precio", nuevaMulta.getPrecio())
                        .append("marca", nuevaMulta.getFecha()).append("id_coche", nuevaMulta.getCoche().getId()))
        );
    }

    @Override
    public void eliminarMulta(Multa multa) {
        collection.deleteOne(new Document("id", multa.getId()));
    }

    @Override
    public List<Multa> getMultas(Coche coche) {
        List<Multa> multas = new ArrayList<>();
        //MongoCollection<Document> collection = con.getDatabase("concesionarioMultas").getCollection("coche");
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Coche c = new CocheDAO().buscarCoche(doc.getInteger("id_coche"));
                if (c.getId() == coche.getId()) {
                    Multa multa = new Multa(
                            doc.getInteger("id"),
                            c,
                            doc.getDouble("precio"),
                            LocalDate.parse(doc.getString("fecha"))
                    );
                    multas.add(multa);
                }
                //Multa multa = gson.fromJson(cursor.next().toJson(), Multa.class);

            }
            return multas;
        } finally {
            cursor.close();
        }
    }


    public List<Multa> getMultasTotales() {
        List<Multa> multas = new ArrayList<>();
        //MongoCollection<Document> collection = con.getDatabase("concesionarioMultas").getCollection("coche");
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Coche c = new CocheDAO().buscarCoche(doc.getInteger("id_coche"));
                Multa multa = new Multa(
                        doc.getInteger("id"),
                        c,
                        doc.getDouble("precio"),
                        LocalDate.parse(doc.getString("fecha"))
                );
                multas.add(multa);
            }
            return multas;
        } finally {
            cursor.close();
        }
    }
}

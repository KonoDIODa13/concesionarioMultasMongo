package application.DAO;

import application.Connection.ConnectionDB;
import application.Model.Coche;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CocheDAO implements  CocheDAOimpl{
    MongoClient con;
    Gson gson = new Gson();
    MongoCollection<Document> collection = null; // instancio aquí la colección ya que solo tengo una "tabla" a la que atacar.

    public CocheDAO() {
        conectarBD();
    }

    public void conectarBD() {
        /*
        Realizo la conexion con el metodo conectar() del fichero ConnectionBD (que es en donde está la conexión)
        y creo la database "concesionario" y la colección "coche" para luego instanciar la colección a "coche" a la
        colección general.
         */
        try {
            con = ConnectionDB.conectar();

            MongoDatabase database = con.getDatabase("concesionarioMultas");

            database.createCollection("coche");
            //database.createCollection("multa");

            System.out.println("Coleccion creada Satisfactoriamente.\n");

            collection = database.getCollection("coche");

        } catch (Exception exception) {
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    public void desconectarBD() {
        // Función para desconectar a la bd.
        ConnectionDB.desconectar(con);
    }

    public List<Coche> getCoches() {
        /*
        Recorro los campos de la colección con el .find().iterator() para luego con el gson parsearme los campos que
        recibo a objetos de tipo Coche e insertarlos a la lista de coches. Por último, cierro el cursor.
        */
        List<Coche> coches = new ArrayList<>();
        //MongoCollection<Document> collection = con.getDatabase("concesionarioMultas").getCollection("coche");
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Coche coche = gson.fromJson(cursor.next().toJson(), Coche.class);
                coches.add(coche);
            }
            return coches;
        } finally {
            cursor.close();
        }
    }

    public void insertarCoche(Coche coche) {
        /*
        Convierto el coche que recibo a un string gracias a la librería gson para luego parsearlo a Documento y llamar
        al metodo insertOne() que inserta lo que pases por parámetro.
         */
        //MongoCollection<Document> collection = con.getDatabase("concesionarioMultas").getCollection("coche");
        String json = gson.toJson(coche);
        Document doc = Document.parse(json);
        collection.insertOne(doc);
    }

    public void modificarCoche(Coche nuevoCoche, Coche antiguoCoche) {
        /*
        Función que modifica los campos del coche ya existente. Para ello, modificamos con la matricula del coche antigüo
        con los campos del coche nuevo. Es necesario, añadir el $set para realizar la modificación.
         */
        //MongoCollection<Document> collection = con.getDatabase("concesionarioMultas").getCollection("coche");

        collection.updateOne(new Document("matricula", antiguoCoche.getMatricula()),
                new Document("$set", new Document("matricula", nuevoCoche.getMatricula())
                        .append("marca", nuevoCoche.getMarca()).append("modelo", nuevoCoche.getModelo())
                        .append("tipo", nuevoCoche.getTipo())
                )
        );
    }

    public void eliminarCoche(Coche coche) {
        /*
        Función para eliminar dicho coche de la base de datos. para ello, llamamos al metodo deleteOne de la colección
        y le pasamos el un nuevo documento con la matrícula del coche que queremos eliminar.
         */
        //MongoCollection<Document> collection = con.getDatabase("concesionario").getCollection("coche");
        // MongoCollection<Document> collection = con.getDatabase("concesionarioMultas").getCollection("coche");

        collection.deleteOne(new Document("id", coche.getId()));
    }

    public Coche buscarCoche(int id) {
        Coche c = null;
        try {
            Document document = collection.find(Filters.eq("id", id)).first();
            //Coge el primer resultado que pille con esa matricula
            if (document != null) {
                c = gson.fromJson(document.toJson(), Coche.class);
                //Pasa de JSON a Coche
            }
        } catch (Exception e) {
            System.out.println("Error de BD Mongo");
        }
        return c;
    }
}

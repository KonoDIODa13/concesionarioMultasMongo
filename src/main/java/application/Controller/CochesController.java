package application.Controller;

import application.DAO.CocheDAO;
import application.Model.Coche;
import application.Model.Tipo;
import application.Utils.AlertUtils;
import application.Utils.CambioEscenas;
import application.Utils.Comprobaciones;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CochesController implements Initializable {
    @FXML
    private AnchorPane concesionario;

    @FXML
    private ComboBox<Tipo> cbTipo;

    @FXML
    private TextField tfMatricula, tfMarca, tfModelo, tfTipo;

    @FXML
    private TableView<Coche> tvCoches;

    @FXML
    private TableColumn<Coche, String> tcMatricula, tcMarca, tcModelo, tcTipo;

    CocheDAO dao;
    List<Coche> coches;
    Coche cocheSeleccionado = null;

    public CochesController() {
        // Creo un constructor en el que instancio el crud para acceder a la funcionalidad del crud.
        dao = new CocheDAO();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarTabla();
        cargarCB();
        tcMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tcMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        tcModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    }

    @FXML
    void insertarCoche(ActionEvent event) {
        String matricula;
        String marca;
        String modelo;
        String tipo;
        int id = coches.size() + 1;
        if (Comprobaciones.compruebaVacio(tfMatricula.getText(), "Matricula")) {
            matricula = tfMatricula.getText();
        } else {
            return;
        }
        if (Comprobaciones.compruebaVacio(tfMarca.getText(), "Marca")) {
            marca = tfMarca.getText();
        } else {
            return;
        }
        if (Comprobaciones.compruebaVacio(tfModelo.getText(), "Modelo")) {
            modelo = tfModelo.getText();
        } else {
            return;
        }
        if (cbTipo.getValue() != null) {
            tipo = cbTipo.getValue().toString();
        } else {
            tipo = null;
            return;
        }
        Coche coche = new Coche(id, matricula, marca, modelo, tipo);

        dao.insertarCoche(coche);
        AlertUtils.mostrarConfirmacion("Coche creado correctamente.");
        cargarTabla();
        limpiarCampos(event);

    }

    @FXML
    void modificarCoche(ActionEvent event) {
        if (cocheSeleccionado != null) {
            String matricula;
            String marca;
            String modelo;
            String tipo;
            int id = coches.size() + 1;
            if (Comprobaciones.compruebaVacio(tfMatricula.getText(), "Matricula")) {
                matricula = tfMatricula.getText();
            } else {
                return;
            }
            if (Comprobaciones.compruebaVacio(tfMarca.getText(), "Marca")) {
                marca = tfMarca.getText();
            } else {
                return;
            }
            if (Comprobaciones.compruebaVacio(tfModelo.getText(), "Modelo")) {
                modelo = tfModelo.getText();
            } else {
                return;
            }
            if (cbTipo.getValue() != null) {
                tipo = cbTipo.getValue().toString();
            } else {
                tipo = null;
                return;
            }
            Coche nuevoCoche = new Coche(id, matricula, marca, modelo, tipo);
            dao.modificarCoche(nuevoCoche, cocheSeleccionado);
        } else {
            AlertUtils.mostrarError("Seleccione primero el coche");
        }
    }

    @FXML
    void eliminarCoche(ActionEvent event) {
        if (cocheSeleccionado != null) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea borrar el coche?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                dao.eliminarCoche(cocheSeleccionado);
                limpiarCampos(event);
                cargarTabla();
            }
        } else {
            AlertUtils.mostrarError("Seleccione primero el coche");
        }
    }

    @FXML
    void mostrarMultas(ActionEvent event) {
        if (cocheSeleccionado != null) {
            CambioEscenas.cambioEscena("multas.fxml", concesionario, cocheSeleccionado);
            //crud.desconectar();
        } else {
            AlertUtils.mostrarError("Seleccione primero el coche");
        }
    }

    @FXML
    void seleccionarCoche(MouseEvent event) {
        try {
            cocheSeleccionado = tvCoches.getSelectionModel().getSelectedItem();
            cargarData();
        } catch (NullPointerException e) {
            AlertUtils.mostrarError("No has seleccionado ningun dato.\n");
        }
    }

    @FXML
    void limpiarCampos(ActionEvent event) {
        tfMatricula.setText("");
        tfMarca.setText("");
        tfModelo.setText("");
        cargarCB();
        cocheSeleccionado = null;
    }

    @FXML
    void salir(ActionEvent event) {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea salir?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            dao.desconectarBD();
            ((Stage) concesionario.getScene().getWindow()).close();
        }

    }

    public void cargarTabla() {
        tvCoches.getItems().clear();
        coches = dao.getCoches();

        tvCoches.setItems(FXCollections.observableList(coches));
    }

    public void cargarCB() {
        // Al igual que el cargarTabla, cargo los valores del enum Tipo.
        cbTipo.getItems().clear();
        cbTipo.getItems().addAll(Tipo.values());
    }

    public void cargarData() {
        // Este metodo carga los datos del coche seleccionado.
        tfMatricula.setText(cocheSeleccionado.getMatricula());
        tfMarca.setText(cocheSeleccionado.getMarca());
        tfModelo.setText(cocheSeleccionado.getModelo());
        Tipo tipoCoche = null;
        for (Tipo tipo : Tipo.values()) {
            if (tipo.toString().equals(cocheSeleccionado.getTipo())) {
                tipoCoche = tipo;
            }
        }
        cbTipo.setValue(tipoCoche);
    }
}

package org.ds.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.ds.model.Usuario;

public class AdminDashboardController implements Initializable {

    @FXML
    private Label lblBienvenida;

    @FXML
    private Button btnSalir;

    private Usuario usuarioActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void IniciarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;

        lblBienvenida.setText(
                "Bienvenido administrador "
                + usuario.getUsername()
        );
    }

    @FXML
    private void eventoSalir(ActionEvent evento) {

        try {
            FXMLLoader cargadorFXML = new FXMLLoader(
                    getClass().getResource(
                            "/org/ds/view/InicioSesionView.fxml"
                    )
            );

            Parent raiz = cargadorFXML.load();

            Stage escenarioInicio = new Stage();
            escenarioInicio.setScene(new Scene(raiz));
            escenarioInicio.setTitle("Inicio de sesión");
            escenarioInicio.show();

            Stage escenarioActual =
                    (Stage) btnSalir.getScene().getWindow();

            escenarioActual.close();

        } catch (IOException e) {
            System.err.println(
                    "Error al regresar al inicio de sesión: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
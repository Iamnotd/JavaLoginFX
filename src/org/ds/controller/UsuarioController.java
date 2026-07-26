package org.ds.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.ds.dao.UsuarioDAO;
import org.ds.util.SecurityUtil;

public class UsuarioController implements Initializable {

    @FXML
    private TextField txtNuevoUsuario;

    @FXML
    private PasswordField txtNuevaPassword;

    @FXML
    private ComboBox<String> cmbRol;

    @FXML
    private Label lblMensajeUsuario;

    private UsuarioDAO usuarioDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        usuarioDAO = new UsuarioDAO();

        cmbRol.setItems(
                FXCollections.observableArrayList(
                        "admin",
                        "empleado",
                        "cajero"
                )
        );

        lblMensajeUsuario.setText("");
    }

    @FXML
    private void eventoRegistrarUsuario(ActionEvent evento) {

        String username =
                txtNuevoUsuario.getText().trim();

        String password =
                txtNuevaPassword.getText();

        String rol =
                cmbRol.getValue();

        lblMensajeUsuario.setStyle(
                "-fx-text-fill: #c43d3d;"
        );

        if (username.isEmpty()
                || password.isEmpty()
                || rol == null) {

            lblMensajeUsuario.setText(
                    "Complete todos los campos."
            );

            return;
        }

        if (username.length() < 3) {

            lblMensajeUsuario.setText(
                    "El usuario debe tener al menos 3 caracteres."
            );

            return;
        }

        if (password.length() < 4) {

            lblMensajeUsuario.setText(
                    "La contraseña debe tener al menos 4 caracteres."
            );

            return;
        }

        String passwordHash =
                SecurityUtil.hashSHA256(password);

        boolean registrado =
                usuarioDAO.registrarUsuario(
                        username,
                        passwordHash,
                        rol
                );

        if (registrado) {

            lblMensajeUsuario.setStyle(
                    "-fx-text-fill: #2f8a53;"
            );

            lblMensajeUsuario.setText(
                    "Usuario registrado correctamente."
            );

            limpiarCampos();

        } else {

            lblMensajeUsuario.setText(
                    "No se pudo registrar el usuario."
            );
        }
    }

    private void limpiarCampos() {
        txtNuevoUsuario.clear();
        txtNuevaPassword.clear();
        cmbRol.getSelectionModel().clearSelection();
    }
}
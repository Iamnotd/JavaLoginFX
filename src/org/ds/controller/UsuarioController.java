package org.ds.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.ds.dao.UsuarioDAO;
import org.ds.model.Usuario;
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

    @FXML
    private TableView<Usuario> tblUsuarios;

    @FXML
    private TableColumn<Usuario, Integer> colId;

    @FXML
    private TableColumn<Usuario, String> colUsername;

    @FXML
    private TableColumn<Usuario, String> colRol;

    @FXML
    private TableColumn<Usuario, Boolean> colActivo;

    @FXML
    private TableColumn<Usuario, Timestamp> colFechaCreacion;

    private UsuarioDAO usuarioDAO;

    private ObservableList<Usuario> listaUsuarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        usuarioDAO = new UsuarioDAO();

        listaUsuarios = FXCollections.observableArrayList();

        cmbRol.setItems(
                FXCollections.observableArrayList(
                        "admin",
                        "empleado",
                        "cajero"
                )
        );

        configurarTabla();
        cargarUsuarios();

        lblMensajeUsuario.setText("");
    }

    private void configurarTabla() {

        colId.setCellValueFactory(
                dato -> new SimpleIntegerProperty(
                        dato.getValue().getId()
                ).asObject()
        );

        colUsername.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue().getUsername()
                )
        );

        colRol.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue().getRol()
                )
        );

        colActivo.setCellValueFactory(
                dato -> new SimpleBooleanProperty(
                        dato.getValue().isActivo()
                )
        );

        colFechaCreacion.setCellValueFactory(
                dato -> new SimpleObjectProperty<>(
                        dato.getValue().getFechaCreacion()
                )
        );
    }

    private void cargarUsuarios() {

        listaUsuarios.clear();
        listaUsuarios.addAll(
                usuarioDAO.listarUsuarios()
        );

        tblUsuarios.setItems(listaUsuarios);
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
            cargarUsuarios();

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
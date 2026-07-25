package org.ds.controller;

import java.io.IOException;
import org.ds.dao.UsuarioDAO;
import org.ds.util.SecurityUtil;
import org.ds.model.Usuario;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;




public class InicioSesionController implements Initializable {
    
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btniniciarSesion;
    @FXML private Label lblMensaje;
    
    private UsuarioDAO usuarioDAO;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO = new UsuarioDAO();
        lblMensaje.setText("");
    }    
    
    public void eventoInicioSesion(ActionEvent evento) {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        
        //verificacion si los datos estan vacios
        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos sus datos.");
            return;
        }
        
        //Datos completos
        String passwordHash = SecurityUtil.hashSHA256(password);
        //llamar al dato para iniciar sesion
        Usuario usuarioIniciado = usuarioDAO.iniciarSesion(usuario, passwordHash);
        // ! =
        if (usuarioIniciado !=  null) {
        lblMensaje.setStyle("-fx-background-color: #60682e;");
        lblMensaje.setText("Inicio correcto");
        abrirDashboard(usuarioIniciado);
    } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
            }
    }
    
    private void abrirDashboard(Usuario usuario){
        String rutaFXML = "";
        String tituloDashboard = "";
        
        switch (usuario.getRol().toLowerCase()) {
            case "admin":
                rutaFXML = "/org/ds/view/AdminDashboardView.fxml";
                tituloDashboard = "Panel de Administracion";
                break;
            case "empleado":
                
                break;
        }
        
        try {
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent raiz = cargadorFXML.load();
            
            AdminDashboardController controlador = cargadorFXML.getController();
            controlador.IniciarUsuario(usuario);
            
            Stage escenario = new Stage();
            escenario.setScene(new Scene(raiz));
            escenario.setTitle(tituloDashboard);
            escenario.show();
            
            Stage escenaActual = (Stage) btniniciarSesion.getScene().getWindow();
            escenaActual.close();
                    
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + rutaFXML + e.getMessage());
            lblMensaje.setText("Error interno");
        }
    }
    }


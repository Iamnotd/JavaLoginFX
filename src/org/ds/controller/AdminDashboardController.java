package org.ds.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.ds.model.Usuario;

public class AdminDashboardController implements Initializable {
    
    @FXML  private Label lblBienvenida;
    @FXML private Usuario usuarioActual;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    public void IniciarUsuario(Usuario usuario){
        this.usuarioActual = usuario;
        lblBienvenida.setText("Bienvenido administrador " + usuario.getUsername());
        //instrucciones
    }
}

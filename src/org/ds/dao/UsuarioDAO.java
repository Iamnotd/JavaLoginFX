package org.ds.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.ds.model.Usuario;
import org.ds.util.Conexion;

public class UsuarioDAO {

    // Iniciar sesión
    public Usuario iniciarSesion(
            String username,
            String passwordHash
    ) {

        Usuario usuario = null;

        String sql = "{call sp_iniciar_sesion(?, ?)}";

        try (
            Connection conexion =
                    Conexion.getInstancia().conectar();

            CallableStatement consulta =
                    conexion.prepareCall(sql)
        ) {

            consulta.setString(1, username);
            consulta.setString(2, passwordHash);

            try (
                ResultSet tablaResultado =
                        consulta.executeQuery()
            ) {

                if (tablaResultado.next()) {

                    usuario = new Usuario();

                    usuario.setId(
                            tablaResultado.getInt("id")
                    );

                    usuario.setUsername(
                            tablaResultado.getString("username")
                    );

                    usuario.setRol(
                            tablaResultado.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al iniciar sesión: "
                    + e.getMessage()
            );
        }

        return usuario;
    }

    // Registrar usuario
    public boolean registrarUsuario(
            String username,
            String passwordHash,
            String rol
    ) {

        String sql =
                "{call sp_registrar_usuario(?, ?, ?)}";

        try (
            Connection conexion =
                    Conexion.getInstancia().conectar();

            CallableStatement consulta =
                    conexion.prepareCall(sql)
        ) {

            consulta.setString(1, username);
            consulta.setString(2, passwordHash);
            consulta.setString(3, rol.toLowerCase());

            int filasAfectadas =
                    consulta.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al registrar usuario: "
                    + e.getMessage()
            );

            return false;
        }
    }
}
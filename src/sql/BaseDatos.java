/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Esteban Ramos Martínez
 */
public class BaseDatos {
    Component rootPane = null;

    public boolean conectarBD(String host, String port, String database,
            String user, String password) {
        boolean valid = false;
        String url = "";
        try {
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
         
            connection = DriverManager.getConnection(
                    url,
                    user, password);
                valid = connection.isValid(50000);
            if (valid) {
                JOptionPane.showMessageDialog(rootPane, "Conexión exitosa", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(rootPane, "NO se pudo conectar", "", JOptionPane.ERROR_MESSAGE);
                //      System.out.println("no es valida");
            }
        } catch (java.sql.SQLException sqle) {

            //System.out.println(sqle);
            JOptionPane.showMessageDialog(rootPane, "Error al conectar con la base de datos", "Error de conexión", JOptionPane.ERROR_MESSAGE);

        }
        return valid;
    }
    
     public Connection conectar(String host, String port, String database,
            String user, String password){
          Connection  conn = null;
        try {
             String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            Class.forName("org.postgresql.Driver");
              conn=DriverManager.getConnection(url,user,password);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error al conectar "+e,"Error",JOptionPane.ERROR_MESSAGE);
        }
   
    return conn;
    }

}

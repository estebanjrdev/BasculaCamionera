/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import getset.VariablesZafras;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import vistas.Configuraciones;

/**
 *
 * @author Esteban Ramos Martínez
 */
public class CrudCamiones extends BaseDatos {
    
      java.sql.Statement st;
    ResultSet rs;
    VariablesZafras var = new VariablesZafras();
    Configuraciones config = new Configuraciones();

    public void insertarCamiones(String chapa,String modelo, String chofer){
        try {
             ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();
             String sql = "insert into camiones(chapa,modelo,chofer) values('" + chapa + "','" + modelo + "','" + chofer + "');";
            st.execute(sql);
            st.close();
            conexion.close();
            JOptionPane.showMessageDialog(null, "El camion se guardo correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "El registro no se guardo " + e, "AVISO", JOptionPane.INFORMATION_MESSAGE);

        }
    }
    
      public void mostrarCamiones(JTable tabla) {

        try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();

            ResultSet counter = st.executeQuery("select * from camiones;");

            int count = 0;
            while (counter.next()) {
                count++;
            }

            int i = 0;
            String list[][] = new String[count][4];
            String sql = "select * from camiones;";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                list[i][0] = rs.getString("id_camion");
                list[i][1] = rs.getString("chapa");
                list[i][2] = rs.getString("modelo");
                list[i][3] = rs.getString("chofer");
                i++;
            }
            DefaultTableModel model = new DefaultTableModel(list, new String[]{"ID", "Chapa", "Modelo", "Chofer"}) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tabla.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e, "error", JOptionPane.ERROR_MESSAGE);
        }

    }
      
       public void eliminarCamion(String idCamion) {
        try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();
            String sql = "delete from camiones where id_camion='" + idCamion + "'; ";
            st.executeUpdate(sql);
            st.close();
            conexion.close();
            JOptionPane.showMessageDialog(null, "Registro eliminado correctamente", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar registro " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
       
       public void editarCamion(String idCamion,String chapa,String modelo,String chofer){
         try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();
            String sql ="update camiones set chapa='" + chapa + "',modelo='" + modelo + "',chofer='" + chofer + "' where id_camion='" + idCamion + "'; ";
            st.executeUpdate(sql);
            st.close();
            conexion.close();
            JOptionPane.showMessageDialog(null, "El registro se actualizo", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar registro" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
       
       }
       
       public void buscarCamion(String chapa,JTable tabla){
       
        try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();

            ResultSet counter = st.executeQuery("select * from camiones;");

            int count = 0;
            while (counter.next()) {
                count++;
            }

            int i = 0;
            String list[][] = new String[count][4];
            String sql = "select * from camiones where chapa='" + chapa + "';";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                list[i][0] = rs.getString("id_camion");
                list[i][1] = rs.getString("chapa");
                list[i][2] = rs.getString("modelo");
                list[i][3] = rs.getString("chofer");
                i++;
            }
            DefaultTableModel model = new DefaultTableModel(list, new String[]{"ID", "Chapa", "Modelo", "Chofer"}) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tabla.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e, "error", JOptionPane.ERROR_MESSAGE);
        }


       }
    
}

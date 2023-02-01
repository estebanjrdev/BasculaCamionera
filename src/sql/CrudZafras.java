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
public class CrudZafras extends BaseDatos {

    java.sql.Statement st;
    ResultSet rs;
    VariablesZafras var = new VariablesZafras();
    Configuraciones config = new Configuraciones();

    public void insertarZafra(String fechaInicio, String fechaFin, String plan, String camiones, String cana, String porcentaje, String ano) {

        try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();
            String sql = "insert into zafras(fecha_inicio,fecha_fin,plan_cana,camiones_pesados,cana_pesada,porcentaje_plan,ano) values('" + fechaInicio + "','" + fechaFin + "','" + plan + "','" + camiones + "','" + cana + "','" + porcentaje + "','" + ano + "');";
            st.execute(sql);
            st.close();
            conexion.close();
            JOptionPane.showMessageDialog(null, "La zafra se guardo correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "El registro no se guardo " + e, "AVISO", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    public void mostrarZafras(JTable tabla) {

        try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();

            ResultSet counter = st.executeQuery("select * from zafras;");

            int count = 0;
            while (counter.next()) {
                count++;
            }

            int i = 0;
            String list[][] = new String[count][8];
            String sql = "select * from zafras;";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                list[i][0] = rs.getString("id_zafra");
                list[i][1] = rs.getString("fecha_inicio");
                list[i][2] = rs.getString("fecha_fin");
                list[i][3] = rs.getString("ano");
                list[i][4] = rs.getString("plan_cana");
                list[i][5] = rs.getString("camiones_pesados");
                list[i][6] = rs.getString("cana_pesada");
                list[i][7] = rs.getString("porcentaje_plan");

                i++;
            }
            DefaultTableModel model = new DefaultTableModel(list, new String[]{"ID", "Fecha Inicio", "Fecha Fin", "Año", "Plan Caña", "Camiones", "Caña Pesada", "%"}) {
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

    public void eliminar(String idZafra) {
        try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();
            String sql = "delete from zafras where id_zafra='" + idZafra + "'; ";
            st.executeUpdate(sql);
            st.close();
            conexion.close();
            JOptionPane.showMessageDialog(null, "Registro eliminado correctamente", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar registro " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int existeLaZafra(int ano) {

        int count = 0;
        try {
            ArrayList<String> arr = config.leerArchivo();
            Connection conexion = conectar(arr.get(0), arr.get(1), arr.get(2), arr.get(3), arr.get(4));
            st = conexion.createStatement();
            String sql = "select ano from zafras where ano='" + ano + "'; ";
            st.executeQuery(sql);
            while (rs.next()) {
                var.setAno(rs.getString("ano"));
            }
            st.close();
            conexion.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error en programa " + e, "Erro de sistema", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println(var.getAno());
        return count;
    }

}

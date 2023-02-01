/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JTable;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author Esteban Ramos Martínez
 */
public class Exportar {

   
     private File file;
    private List<JTable> tabla;
    private List<String> nom_files;

    public Exportar(File file, List<JTable> tabla, List<String> nom_files) throws Exception {
        this.file = file;
        this.tabla = tabla;
        this.nom_files = nom_files;
        if (nom_files.size()!=tabla.size()) {
            throw new Exception ("Error");
        }
    }
   
    public boolean export() {
       
        try {
            DataOutputStream out=new DataOutputStream (new FileOutputStream(file));
            WritableWorkbook w = Workbook.createWorkbook(out);
            for (int index=0; index<tabla.size(); index++){
                JTable table=tabla.get(index);
                WritableSheet s=w.createSheet(nom_files.get(index),0);
               //Para que salga el titulo de las columnas
for (int i = 0; i < table.getColumnCount(); i++) {
      for (int j = 0; j < table.getRowCount(); j++) {
            Object titulo = table.getColumnName(i);
            s.addCell(new Label(i+1, j+1, String.valueOf(titulo)));
       }
}
for (int i = 0; i < table.getColumnCount(); i++) {
      for (int j = 0; j < table.getRowCount(); j++) {
            Object object = table.getValueAt(j, i);
            s.addCell(new Label(i+1, j+2, String.valueOf(object)));
      }
    }
             /*
            *    for sin titulo de columnas:
            *
            *  for (int i=0; i<table.getColumnCount(); i++){
            *     for (int j=0; j<table.getRowCount();j++){
            *         Object object=table.getValueAt(j,i);
            *         s.addCell (new Label(i,j,String.valueOf(object)));
            *       
            *     }  
            * }
            **/

            }
            w.write();
            w.close();
            return true;
        }

        catch (IOException | WriteException e) {
            return false;
        }
    }
   
   
}

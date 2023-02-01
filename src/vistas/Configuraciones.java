/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Esteban Ramos Martínez
 */
public class Configuraciones {

    File archivo = new File("config.properties");
    Properties prop = new Properties();
    File archivoSerial = new File("configSerial.properties");
    Properties propSerial = new Properties();

    public void crearArchivo() throws FileNotFoundException, IOException {

        try (OutputStream outputStream = new FileOutputStream(archivo)) {

            // establecer las claves y valores
            prop.setProperty("pg.hostname", "localhost");
            prop.setProperty("pg.port", "5432");
            prop.setProperty("pg.database", "pesajesdb");
            prop.setProperty("pg.user", "postgres");
            prop.setProperty("pg.password", "root");
            
            prop.store(outputStream, "Configuracion de conexion con Postgresql");
        }

    }
     // leer configuracion de conexion con postgresql
    public ArrayList<String> leerArchivo() throws FileNotFoundException, IOException {
        ArrayList<String> arr = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(archivo)) {

            prop.load(inputStream);

            // obtenemos el valor de cada clave
            arr.add(prop.getProperty("pg.hostname"));
            arr.add(prop.getProperty("pg.port"));
            arr.add(prop.getProperty("pg.database"));
            arr.add(prop.getProperty("pg.user"));
            arr.add(prop.getProperty("pg.password"));
        }
        return arr;
    }
    // modificar configuracion de conexion con postgresql
    public void editarArchivo(String hostname, String port, String database, String user, String password) throws FileNotFoundException, IOException {
        try (InputStream inputStream = new FileInputStream(archivo)) {
            prop.load(inputStream);
        }

        try (OutputStream outputStream = new FileOutputStream(archivo)) {

            prop.setProperty("pg.hostname", hostname);
            prop.setProperty("pg.port", port);
            prop.setProperty("pg.database", database);
            prop.setProperty("pg.user", user);
            prop.setProperty("pg.password", password);

            prop.store(outputStream, "Configuracion de conexion con Postgresql");
        }

    }
    // creacion del archivo de configuracion serial
      public void crearArchivoSerial() throws FileNotFoundException, IOException {

        try (OutputStream outputStream = new FileOutputStream(archivoSerial)) {

            // Ajustes de comunicacion serial
            propSerial.setProperty("bits.segundo", "9600");
            propSerial.setProperty("bits.datos", "8");
            propSerial.setProperty("bits.parada", "1");
            propSerial.store(outputStream, "Configuracion de comunicacion serial");
        }

    }
    
    
    // leer configuracion de conexion con postgresql
    public ArrayList<String> leerConfSerial() throws FileNotFoundException, IOException {
        ArrayList<String> arr = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(archivoSerial)) {

            propSerial.load(inputStream);

            // obtenemos el valor de cada clave
            arr.add(propSerial.getProperty("bits.segundo"));
            arr.add(propSerial.getProperty("bits.datos"));
            arr.add(propSerial.getProperty("bits.parada"));
        }
        return arr;
    }
    
     // modificar configuracion de conexion con postgresql
    public void editarConfSerial(String segundo, String datos, String parada) throws FileNotFoundException, IOException {
        try (InputStream inputStream = new FileInputStream(archivoSerial)) {
            propSerial.load(inputStream);
        }

        try (OutputStream outputStream = new FileOutputStream(archivoSerial)) {

           propSerial.setProperty("bits.segundo", segundo);
         //   propSerial.setProperty("bits.datos", datos);
            propSerial.setProperty("bits.parada", parada);
           propSerial.store(outputStream, "Configuracion de comunicacion serial");
        }

    }

}

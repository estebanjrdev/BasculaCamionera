/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import jssc.SerialPort;
import static jssc.SerialPort.BAUDRATE_9600;
import static jssc.SerialPort.DATABITS_8;
import static jssc.SerialPort.PARITY_NONE;
import static jssc.SerialPort.STOPBITS_1;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author Esteban Ramos Martínez
 */
public class Dashboard extends javax.swing.JFrame {

    int bitSegundo, bitDatos, bitParada;
    String strsegundo, strdatos, strparada;
    int xMouse;
    int yMouse;
    static SerialPort port;

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        jComboPuertos.setForeground(Color.white);
        String[] ports = SerialPortList.getPortNames();

        for (int i = 0; i < ports.length; i++) {
            jComboPuertos.addItem(ports[i]);
        }
        try {
            ArrayList<String> arrPserial = new Configuraciones().leerConfSerial();
            bitSegundo = Integer.parseInt(arrPserial.get(0));
            bitDatos = Integer.parseInt(arrPserial.get(1));
            bitParada = Integer.parseInt(arrPserial.get(2));
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int dia = now.getDayOfMonth();
        int month = now.getMonthValue();
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", " ;Septiembre",
            "Octubre", "Noviembre", "Diciemrbre"};
        //  fecha.setText(dia+"/"+meses[month - 1]+"/"+year);
        Principal p1 = new Principal();
        p1.setSize(750, 465);
        p1.setLocation(0, 0);

        content.removeAll();
        content.add(p1, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    static class MyPortListener implements SerialPortEventListener {

        SerialPort port;
        private String str = "";

        public MyPortListener(SerialPort port) {
            this.port = port;
        }

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() || event.isRXFLAG()) { // data is available
                // read data, if 10 bytes available
                if (event.getEventValue() > 0) {
                    try {
                        byte[] buffer = port.readBytes(event.getEventValue());
                        if (0 == 0) {//Chars
                            str = new String(buffer);
                        }
                        // System.out.println(str);
                        SwingUtilities.invokeAndWait(
                                new Runnable() {
                            public void run() {
                                lectura.setText(str + " KG");
                                //jTextAreaIn.setText("");
                                /* String cadena=str.replaceAll("ST,GS  ", "").replaceAll(",kg", "").replaceAll("US,GS", "").replaceAll("ww", "").replaceAll("kg", "");
                                    String[] arregloString = cadena.split("\n");
                                    String texto = arregloString[arregloString.length-1].trim();
                                    //txtObservacion.setText(texto);
                                    texto.replaceAll("ST,GS  ", "").replaceAll(",kg", "").replaceAll("US,GS", "").replaceAll("ww", "").replaceAll("kg", "");
                                    texto.replace(',', '0');
                                    texto.replace('k', '0');
                                    texto.replace('g', '0');
                                    
                                    if(texto.length()>1){
                                        String texto2="0";
                                        for(int i=0; i<texto.length();i++){
                                            if(texto.charAt(i)==','||texto.charAt(i)=='k'||texto.charAt(i)=='g'){

                                            }else{
                                                texto2=texto2+texto.charAt(i);
                                            }
                                        }
                                        try {
                                            Double peso = Double.parseDouble(texto2.trim());
                                          //  Main.setText(peso.toString());
                                            jLabel1.setText("");
                                            System.out.println(peso.toString());
                                            jLabel1.setText(peso.toString());
                                        } catch (NumberFormatException nfe){
                                           // Main.setText(texto2+" NULL");
                                            System.out.println(texto2+" NULL");
                                        }
                                        
                                    }*/
                            }
                        });
                    } catch (SerialPortException ex) {
                        System.out.println(ex);
                    } catch (InterruptedException | InvocationTargetException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            /*else if (event.isCTS()) { // CTS line has changed state
                if (event.getEventValue() == 1) { // line is ON
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) { // DSR line has changed state
                if (event.getEventValue() == 1) { // line is ON
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }*/
        }
    }

    public void cerrarPuerto() {
        try {
            if (port.closePort()) {

                JOptionPane.showMessageDialog(rootPane, "Puerto " + jComboPuertos.getSelectedItem().toString() + " cerrado saticfactoriamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                btnCerrar.setEnabled(false);
                jComboPuertos.setSelectedIndex(0);
                jComboPuertos.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(rootPane, "No se puedo cerrar el puerto.", "ERROR", JOptionPane.ERROR_MESSAGE);

            }
        } catch (Exception e) {
            //  JOptionPane.showMessageDialog(rootPane, e, "Cerrando Puerto", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void abrirPuerto(String puerto) {
        port = new SerialPort(puerto);

        try {
            if (port.openPort()) {
                // port.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);
                // btnCerrar.setText("Cerrar Puerto");
                port.setParams(bitSegundo, bitDatos, bitParada, 0); // alternate technique
                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
                port.setEventsMask(mask);
                port.addEventListener(new Dashboard.MyPortListener(port));
                JOptionPane.showMessageDialog(rootPane, "Puerto " + puerto + " abierto saticfactoriamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(rootPane, "No se puede abrir el puerto.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            //  JOptionPane.showMessageDialog(rootPane, e, "Abriendo Puerto", JOptionPane.ERROR_MESSAGE);

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Background = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        app_name = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btn_principal = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btn_pesaje = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btn_camiones = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_zafras = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btn_reportes = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btn_config = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Title = new javax.swing.JPanel();
        red_squr = new javax.swing.JPanel();
        exit = new javax.swing.JLabel();
        red_mini = new javax.swing.JPanel();
        jLabelMini = new javax.swing.JLabel();
        Header = new javax.swing.JPanel();
        lectura = new javax.swing.JLabel();
        jComboPuertos = new javax.swing.JComboBox<>();
        btnCerrar = new javax.swing.JButton();
        content = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1020, 640));
        setUndecorated(true);
        setResizable(false);

        Background.setBackground(new java.awt.Color(255, 255, 255));
        Background.setMinimumSize(new java.awt.Dimension(1020, 640));
        Background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Menu.setBackground(new java.awt.Color(13, 71, 161));
        Menu.setMinimumSize(new java.awt.Dimension(270, 640));
        Menu.setPreferredSize(new java.awt.Dimension(270, 640));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        app_name.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        app_name.setForeground(new java.awt.Color(255, 255, 255));
        app_name.setText("SCPC");
        Menu.add(app_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        jSeparator1.setPreferredSize(new java.awt.Dimension(50, 5));
        Menu.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 190, 20));

        btn_principal.setBackground(new java.awt.Color(21, 101, 192));
        btn_principal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_principalMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_principalMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_principalMousePressed(evt);
            }
        });
        btn_principal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Principal");
        btn_principal.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 30));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/home-outline.png"))); // NOI18N
        btn_principal.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        Menu.add(btn_principal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 270, 50));

        btn_pesaje.setBackground(new java.awt.Color(18, 90, 173));
        btn_pesaje.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_pesaje.setPreferredSize(new java.awt.Dimension(270, 51));
        btn_pesaje.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_pesajeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_pesajeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_pesajeMousePressed(evt);
            }
        });
        btn_pesaje.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icons8-básculas-industriales-24.png"))); // NOI18N
        btn_pesaje.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Pesaje");
        btn_pesaje.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 30));

        Menu.add(btn_pesaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, -1, -1));

        btn_camiones.setBackground(new java.awt.Color(18, 90, 173));
        btn_camiones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_camiones.setPreferredSize(new java.awt.Dimension(270, 51));
        btn_camiones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_camionesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_camionesMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_camionesMousePressed(evt);
            }
        });
        btn_camiones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icons8-camión-24.png"))); // NOI18N
        btn_camiones.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Camiones");
        btn_camiones.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 30));

        Menu.add(btn_camiones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, -1, -1));

        btn_zafras.setBackground(new java.awt.Color(18, 90, 173));
        btn_zafras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_zafras.setPreferredSize(new java.awt.Dimension(270, 51));
        btn_zafras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_zafrasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_zafrasMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_zafrasMousePressed(evt);
            }
        });
        btn_zafras.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/book-open-page-variant.png"))); // NOI18N
        btn_zafras.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Zafras");
        btn_zafras.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 30));

        Menu.add(btn_zafras, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, -1, -1));

        btn_reportes.setBackground(new java.awt.Color(18, 90, 173));
        btn_reportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_reportes.setPreferredSize(new java.awt.Dimension(270, 51));
        btn_reportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_reportesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_reportesMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_reportesMousePressed(evt);
            }
        });
        btn_reportes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/file-chart.png"))); // NOI18N
        btn_reportes.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Reportes");
        btn_reportes.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 30));

        Menu.add(btn_reportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, -1, -1));

        btn_config.setBackground(new java.awt.Color(18, 90, 173));
        btn_config.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_config.setPreferredSize(new java.awt.Dimension(270, 51));
        btn_config.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_configMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_configMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_configMousePressed(evt);
            }
        });
        btn_config.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icons8-ajustes-24.png"))); // NOI18N
        btn_config.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Configuración");
        btn_config.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 30));

        Menu.add(btn_config, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, -1, -1));

        Background.add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 640));

        Title.setBackground(new java.awt.Color(255, 255, 255));
        Title.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                TitleMouseDragged(evt);
            }
        });
        Title.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TitleMousePressed(evt);
            }
        });

        red_squr.setBackground(new java.awt.Color(255, 255, 255));
        red_squr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        red_squr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                red_squrMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                red_squrMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                red_squrMousePressed(evt);
            }
        });

        exit.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        exit.setForeground(new java.awt.Color(102, 102, 102));
        exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exit.setText("X");
        exit.setToolTipText("Cerrar");
        exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitMousePressed(evt);
            }
        });

        javax.swing.GroupLayout red_squrLayout = new javax.swing.GroupLayout(red_squr);
        red_squr.setLayout(red_squrLayout);
        red_squrLayout.setHorizontalGroup(
            red_squrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, red_squrLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        red_squrLayout.setVerticalGroup(
            red_squrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        red_mini.setBackground(new java.awt.Color(255, 255, 255));

        jLabelMini.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelMini.setForeground(new java.awt.Color(102, 102, 102));
        jLabelMini.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMini.setText("-");
        jLabelMini.setToolTipText("Minimizar");
        jLabelMini.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelMini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelMiniMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelMiniMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabelMiniMousePressed(evt);
            }
        });

        javax.swing.GroupLayout red_miniLayout = new javax.swing.GroupLayout(red_mini);
        red_mini.setLayout(red_miniLayout);
        red_miniLayout.setHorizontalGroup(
            red_miniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, red_miniLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelMini, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        red_miniLayout.setVerticalGroup(
            red_miniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, red_miniLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelMini, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout TitleLayout = new javax.swing.GroupLayout(Title);
        Title.setLayout(TitleLayout);
        TitleLayout.setHorizontalGroup(
            TitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TitleLayout.createSequentialGroup()
                .addGap(0, 942, Short.MAX_VALUE)
                .addComponent(red_mini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(red_squr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        TitleLayout.setVerticalGroup(
            TitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(red_squr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(TitleLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(red_mini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Background.add(Title, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, -1));

        Header.setBackground(new java.awt.Color(25, 118, 210));

        lectura.setFont(new java.awt.Font("Segoe UI", 1, 70)); // NOI18N
        lectura.setForeground(new java.awt.Color(255, 255, 255));
        lectura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lectura.setText("0.00");
        lectura.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jComboPuertos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jComboPuertos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Puerto" }));
        jComboPuertos.setBorder(null);
        jComboPuertos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboPuertosItemStateChanged(evt);
            }
        });

        btnCerrar.setBackground(new java.awt.Color(13, 71, 161));
        btnCerrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCerrar.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrar.setText("Cerrar Puerto");
        btnCerrar.setEnabled(false);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HeaderLayout.createSequentialGroup()
                        .addComponent(jComboPuertos, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCerrar))
                    .addComponent(lectura, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(416, Short.MAX_VALUE))
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lectura, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboPuertos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerrar))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        Background.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 25, 750, 150));

        content.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );

        Background.add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 175, 750, 465));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseEntered
        red_squr.setBackground(new Color(204, 0, 0));
        exit.setForeground(Color.white);
    }//GEN-LAST:event_exitMouseEntered

    private void exitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseExited
        red_squr.setBackground(new Color(255, 255, 255));
        exit.setForeground(new Color(102, 102, 102));
    }//GEN-LAST:event_exitMouseExited

    private void exitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMousePressed
        System.exit(0);
    }//GEN-LAST:event_exitMousePressed

    private void red_squrMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_red_squrMouseEntered
        red_squr.setBackground(new Color(204, 0, 0));
        exit.setForeground(Color.white);
    }//GEN-LAST:event_red_squrMouseEntered

    private void red_squrMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_red_squrMouseExited
        red_squr.setBackground(new Color(255, 255, 255));
        exit.setForeground(new Color(102, 102, 102));
    }//GEN-LAST:event_red_squrMouseExited

    private void red_squrMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_red_squrMousePressed
        System.exit(0);
    }//GEN-LAST:event_red_squrMousePressed

    private void TitleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TitleMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_TitleMouseDragged

    private void TitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TitleMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_TitleMousePressed

    private void jComboPuertosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboPuertosItemStateChanged
        if (jComboPuertos.getSelectedItem().toString().startsWith("C")) {
            abrirPuerto(jComboPuertos.getSelectedItem().toString());
            btnCerrar.setEnabled(true);
            jComboPuertos.setEnabled(false);
            //  JOptionPane.showMessageDialog(rootPane, jComboBox2.getSelectedItem().toString(), "Eventos", JOptionPane.WARNING_MESSAGE);
        } else {
            cerrarPuerto();

        }
    }//GEN-LAST:event_jComboPuertosItemStateChanged

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        cerrarPuerto();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btn_pesajeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pesajeMouseEntered
        if (btn_pesaje.getBackground().getRGB() == -15574355)
            setColor(btn_pesaje);
    }//GEN-LAST:event_btn_pesajeMouseEntered

    private void btn_pesajeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pesajeMouseExited
        if (btn_principal.getBackground().getRGB() != -15574355 || btn_camiones.getBackground().getRGB() != -15574355
                || btn_zafras.getBackground().getRGB() != -15574355 || btn_reportes.getBackground().getRGB() != -15574355 || btn_config.getBackground().getRGB() != -15574355)
            resetColor(btn_pesaje);
    }//GEN-LAST:event_btn_pesajeMouseExited

    private void btn_pesajeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pesajeMousePressed
        resetColor(btn_principal);
        setColor(btn_pesaje);
        resetColor(btn_camiones);
        resetColor(btn_zafras);
        resetColor(btn_reportes);
        resetColor(btn_config);
    }//GEN-LAST:event_btn_pesajeMousePressed

    private void btn_camionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_camionesMouseEntered
        if (btn_camiones.getBackground().getRGB() == -15574355)
            setColor(btn_camiones);
    }//GEN-LAST:event_btn_camionesMouseEntered

    private void btn_camionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_camionesMouseExited
        if (btn_principal.getBackground().getRGB() != -15574355 || btn_pesaje.getBackground().getRGB() != -15574355
                || btn_zafras.getBackground().getRGB() != -15574355 || btn_reportes.getBackground().getRGB() != -15574355 || btn_config.getBackground().getRGB() != -15574355)
            resetColor(btn_camiones);
    }//GEN-LAST:event_btn_camionesMouseExited

    private void btn_camionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_camionesMousePressed
        resetColor(btn_principal);
        resetColor(btn_pesaje);
        resetColor(btn_zafras);
        setColor(btn_camiones);
        resetColor(btn_reportes);
        resetColor(btn_config);
         Camiones k = new Camiones();
        k.setSize(750, 465);
        k.setLocation(0, 0);

        content.removeAll();
        content.add(k, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }//GEN-LAST:event_btn_camionesMousePressed

    private void btn_zafrasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_zafrasMouseEntered
        if (btn_zafras.getBackground().getRGB() == -15574355)
            setColor(btn_zafras);
    }//GEN-LAST:event_btn_zafrasMouseEntered

    private void btn_zafrasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_zafrasMouseExited
        if (btn_principal.getBackground().getRGB() != -15574355 || btn_pesaje.getBackground().getRGB() != -15574355
                || btn_camiones.getBackground().getRGB() != -15574355 || btn_reportes.getBackground().getRGB() != -15574355 || btn_config.getBackground().getRGB() != -15574355)
            resetColor(btn_zafras);
    }//GEN-LAST:event_btn_zafrasMouseExited

    private void btn_zafrasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_zafrasMousePressed
        resetColor(btn_principal);
        resetColor(btn_pesaje);
        resetColor(btn_camiones);
        resetColor(btn_reportes);
        setColor(btn_zafras);
        resetColor(btn_config);
        Zafras z = new Zafras();
        z.setSize(750, 430);
        z.setLocation(0, 0);

        content.removeAll();
        content.add(z, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }//GEN-LAST:event_btn_zafrasMousePressed

    private void btn_reportesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reportesMouseEntered
        if (btn_reportes.getBackground().getRGB() == -15574355)
            setColor(btn_reportes);
    }//GEN-LAST:event_btn_reportesMouseEntered

    private void btn_reportesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reportesMouseExited
        if (btn_principal.getBackground().getRGB() != -15574355 || btn_pesaje.getBackground().getRGB() != -15574355
                || btn_camiones.getBackground().getRGB() != -15574355 || btn_zafras.getBackground().getRGB() != -15574355 || btn_config.getBackground().getRGB() != -15574355)
            resetColor(btn_reportes);
    }//GEN-LAST:event_btn_reportesMouseExited

    private void btn_reportesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reportesMousePressed
        resetColor(btn_principal);
        resetColor(btn_pesaje);
        resetColor(btn_camiones);
        resetColor(btn_zafras);
        resetColor(btn_config);
        setColor(btn_reportes);
    }//GEN-LAST:event_btn_reportesMousePressed

    private void btn_configMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_configMouseEntered
        if (btn_config.getBackground().getRGB() == -15574355)
            setColor(btn_config);
    }//GEN-LAST:event_btn_configMouseEntered

    private void btn_configMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_configMouseExited
        if (btn_principal.getBackground().getRGB() != -15574355 || btn_pesaje.getBackground().getRGB() != -15574355
                || btn_camiones.getBackground().getRGB() != -15574355 || btn_zafras.getBackground().getRGB() != -15574355 || btn_reportes.getBackground().getRGB() != -15574355)
            resetColor(btn_config);
    }//GEN-LAST:event_btn_configMouseExited

    private void btn_configMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_configMousePressed
        resetColor(btn_principal);
        resetColor(btn_pesaje);
        resetColor(btn_camiones);
        resetColor(btn_zafras);
        resetColor(btn_reportes);
        setColor(btn_config);

        Configuracion c = null;
        try {
            c = new Configuracion();
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        c.setSize(750, 430);
        c.setLocation(0, 0);

        content.removeAll();
        content.add(c, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }//GEN-LAST:event_btn_configMousePressed

    private void btn_principalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_principalMouseEntered
        if (btn_principal.getBackground().getRGB() == -15574355)
            setColor(btn_principal);
    }//GEN-LAST:event_btn_principalMouseEntered

    private void btn_principalMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_principalMouseExited
        if (btn_pesaje.getBackground().getRGB() != -15574355 || btn_camiones.getBackground().getRGB() != -15574355
                || btn_zafras.getBackground().getRGB() != -15574355 || btn_reportes.getBackground().getRGB() != -15574355 || btn_config.getBackground().getRGB() != -15574355)
            resetColor(btn_principal);
    }//GEN-LAST:event_btn_principalMouseExited

    private void btn_principalMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_principalMousePressed
        setColor(btn_principal);
        resetColor(btn_pesaje);
        resetColor(btn_camiones);
        resetColor(btn_zafras);
        resetColor(btn_reportes);
        resetColor(btn_config);
        // Abrir sección
        Principal p1 = new Principal();
        p1.setSize(750, 430);
        p1.setLocation(0, 0);

        content.removeAll();
        content.add(p1, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }//GEN-LAST:event_btn_principalMousePressed

    private void jLabelMiniMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMiniMouseEntered
        red_mini.setBackground(new Color(204, 0, 0));
        jLabelMini.setForeground(Color.white);
    }//GEN-LAST:event_jLabelMiniMouseEntered

    private void jLabelMiniMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMiniMouseExited
        red_mini.setBackground(new Color(255, 255, 255));
        jLabelMini.setForeground(new Color(102, 102, 102));
    }//GEN-LAST:event_jLabelMiniMouseExited

    private void jLabelMiniMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMiniMousePressed
       this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jLabelMiniMousePressed
    void setColor(JPanel panel) {
        panel.setBackground(new Color(21, 101, 192));
    }

    void resetColor(JPanel panel) {
        panel.setBackground(new Color(18, 90, 173));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
                File archivo = new File("config.properties");
                File archivoserial = new File("configSerial.properties");
                if (!archivo.exists() && !archivoserial.exists()) {
                    try {
                        new Configuraciones().crearArchivo();
                        new Configuraciones().crearArchivoSerial();
                    } catch (IOException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (!archivo.exists()) {
                    try {
                        new Configuraciones().crearArchivo();

                    } catch (IOException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (!archivoserial.exists()) {
                    try {
                        new Configuraciones().crearArchivoSerial();

                    } catch (IOException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Background;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel Title;
    private javax.swing.JLabel app_name;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JPanel btn_camiones;
    private javax.swing.JPanel btn_config;
    private javax.swing.JPanel btn_pesaje;
    private javax.swing.JPanel btn_principal;
    private javax.swing.JPanel btn_reportes;
    private javax.swing.JPanel btn_zafras;
    private javax.swing.JPanel content;
    private javax.swing.JLabel exit;
    private javax.swing.JComboBox<String> jComboPuertos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelMini;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lectura;
    private javax.swing.JPanel red_mini;
    private javax.swing.JPanel red_squr;
    // End of variables declaration//GEN-END:variables
}

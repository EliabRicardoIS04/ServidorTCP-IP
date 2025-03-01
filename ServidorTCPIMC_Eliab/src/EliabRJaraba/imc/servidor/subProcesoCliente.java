package EliabRJaraba.imc.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import EliabRJaraba.imc.modelo.calculoImc;
import EliabRJaraba.imc.vistas.VentanaPrincipal;

public class subProcesoCliente extends Thread {

    private Socket cliente;
    private String ip;
    private VentanaPrincipal ventana;

    public subProcesoCliente(Socket cliente, VentanaPrincipal v) {

        this.cliente = cliente;
        ip = cliente.getInetAddress().getHostAddress();
        ventana = v;
    }

    @Override
    public void run() {
        try {
            calculoImc.Imc imc = calcularImc();
            enviarRespuesta(imc);
        } catch (Exception ex) {
            System.out.println(log() + ex.getMessage());
            ventana.getCajaLog().append(log() + ex.getMessage() + "\n");
            try {
                cliente.close();
            } catch (IOException ex1) {
                ServidorTcp.listaDeClientes.remove(ip);
            } finally {
                ServidorTcp.listaDeClientes.remove(ip);

            }
        }
    }

    public calculoImc.Imc calcularImc() throws Exception {
        DataInputStream input = null;
        try {
            input = new DataInputStream(cliente.getInputStream());
            String msg = "Esperando el peso: ";
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n" + "\n");
            float peso = input.readFloat();
            msg = "peso: " + peso;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            msg = "Esperando La Altura: ";
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            float altura = input.readFloat();
            msg = "ALTURA: " + altura;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            calculoImc datosImc = new calculoImc(peso, altura);
            System.out.println(log() + "IMC: " + datosImc.getImc().resultado);
            msg = "IMC: " + datosImc.getImc().resultado;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            System.out.println(log() + "MENSAJE: " + datosImc.getImc().mensaje);
            msg = "MENSAJE: " + datosImc.getImc().mensaje;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            return datosImc.getImc();

        } catch (Exception e) {
            String msg = "Error al caputurar datos del cliente " + ip;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            throw new Exception("Error al caputurar datos del cliente " + ip);

        }
    }

    public void enviarRespuesta(calculoImc.Imc imc) {

        Thread hiloResponde;
        hiloResponde = new Thread() {
            @Override
            public void run() {
                DataOutputStream output = null;
                try {
                    output = new DataOutputStream(cliente.getOutputStream());
                    output.writeFloat(imc.resultado);
                    output.writeUTF(imc.mensaje);
                    String msg = "IMC: " + imc.resultado;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    msg = "MENSAJE: " + imc.mensaje;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    output.flush();
                    enviarRespuesta(calcularImc());
                } catch (IOException e) {
                    String msg = "Error al enviar datos al cliente " + ip;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    ServidorTcp.listaDeClientes.remove(ip);
                } catch (Exception ex) {
                    String msg = "Error al enviar datos al cliente " + ip;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    try {
                        cliente.close();
                    } catch (IOException ex1) {
                        ServidorTcp.listaDeClientes.remove(ip);
                    } finally {
                        ServidorTcp.listaDeClientes.remove(ip);

                    }
                }
            }
        };
        hiloResponde.start();
    }

    public Socket getCliente() {
        return cliente;
    }

    public void setCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public String log() {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        return ip + " -> " + f.format(new Date()) + " - ";
    }

}

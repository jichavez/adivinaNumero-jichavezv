package cloudComputing.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket socketServidor;
	    Socket socketServicio = null;
	    int port = 8989;

	    try {
	      socketServidor = new ServerSocket(port);
	      do {
	        socketServicio = socketServidor.accept();
	        Procesador procesador = new Procesador(socketServicio);
	        procesador.start();

	      } while (true);

	    } catch (IOException e) {
	      System.err.println("Error al escuchar en el puerto " + port);
	    }
	}

}

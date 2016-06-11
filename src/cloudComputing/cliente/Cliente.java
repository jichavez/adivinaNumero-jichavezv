package cloudComputing.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final int IS_SMALLER = 0;
		final int IS_GREATER = 1;
		final int IS_EQUAL = 2;

		String host = "82.223.79.233";
		int port = 8989;

		Socket socketServicio = null;

		Random random = new Random();
		final int MAX_NUMBER = 100;

		int myThougtNumber = random.nextInt(MAX_NUMBER);
		System.out.println("Mi numero es: " + myThougtNumber);

		try {

			socketServicio = new Socket(host, port);

			PrintWriter outPrinter = new PrintWriter(
					socketServicio.getOutputStream(), true);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));

			System.out.println("ncual es el numero del servidor entre 0 y " + MAX_NUMBER);
			Scanner readInt = new Scanner(System.in);
			int myGuessedNumber = readInt.nextInt();

			outPrinter.println(Integer.toString(-1) + " " + myGuessedNumber);

			boolean noWinner = true;

			while (noWinner) {

				String serverResponseCodeAndGuessedNumber = inReader.readLine();

				Scanner parseResponse = new Scanner(
						serverResponseCodeAndGuessedNumber);

				int responseCode = parseResponse.nextInt();
				int serverGuessed = parseResponse.nextInt();

				parseResponse.close();

				if (responseCode == IS_EQUAL) {
					System.out.println("+ Servidor: Mi número es igual, has ganado, adios");
					noWinner = false;
				} else if (responseCode == IS_GREATER) {
					System.out.println("+ Servidor: Mi número es mayor, introduce otro:");
				} else {
					System.out.println("+ Servidor: Mi número es menor, introduce otro:");
				}

				if (noWinner)
					System.out.println("+ Servidor dice " + serverGuessed + ", y el mio es "
							+ myThougtNumber);

				String clientResponseCode;
				if (serverGuessed == myThougtNumber) {
					clientResponseCode = Integer.toString(IS_EQUAL);
					System.out.println("Ha ganado el servidor! bye");
					noWinner = false;
				} else if (serverGuessed > myThougtNumber) {
					clientResponseCode = Integer.toString(IS_SMALLER);
				} else {
					clientResponseCode = Integer.toString(IS_GREATER);
				}

				// Ask the client for a new number
				if (noWinner)
					myGuessedNumber = readInt.nextInt();
				outPrinter.println(clientResponseCode + " " + myGuessedNumber);
			}

			// socketServicio.close();
			readInt.close();

			System.out.println("Hasta pronto!!");

		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}

}

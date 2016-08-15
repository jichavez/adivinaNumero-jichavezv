package cloudComputing.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Procesador extends Thread {
	private Socket mServiceSock;

	private Random mRandom;
	private int mThougtNumber;
	private int mMyGuessedNumber;

	private final int MAX_NUMBER = 100;
	private final int IS_SMALLER = 0;
	private final int IS_GREATER = 1;
	private final int IS_EQUAL = 2;

	public Procesador(Socket valorSocket) {
		// TODO Auto-generated constructor stub
		this.mServiceSock = valorSocket;
		mRandom = new Random();
		mThougtNumber = mRandom.nextInt(MAX_NUMBER);
		System.out.println("Numero del Servidor: " + mThougtNumber + " para el jugador " + valorSocket);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			PrintWriter outPrinter = new PrintWriter(
					mServiceSock.getOutputStream(), true);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(
					mServiceSock.getInputStream()));

			boolean noWinner = true;

			while (noWinner) {

				// Get the answer for my guessed number and his guessed number
				// from the client
				String clientResponseCodeAndGuessedNumber = inReader.readLine();

				Scanner parseResponse = new Scanner(
						clientResponseCodeAndGuessedNumber);

				int responseCode = parseResponse.nextInt();
				int clientGuessed = parseResponse.nextInt();
				parseResponse.close();

				// Hint for the client
				String serverResponseCode;
				if (clientGuessed == mThougtNumber) {
					serverResponseCode = Integer.toString(IS_EQUAL);
					noWinner = false;
				} else if (clientGuessed > mThougtNumber) {
					serverResponseCode = Integer.toString(IS_SMALLER);
				} else {
					serverResponseCode = Integer.toString(IS_GREATER);
				}

				// See if I won
				if (responseCode == IS_EQUAL) {
					outPrinter.println(serverResponseCode + " " + -1);
					noWinner = false;
				} else if (responseCode == IS_GREATER) {
					mMyGuessedNumber = randInt(mMyGuessedNumber + 1, MAX_NUMBER);
				} else if (responseCode == IS_SMALLER) {
					mMyGuessedNumber = randInt(0, mMyGuessedNumber - 1);
				} else { // First connection, the server has not send any number
					mMyGuessedNumber = mRandom.nextInt(MAX_NUMBER);
				}
				outPrinter.println(serverResponseCode + " " + mMyGuessedNumber);
			}

			System.out.println("Closing connection for " + mServiceSock);
			mServiceSock.close();

		} catch (IOException e) {
			System.err.println("Error al obtener los flujso de entrada/salida.");
		}
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 * @author http://stackoverflow.com/a/363692/1612432
	 */
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}

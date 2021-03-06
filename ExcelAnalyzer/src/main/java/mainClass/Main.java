package mainClass;

import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import controller.Controller;

public class Main {

	public static void main(String[] args) {

		try {
			Controller controller = new Controller();
			controller.analyze();

			System.out.println("Færdig");
		} catch (EncryptedDocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Enable the user to see the log.
		Scanner scanner = new Scanner(System.in);
		System.out.println("Press enter to close");
		scanner.nextLine();
		scanner.close();

	}

}

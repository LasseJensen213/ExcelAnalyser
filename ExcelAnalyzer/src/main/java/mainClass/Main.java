package mainClass;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import controller.Controller;
import excel.ExcelInputController;
import exceptions.UnknownSettingsVariableNameException;

public class Main {

	public static void main(String[] args) {
		
		
		
		try {
			ExcelInputController excelInputController = new ExcelInputController();
			excelInputController.readControlDocoument();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownSettingsVariableNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			Controller controller = new Controller();
			controller.analyze();
		
			System.out.println("Done");
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
		
	}
	

}

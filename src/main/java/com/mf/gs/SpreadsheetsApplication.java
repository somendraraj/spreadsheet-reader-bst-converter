package com.mf.gs;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mf.gs.reader.SpreadSheetReaderAndBSTConverter;

@SpringBootApplication
@ComponentScan
public class SpreadsheetsApplication {

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		SpringApplication.run(SpreadsheetsApplication.class, args);

		SpreadSheetReaderAndBSTConverter reader = new SpreadSheetReaderAndBSTConverter();

		System.out.println("Starting app");

		reader.startSpreadSheetsReader();

		System.out.println("\nApp Ends...");

		/**
		 * Output: Spread sheet result numbers: [12, 11, 14, 44, 104, 12, 12, 14, 12, 28, 84, 52, 46, 26, 102, 44, 12, 22, 21, 362, 33, 13, 432, 99, 12, 12, 44, 22, 22, 23, 24] 
		 * 
		 * Decending order BST: 432, 362, 104, 102, 99, 84, 52, 46, 44, 44, 44, 33, 28, 26, 24, 23, 22, 22, 22, 21, 14, 14, 13, 12, 12, 12, 12, 12, 12, 12, 11, App Ends...
		 * 
		 */
	}

}

package com.mf.gs.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SpreadSheetReaderAndBSTConverter {

	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = SpreadSheetReaderAndBSTConverter.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	public void startSpreadSheetsReader() throws IOException, GeneralSecurityException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		// final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
		final String spreadsheetId = "1er_hWK1iarUIyc5GQ4-Qzp8B6K94AhkDrz-9QFgB0Tw";

		final String range = "input_01";
		Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();

		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
		List<List<Object>> values = response.getValues();
		List<Integer> resultNumber = new ArrayList();
		if (values == null || values.isEmpty()) {
			System.out.println("Data doesn't exits...");
		} else {
			for (int i = 0; i < values.size(); i++) {
				List<Object> numberList = values.get(i);
				// System.out.println(i + "th list: " + numberList);
				if (!numberList.isEmpty()) {
					for (int j = 0; j < numberList.size(); j++) {
						if (numberList.get(j) != null && numberList.get(j) != "") {
							Integer val = Integer.parseInt((String) numberList.get(j));
							resultNumber.add(val);
						}

					}
				}

			}

			System.out.println("Spread sheet result numbers: " + resultNumber);
			SpreadSheetReaderAndBSTConverter tree = new SpreadSheetReaderAndBSTConverter();
			for (int i = 0; i < resultNumber.size(); i++) {
				tree.insert(resultNumber.get(i));
			}
			System.out.println("Decending order BST: ");
			printDecendingBST(root);

		}
	}

	static Node root;

	private void insert(int key) {
		root = createDecendingBST(root, key);
	}

	class Node {
		Node left, right;
		int data;

		Node(int data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}
	}

	private Node createDecendingBST(Node node, int key) {
		if (node == null) {
			return new Node(key);
		}

		if (key > node.data) {
			node.left = createDecendingBST(node.left, key);
		} else if (key <= node.data) {
			node.right = createDecendingBST(node.right, key);
		}

		return node;
	}

	private void printDecendingBST(Node root) {
		if (root != null) {
			printDecendingBST(root.left);
			System.out.print(root.data + ", ");
			printDecendingBST(root.right);
		}
	}

}

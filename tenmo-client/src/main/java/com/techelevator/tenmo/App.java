package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.ApiService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;
import io.cucumber.java.sl.In;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.sql.SQLOutput;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private ApiService apiService;
	private TransferService transferService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),new TransferService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub

		ApiService apiService = new ApiService(API_BASE_URL, currentUser);
		try {
			apiService.getBalance();
		} catch (NullPointerException e) {
			System.out.println("No balance");
		}
	}

// Was working before, now only printing out one transfer. SQL statement in DAO gets all transfers
	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		Transfer[] arraylist;
		String username = currentUser.getUser().getUsername();
	try{
		arraylist = transferService.viewMyTransfer(currentUser.getUser().getId());
		for (Transfer transfer: arraylist){
			System.out.println("From:" + "   " + "To:" + "   " + "Amount:");
			System.out.println(username + "   " + transfer.getAccountTo() + "   " + transfer.getAmount());
		}
	}catch (RestClientException rce){
		System.out.println("Bad path");
	}

	String input = console.getUserInput("Please enter transfer ID to view details (0 to cancel)");
	Integer id = Integer.parseInt(input);
	transferService.getTransferDetails(id);
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		TransferService transferService = new TransferService(API_BASE_URL);
		showAllUsers();
		String input = console.getUserInput("Please select the ID you would like to transfer funds to (0 to cancel): ");
		if (input.equals(currentUser.getUser().getId().toString())){
			System.out.println("Invalid transfer. Can't send money to self.");
			return;
		}
		String amount = console.getUserInput("Please select amount you would like to transfer: $");
		Integer toId = Integer.parseInt(input);
		Double doubleAmount = Double.parseDouble(amount);
		try {
			Transfer transfer = new Transfer();
			transfer.setAccountFrom(currentUser.getUser().getId());
			transfer.setAccountTo(toId);
			transfer.setAmount(doubleAmount);
			transfer.setTransferStatusDesc("Approved");

			transferService.sendBucks(currentUser,transfer);
			System.out.println("Transfer completed.");
		} catch(RestClientException e) {
			e.getMessage();
		}

	}

	//		try{
//		TransferService transferService = new TransferService(API_BASE_URL, currentUser);
//		transferService.sendBucks();
//	} catch(ResourceAccessException e){
//			System.out.println(e.getMessage());
//		}
//	}
	private void requestBucks() {
		// TODO Auto-generated method stub

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) //will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

	private User[] showAllUsers() {
		TransferService transferService = new TransferService(API_BASE_URL);
		User[] users = transferService.getAllUsers();
		System.out.println("Username | ID");
		System.out.println("-------------");
		for (User u : users) {
			if (!u.getId().equals(currentUser.getUser().getId())) {
				System.out.println(u.getUsername() + " " + u.getId());
			}
		}
		return users;
	}
}

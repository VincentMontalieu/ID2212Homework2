package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.StringTokenizer;

import marketplace.Item;
import marketplace.ItemImpl;
import marketplace.MarketPlace;
import marketplace.Wish;
import marketplace.WishImpl;
import bankrmi.Account;
import bankrmi.Bank;
import bankrmi.RejectedException;

public class SuperClient {
	private static final String DEFAULT_BANK_NAME = "Nordea";
	private static final String DEFAULT_MARKET_NAME = "Agora";
	Account account;
	Bank bankobj;
	MarketPlace marketobj;
	private String bankname, marketname;
	String clientname, displayedHostName;

	static enum CommandName {
		newAccount, getAccount, deleteAccount, deposit, withdraw, balance, quit, help, list, newTrader, deleteTrader, buy, sell, displayItems, wish;
	};

	public SuperClient(String bankName, String marketName) {
		this.bankname = bankName;
		this.marketname = marketName;
		this.displayedHostName = bankname;
		try {
			try {
				LocateRegistry.getRegistry(1099).list();
			} catch (RemoteException e) {
				LocateRegistry.createRegistry(1099);
			}
			bankobj = (Bank) Naming.lookup(bankname);
			marketobj = (MarketPlace) Naming.lookup(marketname);
		} catch (Exception e) {
			System.out.println("The runtime failed: " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Connected to bank: " + bankname);
		System.out.println("Connected to market: " + marketname);
	}

	public SuperClient() {
		this(DEFAULT_BANK_NAME, DEFAULT_MARKET_NAME);
	}

	public void run() {
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(
				System.in));
		
		System.out.println();
		
		while (true) {
			System.out.print(clientname + "@" + displayedHostName + ">");
			try {
				String userInput = consoleIn.readLine();
				execute(parse(userInput));
			} catch (RejectedException re) {
				System.out.println(re.toString());
			} catch (IOException e) {
				System.out.println(e.toString());
			} catch (marketplace.exception.RejectedException e) {
				System.out.println(e.toString());
			}
		}
	}

	public void notifySale(Item item) {
		System.out.println("\n\nYour item: [ " + item + " ] was purchased!\n");
	}

	public void notifyWish(Wish wish) throws RemoteException {
		System.out.println("\n\nThe item: [ " + wish.getItem()
				+ " ] is now available!\n");
	}

	private Command parse(String userInput) {
		if (userInput == null) {
			return null;
		}

		StringTokenizer tokenizer = new StringTokenizer(userInput);
		if (tokenizer.countTokens() == 0) {
			return null;
		}

		CommandName commandName = null;
		String userName = null;
		String sellerName = null;
		float amount = 0;
		int userInputTokenNo = 1;

		while (tokenizer.hasMoreTokens()) {
			switch (userInputTokenNo) {
			case 1:
				try {
					String commandNameString = tokenizer.nextToken();
					commandName = CommandName.valueOf(CommandName.class,
							commandNameString);
				} catch (IllegalArgumentException commandDoesNotExist) {
					System.out.println("Illegal command");
					return null;
				}
				break;
			case 2:
				userName = tokenizer.nextToken();
				break;
			case 3:
				try {
					amount = Float.parseFloat(tokenizer.nextToken());
				} catch (NumberFormatException e) {
					System.out.println("Illegal amount");
					return null;
				}
				break;
			case 4:
				sellerName = tokenizer.nextToken();
				break;
			default:
				System.out.println("Illegal command");
				return null;
			}
			userInputTokenNo++;
		}
		return new Command(commandName, userName, amount, sellerName);
	}

	private boolean isBankCommand(CommandName input) {
		return input == CommandName.newAccount
				|| input == CommandName.getAccount
				|| input == CommandName.deleteAccount
				|| input == CommandName.deposit
				|| input == CommandName.withdraw
				|| input == CommandName.balance || input == CommandName.list;
	}

	private boolean isMarketCommand(CommandName input) {
		return input == CommandName.newTrader
				|| input == CommandName.deleteTrader
				|| input == CommandName.buy || input == CommandName.sell
				|| input == CommandName.displayItems
				|| input == CommandName.wish;
	}

	void execute(Command command) throws RemoteException, RejectedException,
			MalformedURLException, marketplace.exception.RejectedException {

		if (command == null) {
			return;
		}

		CommandName inputCommand = command.getCommandName();
		String userOrItemName = command.getUserOrItemName();

		// When trying to call a Market command from the bank account
		if (isMarketCommand(inputCommand) && displayedHostName.equals(bankname)
				&& inputCommand != CommandName.newTrader) {
			System.out.println("Please switch to your trading account.");
		}

		// When trying to call a Bank command from the market account
		else if (isBankCommand(inputCommand)
				&& displayedHostName.equals(marketname)
				&& inputCommand != CommandName.newAccount) {
			System.out.println("Please switch to your bank account.");
		}

		// Account creation
		else if (inputCommand == CommandName.newAccount) {
			clientname = userOrItemName;
			displayedHostName = bankname;
			bankobj.newAccount(userOrItemName);
		}

		// Trader creation
		else if (inputCommand == CommandName.newTrader
				&& bankobj.getAccount(userOrItemName) == null) {
			System.out.println("Please create a bank account first.");
		}

		// Trader creation
		else if (inputCommand == CommandName.newTrader) {
			clientname = userOrItemName;
			displayedHostName = marketname;
			marketobj.registerClient(new TraderImpl(userOrItemName, this));
		}

		// General commands
		else if (inputCommand == CommandName.help) {
			for (CommandName commandName : CommandName.values()) {
				System.out.println(commandName);
			}
		}

		else if (inputCommand == CommandName.quit) {
			System.exit(0);
		}

		// The command is a bank command and we have already created an account
		else if (isBankCommand(inputCommand)
				&& displayedHostName.equals(bankname)) {
			doBankCommand(command);
		}

		// The command is a market command and we have already created an trader
		else if (isMarketCommand(inputCommand)
				&& displayedHostName.equals(marketname)) {
			doMarketCommand(command);
		}

		else {
			System.out.println("Illegal command.");
		}
	}

	private void doMarketCommand(Command command) throws RemoteException,
			MalformedURLException, marketplace.exception.RejectedException,
			RejectedException {
		CommandName inputCommand = command.getCommandName();
		String userOrItemName = command.getUserOrItemName();
		Float amount = command.getAmount();
		String sellerName = command.getSellerName();

		switch (inputCommand) {
		case buy:
			marketobj.buyItem(new ItemImpl(userOrItemName, amount,
					new TraderImpl(sellerName, this)), new TraderImpl(
					clientname, this));
			break;
		case deleteTrader:
			marketobj.unRegisterClient(new TraderImpl(userOrItemName, this));
			clientname = null;
			break;
		case displayItems:
			System.out.println();
			for (Item item : marketobj.getItemsOnSale()) {
				System.out.println(item);
			}
			System.out.println();
			break;
		case sell:
			marketobj.placeItemOnSale(new ItemImpl(userOrItemName, amount,
					new TraderImpl(clientname, this)));
			break;
		case wish:
			marketobj.placeWish(new WishImpl(new ItemImpl(userOrItemName,
					amount, new TraderImpl("*WISH*", this)), new TraderImpl(
					clientname, this)));
			break;
		default:
			System.out.println("Illegal command.");
			break;
		}
	}

	private void doBankCommand(Command command) throws RemoteException,
			RejectedException {
		CommandName inputCommand = command.getCommandName();
		String userOrItemName = command.getUserOrItemName();
		Float amount = command.getAmount();
		account = bankobj.getAccount(clientname);

		switch (inputCommand) {
		case balance:
			System.out.println("balance: $" + account.getBalance());
			break;
		case deleteAccount:
			clientname = null;
			bankobj.deleteAccount(userOrItemName);
			break;
		case deposit:
			account.deposit(amount);
			break;
		case getAccount:
			System.out.println(account);
			break;
		case withdraw:
			account.withdraw(amount);
			break;
		default:
			System.out.println("Illegal command.");
			break;
		}
	}

	private class Command {
		private String userOrItemName, sellerName;
		private float amount;
		private CommandName commandName;

		private String getUserOrItemName() {
			return userOrItemName;
		}

		private String getSellerName() {
			return sellerName;
		}

		private float getAmount() {
			return amount;
		}

		private CommandName getCommandName() {
			return commandName;
		}

		private Command(SuperClient.CommandName commandName,
				String userOrItemName, float amount, String sellerName) {
			this.commandName = commandName;
			this.userOrItemName = userOrItemName;
			this.amount = amount;
			this.sellerName = sellerName;
		}
	}

	public static void main(String[] args) {
		new SuperClient().run();
	}
}

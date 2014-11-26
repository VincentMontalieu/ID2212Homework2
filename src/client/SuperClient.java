package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.StringTokenizer;

import marketplace.ItemImpl;
import marketplace.MarketPlace;
import marketplace.WishImpl;
import bankrmi.Account;
import bankrmi.Bank;
import bankrmi.RejectedException;

public class SuperClient {
	private static final String DEFAULT_BANK_NAME = "Nordea";
	private static final String DEFAULT_MARKET_NAME = "Agora";
	Account account;
	Trader trader;
	Bank bankobj;
	MarketPlace marketobj;
	private String bankname, marketname;
	String clientname, itemname;

	static enum CommandName {
		newAccount, getAccount, deleteAccount, deposit, withdraw, balance, quit, help, list, newTrader, getTrader, deleteTrader, buy, sell, displayItems, wish;
	};

	public SuperClient(String bankName, String marketName) {
		this.bankname = bankName;
		this.marketname = marketName;
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

		while (true) {
			System.out.print(clientname + "@" + bankname + "/" + marketname
					+ ">");
			try {
				String userInput = consoleIn.readLine();
				execute(parse(userInput));
			} catch (RejectedException re) {
				System.out.println(re);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	@SuppressWarnings("incomplete-switch")
	void execute(Command command) throws RemoteException, RejectedException {
		if (command == null) {
			return;
		}

		switch (command.getCommandName()) {
		case list:
			try {
				for (String accountHolder : bankobj.listAccounts()) {
					System.out.println(accountHolder);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			return;
		case displayItems:
			try {
				for (String item : marketobj.getItemsOnSale()) {
					System.out.println(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			return;
		case quit:
			System.exit(0);
		case help:
			for (CommandName commandName : CommandName.values()) {
				System.out.println(commandName);
			}
			return;
		}

		// all further commands require a name to be specified
		String userName = command.getUserName();
		if (userName == null) {
			userName = clientname;
		}

		if (userName == null) {
			System.out.println("name is not specified");
			return;
		}

		switch (command.getCommandName()) {
		case newAccount:
			clientname = userName;
			bankobj.newAccount(userName);
			return;
		case deleteAccount:
			clientname = userName;
			bankobj.deleteAccount(userName);
			return;
		case newTrader:
			clientname = userName;
			try {
				marketobj.registerClient(new TraderImpl(userName));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (marketplace.exception.RejectedException e) {
				e.printStackTrace();
			}
			return;
		case deleteTrader:
			clientname = userName;
			try {
				marketobj.unRegisterClient(new TraderImpl(userName));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (marketplace.exception.RejectedException e) {
				e.printStackTrace();
			}
			return;
		}

		// all further commands require a Account reference
		Account acc = bankobj.getAccount(userName);
		Trader trd = marketobj.getTrader(userName);
		if (acc == null) {
			System.out.println("No account for " + userName);
			return;
		}
		if (trd == null) {
			System.out.println("No trader for " + userName);
			return;
		} else {
			account = acc;
			trader = trd;
			itemname = userName;
		}

		switch (command.getCommandName()) {
		case getAccount:
			System.out.println(account);
			break;
		case getTrader:
			System.out.println(trader);
		case deposit:
			account.deposit(command.getAmount());
			break;
		case withdraw:
			account.withdraw(command.getAmount());
			break;
		case balance:
			System.out.println("balance: $" + account.getBalance());
			break;
		case buy:
			try {
				marketobj.buyItem(new ItemImpl(itemname, command.getAmount(),
						new TraderImpl(command.getSellerName())),
						new TraderImpl(userName));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (marketplace.exception.RejectedException e) {
				e.printStackTrace();
			}
			break;
		case sell:
			try {
				marketobj.placeItemOnSale(new ItemImpl(itemname, command
						.getAmount(), new TraderImpl(userName)));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (marketplace.exception.RejectedException e) {
				e.printStackTrace();
			}
			break;
		case wish:
			try {
				marketobj.placeWish(new WishImpl(new ItemImpl(itemname, command
						.getAmount(), new TraderImpl(command.getSellerName())),
						new TraderImpl(userName)));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (marketplace.exception.RejectedException e) {
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("Illegal command");
		}
	}

	private class Command {
		private String userOrItemName, sellerName;
		private float amount;
		private CommandName commandName;

		private String getUserName() {
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

		private Command(SuperClient.CommandName commandName, String userName,
				float amount, String sellerName) {
			this.commandName = commandName;
			this.userOrItemName = userName;
			this.amount = amount;
			this.sellerName = sellerName;
		}
	}

	public static void main(String[] args) {
		new SuperClient().run();
	}
}

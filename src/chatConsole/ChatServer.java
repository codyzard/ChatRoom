package chatConsole;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	private int port;
	private Set<String> userNames = new HashSet<>();
	private Set<UserThread> userThreads = new HashSet<>();

	public ChatServer(int port) {
		this.port = port;
	}

	public void execute() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("Chat Server is listening on port " + port);

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("New user connected");

				UserThread newUser = new UserThread(socket, this);
				userThreads.add(newUser);
				newUser.start();

			}

		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	} 

	public static void main(String[] args) {
		int port = 5000;
		ChatServer server = new ChatServer(port);
		server.execute();
	}

	// send mess everyone 
	void broadcast(String message, UserThread excludeUser) {
		for (UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				aUser.sendMessage(message);
			}
		}
	}

	// store userName
	void addUserName(String userName) {
		userNames.add(userName);
	}

	// disconnected, remove user from UserThread and userName
	void removeUser(String userName, UserThread aUser) {
		boolean removed = userNames.remove(userName);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("The user " + userName + " quitted");
		}
	}

	Set<String> getUserNames() {
		return this.userNames;
	}

	// check has Server users ?
	boolean hasUsers() {
		return !this.userNames.isEmpty();
	}
}
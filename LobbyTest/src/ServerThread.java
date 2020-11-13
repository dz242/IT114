import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket client;
	private ObjectInputStream in;// from client
	private ObjectOutputStream out;// to client
	private boolean isRunning = false;
	private SocketServer server;// ref to our server so we can call methods on it
	// more easily

	public ServerThread(Socket myClient, SocketServer server) throws IOException {
		this.client = myClient;
		this.server = server;
		isRunning = true;
		out = new ObjectOutputStream(client.getOutputStream());
		in = new ObjectInputStream(client.getInputStream());
	}

	public boolean send(String message) {
		// added a boolean so we can see if the send was successful
		try {
			out.writeObject(message);
			return true;
		} catch (IOException e) {
			Debug.log("Error sending message to client");
			e.printStackTrace();
			cleanup();
			return false;
		}
	}

	@Override
	public void run() {
		try {
			String fromClient;
			while (isRunning && !client.isClosed() && (fromClient = (String) in.readObject()) != null) {// open while
																										// loop
				// TODO make it cooler
				Debug.log("Received from client: " + fromClient);
				server.broadcast(fromClient, this.getId());
			} // close while loop
		} catch (Exception e) {
			e.printStackTrace();
			Debug.log("Terminating Client");
		} finally {
			// TODO
			Debug.log("Server Cleanup");
			cleanup();
		}
	}

	private void cleanup() {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				Debug.log("Input already closed");
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				Debug.log("Client already closed");
			}
		}
		if (client != null && !client.isClosed()) {
			try {
				client.shutdownInput();
			} catch (IOException e) {
				Debug.log("Socket/Input already closed");
			}
			try {
				client.shutdownOutput();
			} catch (IOException e) {
				Debug.log("Socket/Output already closed");
			}
			try {
				client.close();
			} catch (IOException e) {
				Debug.log("Client already closed");
			}
		}
	}
}
/**
* This program written by Michael Giansiracusa
* for COSC 345-001 taught by Dr. Farag
* This program implements a simple web server
* last edit 12/6/2016
*/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
	public static void main(String argv[]) {
		// create package access variables
		String clientRequest;
		ServerSocket welcomeSocket;
		int port = 4242;
		
		// large try that encompases setting the server socket port 
		// catches an IO Exception
		try {
			welcomeSocket = new ServerSocket(port);
			System.out.println("Server started at port: " + port);
			while (true) {
				System.out.println("Waiting for connection...");
				Socket connectionSocket = welcomeSocket.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				System.out.println("in and out data stream started.\n"
						+ "Getting request from browser...");
				
				clientRequest = inFromClient.readLine();
				System.out.println(clientRequest);
				String[] tokens = clientRequest.split(" ");
                                String file = tokens[1].substring(1);
				System.out.println("Page requested is: " + file);
				try {
					System.out.println(tokens[1].substring(1));
					if(tokens[0].equalsIgnoreCase("GET") && file.length() <= 1) {
						outToClient.writeBytes("Welcome to this very basic web server."
			                       + " This server was developed by Michael Giansiracusa");
						System.out.println("Data streams closed. Make another request.");
					}
					else {
                                            try {
                                                // create file input stream
                                                InputStream inFile = new FileInputStream(file);
                                                // Send file to client
                                                byte[] a=new byte[4096];
                                                int n;
                                                while ((n=inFile.read(a))>0)
                                                  outToClient.write(a, 0, n);
                                                
                                            } catch(FileNotFoundException fnfe) {
                                                outToClient.writeBytes("404: File Not Found. " 
                                                        + file + " does not exist or has been moved.");
                                                outToClient.writeBytes(" These are not the droids you are looking for...");
                                                fnfe.printStackTrace();
                                            }
					}
				} catch (Exception e) {
					System.out.println("Every excetion caught!");
				}
				inFromClient.close();
				outToClient.close();
				connectionSocket.close();
			}
		} catch (IOException ioe) {
			System.out.println("This whole program exploded!");
			ioe.printStackTrace();
		}
	}
}

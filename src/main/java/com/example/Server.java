package com.example;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int thePort = 0;
    private String theIPAddress = null;
    private ServerSocket serverSocket =  null;

    //Support for closing the server
    //private boolean keypressedFlag = false;


    //Class constructor
    public Server(){
        //Initialize the TCP socket
        thePort = Credentials.PORT;
        theIPAddress = Credentials.HOST;

        //Initialize the socket and runs the service loop
        System.out.println("Server: Initializing server socket at " + theIPAddress + " with listening port " + thePort);
        System.out.println("Server: Exit server application by pressing Ctrl+C (Windows or Linux) or Opt-Cmd-Shift-Esc (Mac OSX)." );
        try {
            //Initialize the socket

            //TO BE COMPLETED
            H2Setup h2Setup = new H2Setup();
            h2Setup.initialise();
            int maxConnectionQueue = 3;
            serverSocket = new ServerSocket(thePort, maxConnectionQueue, InetAddress.getByName(theIPAddress));
            //serverSocket = new ServerSocket(thePort);

            System.out.println("Server: Server at " + theIPAddress + " is listening on port : " + thePort);
        } catch (Exception e){
            //The creation of the server socket can cause several exceptions;
            //See https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
            System.out.println(e);
            System.exit(1);
        }
    }




    //Runs the service loop
    public void executeServiceLoop()
    {
        System.out.println("Server: Start service loop.");
        try {
            //Service loop
            while (true) {

                //TO BE COMPLETED

                Socket aSocket = this.serverSocket.accept();
                Service tempServiceThread = new Service(aSocket); //do I need to start this thread?

            }
        } catch (Exception e){
            //The creation of the server socket can cause several exceptions;
            //See https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
            System.out.println(e);
        }
        System.out.println("Server: Finished service loop.");
    }


/*
	@Override
	protected void finalize() {
		//If this server has to be killed by the launcher with destroyForcibly
		//make sure we also kill the service threads.
		System.exit(0);
	}
*/

    public static void main(String[] args){
        //Run the server
        Server server=new Server(); //inc. Initializing the socket
        server.executeServiceLoop();
        System.out.println("Server: Finished.");
        System.exit(0);
    }

}

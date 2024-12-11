package com.example;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int thePort = 0;
    private String theIPAddress = null;
    private ServerSocket serverSocket =  null;

    //Class constructor
    public Server(){
        //Initialize the TCP socket
        thePort = Credentials.PORT;
        theIPAddress = Credentials.HOST;

        //Initialize the socket and runs the service loop
        System.out.println("Server: Initializing server socket at " + theIPAddress + " with listening port " + thePort);
        System.out.println("Server: Exit server application by pressing Ctrl+C (Windows or Linux) or Opt-Cmd-Shift-Esc (Mac OSX)." );
        try {
            H2Setup h2Setup = new H2Setup();
            h2Setup.initialise();
            int maxConnectionQueue = 3;
            serverSocket = new ServerSocket(thePort, maxConnectionQueue, InetAddress.getByName(theIPAddress));

            System.out.println("Server: Server at " + theIPAddress + " is listening on port : " + thePort);
        } catch (Exception e){
            System.out.println(e);
            System.exit(1);
        }
    }

    //Runs the service loop
    public void executeServiceLoop()
    {
        System.out.println("Server: Start service loop");
        try {
            while (true) {
                Socket aSocket = this.serverSocket.accept();
                Service tempServiceThread = new Service(aSocket); //do I need to start this thread?
            }
        } catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Server: Finished service loop.");
    }

    public static void main(String[] args){
        //Run the server
        Server server=new Server();
        server.executeServiceLoop();
        System.out.println("Server: Finished.");
        System.exit(0);
    }

}

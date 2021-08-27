package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static final int portNumber = 4444;

    private int serverPort;
    private static ClientThread clientThread;
    protected static List<ClientThread> clients;

    /**
     * Metodo principal, crea el servidor, lo incia e inicia el clientThread
     */
    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
        clientThread.run();
        
    }

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients(){
        return clients;
    }

    /**
     * Crea el socket para conectarse con el cliente y acepta al cliente
     */
    private void startServer(){
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    /**
     * Crea un nuevo socket y thread del cliente, lo inicia y anade al cliente a la lista de clientes
     * @param serverSocket el socket del servidor
     */
    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
}
package Client;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private Socket socket;
    private String userName;
    private boolean isAlived;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    /**
     * Crea un String de los mensajes para enviar
     * @param socket el socket creado cuando se inicia el cliente
     * @param userName el nomre de usuario del cliente que envia el mensaje
     */
    public ServerThread(Socket socket, String userName){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }

    /**
     * Sincroniza los mensajes por enviar, cambia e valor de hasMessages a true y anade el nuevo mensaje a la lista de mensajes por enviar.
     * @param message el string del mensaje que se va a enviar
     */
    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    /**
     * Metodo run de una clase runnable, se recorre constantemente, imprime el mensaje cuando hay uno nuevo.  
     */
    public void run(){
        System.out.println("Bienvenid@ :" + userName);

        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());
        System.out.println("Escriba 'MONTO' para ingresar los datos del producto ");

        try{
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend = "";
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.println(userName + " > " + nextSend);
                    serverOut.flush();
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
    /**
     * Metodo que retorna el monto del producto
     * @param data1 valor del producto
     * @param data2 peso del producto
     * @param data3 porcentaje
     */
    public void Answer(int data1,int data2,int data3){
        double ans;
        ans = (data1*data3/100)+(data2*0.15);
        System.out.println("El monto es:"+ ans);
        
    }
}
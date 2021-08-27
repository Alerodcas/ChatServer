package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String host = "localhost";
    private static final int portNumber = 4444;

    private String userName;
    private String serverHost;
    private int serverPort;
    private Scanner userInputScanner;
    
    /**
     * El método principal toma el nombre de usuario del cliente y crea un nuevo cliente con esta información, y llama el startClient
     */

    public static void main(String[] args){
        String readName = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Por favor digite un nombre de usuario:");
        
        while(readName == null || readName.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            readName = scan.nextLine();
            
            if(readName.trim().equals("")){
                System.out.println("Invalido, intente de nuevo:");
            }
        }

        Client client = new Client(readName, host, portNumber);
        client.startClient(scan);
    }

    /**
     * Crea un cliente cn el nombre de usuario, el host y el numero de puerto
     * @param userName es el nombre de usuario
     * @param host es el anfitrion
     * @param portNumber es el numero de puerto
     */
    private Client(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    /**
     * El metodo crea el socket para conectarse con el servidor, detecta cuanto se envia String o Int en el chat e inicia el proceso correspondiente.
     * @param scan todo valor dado por el cliente 
     */
    private void startClient(Scanner scan){
        int dato1;
        int dato2;
        int dato3;
        String monto = "MONTO";
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); 

            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(scan.hasNextLine()){
                    if(scan.findInLine(monto)==null){
                        serverThread.addNextMessage(scan.nextLine());
                    }
                }
                if(scan.findInLine(monto)!=null){
                    System.out.println("Digite el valor del producto, el peso y el impuesto respectivamente");
                        dato1 = scan.nextInt();
                        dato2 = scan.nextInt();
                        dato3 = scan.nextInt();
                        if(dato3!=0){
                            serverThread.Answer(dato1,dato2,dato3);   
                          }
                    }
            }
        }catch(IOException ex){
            System.err.println("Error de conexion!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupcion");
        }
    }
}
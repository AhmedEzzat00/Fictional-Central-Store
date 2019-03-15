import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Server class is used to interact with a client or clients
 * using pre-defined Operation commands (CAPITAL LETTERS)
 *
 */
public class Server extends Thread{
        public static void main(String[] args) throws IOException {


Thread client_Thread=new Server();
Thread server_Thread=new Server();


            final int PORT_NUMBER = 5020;
            //display the server local IP address to connect
            System.out.println("Server IP-> 192.168.1.3\tPort Number-> " + PORT_NUMBER);
            //check for the necessary arguments

            /*
             Scanner portInput=new Scanner(System.in);

            System.out.print("Port Number-> ");
            int PORT_NUMBER =portInput.nextInt();
           */
//while(true)
            ServerSocket serverSocket=null;
            Socket clientsocket=null;
            PrintWriter out=null;
            BufferedReader in = null;

            InputStream fileInput = null;
            OutputStream fileOutput = null;
            try {

                     serverSocket = new ServerSocket(PORT_NUMBER);
                    //Listening For Incoming Connections
                     clientsocket = serverSocket.accept();

                    //auto flush to auto send the data across IP/TCP channel
                     out = new PrintWriter(clientsocket.getOutputStream(), true);

                    //Asking for UserName



                        out.println("Username");
                        in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
                        String username = in.readLine();
                        //  out.println(username + " CONNECTED");
                System.out.println(username + " CONNECTED");

                out.println("SEND filename.xml to send file ");
                /*        while(true) {
                            out.print("inpute: ");
                            String inputuser=in.readLine();
                        if(inputuser.equals("exit"))
                            break;
                        else if(inputuser.equals("SEND"))
                        {
                            //Call function to receive the file
                            System.out.println(inputuser);
                            continue;
                        }
                        System.out.println(" User Say "+inputuser );
                    }
                  */  //
                try {
                    fileInput = clientsocket.getInputStream();
                } catch (IOException ex) {
                    System.out.println("Can't get socket input stream. ");
                }

                try {
                    fileOutput = new FileOutputStream("E:\\Final Project\\src\\invoice_out.xml");
                } catch (FileNotFoundException ex) {
                    System.out.println("File not found. ");
                }

                byte[] bytes = new byte[16*1024];

                int count;
                while ((count = fileInput.read(bytes)) > 0) {
                    fileOutput.write(bytes, 0, count);
                }

                //
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        out.println(inputLine);
                    }

                  //  out.close();in.close();serverSocket.close();clientsocket.close();
                } catch (IOException e) {
                    System.err.println("Error occur while trying to connect to port " + PORT_NUMBER);
                    System.out.println(e.getMessage());
                }finally {
                in.close();
                out.close();
                serverSocket.close();
                clientsocket.close();

                }

            }

}

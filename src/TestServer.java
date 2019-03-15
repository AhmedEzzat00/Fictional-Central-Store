import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ahmed on 23/06/2017.
 */
public class TestServer extends Thread {
    public static void main(String args[]) throws IOException {


        final int PORT_NUMBER = 8080;

        //display the server local IP address to connect
        System.out.println("Server IP-> 192.168.1.3\tPort Number-> " + PORT_NUMBER);
        //Establish the server socket to the port number
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

        //Initialization
        Socket clientsocket = null;
        OutputStream os = null;
        PrintWriter out = null;
        BufferedReader in = null;
        InputStream fileInput = null;
        OutputStream fileOutput = null;

        try {
            //accept client connection
            clientsocket = serverSocket.accept();

            os = clientsocket.getOutputStream();
            out = new PrintWriter(os, true); //auto flush to auto send the data across IP/TCP channel

            //Asking for UserName
            out.println("UserName: ");

            //Get the Client input
            in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
            String userName = in.readLine();
            System.out.println("Just Connect to: " + userName);

           // out.println( userName+" Connected");

         //   System.out.println("User Says");
            String inputUser;
            while((inputUser=in.readLine())!=null) {
             //   out.println( "--> ");

               // System.out.println("  "+inputUser );

                if(inputUser.equals("exit"))
                    break;
                //check if the userInput contain the SEND request
                else if(inputUser.contains("SEND"))
                {
                    //Extracting the fileName
                 //   String order=inputUser.replaceFirst("SEND", "").trim();
                  //  System.out.println(userName+" Sent "+order);

                    //Writing file
                    try {
                        fileInput = clientsocket.getInputStream();

                        //Create a new file and save to it
                       // File received_invoice=new File ("E:\\Final Project\\server"+order);
                        fileOutput = new FileOutputStream("E:\\Final Project\\server\\invoice_ut.xml");

                        //writing the file
                        byte[] bytes = new byte[16*1024];
                        int count;
                        while ((count = fileInput.read(bytes)) > 0) {
                            fileOutput.write(bytes, 0, count);
                        }
                    } catch (IOException ex) {
                        System.out.println("Can't get socket input stream. ");
                    }
                }
            }
            out.close();
            clientsocket.close();

           // System.out.println("Just Connect to: " + userName);
        } catch (IOException e) {
            System.err.println("Error occur while trying to connect to port " + PORT_NUMBER);
            System.out.println(e.getMessage());
        }
    }
}

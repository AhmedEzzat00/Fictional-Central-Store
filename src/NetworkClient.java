import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetworkClient {

    public static void main(String args[]) throws IOException {


        //Initialization
        Socket localSocket = null;
        String line = null;
        BufferedReader br = null;
        BufferedReader is = null;//Input System
        PrintWriter os = null;//Output System

        //Get the userIP and PortNumber
        Scanner scanner = new Scanner(System.in);

        //The HostName is the IP Number of the connected device (client)
        System.out.print("IP Address-> ");
        final String IP_Address = scanner.nextLine();

        System.out.print("Port Number-> ");
        final int portNumber = scanner.nextInt();

        try {
            //Activating Socket
            localSocket = new Socket(IP_Address, portNumber);

            //Get the Streams ON
            br = new BufferedReader(new InputStreamReader(System.in));
            is = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
            os = new PrintWriter(localSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : " + IP_Address);
        System.out.println("Enter Data to echo Server");

        String response = null;
        try {
            //getting the UserName
            System.out.print(is.readLine());
            String userInput = br.readLine();
            os.println(userInput);

            //Establish the connection
            while ((line = br.readLine()) != null) {

                if (line.contains("SEND")) {
                    //Send to the server to request to sending file
                    String order = line.replaceFirst("SEND", "").trim();
                    System.out.print(order + "\n");

                    //Creating file and getPath
                    File directory = new File(order);
                    File file = new File(directory.getAbsolutePath());

                    byte[] bytes = new byte[16 * 1024];
                    //Get Stream from the file
                    InputStream fileInput = null;
                    OutputStream fileOutput = null;
                    try {
                        fileInput = new FileInputStream(file);
                        os.println("SEND" + order);
                        fileOutput = localSocket.getOutputStream();

                        int count;

                        while ((count = fileInput.read(bytes)) > 0) {
                            fileOutput.write(bytes, 0, count);//from the documentation
                        }

                    } catch (FileNotFoundException e) {
                        System.out.println("File Not Found Check YOUR Typing or File Path");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //Communication test
                /*
                os.println(line);//Send to server
               response = is.readLine();//receive from Server
              System.out.println("Server Response : " + response);
              */
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        } finally {

            is.close();
            os.close();
            br.close();
            localSocket.close();
            System.out.println("Connection Closed");
        }
    }
}
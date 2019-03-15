import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Ahmed on 23/06/2017.
 */
public class TestClient extends Thread {
    public static void main(String args[]) {

        Thread client = new TestClient();
        client.start();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        //The HostName is the IP Number of the connected device (client)
        System.out.print("IP Address-> ");
        final String IP_Address = scanner.nextLine();

        System.out.print("Port Number-> ");
        final int portNumber = scanner.nextInt();

        //Initialization
        Socket localSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedReader userInputBR = null;

        try {
            //establish the socket connection
            localSocket = new Socket(IP_Address, portNumber);
            //get the socket Input/Output Stream
            in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
            out = new PrintWriter(localSocket.getOutputStream(), true);//out in the Client Console

            //Displaying the Server Message (UserName)
            //getting the UserName
            System.out.print(in.readLine());

            userInputBR = new BufferedReader(new InputStreamReader(System.in));
            String userInput = userInputBR.readLine();
            out.println(userInput);
            // System.out.println(in.readLine());

            //Establish a connection with the SERVER
            while ((userInput = userInputBR.readLine()) != null) {
                // System.out.print(in.readLine());

                //out.println(userInput);
                //  System.out.println(in.readLine());
                if (userInput.contains("SEND")) {
                    //Send to the server to request to sending file
                    // out.println(userInput);
                    String order = userInput.replaceFirst("SEND", "").trim();
                    System.out.print(order + "\n");
                  //  readFile(order, localSocket);

                    File directory = new File(order);

                    File file = new File(directory.getAbsolutePath());

                    byte[] bytes = new byte[16 * 1024];

                    //Get Stream from the file
                    InputStream fileInput = null;
                    OutputStream fileOutput = null;
                    try {
                        fileInput = new FileInputStream(file);
                        out.println("SEND");
                        fileOutput = localSocket.getOutputStream();

                        //Read byte by byte
                        int count;
                        while ((count = fileInput.read(bytes)) > 0) {
                            fileOutput.write(bytes, 0, count);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


        } catch (UnknownHostException e) {
            System.err.println("Unknown Host " + IP_Address);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error in I/O fot the connection to " + IP_Address);
            System.exit(1);
        }
    }


    public static void readFile(String userInput, Socket localSocket) {
    }


}
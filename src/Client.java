import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 */
public class Client extends Thread {

    public static void main(String[] args) throws IOException {
        //Check that our program receive the necessary data


        /*
The user can write commands on the client’s terminal.
The client program can read files.
The client program establishes a connection to the server program.
The client program can send files to the server.
         */
        Thread client1 = new Client();
        client1.start();


    }

    public void run() {

        Scanner scanner = new Scanner(System.in);

        //The HostName is the IP Number of the connected device (client)
        System.out.print("IP Address-> ");
        String IP_Address = scanner.nextLine();

        System.out.print("Port Number-> ");
        int portNumber = scanner.nextInt();

        //  System.out.print("User-> ");

//while(true)
        try {


            Socket localSocket = new Socket(IP_Address, portNumber);//establish the socket connection
            //get the socket Input Stream
            BufferedReader in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));

            //get the socket Output Stream
            PrintWriter out = new PrintWriter(localSocket.getOutputStream(), true);


            System.out.println(in.readLine());

            //get the socket input stream and open a buffered reader on it
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));


                String userName = stdIn.readLine();
                out.println(userName);
                System.out.println(in.readLine());

            String userInput;
            //Receive the user data
            String order="";
            while ((userInput = stdIn.readLine()) != null) {

           //     out.println(userInput);
                if(userInput.contains("SEND"))
                {
                    order=userInput.replaceFirst("SEND","").trim();
                    System.out.print(order+"\n");

                    File directory = new File(order);
                //    System.out.println(directory.getAbsolutePath());

                    File file = new File(directory.getAbsolutePath());
                    // Get the size of the file
                   // long length = (int)file.length();

                    byte[] bytes = new byte[16 * 1024];

                    //Get Stream from the file
                    InputStream fileInput = new FileInputStream(file);
                    OutputStream fileOutpt = localSocket.getOutputStream();

                    int count;
                    while ((count = fileInput.read(bytes)) > 0) {
                        fileOutpt.write(bytes, 0, count);
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

}




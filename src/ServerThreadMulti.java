import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class ServerThreadMulti {

    //List of Every Client connect to the server
    public static ArrayList<ServerThread> client_list = new ArrayList<>();

    public static void main(String args[]) {

//we need a thread to handle the user input
        final int PORT_NUMBER = 8080;
        Socket localSocket = null;
        ServerSocket s_Socket = null;

        try {
            s_Socket = new ServerSocket(PORT_NUMBER); // can also use static final PORT_NUM , when defined
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");
        }

        UserThread userChannel = new UserThread();
        userChannel.start();

        //Displaying PortNumber and Local IP address
        System.out.println("Server IP-> 192.168.1.3\tPort Number-> " + PORT_NUMBER);
        System.out.println("Server Listening......");

        //Initialize Connection
        while (true) {
            try {
                localSocket = s_Socket.accept();
                System.out.println("connection Established");

                //Start New Client Thread
                ServerThread st = new ServerThread(localSocket);

                //Add the current client to the list
                client_list.add(st);
                st.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }
    }
}

class ServerThread extends Thread {

    //Initialization the Streams
    String line = null;

    //Stream lines
    BufferedReader is = null;
    PrintWriter os = null;
    InputStream fileInput = null;
    OutputStream fileOutput = null;

    Socket clientsocket = null;

    public ServerThread(Socket clientsocket) {
        this.clientsocket = clientsocket;
    }

    public void run() {
        try {
            //get the client socket input and output stream
            is = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
            os = new PrintWriter(clientsocket.getOutputStream(), true);

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {
            //Get the Client input
            os.println("UserName-> ");
            String userName = is.readLine();
            System.out.println("Just Connect to: " + userName);

            while ((line = is.readLine()) != null)//receive from client
            {
                if (line.contains("SEND")) {
                    try {
                        String orderName = line.replace("SEND", "").trim();
                        System.out.println(userName+" Sent " + orderName);

                        fileInput = clientsocket.getInputStream();
                        //Create a new file and save to it
                        File temp = new File("E:\\Final Project\\server\\" + userName + "_" + orderName);
                        File clientOrder = new File(String.valueOf(temp));

                        fileOutput = new FileOutputStream(clientOrder);

                        //writing the file
                        byte[] bytes = new byte[16 * 1024];
                        int count;
                        if ((count = fileInput.read(bytes)) > 0) {
                            fileOutput.write(bytes, 0, count);//from the documentation
                        }
                    } catch (IOException ex) {
                        System.out.println("Can't get socket input stream. ");
                    }/*finally {
                        //fileOutput.close();
                        //fileInput.close();
                    }*/ //ERROR CLose the connection
                }

                //Communication test
                /*
                os.println(line);//Sent to Client
               System.out.println("Response to Client  :  "+line);
               */
            }
        } catch (IOException e) {
            line = this.getName(); //reused String line for getting thread name
            //   System.out.println("IO Error/ Client "+line+" terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName(); //reused String line for getting thread name
            //        System.out.println("Client "+line+" Closed");
        }

    }
}

class UserThread extends Thread {
    public void run() {
        Scanner in = new Scanner(System.in);
        String user_Input;
        while ((user_Input = in.nextLine()) != null) {
            switch (user_Input) {

                case "DELETE":

                    File temp = new File("E:\\Final Project\\server\\");
                    for (File f : temp.listFiles())
                        f.delete();
                    System.out.println("All Files in server path DELETED");
                    break;

                case "REPORT":

                    File dir=new File("E:\\Final Project\\server\\");
                    File[] list_of_files=dir.listFiles();

                    if(list_of_files!=null)
                    {
                        double total_of_invoices=0;
                        for(File invoice :list_of_files)
                        {
                            FileParser.fileName=invoice;
                            try {
                                FileParser.main();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            total_of_invoices=total_of_invoices+Double.parseDouble(FileParser.total);
                            System.out.println("Total is "+ total_of_invoices+"$");
                        }
                    }
                    break;

                case "CLOSE":

                    for (ServerThread clinet : ServerThreadMulti.client_list) {
                        try {
                            System.out.println("Connection Closing..");
                            if (clinet.is != null) {
                                clinet.is.close();
                                System.out.println(" Socket Input Stream Closed");
                            }

                            if (clinet.os != null) {
                                clinet.os.close();
                                System.out.println("Socket Out Closed");
                            }
                            if (clinet.clientsocket != null) {
                                clinet.clientsocket.close();
                                System.out.println("Socket Closed");
                            }
                        } catch (IOException ie) {
                            System.out.println("Socket Close Error");
                        }
                    }
                    System.out.println("Connection Ended getting out.....");
                    System.exit(0);

                default:
                    System.out.print("WRONG COMMAND ");
            }
        }
    }
}
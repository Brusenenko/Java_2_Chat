package ConsoleChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int PORT = 8191;
    static final String LOCALHOST = "localhost";

    public static void main(String[] args) {

        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(PORT);

            socket = server.accept();
            System.out.println("Клиент подключился!");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = null;
                        try {
                            str = in.readUTF();
                            if(str.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Client: " + str);
                    }
                }
            });


            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.println("Введите сообщение");
                        try {
                            String str = console.readLine();
                            out.writeUTF(str);
                            System.out.println("Сообщение отправлено!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package ConsoleChat;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket(Server.LOCALHOST, Server.PORT);

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
                        System.out.println("Server: " + str);
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
        }

    }
}

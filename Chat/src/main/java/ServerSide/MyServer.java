package ServerSide;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class MyServer {

    private static Socket socket;
    private static ServerSocket serverSocket;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен, ожидаем подключения...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            boolean isEndSession = false;

            Thread t1 = new Thread(() ->{
               while (true){

                   try {
                       String messageFromServer = "";
                       if (bufferedReader.ready()){
                            messageFromServer = bufferedReader.readLine();
                       }
                       if (!messageFromServer.isEmpty()){
                           dos.writeUTF(messageFromServer);
                           System.out.println(messageFromServer);
                       }
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            });
            t1.start();

            while (true) {
                String messageFromClient = dis.readUTF();
                if (messageFromClient.equals("/end")) {
                    break;
                }
                System.out.println(messageFromClient);
            }
        } catch (IOException ignored) {
            System.out.println("Соединение разрванно.");
        }finally {
            close();
        }
    }

    private static void close() {
        if (serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

//Т.е. если на клиентской стороне написать «Привет», нажать Enter,
// то сообщение должно передаться на сервер и там отпечататься в консоли.
// Если сделать то же самое на серверной стороне, сообщение, соответственно, передаётся клиенту и печатается у него в консоли.
// Есть одна особенность, которую нужно учитывать: клиент или сервер может написать несколько сообщений подряд.
// Такую ситуацию необходимо корректно обработать.

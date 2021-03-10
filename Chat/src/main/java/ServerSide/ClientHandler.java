package ServerSide;

import ClientSide.One.EchoClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private String name;
    private boolean endSession;
    private boolean isAuthorized;
    private long time;


    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.name = "";

            new Thread(() -> {
                try {
                    authentification();
                    time = System.currentTimeMillis();
                    readMessage();
                } catch (IOException ignored) {
                } finally {
                    closeConnection();
                }
            }).start();

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException ignored) {
                    }
                    if (!isAuthorized) {
                        closeConnection();
                    }
                }
            }).start();

        } catch (IOException e) {
            closeConnection();
            throw new RuntimeException("Problem with ClientHAndler");
        }
    }


    public void authentification() throws IOException {
        while (true) {
            String str = dis.readUTF();
            if (str.startsWith("/auth")) {
                String[] arr = str.split("\\s");
                String nick = myServer
                        .getAuthService()
                        .getNickByLoginAndPassword(arr[1], arr[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        isAuthorized = true;
                        sendMessage("/authok " + nick);
                        name = nick;
                        myServer.subscribe(this);
                        myServer.broadcastMessage("Hello " + name);
                        return;
                    } else {
                        sendMessage("Nick is busy");
                    }
                } else {
                    sendMessage("Wrong login and password");
                }

            } else {
                sendMessage("Start with /auth ");
            }
        }
    }


    public void readMessage() throws IOException {
//        long currentTime = System.currentTimeMillis();
//        long waitForMsg = 5000;
        while (true) {
//            if (endSession) {
//                return;
//            }
            String messageFromClient = dis.readUTF();
            System.out.println(name + " send message " + messageFromClient);
//            if (messageFromClient.isEmpty() && (time + waitForMsg) < currentTime){
//                closeConnection();
//                myServer.broadcastMessage(this.name + "time out ban");
//            }
            if (messageFromClient.trim().startsWith("/")) {
                if (messageFromClient.startsWith("/w")) {
                    String[] arr = messageFromClient.split(" ", 3);
                    //  messageFromClient.replace("/w", "");
                    myServer.sendMessageToCertainClient(this, arr[1], name + ": " + arr[2]);
                }
                if (messageFromClient.trim().startsWith("/list")) {
                    myServer.getOnlineUsersList(this);
                }
                if (messageFromClient.trim().startsWith("/end")) {
                    return;
                }
            } else {
                myServer.broadcastMessage(name + ": " + messageFromClient);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException ignored) {
        }
    }

    private void closeConnection() {
        myServer.unSubscribe(this);
        myServer.broadcastMessage(name + "Left chat");

        try {
            dis.close();
            dos.close();

            socket.close();

        } catch (IOException ignored) {
        }
    }

    public String getName() {
        return name;
    }

    private void closeSessionByTimeOut() {

    }
}

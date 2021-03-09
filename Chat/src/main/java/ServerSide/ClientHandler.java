package ServerSide;

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
                    readMessage();
                } catch (IOException ignored) {
                } finally {
                    closeConnection();
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
                        sendMessage("/authok " + nick);
                        name = nick;
                        myServer.subscribe(this);
                        myServer.broadcastMessage("Hello " + name);
                        return;
                    }
                } else {
                    sendMessage("Nick is busy");
                }
            } else {
                sendMessage("Wrong login and password");
            }
        }
    }


    public void readMessage() throws IOException {
        while (true) {
            String messageFromClient = dis.readUTF();
            System.out.println(name + " send message " + messageFromClient);
            if (messageFromClient.equals("/end")) {
                return;
            }
            myServer.broadcastMessage(name + ": " + messageFromClient);

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
}

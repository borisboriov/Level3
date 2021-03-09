package ServerSide;

import ServerSide.Interface.AuthService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class MyServer {

    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService(){
        return this.authService;
    }

    public MyServer(){
        try (ServerSocket server = new ServerSocket(PORT)){
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true){
                System.out.printf("Server is waiting for client" + "\n");
                Socket socket = server.accept();
                System.out.printf("Client has been connected" + "\n");
                new ClientHandler(this, socket);
            }
        } catch (IOException e){
            System.out.printf("Server fall");
        } finally {
            if (authService != null){
                authService.stop();
            }
        }
    }


    public synchronized void broadcastMessage(String message){
        for (ClientHandler c : clients) {
            c.sendMessage(message);
        }
    }

    public synchronized void subscribe(ClientHandler client){
        clients.add(client);
    }

    public synchronized void unSubscribe(ClientHandler client){
        clients.remove(client);
    }

    public boolean isNickBusy(String nick) {
        for (ClientHandler c : clients) {
            if (c.getName().equals(nick)){
                return true;
            }
        }
        return false;
    }
}
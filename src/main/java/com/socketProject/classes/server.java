package com.socketProject.classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server{
    private ServerSocket server;
    public server(ServerSocket server){
        this.server=server;
    }
    public void StartServer() {
        while (!server.isClosed()) {


            try {
                Socket client = server.accept();
                System.out.println("Client connecté");
                connexionHandler connexion = new connexionHandler(client);
                Thread t = new Thread(connexion);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(1234);
        server s=new server(serverSocket);
        s.StartServer();
    }
}

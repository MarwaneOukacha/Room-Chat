package com.socketProject.classes;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    public String username;
    public Client(Socket socket,String username){
        try {
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.socket=socket;
            this.username=username;
        }catch (IOException e){
            closeEveryThing(socket,bufferedReader,bufferedWriter);
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter your username for group chat: ");
        String username= scanner.nextLine();
        Socket socket=new Socket("localhost",1234);
        Client client=new Client(socket,username);
        client.listenForMessages();
        client.sendMessage();
    }
    public void closeEveryThing(Socket s,BufferedReader bufferredear ,BufferedWriter bufferedWriter){
        try {
            if (bufferredear!=null) bufferredear.close();
            if (bufferedWriter!=null) bufferedWriter.close();
            if (s!=null) s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner=new Scanner(System.in);
            while (socket.isConnected()){
                String message=scanner.nextLine();
                bufferedWriter.write(username+": "+message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEveryThing(socket,bufferedReader,bufferedWriter);
        }

    }
    public void listenForMessages(){
        new Thread(new Runnable() {
            String messageFromChat;
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        messageFromChat=bufferedReader.readLine();
                        System.out.println(messageFromChat);
                    }catch (IOException e){
                        closeEveryThing(socket,bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();

    }
}


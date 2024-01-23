package com.socketProject.classes;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class connexionHandler implements Runnable{
    private Socket client;
    private static ArrayList<connexionHandler> connexions=new ArrayList<>();;
    private BufferedReader bufferredear;
    private BufferedWriter bufferedWriter;
    private String Clientusername;

    public void setClient(Socket client) {
        this.client = client;
    }

    public String getClientusername() {
        return Clientusername;
    }

    public connexionHandler(Socket client) {
        try{
            this.client=client;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            this.bufferredear=new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.Clientusername=bufferredear.readLine();
            connexions.add(this);
            broadcastMessage("SERVER: "+Clientusername+"est connecté");
        }catch (IOException e){
            closeEveryThing(client,bufferredear,bufferedWriter);

        }
    }
    @Override
    public void run() {

        String messageFromClient;
        while (client.isConnected()) {
            try {
                messageFromClient=bufferredear.readLine();
                broadcastMessage(messageFromClient);
            }catch (IOException e){
                closeEveryThing(client,bufferredear,bufferedWriter);
                break;
            }
        }
    }

    private void broadcastMessage(String messageFromClient) {
        for(connexionHandler clienthandler:connexions){
            try {
                if(!clienthandler.getClientusername().equals(Clientusername)){
                    clienthandler.bufferedWriter.write(messageFromClient);
                    clienthandler.bufferedWriter.newLine();
                    clienthandler.bufferedWriter.flush();
                }
            }catch (Exception e){

            }
        }
    }
    public void removeHandler(){
        connexions.remove(this);
        broadcastMessage("Server: "+Clientusername +"est deconnecté");
    }
    public void closeEveryThing(Socket s,BufferedReader bufferredear ,BufferedWriter bufferedWriter){
        removeHandler();
        try {
            if (bufferredear!=null) bufferredear.close();
            if (bufferedWriter!=null) bufferedWriter.close();
            if (s!=null) s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

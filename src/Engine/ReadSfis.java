/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Engine.SfisRuning;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Administrator
 */
public class ReadSfis extends Thread {

    private final SfisRuning sfisRuning;
    private final Queue<String> dataReadQueue;
    private Socket socket;

    public ReadSfis(SfisRuning sfisRuning) {
        this.sfisRuning = sfisRuning;
        this.dataReadQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        while (true) {
            if (socket != null && sfisRuning.isConnect()) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String data = reader.readLine();
                    if (data != null) {
                        addQuuen(data);
                    }
                } catch (Exception ex) {
                    try {
                        sfisRuning.closeSocket();
                    } catch (IOException ex1) {
                        ex.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getData() {
        if (this.dataReadQueue.isEmpty()) {
            return null;
        }
        return this.dataReadQueue.poll();
    }

    public void setReader(Socket socket) {
        this.socket = socket;
    }

    private void addQuuen(String data) {
        if(data == null){
            return;
        }
        this.dataReadQueue.add(data);
        System.out.println(data);
    }
}

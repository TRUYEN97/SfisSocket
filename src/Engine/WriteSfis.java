/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Administrator
 */
public class WriteSfis extends Thread {

    private Socket socket;
    private SfisRuning sfisRuning;
    private Queue<String> dataWriteQueue;

    public WriteSfis(SfisRuning sfisRuning) {
        this.sfisRuning = sfisRuning;
        this.dataWriteQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (sfisRuning.isConnect() && socket != null) {
                    if (!dataWriteQueue.isEmpty()) {
                        Thread.sleep(100);
                        freeQueue();
                        continue;
                    } else {
                        sendHand();
                        Thread.sleep(3000);
                        continue;
                    }
                } else {
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                continue;
            }
        }
    }

    private boolean freeQueue() {
        String peekData = dataWriteQueue.peek();
        if (sendData(peekData)) {
            dataWriteQueue.poll();
            return true;
        }
        return false;
    }

    public WriteSfis(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public synchronized boolean addQueue(String dataResult) {
        if (!dataResult.isEmpty() && !dataWriteQueue.contains(dataResult)) {
            try {
                dataWriteQueue.add(dataResult);
                interrupt();
                Thread.sleep(100);
                return true;
            } catch (Exception ex) {
                
            }
        }
        return false;
    }

    public boolean sendHand() {
        char[] ma = {0x1b, 0x11, 0x0d, 0x0d, 0x0a};
        String data = String.copyValueOf(ma);
        return sendData(data);
    }

    private synchronized boolean sendData(String data) {
        try {
            if (data != null) {
                PrintWriter write = new PrintWriter(socket.getOutputStream(), true);
                write.println(data);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class SfisRuning extends Thread {

    private Socket socket = null;
    private int ipPort = 55962;
    private WriteSfis writerSfis;
    private boolean isConnect = false;
    private ReadSfis readSfis;
    private String fisrtConnect;
    private String hostNameOld;

    public SfisRuning() {
        this.socket = new Socket();
        writerSfis = new WriteSfis(this);
        readSfis = new ReadSfis(this);
        fisrtConnect = new String();
    }

    public SfisRuning( int ipPort) {
        this();
        this.ipPort = ipPort;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(ipPort);
            RunWriter();
            RunReadData();
            while (true) {
                try {
                    Socket skTemp = ss.accept();
                    String hostName = skTemp.getInetAddress().getHostAddress();
                    if (isNewHostName(hostName)) {
                        updateSocket(skTemp);
                    } else {
                        if (fisrtConnect.isEmpty()) {
                            fisrtConnect = hostName;
                        }
                        if (hostName.equals(fisrtConnect)) {
                            if (!isConnect()) {
                                setupNewSocket(skTemp, hostName);
                            }
                        } else {
                            setupNewSocket(skTemp, hostName);
                        }
                    }
                } catch (IOException ex) {
                    try {
                        closeSocket();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
                    }
                }
            }
        } catch (IOException ex) {
            
        }
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void closeSocket() throws IOException {
        socket.close();
        isConnect = false;
        hostNameOld = "";
    }

    public boolean addQueue(String dataResult) {
        return writerSfis.addQueue(dataResult);
    }

    private boolean isNewHostName(String hostName) {
        return hostName.equals(hostNameOld);
    }

    private void setupNewSocket(Socket skTemp, String hostName) throws IOException {
        updateSocket(skTemp);
        writerSfis.sendHand();
        writerSfis.addQueue("config");
        hostNameOld = hostName;
    }
    
    private void RunWriter() {
        if (writerSfis.isAlive()) {
            return;
        }
        writerSfis.start();
    }

    private void RunReadData() {
        if (readSfis.isAlive()) {
            return;
        }
        readSfis.start();
    }

    private void updateSocket(Socket skTemp) {
        socket = skTemp;
        writerSfis.setSocket(socket);
        readSfis.setReader(socket);
        isConnect = true;
    }

}

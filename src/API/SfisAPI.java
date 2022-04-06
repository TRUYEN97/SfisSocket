/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package API;

import Engine.SfisRuning;

/**
 *
 * @author Administrator
 */
public class SfisAPI {
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        SfisRuning sfisRuning = new SfisRuning(55962);
        sfisRuning.start();
        long timeStart = System.currentTimeMillis();
        while(true)
        {
            System.out.println("Listening ...");
            if(System.currentTimeMillis() - timeStart > 5000)
            {
                System.out.println("No client connect to!");
                System.exit(1);
            }
            Thread.sleep(1000);
        }
        
    }
    
}


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kieda
 */
public class F9 {
    public static void main(String[] args){
        try {
            Robot r = new Robot();
            Thread.currentThread().sleep(6000);
            while(true){
                r.keyPress(KeyEvent.VK_F9);
                Thread.currentThread().sleep(6000);
            }
        } catch (Exception ex) {}
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dms2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author delan
 */
public class FileSharing {
    
    public static void main(String [] args)
    {
        
        
    }
    public void sendingfile(String filename, int port)
    {
        System.out.println("he");
        FileInputStream fis = null; 
        BufferedInputStream bis = null; 
        OutputStream os = null; 
        ServerSocket sendingsock = null; 
        Socket sock = null; 
        try{
            
            sendingsock = new ServerSocket(2014); 

                sock = sendingsock.accept(); 
                System.out.println("connected");
                File sf = new File(filename); 
                byte [] bytefilearray = new byte[(int)sf.length()]; 
                fis = new FileInputStream(sf); 
                bis = new BufferedInputStream(fis); 
                bis.read(bytefilearray,0,bytefilearray.length); 
                os = sock.getOutputStream(); 
                os.write(bytefilearray,0,bytefilearray.length);
                os.flush();
                System.out.println("sent");
                if(bis != null)
                    bis.close();
                if(os != null)
                    os.close();
                if(sock != null)
                    sock.close();
                if(sendingsock != null)
                    sendingsock.close();
        } catch (IOException ex) {
            Logger.getLogger(FileSharing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void reciving(long filesize, String recivingfilelocation)
    {
        int bytesread; 
        int current =0; 
        FileOutputStream fos = null; 
        BufferedOutputStream bos = null;
        Socket sock = null; 
        try{
            sock = new Socket("192.168.20.8", 2014); 
            
            byte [] bytefilearray = new byte[600000];
            InputStream is = sock.getInputStream(); 
            fos = new FileOutputStream(recivingfilelocation);
            bos = new BufferedOutputStream(fos); 
            bytesread = is.read(bytefilearray,0,bytefilearray.length); 
            current =bytesread; 
            
            do
            { bytesread=is.read(bytefilearray, current, (bytefilearray.length-current));
                if(bytesread >=0)
                    current += bytesread; 
            }while(bytesread >-1);
            System.out.println("th");
            bos.write(bytefilearray,0, current);
            bos.flush();
            if(fos != null)
                fos.close();
            if(bos !=null)
                bos.close();
            if(sock != null)
                sock.close();
        } catch (IOException ex) {
            Logger.getLogger(FileSharing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

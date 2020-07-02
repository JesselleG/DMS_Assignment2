/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dms2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author ydw6617 and dqp6065
 */
public interface connected extends Remote{
    
    public void usersupdate(int port) throws RemoteException;
    public int newuser(int port) throws RemoteException;
    public void set_pass(int port) throws RemoteException; 
    public void pass() throws RemoteException; 
    public int getuser(int current) throws RemoteException;
    public boolean filecheck(String fileName) throws RemoteException;
    public long filesize(String fileName) throws RemoteException;
    public void sharefile(String name) throws RemoteException;
    
    public void recieveMessage(String msg) throws RemoteException;
    
    public String timestampMsg(int otherPort) throws RemoteException;
    public void incrementTimestamp() throws RemoteException;
    public String initialSnapshotMsg(int sender) throws RemoteException;  
    public void snapshotRecieveMsg(String snapshot, int port) throws RemoteException;
    public void debug() throws RemoteException;
    
    public void leaderRecieverMsg(ArrayList<Integer> newValues) throws RemoteException;
    public void leaderReachedMsg() throws RemoteException;
}

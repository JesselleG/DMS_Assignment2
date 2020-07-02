/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dms2;

/**
 *
 * @author ydw6617 and dqp6065
 */

import java.io.File;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class peer implements connected {
    private int port, systemLeader, pass;
    private ArrayList<Integer> Members_known; 
    private HashMap<String,File> files;
    private HashMap<Integer,Integer> timestamp;
    private FileSharing send;
    
    private HashMap<Integer,Boolean> neighbourTaken;
    private HashMap<Integer,String> neighbourSnapshots;
    private boolean ownTaken, Token, Request, joining_in_prosses;
    private String snapshot, systemSnapshot, filesSaved;
    private ArrayList<Integer> knownValues, sentValues;
    
    public peer(int port)
    {
        Members_known = new ArrayList<>();
        System.out.println(Members_known.size());
        files = new HashMap<>(); 
        timestamp = new HashMap<>();
        send = new FileSharing(); 
        this.port = port; 
        systemSnapshot = ""; filesSaved = "";
        neighbourSnapshots = new HashMap<>();
        
        systemLeader = 0;
        knownValues = new ArrayList<>();
        neighbourTaken = new HashMap<>();
        ownTaken = false;
        Token = false; 
        Request = false; 
        joining_in_prosses = false; 
        sentValues = new ArrayList<>();
                
        try {
            usersupdate(port);
        } catch (RemoteException ex) {
            Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
    
    public ArrayList getMembersList(){
        return Members_known;
    }

    @Override
    public int newuser(int port) {
        //adds a new user to the peer to peer system and starts the token ring if its the firt peer to join 
        int current = this.Members_known.size(); 
        int temp = this.port; 
        joining_in_prosses = true; 
        if(current ==1)
        {
            System.out.println("how many time");
            pass = port; 
            Thread thread = new Thread(new Runnable()
            {
                public void run(){
                    try {
                        Registry regpass = LocateRegistry.getRegistry(port);
                        connected remoteproxy = (connected) regpass.lookup("file"); 
                                remoteproxy.set_pass(temp);
                                remoteproxy.pass();
                            } catch (RemoteException ex) {
                                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NotBoundException ex) {
                        Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }}); 
            thread.start();
            
        }
        else 
        {
        connected remoteproxypass;
        try {
            Registry regpass = LocateRegistry.getRegistry(port);
            remoteproxypass = (connected) regpass.lookup("file");
            remoteproxypass.set_pass(pass);
            pass = port; 
        } catch (RemoteException ex) {
            Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
        for(int i=0; i<=current;i++)
        {
            try { 
                Registry reg = LocateRegistry.getRegistry(this.Members_known.get(i));
                connected remoteproxy = (connected) reg.lookup("file");
                System.out.println(this.Members_known.get(i));
                remoteproxy.usersupdate(port);
                remoteproxy.timestampMsg(port);
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            usersupdate(port);
        } catch (RemoteException ex) {
            Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        current = this.Members_known.size();
        Collections.sort(Members_known);

        incrementTimestamp();
        joining_in_prosses = false; 
        return current; 
    }
    @Override
    public int getuser(int current) {
        //return a user to add to the list 
        incrementTimestamp();
        return this.Members_known.get(current); 
    }
    @Override
    public void usersupdate(int port) throws RemoteException {
        //notifies the other peers on the system about the new peer 
        System.out.println("user"+port);
        if(!Members_known.contains(port)){
            this.Members_known.add(port);
            timestamp.put(port, 0);
            neighbourTaken.put(port,false);
        }
        incrementTimestamp();
        Collections.sort(Members_known);
    }

    @Override
    public boolean filecheck(String fileName) {
        //sees if this peer contains the request fiel 
        incrementTimestamp();
        return this.files.containsKey(fileName); 
    }

    public String add_file()
    {
        //adds a file to the system that other peers and get 
        boolean name_free = true; 
        String name; 
        do{
            name = JOptionPane.showInputDialog("File name please enter a name that is not already in use:");
        for(int i=0; i<this.Members_known.size();i++)
        {
            try { 
                Registry reg =LocateRegistry.getRegistry(this.Members_known.get(i));
                connected remoteproxy = (connected) reg.lookup("file");
                if(remoteproxy.filecheck(name) == true)
                {
                    name_free = false; 
                    break; 
                }
            } catch (RemoteException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }while(name_free ==false); 
        File selected = null; 
        JFileChooser choose = new JFileChooser();
        int value; 
        do{
        value = choose.showOpenDialog(null); 
        }while(value !=JFileChooser.APPROVE_OPTION); 
        selected =choose.getSelectedFile(); 
        files.put(name, selected); 
        filesSaved += name;
        
        incrementTimestamp();
        return name;
    }
    
    public boolean get_file(String filename)
    {
        // checks to see if they have the token 
        System.out.println("here");
        Request = true; 
        while(Token == false)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        boolean sent = false;
        int stopped =-1; 
        long size =0; 
        //checks to see if it is a real file then tells the peer containg that file to get ready to send it
        for(int i=0;i<this.Members_known.size();i++)
        {
            try { 
                Registry reg =LocateRegistry.getRegistry(this.Members_known.get(i));
                connected remoteproxy = (connected) reg.lookup("file");
                if(remoteproxy.filecheck(filename) == true)
                {
                    System.out.println("correct");
                    size = remoteproxy.filesize(filename); 
                    Thread thread = new Thread(new Runnable()
                    {
                        public void run(){
                        
                            try {
                                remoteproxy.sharefile(filename);
                            } catch (RemoteException ex) {
                                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    }}); 
                    thread.start();
                    
                    stopped = i; 
                    break;
                } 
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //tells the current peer to get ready to recive the file 
        if(stopped == -1)
        {
            JOptionPane.showMessageDialog(null, "file not found");
        }
        else 
        {
            JFileChooser choose = new JFileChooser();
            int value; 
            do{
                value = choose.showSaveDialog(null); 
            }while(value !=JFileChooser.APPROVE_OPTION); 
            send.reciving(size, choose.getSelectedFile().getAbsolutePath());
            filesSaved += filename;

            sent = true;
        }

        incrementTimestamp();
        Request = false; 
        Thread thread = new Thread(new Runnable()
            {
                public void run(){
                    try {
                        Registry regpass = LocateRegistry.getRegistry(port);
                        connected remoteproxy = (connected) regpass.lookup("file"); 
                                remoteproxy.pass();
                            } catch (RemoteException ex) {
                                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NotBoundException ex) {
                        Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }}); 
            thread.start();
        return sent;
    }

    @Override
    public void sharefile(String name) throws RemoteException {
        //calls the file sharing class 
        incrementTimestamp();        
        send.sendingfile(files.get(name).getAbsolutePath(), port);
    }

    @Override
    public long filesize(String fileName) throws RemoteException {
        //gets the file size 
        incrementTimestamp();
        return files.get(fileName).length(); 
    }
    @Override
    public void recieveMessage(String msg){        
        String delims = "[ :]";
        String[] tokens = msg.split(delims);
        if(msg.contains("timestamp")){
            int sender = Integer.parseInt(tokens[1]);
            int senderStamp = Integer.parseInt(tokens[2]);
            int reciever = Integer.parseInt(tokens[3]);
            int recieverStamp = Integer.parseInt(tokens[4]);
            
            if(timestamp.get(sender)<senderStamp) timestamp.put(sender,senderStamp);
            if(timestamp.get(reciever)<recieverStamp) timestamp.put(reciever,recieverStamp);
            
            System.out.println(reciever+": "+timestamp.get(reciever)+" - "+sender+": "+timestamp.get(sender));
        }
        else if(msg.contains("initialSnapshot")){
            if(!ownTaken){
                snapshot = port + "has files: "+filesSaved+" @ "+timestamp.get(port);
                ownTaken = true;
                
                neighbourTaken.put(port, true);
                System.out.println(neighbourTaken.entrySet());

                for (Integer member : Members_known) {
                    try { 
                        Registry reg = LocateRegistry.getRegistry(member);
                        connected remoteproxy = (connected) reg.lookup("file");

                        remoteproxy.recieveMessage(initialSnapshotMsg(port));
                    } catch (RemoteException | NotBoundException ex) {
                        Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            neighbourTaken.put(Integer.parseInt(tokens[1]), true);
            if(ownTaken && !neighbourTaken.containsValue(false)){
                try { 
                    Registry reg = LocateRegistry.getRegistry(Integer.parseInt(tokens[1]));
                    connected remoteproxy = (connected) reg.lookup("file");

                    remoteproxy.snapshotRecieveMsg(snapshot, port);
                    systemSnapshot = neighbourSnapshots.toString();

                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
           }
        }
        else if(msg.contains("leaderElection")){
            int lePort = Integer.parseInt(tokens[1]);
            try {
                if(!knownValues.contains(lePort)){
                    knownValues.add(lePort);
                    this.leaderReachedMsg();
                }
            } catch (RemoteException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public String timestampMsg(int otherPort) {
        //does the time stamp
        incrementTimestamp();
        
        return "timestamp "+port+": "+timestamp.get(port)+" "+otherPort+": "+timestamp.get(otherPort);
    }
    public String getCurrentTimestamp(){
        //gets the current time stamp 
        return port+": "+timestamp.get(port);
    }
    @Override
    public void incrementTimestamp(){
        //increments the timestamp 
        int currentTimestamp = timestamp.get(port)+1;
        timestamp.put(port, currentTimestamp);
    }
    
    @Override
    public void debug(){
        //only if leader
        for(Integer member: Members_known){
            try { 
                Registry reg = LocateRegistry.getRegistry(member);
                connected remoteproxy = (connected) reg.lookup("file");

                remoteproxy.recieveMessage(initialSnapshotMsg(port));
            } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @Override
    public String initialSnapshotMsg(int sender){
        return "initialSnapshot "+sender; 
    }
    @Override
    public void snapshotRecieveMsg(String snapshot, int port){
        neighbourSnapshots.put(port, snapshot);
    }
    public String getSystemSnapshot() {
        return systemSnapshot;
    }
    
    public void startElection(){
        this.recieveMessage(initLeaderMsg(port));
    }
    public String initLeaderMsg(int port){
        return "leaderElection "+port;
    }
    @Override
    public void leaderRecieverMsg(ArrayList<Integer> newValues) throws RemoteException {
        for(Integer i: newValues){
            if(!knownValues.contains(i)) knownValues.add(i);
        }
        System.out.println(knownValues);
    }
    @Override
    public void leaderReachedMsg() throws RemoteException {
        for(int i=0;i<Members_known.size();i++){
            ArrayList newValues = knownValues;
            newValues.remove(sentValues);
            sentValues.addAll(newValues);

                for(Integer member: Members_known){
                    try { 
                        Registry reg = LocateRegistry.getRegistry(member);
                        connected remoteproxy = (connected) reg.lookup("file");

                        remoteproxy.recieveMessage(initLeaderMsg(port));
                        remoteproxy.leaderRecieverMsg(newValues);
                    } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        Collections.sort(knownValues);
        systemLeader = knownValues.get(knownValues.size()-1);
    }
    public int getSystemLeader(){
        return systemLeader;
    }

    @Override
    public void set_pass(int port) {
        this.pass = port; 
    }

    @Override
    public void pass() throws RemoteException {
        //tell the peer that it now holds the token and if it does not need it to pass it on 
        Token = true;
        if(joining_in_prosses == true)
            try {
                Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
        }  
        if(Request == false)
        {
            Token = false; 
            Registry reg = LocateRegistry.getRegistry(pass);
            try {         
                connected remoteproxy = (connected) reg.lookup("file");
                remoteproxy.pass();
            } catch (NotBoundException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AccessException ex) {
                Logger.getLogger(peer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
    
 
    
    


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dms2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author ydw6617 and dqp6065
 */
public class fileSharingGUI extends javax.swing.JFrame implements ActionListener{

    /**
     * Creates new form fileSharingGUI
     */
    public fileSharingGUI() {
        initComponents();
        
        joinBttn.addActionListener(this);
        newBttn.addActionListener(this);
        exitBttn.addActionListener(this);    
        uploadFileBttn.addActionListener(this);
        getFileBttn.addActionListener(this);  
        debugBttn.addActionListener(this);
        electionBttn.addActionListener(this);
        
        peerString="";
        systemSnapshot="";
        systemLeader=0;
        timer = new Timer(0,this);
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newBttn = new javax.swing.JButton();
        joinBttn = new javax.swing.JButton();
        exitBttn = new javax.swing.JButton();
        portNoSentence = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        peerList = new javax.swing.JTextArea();
        getFileBttn = new javax.swing.JButton();
        uploadFileBttn = new javax.swing.JButton();
        sysPortSentence = new javax.swing.JLabel();
        peerListTextArea = new javax.swing.JLabel();
        debugBttn = new javax.swing.JButton();
        systemLeaderTxt = new javax.swing.JLabel();
        electionBttn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        newBttn.setText("Connect to Port");

        joinBttn.setText("Join System");
        joinBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinBttnActionPerformed(evt);
            }
        });

        exitBttn.setText("Exit System");

        portNoSentence.setText("Your port number is: ");

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        peerList.setEditable(false);
        peerList.setColumns(20);
        peerList.setRows(5);
        jScrollPane2.setViewportView(peerList);

        getFileBttn.setText("Get File");

        uploadFileBttn.setText("Upload File");

        sysPortSentence.setText("You are connected to peer: ");

        peerListTextArea.setText("Peer List:");

        debugBttn.setText("Debug");

        systemLeaderTxt.setText("The leader is: ");

        electionBttn.setText("Election");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portNoSentence)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(getFileBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(uploadFileBttn)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(sysPortSentence)
                        .addComponent(peerListTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(systemLeaderTxt)
                        .addComponent(jScrollPane2)
                        .addComponent(exitBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(joinBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(electionBttn)
                        .addGap(2, 2, 2)
                        .addComponent(debugBttn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portNoSentence)
                    .addComponent(systemLeaderTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sysPortSentence)
                        .addGap(10, 10, 10)
                        .addComponent(newBttn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(joinBttn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitBttn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(peerListTextArea)
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(getFileBttn)
                            .addComponent(debugBttn)
                            .addComponent(electionBttn))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(uploadFileBttn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void joinBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinBttnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_joinBttnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(fileSharingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fileSharingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fileSharingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fileSharingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fileSharingGUI().setVisible(true);
            }
        });        
    }
    
    private void createSystem() throws RemoteException, NotBoundException{
        Registry reg = LocateRegistry.createRegistry(portNo); 
        remote = new peer(portNo);
        try 
        {
            //making the registry 
            connected stub = (connected) UnicastRemoteObject.exportObject(remote,0); 
            reg.rebind("file", stub);
        }
        catch(RemoteException e)
        {
            System.out.println("unable to bind to registry"+ e);
        }        
    }
    private void joinSystem(int otherPort) throws RemoteException, NotBoundException{        
        Registry reg = LocateRegistry.getRegistry(otherPort);
        try 
        {
            //making the registry 
            connected stub = (connected) UnicastRemoteObject.exportObject(remote,0); 
            reg.rebind("file", stub);
        }
        catch(RemoteException e)
        {
            System.out.println("unable to bind to registry"+ e);
        }                
        Registry reg2 = LocateRegistry.getRegistry(otherPort); 
        connected con = (connected) reg2.lookup("file"); 
        int noOfPeers = con.newuser(portNo); 
        remote.usersupdate(otherPort);
        for(int i=0;i<noOfPeers;i++){
            remote.usersupdate(con.getuser(i));
        }        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton debugBttn;
    private javax.swing.JButton electionBttn;
    private javax.swing.JButton exitBttn;
    private javax.swing.JButton getFileBttn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton joinBttn;
    private javax.swing.JButton newBttn;
    private javax.swing.JTextArea peerList;
    private javax.swing.JLabel peerListTextArea;
    private javax.swing.JLabel portNoSentence;
    private javax.swing.JLabel sysPortSentence;
    private javax.swing.JLabel systemLeaderTxt;
    private javax.swing.JTextArea textArea;
    private javax.swing.JButton uploadFileBttn;
    // End of variables declaration//GEN-END:variables
    private int portNo,systemLeader;
    private peer remote;
    private Timer timer;
    private String peerString, systemSnapshot;

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source == newBttn){
            String answer = JOptionPane.showInputDialog("Enter port number to connect to: ");
            //ASSUMES ANSWER IS INTEGER
            if(Integer.parseInt(answer) !=2014)
            {
            portNo = Integer.parseInt(answer);
            
            try {
                createSystem();
            } catch (RemoteException ex) {
                Logger.getLogger(fileSharingGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(fileSharingGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            portNoSentence.setText(portNoSentence.getText()+portNo);
            }
                
        }
        if(source == joinBttn){
            if(remote == null) JOptionPane.showMessageDialog(rootPane, "You must connect to a port first!");
            else{
                String answer = JOptionPane.showInputDialog("Enter port number to the registry you want to connect to: ");
                //ASSUMES ANSWER IS INTEGER
                int otherport = Integer.parseInt(answer);

                try {
                    joinSystem(otherport);
                } catch (RemoteException ex) {
                    Logger.getLogger(fileSharingGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(fileSharingGUI.class.getName()).log(Level.SEVERE, null, ex);
                } 
                sysPortSentence.setText(sysPortSentence.getText()+otherport);
            }
        }
        if(source == exitBttn){
            System.exit(1);
        }
        if(source == uploadFileBttn){
            String name = remote.add_file();            
            textArea.append("'"+name+"' file uploaded! @ ");
            textArea.append(remote.getCurrentTimestamp()+"\n");
        }
        if(source == getFileBttn){
            String filename = JOptionPane.showInputDialog("Enter the name of your file: ");
            boolean recieved = remote.get_file(filename);
            if(recieved) textArea.append("'"+filename+"' saved! @ "+remote.getCurrentTimestamp()+"\n");
            else textArea.append("'"+filename+"' not found :( @ "+remote.getCurrentTimestamp()+"\n");
        }
        if(source == timer){
            if(remote!=null && !remote.getMembersList().isEmpty()){
                peerList.setText(remote.getMembersList().toString());    
                String str = peerList.getText();
                if(!peerString.contains(str)){
                    textArea.append("Peerlist updated at @ "+remote.getCurrentTimestamp()+"\n");
                    peerString = str;
                }                
            }
            if(remote!=null && !systemSnapshot.equals(remote.getSystemSnapshot())){
                systemSnapshot = remote.getSystemSnapshot();
                textArea.append(systemSnapshot+"\n");
            }
            if(remote!=null && remote.getSystemLeader()!=0){
                if(systemLeader!=remote.getSystemLeader()){
                    systemLeader = remote.getSystemLeader();
                    systemLeaderTxt.setText("System Leader is: "+systemLeader);
                }
            }
        }
        if(source == debugBttn){
            if(remote!=null && portNo == remote.getSystemLeader()){
                remote.debug();
            }
            else JOptionPane.showMessageDialog(rootPane,"Only the leader can debug!");
        }
        if(source == electionBttn){
            remote.startElection();
        }
    }
}
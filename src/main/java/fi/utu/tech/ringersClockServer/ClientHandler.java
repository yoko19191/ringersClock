package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.ClientCmd;
import fi.utu.tech.ringersClock.entities.SCMD;
import fi.utu.tech.ringersClock.entities.ServerCmd;
import fi.utu.tech.ringersClock.entities.WakeUpGroup;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Every Client need a ClientHandler
 */
public class ClientHandler extends Thread{

    //private MessageHandler mh;
    private Socket cs;
    private InputStream iS; private ObjectInputStream oIn;
    private OutputStream oS; private ObjectOutputStream oOut;

    private WakeUpGroup localGroup;

    public ClientHandler(Socket cs) {
        this.cs = cs;
        //mh = new MessageHandler();
    }


    @Override
    public void run(){
        System.out.println("Client connected: "+ cs.getInetAddress()+":"+cs.getPort());
        try{
            iS = cs.getInputStream(); oIn = new ObjectInputStream(iS);
            oS = cs.getOutputStream(); oOut = new ObjectOutputStream(oS);
            /* Send ServerCmd once Client*/
            initialize();
            while(cs.isConnected()){
                handle(oIn.readObject());
            }//while
        }catch(IOException | ClassNotFoundException e) {
            System.err.println("Client connection closed: "+cs.getRemoteSocketAddress());
        }//try-catch
    }//run

    //sender
    public void send(ServerCmd cmd){
        try{
            oOut.writeObject(cmd);
            oOut.flush();
        }catch(IOException e){
            e.printStackTrace();
        }//try-catch
    }//send

    //static sender
    public static void send(ServerCmd cmd, ObjectOutputStream oOut){
        try{
            oOut.writeObject(cmd);
            oOut.flush();
        }catch(IOException e){
            e.printStackTrace();
        }//try-catch
    }

    //local getter and setter
    public WakeUpGroup getLocalGroup() {
        return localGroup;
    }

    public void setLocalGroup(WakeUpGroup localGroup) {
        this.localGroup = localGroup;
    }

    //initialize
    private void initialize(){
        var appendStatus = new ServerCmd(SCMD.APPEND_TO_STATUS_CMD,"Connect to server as: "+cs.getPort());
        var updateGroups = new ServerCmd(SCMD.UPDATE_EXISTING_GROUP_CMD,Container.getAllWakeUpGroups());
        send(appendStatus);
        send(updateGroups);
    }//initialize

    public void handle(Object obj){
        try{
            if(obj instanceof ClientCmd){
                var cmd = (ClientCmd) obj;
                var command = cmd.getCommand();
                var payload = cmd.getPayload();
                System.out.println("Received Client's Command: "+command.toString());
                switch(command){
                    case CREATE_GROUP_CMD:
                        if(payload instanceof WakeUpGroup){
                            handleCreateGroup((WakeUpGroup) payload);
                        }
                        break;
                    case JOIN_GROUP_CMD:
                        if(payload instanceof WakeUpGroup){
                            handleJoinGroup((WakeUpGroup) payload);
                        }
                        break;
                    case RESIGN_GROUP_CMD:
                        handleResignGroup();
                        break;
                    case ALARM_ALL_CMD:
                        handleAlarmAll();
                        break;
                    case CANCEL_ALARM_CMD:
                        handleCancelAlarm();
                        break;
                    default:
                        System.err.println("Unknown CCMD TYPE, please check..");
                        break;
                }
            }else{
                System.err.println("Received non-ClientCmd object.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }//handle
    /*-----handle-methods-----*/
    private void handleCreateGroup(WakeUpGroup group){
        this.localGroup = group;
        var newService = new WakeUpService(group,this.oOut);
        Container.serviceQueue.add(newService);
        //typical
        var groupList = Container.getAllWakeUpGroups();
        var updateGroupCmd = new ServerCmd(SCMD.UPDATE_EXISTING_GROUP_CMD,groupList);
        send(updateGroupCmd);
        send(new ServerCmd(SCMD.APPEND_TO_STATUS_CMD,"NEW_GROUP_CREATED."));
    }

    private void handleJoinGroup(WakeUpGroup group){
        this.localGroup = group;
        var service = Container.getServiceByGroup(group);
        service.addObjectOutputStream(this.oOut);
        //typical
        var groupList = Container.getAllWakeUpGroups();
        var updateGroupCmd = new ServerCmd(SCMD.UPDATE_EXISTING_GROUP_CMD,groupList);
        send(updateGroupCmd);
        send(new ServerCmd(SCMD.APPEND_TO_STATUS_CMD,group.getName()+" JOIN."));
    }

    private void handleResignGroup(){
        var service = Container.getServiceByGroup(this.localGroup);
        service.removeObjectOutputStream(this.oOut);
        //typical
        var groupList = Container.getAllWakeUpGroups();
        var updateGroupCmd = new ServerCmd(SCMD.UPDATE_EXISTING_GROUP_CMD,groupList);
        send(updateGroupCmd);
        send(new ServerCmd(SCMD.APPEND_TO_STATUS_CMD,"GROUP_RESIGNED."));
    }

    //find locate service and set
    private void handleAlarmAll(){
        var service = Container.getServiceByGroup(this.localGroup);
        service.setConfirmAlarm(true);
        //typical
        var groupList = Container.getAllWakeUpGroups();
        var updateGroupCmd = new ServerCmd(SCMD.UPDATE_EXISTING_GROUP_CMD,groupList);
        send(updateGroupCmd);
        send(new ServerCmd(SCMD.ALARM_USER_CMD));
        send(new ServerCmd(SCMD.APPEND_TO_STATUS_CMD,"ALARM--aha."));
        //remove this service from Container
        //Container.serviceQueue.remove(service);
    }

    private void handleCancelAlarm(){
        var service = Container.getServiceByGroup(this.localGroup);
        service.removeObjectOutputStream(this.oOut);
        //typical
        var groupList = Container.getAllWakeUpGroups();
        var updateGroupCmd = new ServerCmd(SCMD.UPDATE_EXISTING_GROUP_CMD,groupList);
        send(updateGroupCmd);
        send(new ServerCmd(SCMD.APPEND_TO_STATUS_CMD,"GROUP_ALARM_CANCELED."));
    }

    /*
    private class MessageHandler{



    }//class MessageHandler
     */



}

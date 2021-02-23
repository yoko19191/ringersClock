package fi.utu.tech.ringersClock.entities;

import java.io.Serializable;

public class ServerCmd <T extends Serializable> implements Serializable{

    private SCMD cmd;
    private T payload;

    public ServerCmd(SCMD cmd, T payload){
        this.cmd = cmd;
        this.payload = payload;
    }

    public ServerCmd(SCMD cmd){
        this.cmd = cmd;
        this.payload = null;
    }

    public SCMD getCommand(){
        return this.cmd;
    }

    public T getPayload(){return this.payload;}

}

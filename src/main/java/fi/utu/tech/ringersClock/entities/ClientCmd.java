package fi.utu.tech.ringersClock.entities;

import java.io.Serializable;

public class ClientCmd<T extends Serializable> implements Serializable {

    private CCMD cmd;
    private T payload;

    public ClientCmd(CCMD cmd, T payload){
        this.cmd = cmd;
        this.payload = payload;
    }

    public ClientCmd(CCMD cmd){
        this.cmd = cmd;
        this.payload = null;
    }

    public CCMD getCommand(){
        return this.cmd;
    };

    public T getPayload(){
        return this.payload;
    }
}

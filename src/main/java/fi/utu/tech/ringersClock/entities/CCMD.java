package fi.utu.tech.ringersClock.entities;

public enum CCMD {
    CREATE_GROUP_CMD,       //Send with created WakeUpGroup
    JOIN_GROUP_CMD,         //Send with join WakeUpGroup
    RESIGN_GROUP_CMD,       //Send without payload
    ALARM_ALL_CMD,          //Send without payload
    CANCEL_ALARM_CMD,       //Send without payload
}

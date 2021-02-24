package fi.utu.tech.ringersClock.entities;

public enum SCMD {
    UPDATE_EXISTING_GROUP_CMD,  //ArrayList<WakeUpGroup>
    APPEND_TO_STATUS_CMD,       //String
    CONFIRM_ALARM_CMD,          //WakeUpGroup
    ALARM_USER_CMD,             // send without payload
    ALARM_CANCELED_CMD,         // send without payload
    ALARM_SETUP_CMD,            //Instant
}

package fi.utu.tech.ringersClock.entities;

public enum SCMD {
    UPDATE_EXISTING_GROUP_CMD, //ArrayList<WakeUpGroup>
    APPEND_TO_STATUS_CMD, //String
    CONFIRM_ALARM_CMD,  //WakeUpGroup
    ALARM_USER_CMD,  //
    ALARM_CANCELED_CMD, //
    ALARM_SETUP_CMD, //Instant
}

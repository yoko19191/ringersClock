package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.WakeUpGroup;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Container {

    /**
     * arr [service][ClientHandler]
     */

    public static BlockingQueue<ClientHandler> clientQueue = new ArrayBlockingQueue<ClientHandler>(500);

    /* MAX_GROUP_SERVICE_CAPACITY =  100 */
    public static BlockingQueue<WakeUpService> serviceQueue = new ArrayBlockingQueue<WakeUpService>(100);




    //Container control

    public static ArrayList<WakeUpGroup> getAllWakeUpGroups(){
        ArrayList<WakeUpGroup> groupList = new ArrayList<WakeUpGroup>();
        for(WakeUpService service: serviceQueue) {
            groupList.add(service.getGroup());
        }
        return groupList;
    }

    public static WakeUpService getServiceByGroup(WakeUpGroup search){

        for(WakeUpService service: serviceQueue){
            if(service.getGroup() == search){
                return service;
            }
        }//for
        System.err.println("GetServiceByGroup failed. return null");
        return null;
    }




}

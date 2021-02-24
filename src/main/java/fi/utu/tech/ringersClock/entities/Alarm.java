package fi.utu.tech.ringersClock.entities;

import fi.utu.tech.weatherInfo.WeatherData;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class Alarm implements Serializable {
    private static final long serialVersionUID = 1L;

    private Instant targetInstant;
    private boolean isAllowRain;
    private boolean isAllowTempUnderZero;

    public Alarm(int hour, int minute, boolean isAllowRain, boolean isAllowTempUnderZero) {
        this.targetInstant = hoursAndMinutesToInstant(hour,minute);
        this.isAllowRain = isAllowRain;
        this.isAllowTempUnderZero = isAllowTempUnderZero;
    }

    public boolean isWeatherAllowed(WeatherData wd){
        boolean rainAccept = true,tempAccept=true;
        if(!this.isAllowRain && wd.getIsRaining()){
            rainAccept = false;
        }
        if(!this.isAllowTempUnderZero && (wd.getTemperature()<0) ){
            tempAccept = false;
        }
        return rainAccept && tempAccept;
    }

    public static Instant hoursAndMinutesToInstant(int hour,int minute){
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(hour,minute);
        Instant targetDateTime = LocalDateTime.of(date,time)
                .atZone(ZoneId.systemDefault()).toInstant();
        Instant now = Instant.now();
        if(now.toEpochMilli()<= targetDateTime.toEpochMilli()){
            return targetDateTime;
        }else{
            return targetDateTime.plus(1, ChronoUnit.DAYS);
        }
    }

    public Instant toInstant() {
        return targetInstant;
    }

}

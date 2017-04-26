package eu.applogic.onlinealb.Objects;

import java.io.Serializable;

/**
 * Created by makis on 4/6/2017.
 */

public class RadioStationObject implements Serializable {

    /**
     <station
     id="1383"
     name="RTSH-Radio Tirana1 102.2"
     dial="102.2"
     type="FM"
     image="s197394"
     status="1"
     cc="AL"
     cn="Albania"
     rn="Diber County"
     streaming1URL="http://167.114.131.90:5054"
     streaming1Type="MP3"
     streaming2URL=""
     streaming2Type=""
     lastUpdate="2017-01-01 07:17:25"/>
     */

    public static String KEY_STATION = "station";
    public static String KEY_ID = "id";
    public static String KEY_NAME = "name";
    public static String KEY_DIAL = "dial";
    public static String KEY_TYPE = "type";
    public static String KEY_IMAGE = "image";
    public static String KEY_STATUS = "status";
    public static String KEY_CC = "cc";
    public static String KEY_CN = "cn";
    public static String KEY_RN = "rn";
    public static String KEY_STREAM_URL_1 = "streaming1URL";
    public static String KEY_STREAM_TYPE_1 = "streaming1Type";
    public static String KEY_STREAM_URL_2 = "streaming2URL";
    public static String KEY_STREAM_TYPE_2 = "streaming2Type";
    public static String KEY_LAST_UPDATE = "lastUpdate";

    private String id;
    private String name;
    private String dial;
    private String type;
    private String image;
    private String status;
    private String cc;
    private String cn;
    private String rn;
    private String streaming1URL;
    private String streaming1Type;
    private String streaming2URL;
    private String streaming2Type;
    private String lastUpdate;

    public RadioStationObject(String id, String name, String dial, String type, String image, String status, String cc, String cn, String rn, String streaming1URL, String streaming1Type,
                              String streaming2URL, String streaming2Type, String lastUpdate){
        this.id = id;
        this.name = name;
        this.dial = dial;
        this.type = type;
        this.image = image;
        this.status = status;
        this.cc = cc;
        this.cn = cn;
        this.rn = rn;
        this.streaming1URL = streaming1URL;
        this.streaming1Type = streaming1Type;
        this.streaming2URL = streaming2URL;
        this.streaming2Type = streaming2Type;
        this.lastUpdate = lastUpdate;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getDial(){
        return this.dial;
    }

    public String getType(){
        return this.type;
    }

    public String getImage(){
        return this.image;
    }

    public String getStatus(){
        return this.status;
    }

    public String getCc(){
        return this.cc;
    }

    public String getCn(){
        return this.cn;
    }

    public String getRn(){
        return this.rn;
    }

    public String getStreaming1URL(){
        return this.streaming1URL;
    }

    public String getStreaming1Type(){
        return this.streaming1Type;
    }

    public String getStreaming2URL(){
        return this.streaming2URL;
    }

    public String getStreaming2Type(){
        return this.streaming2Type;
    }

    public String getLastUpdate(){
        return this.lastUpdate;
    }
}

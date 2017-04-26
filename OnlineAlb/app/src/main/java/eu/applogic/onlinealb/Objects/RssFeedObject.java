package eu.applogic.onlinealb.Objects;

import java.io.Serializable;

/**
 * Created by makis on 4/4/2017.
 */

public class RssFeedObject implements Serializable {

    /**
     <item>
         <title>DigitAlb</title>
         <description>tv</description>
         <imageService>http://bit.ly/1QK7Jvo</imageService>
         <serviceLink>http://atlantic2126.serverprofi24.com/Android/Service/dga.xml</serviceLink>
         <player>p1</player>
         <auth>1</auth>
     </item>
     */

    public static String KEY_ITEM = "item";
    public static String KEY_TITLE = "title";
    public static String KEY_DESCRIPTION = "description";
    public static String KEY_IMAGE_SERVICE = "imageService";
    public static String KEY_SERVICE_LINK = "serviceLink";
    public static String KEY_PLAYER = "player";
    public static String KEY_AUTH = "auth";
    public static String KEY_LINK = "link";
    public static String KEY_IMAGE = "image";

    private String title;
    private String description;
    private String imageService;
    private String serviceLink;
    private String player;
    private String auth;

    /**
     <item>
         <title>BB1</title>
         <link>http://85.25.218.196:8081/streamrtmp/bb1/playlist.m3u8</link>
         <description>BB1</description>
         <image>https://1.bp.blogspot.com/-iTm1vojRFU0/WHNzL9_HpkI/AAAAAAAAADY/uH6Mxg1AIaA3mu4gYFqHeGNn8dQ9zGQ0QCLcB/s320/big-b-1.jpg</image>
         <player>1</player>
     </item>
     */

    private String link;
    private String image;

    public RssFeedObject(){}

    public RssFeedObject(String title, String description, String imageService, String serviceLink, String player, String auth){
        this.title = title;
        this.description = description;
        this.imageService = imageService;
        this.serviceLink = serviceLink;
        this.player = player;
        this.auth = auth;
    }

    public RssFeedObject(String title, String link, String description, String image, String player){
        this.title = title;
        this.link = link;
        this.description = description;
        this.image = image;
        this.player = player;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getImageService(){
        return this.imageService;
    }

    public String getServiceLink(){
        return this.serviceLink;
    }

    public String getPlayer(){
        return this.player;
    }

    public String getAuth(){
        return this.auth;
    }

    public String getLink(){
        return this.link;
    }

    public String getImage(){
        return this.image;
    }
}

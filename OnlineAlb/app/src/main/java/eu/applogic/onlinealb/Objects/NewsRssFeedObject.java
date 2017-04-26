package eu.applogic.onlinealb.Objects;

/**
 * Created by makis on 4/5/2017.
 */

public class NewsRssFeedObject {

    /**
     <item>
         <title>KQZ-ja ka pranuar dokumentin për emërimin e Jabllanoviqit në krye të Listës Serbe</title>
         <link>http://www.gazetaexpress.com/lajme/kqz-ja-ka-pranuar-dokumentin-per-emerimin-e-jabllanoviqit-ne-krye-te-listes-serbe-351963/</link>
         <guid>http://www.gazetaexpress.com/lajme/kqz-ja-ka-pranuar-dokumentin-per-emerimin-e-jabllanoviqit-ne-krye-te-listes-serbe-351963/</guid>
         <description>Kryetarja e KQZ-së, Valdete Daka, ka konfirmuar për Express se ka pranuar nga Aleksander Jabllanoviqi dokumentin për ndryshimet e fundit brenda Listës serbe, ku sipas këtyre ndryshimeve Jabllanoviq i rikthehet pozitës së vjetër duke u bërë kështu kryetar i këtij subjekti.</description>
         <pubDate>Wed, 05 Apr 2017 13:48:42 +0200</pubDate>
         <enclosure  length="25514" type="" url="http://www.gazetaexpress.com/public/uploads/image/2017/04/633x347/jabllanoviq-s-euml-shpejti-asociacioni-i-komunave-serbe-hd_1491393033-6797129.jpg"/>
     </item>
     */

    public static String KEY_ITEM = "item";
    public static String KEY_TITLE = "title";
    public static String KEY_LINK = "link";
    public static String KEY_GUID = "guid";
    public static String KEY_DESCRIPTION = "description";
    public static String KEY_PUB_DATE = "pubDate";
    public static String KEY_ENCLOSURE = "enclosure";

    private String title;
    private String link;
    private String guid;
    private String description;
    private String pubDate;
    private String enclosure;

    public NewsRssFeedObject(String title, String link, String guid, String description, String pubDate, String enclosure){
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.pubDate = pubDate;
        this.enclosure = enclosure;
    }

    public String getTitle(){
        return this.title;
    }

    public String getLink(){
        return this.link;
    }

    public String getGuid(){
        return this.guid;
    }

    public String getDescription(){
        return this.description;
    }

    public String getPubDate(){
        return this.pubDate;
    }

    public String getEnclosure(){
        return this.enclosure;
    }
}

package eu.applogic.onlinealb.HelperClasses;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import eu.applogic.onlinealb.Objects.NewsRssFeedObject;
import eu.applogic.onlinealb.Objects.RadioStationObject;
import eu.applogic.onlinealb.Objects.RssFeedObject;

/**
 * Created by makis on 4/4/2017.
 */

public class ParsingFunctions {

    private Context mContext;
    private XMLParser xml;

    private ArrayList<RssFeedObject> mData = new ArrayList<>();
    private ArrayList<NewsRssFeedObject> mNews = new ArrayList<>();
    private ArrayList<RadioStationObject> mRadio = new ArrayList<>();

    public ParsingFunctions(Context context){
        this.mContext = context;
        this.xml = new XMLParser();
    }

    public ArrayList<RssFeedObject> parseChannels(String responseBody){
        mData = new ArrayList<>();
        Document doc = xml.getDomElement(responseBody);
        NodeList channels = doc.getElementsByTagName(RssFeedObject.KEY_ITEM);
        DebugLogger.debug("Length of channels list is: "+channels.getLength());

        for (int i = 0; i < channels.getLength(); i++) {
            Element e = (Element) channels.item(i);

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
            String title = xml.getValue(e, RssFeedObject.KEY_TITLE);
            String description = xml.getValue(e, RssFeedObject.KEY_DESCRIPTION);
            String imageService = xml.getValue(e, RssFeedObject.KEY_IMAGE_SERVICE);
            String serviceLink = xml.getValue(e, RssFeedObject.KEY_SERVICE_LINK);
            String player = xml.getValue(e, RssFeedObject.KEY_PLAYER);
            String auth = xml.getValue(e, RssFeedObject.KEY_AUTH);

            RssFeedObject rssFeedObject = new RssFeedObject(title, description, imageService, serviceLink, player, auth);
            if(!title.trim().toLowerCase().equals("settings")){
                mData.add(rssFeedObject);
            }
        }

        return mData;
    }

    public ArrayList<RssFeedObject> parseSpecificChannels(String responseBody){
        mData = new ArrayList<>();
        Document doc = xml.getDomElement(responseBody);
        NodeList channels = doc.getElementsByTagName(RssFeedObject.KEY_ITEM);
        DebugLogger.debug("Length of channels list is: "+channels.getLength());

        for (int i = 0; i < channels.getLength(); i++) {
            Element e = (Element) channels.item(i);

            /**
             <item>
             <title>BB1</title>
             <link>http://85.25.218.196:8081/streamrtmp/bb1/playlist.m3u8</link>
             <description>BB1</description>
             <image>https://1.bp.blogspot.com/-iTm1vojRFU0/WHNzL9_HpkI/AAAAAAAAADY/uH6Mxg1AIaA3mu4gYFqHeGNn8dQ9zGQ0QCLcB/s320/big-b-1.jpg</image>
             <player>1</player>
             </item>
             */
            String title = xml.getValue(e, RssFeedObject.KEY_TITLE);
            String link = xml.getValue(e, RssFeedObject.KEY_LINK);
            String description = xml.getValue(e, RssFeedObject.KEY_DESCRIPTION);
            String image = xml.getValue(e, RssFeedObject.KEY_IMAGE);
            String player = xml.getValue(e, RssFeedObject.KEY_PLAYER);

            RssFeedObject rssFeedObject = new RssFeedObject(title,link, description, image, player);
            mData.add(rssFeedObject);
        }

        return mData;
    }

    public ArrayList<NewsRssFeedObject> parseRssNews(String responseBody){
        mNews = new ArrayList<>();
        Document doc = xml.getDomElement(responseBody);
        NodeList news = doc.getElementsByTagName(NewsRssFeedObject.KEY_ITEM);
        DebugLogger.debug("Length of news list is: "+news.getLength());

        for (int i = 0; i < news.getLength(); i++) {
            Element e = (Element) news.item(i);

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

            String title = xml.getValue(e, NewsRssFeedObject.KEY_TITLE);
            String link = xml.getValue(e, NewsRssFeedObject.KEY_LINK);
            String guid = xml.getValue(e, NewsRssFeedObject.KEY_GUID);
            String description = xml.getValue(e, NewsRssFeedObject.KEY_DESCRIPTION);
            String pubDate = xml.getValue(e, NewsRssFeedObject.KEY_PUB_DATE);

            NodeList enclosureNode = e.getElementsByTagName(NewsRssFeedObject.KEY_ENCLOSURE);
            Element enclosureElement = (Element) enclosureNode.item(0);
            String enclosure = "";
            if(enclosureElement!=null){
                enclosure = enclosureElement.getAttribute("url");
                DebugLogger.debug("Enclosure value is: "+enclosure);
            }

            NewsRssFeedObject newsRssFeedObject = new NewsRssFeedObject(title, link, guid, description, pubDate, enclosure);
            mNews.add(newsRssFeedObject);
        }

        return mNews;
    }

    public ArrayList<RadioStationObject> parseRadioStations(String responseBody){
        mRadio = new ArrayList<>();
        Document doc = xml.getDomElement(responseBody);
        NodeList radios = doc.getElementsByTagName(RadioStationObject.KEY_STATION);
        DebugLogger.debug("Length of radio list is: "+radios.getLength());

        for (int i = 0; i < radios.getLength(); i++) {
            Element e = (Element) radios.item(i);

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
            String id = e.getAttribute(RadioStationObject.KEY_ID);
            String name = e.getAttribute(RadioStationObject.KEY_NAME);
            String dial = e.getAttribute(RadioStationObject.KEY_DIAL);
            String type = e.getAttribute(RadioStationObject.KEY_TYPE);
            String image = e.getAttribute(RadioStationObject.KEY_IMAGE);
            String status = e.getAttribute(RadioStationObject.KEY_STATUS);
            String cc = e.getAttribute(RadioStationObject.KEY_CC);
            String cn = e.getAttribute(RadioStationObject.KEY_CN);
            String rn = e.getAttribute(RadioStationObject.KEY_RN);
            String streaming1URL = e.getAttribute(RadioStationObject.KEY_STREAM_URL_1);
            String streaming1Type = e.getAttribute(RadioStationObject.KEY_STREAM_TYPE_1);
            String streaming2URL = e.getAttribute(RadioStationObject.KEY_STREAM_URL_2);
            String streaming2Type = e.getAttribute(RadioStationObject.KEY_STREAM_TYPE_2);
            String lastUpdate = e.getAttribute(RadioStationObject.KEY_LAST_UPDATE);

            RadioStationObject radioStationObject = new RadioStationObject(id, name, dial, type, image, status, cc, cn, rn, streaming1URL, streaming1Type, streaming2URL, streaming2Type, lastUpdate);
            mRadio.add(radioStationObject);
        }

        return mRadio;
    }
}

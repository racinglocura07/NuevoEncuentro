package coop.nuevoencuentro.nofuemagia.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jlionti on 02/08/2016. No Fue Magia
 */
public class XMLNuestrasVoces {
    public String title;
    public String link;
    public String description;
    public String pubDate;

    public static List<XMLNuestrasVoces> parse(String response, boolean pagina) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(response));
        parser.nextTag();

        List<XMLNuestrasVoces> lista = new ArrayList<>();
        XMLNuestrasVoces item = new XMLNuestrasVoces();
        String text = null;

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("item")) {
                        item = new XMLNuestrasVoces();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("item")) {
                        lista.add(item);
                    } else if (tagname.equalsIgnoreCase("description")) {
                        if (pagina && text != null) {
                            text = text.replaceAll("(?i)<em.*?>.*?</em>", "");
                            text = text.replaceAll("\t", "");
                            text = text.replace("<br />", "");
                        }

                        item.description = text;
                    } else if (tagname.equalsIgnoreCase("title")) {
                        item.title = text;
                    } else if (tagname.equalsIgnoreCase("link")) {
                        item.link = text;
                    } else if (tagname.equalsIgnoreCase("pubDate")) {
                        item.pubDate = text;
                    }
                    break;

                default:
                    break;
            }

            eventType = parser.next();
        }

        return lista;
    }

}

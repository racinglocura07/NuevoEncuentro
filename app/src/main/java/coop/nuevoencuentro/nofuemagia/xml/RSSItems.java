package coop.nuevoencuentro.nofuemagia.xml;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jlionti on 02/08/2016. No Fue Magia
 */
public class RSSItems {
    public String Titulo;
    public String Link;
    public String Descripcion;
    public String Creador;
    public Date FechaPublicado;

    public RSSItems(String title, String description, String creator, String link, String pubDate) {
        Titulo = title;
        Link = link;
        if (creator != null)
            Creador = "<strong>" + creator + "</strong> - ";
        Descripcion = description;

        //Sun, 07 Aug 2016 04:00:00 -0300
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.US);
        try {
            FechaPublicado = format.parse(pubDate.trim());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getTitulo() {
        return Titulo;
    }

    public String getLink() {
        return Link;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getCreador() {
        return Creador;
    }

    public String getFechaPublicado() {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.getDefault());

        return FechaPublicado == null ? "" : df.format(FechaPublicado);
    }

    public static List<RSSItems> parse(String response, boolean pagina) {
        List<RSSItems> lista = new ArrayList<>();

        Document doc = Jsoup.parse(response, "", Parser.xmlParser());
        for (Element e : doc.select("item")) {
            String title = e.select(("title")).text();
            if (title.contains("paz, pan y trabajo"))
                System.out.println(title);
            String description = e.select(("description")).text();
            if (pagina) {
                Document doca = Jsoup.parse(description);
                Element eme = doca.select("em").first();
                if (eme != null) {
                    String em = eme.text();
                    eme.remove();
                    description = doca.text();
                    title = em + " - " + title;
                } else
                    title = "<em>Últimas</em> - " + title;

            } else {
                Document doca = Jsoup.parse(description);
                Element eme = doca.select("p").last();
                eme.remove();
                description = doca.text();
            }
            String link = e.select(("link")).text();
            String creator = e.select(("dc|creator")).text();
            String pubDate = e.select(("pubDate")).text();

            if (TextUtils.isEmpty(creator))
                creator = null;

            lista.add(new RSSItems(title, description, creator, link, pubDate));
        }

        return lista;
    }

    /*public static List<RSSItems> parse(String response, boolean pagina) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(response));
        parser.nextTag();

        List<RSSItems> lista = new ArrayList<>();
        RSSItems item = new RSSItems();
        String text = null;

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("item")) {
                        item = new RSSItems();
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
    }*/

}

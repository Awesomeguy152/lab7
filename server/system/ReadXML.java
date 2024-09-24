package org.example.server.system;


import org.example.data.*;
import org.example.server.manager.CollectionManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class ReadXML {
    public static void read(CollectionManager collectionManager, String xmlFilePath) {
        try {
            File xmlFile = new File(xmlFilePath);

            if (xmlFile.length() == 0) {
                System.out.println("Файл пуст");
                return;
            }

            if (!xmlFile.canRead()){
                System.out.println("Невозможно прочитать файл (ошибка доступа)");
                System.exit(1);
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("musicBand");

            DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z uuuu", Locale.US);

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    long id = Long.parseLong(eElement.getElementsByTagName("id").item(0).getTextContent());

                    ZonedDateTime crDate = ZonedDateTime.parse(eElement.getElementsByTagName("creationDate").item(0).getTextContent());
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();

                    long x = Long.parseLong(eElement.getElementsByTagName("x").item(0).getTextContent());
                    long y = Long.parseLong(eElement.getElementsByTagName("y").item(0).getTextContent());

                    Integer numberOfParticipants = Integer.parseInt(eElement.getElementsByTagName("numberOfParticipants").item(0).getTextContent());
                    String genre = eElement.getElementsByTagName("genre").item(0).getTextContent();

                    String nameOfFrontMan = eElement.getElementsByTagName("name").item(1).getTextContent();
                    Date date = (Date) formatter.parse(eElement.getElementsByTagName("birthday").item(0).getTextContent());
                    int weight = Integer.parseInt(eElement.getElementsByTagName("weight").item(0).getTextContent());
                    Color eyeColor = Color.valueOf(eElement.getElementsByTagName("eyeColor").item(0).getTextContent());

                    MusicBand musicBand = new MusicBand();
                    musicBand.setCreationDate(crDate);
                    musicBand.setId(id);
                    musicBand.setName(name);
                    musicBand.setCoordinates(new Coordinates((int) x, y));
                    musicBand.setNumberOfParticipants(numberOfParticipants);
                    musicBand.setGenre(MusicGenre.valueOf(genre));

                    Person person = new Person();
                    person.setName(nameOfFrontMan);
                    person.setBirthday(date);
                    person.setWeight(weight);
                    person.setEyeColor(eyeColor);

                    musicBand.setFrontMan(person);
                    collectionManager.addMusicBand(musicBand);
                }
            }

            System.out.println("Данные успешно загружены из файла.");
        } catch (SAXException | ParserConfigurationException | IOException | ParseException e) {
            System.out.println("Ошибка при загрузке данных\n" + e.getMessage());
            System.exit(-1);
        }
    }
}

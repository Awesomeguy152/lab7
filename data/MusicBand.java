package org.example.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class MusicBand implements Comparable<MusicBand>, Serializable {
    private long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Integer numberOfParticipants;
    private MusicGenre genre;
    private Person frontMan;
    private static long id_counter = 0;

    public MusicBand() {
        this.creationDate = ZonedDateTime.now();
        this.id = id_counter;
        id_counter += 1;
    }

    public long getId() {
        return id;
    }

    public MusicBand setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public Person getFrontMan() {
        return frontMan;
    }

    public void setFrontMan(Person frontMan) {
        this.frontMan = frontMan;
    }

    @Override
    public int compareTo(MusicBand other) {
        return Long.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", genre=" + genre +
                ", frontMan=" + frontMan +
                '}';
    }


    public void updateFrom(MusicBand newBand) {
        this.name = newBand.name;
        this.coordinates = newBand.coordinates;
        if (newBand == null) {
            throw new IllegalArgumentException("Объект для обновления не может быть null.");
        }
    }

    public String toXML() {
        return "<musicBand>\n" +
                "  <id>" + id + "</id>\n" +
                "  <creationDate>" + creationDate + "</creationDate>\n" +
                "  <name>" + name + "</name>\n" +
                "  <coordinates>\n" +
                "    <x>" + coordinates.getX() + "</x>\n" +
                "    <y>" + coordinates.getY() + "</y>\n" +
                "  </coordinates>\n" +
                "  <numberOfParticipants>" + numberOfParticipants + "</numberOfParticipants>\n" +
                "  <genre>" + genre + "</genre>\n" +
                frontMan.toXML() + "\n" +
                "</musicBand>";
    }

}


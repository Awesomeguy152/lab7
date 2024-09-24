package org.example.server.manager;

import org.example.data.MusicBand;

import java.util.TreeSet;

/**
 * Менеджер для управления коллекцией MusicBand.
 */
import java.util.concurrent.ConcurrentSkipListSet;

public class CollectionManager {
    private final ConcurrentSkipListSet<MusicBand> musicBands = new ConcurrentSkipListSet<>();

    /*
     * Добавляет новый элемент в коллекцию.
     * @param musicBand элемент для добавления
     */
    public void addMusicBand(MusicBand musicBand) {
        musicBands.add(musicBand);
    }

    /*
     * Удаляет элемент из коллекции.
     * @param musicBand элемент для удаления
     * @return true, если элемент был удален
     */
    public boolean removeMusicBand(MusicBand musicBand) {
        return musicBands.remove(musicBand);
    }

    /*
     * Получает элемент коллекции по ID.
     * @param id идентификатор элемента
     * @return элемент коллекции
     */
    public MusicBand getMusicBandById(long id) {
        for (MusicBand musicBand : musicBands) {
            if (musicBand.getId() == id) {
                return musicBand;
            }
        }
        return null;
    }

    public ConcurrentSkipListSet<MusicBand> getMusicBands() {
        return musicBands;
    }

    public void clear() {
        musicBands.clear();
    }

    public String size() {
        return String.valueOf(musicBands.size());
    }
}


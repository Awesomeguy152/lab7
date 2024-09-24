package org.example.data;

import java.util.Comparator;

public class MusicBandComparatorByName implements Comparator<MusicBand> {

    @Override
    public int compare(MusicBand o1, MusicBand o2) {
            return o1.getName().compareTo(o2.getName());
    }
}

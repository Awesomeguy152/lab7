package org.example.data;

import java.util.Comparator;

public class MusicBandComparator implements Comparator<MusicBand> {

    @Override
    public int compare(MusicBand band1, MusicBand band2) {
        return band1.getNumberOfParticipants() - band2.getNumberOfParticipants();
    }

}

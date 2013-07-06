package com.vijayrc.scribbles.radio.repository;

import com.vijayrc.scribbles.radio.data.DataSetup;
import com.vijayrc.scribbles.radio.documents.Song;
import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("singleton")
public class AllSongs extends BaseRepo<Song> {
    @Autowired
    protected AllSongs(CouchDbConnector db) {
        super(Song.class, db);
    }

    @DataSetup(order = 2, description = "songs setup")
    public void addData() {
    }

}

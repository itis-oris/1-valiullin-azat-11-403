package com.itis403.app.service;

import com.itis403.app.dao.SongDao;
import com.itis403.app.model.Song;
import java.util.List;

public class SongService {

    private final SongDao songDao;

    public SongService(SongDao songDao) {
        this.songDao = songDao;
    }

    public List<Song> getSongsByArtist(Long artistId) {
        return songDao.findByArtistId(artistId);
    }

    public void uploadSong(Long artistId, String title, String genre, String fileUrl, Integer duration, Long fileSize) {
        Song song = new Song(artistId, title, genre, fileUrl, duration, fileSize);
        songDao.save(song);
    }

    public void approveSong(Long songId) {
        songDao.approveSong(songId);
    }

    public List<Song> getPendingSongs() {
        return songDao.findPendingSongs();
    }
}
package com.poupa.vinylmusicplayer.glide.audiocover;

import android.media.MediaMetadataRetriever;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AudioFileCoverFetcher implements DataFetcher<InputStream> {
    private final AudioFileCover model;
    private FileInputStream stream;

    public AudioFileCoverFetcher(AudioFileCover model) {
        this.model = model;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        InputStream data;
        try {
            retriever.setDataSource(model.filePath);
            byte[] picture = retriever.getEmbeddedPicture();
            if (picture != null) {
                data = new ByteArrayInputStream(picture);
            } else {
                data = fallback(model.filePath);
            }
            callback.onDataReady(data);
        } catch (FileNotFoundException e) {
            callback.onLoadFailed(e);
        } finally {
            try {
                retriever.release();
            } catch (IOException ioe) {
                callback.onLoadFailed(ioe);
            }
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    private static final String[] FALLBACKS =
            {"cover.jpg", "album.jpg", "folder.jpg", "cover.png", "album.png", "folder.png"};

    private InputStream fallback(String path) throws FileNotFoundException {
        try {
            MP3File mp3File = new MP3File(path);
            if (mp3File.hasID3v2Tag()) {
                Artwork art = mp3File.getTag().getFirstArtwork();
                if (art != null) {
                    byte[] imageData = art.getBinaryData();
                    return new ByteArrayInputStream(imageData);
                }
            }
        } catch (Exception ignored) { }

        File parent = new File(path).getParentFile();
        for (String fallback : FALLBACKS) {
            File cover = new File(parent, fallback);
            if (cover.exists()) {
                return stream = new FileInputStream(cover);
            }
        }
        return null;
    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
            }
        }
    }

    @Override
    public void cancel() {
    }
}

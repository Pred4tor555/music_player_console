import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.junit.jupiter.api.Assertions.*;


class PlayerTest {

    private Player player = new Player();

    @Test
    void createPlaylist() {
        player.createPlaylist("My Playlist");
        assertFalse(player.getPlaylists().isEmpty(), "Playlist should be created");
        assertEquals("My Playlist", player.getPlaylists().get(0).getName(), "Playlist name should match");
    }

    @Test
    void removePlaylist() {
        player.createPlaylist("My Playlist");
        assertFalse(player.getPlaylists().isEmpty(), "Playlist should be created");
        player.removePlaylist(0);
        assertTrue(player.getPlaylists().isEmpty(), "Playlist should be removed");
    }

    @Test
    void addSongToPlaylist() {
        player.createPlaylist("My Playlist");
        player.addSongToPlaylist(0, "Song Title", "Song Artist");
        assertFalse(player.getPlaylists().get(0).getSongs().isEmpty(), "Song should be added to the playlist");
        assertEquals("Song Title", player.getPlaylists().get(0).getSongs().get(0).getTitle(), "Song title should match");
        assertEquals("Song Artist", player.getPlaylists().get(0).getSongs().get(0).getArtist(), "Song artist should match");
    }

    @Test
    void removeSongFromPlaylist() {
        player.createPlaylist("My Playlist");
        player.addSongToPlaylist(0, "Song Title", "Song Artist");
        assertFalse(player.getPlaylists().get(0).getSongs().isEmpty(), "Song should be added to the playlist");
        player.selectPlaylist(0);
        player.removeSongFromPlaylist(0);
        assertTrue(player.getPlaylists().get(0).getSongs().isEmpty(), "Song should be removed from the playlist");
    }

    @Test
    void savePlaylist() {
        player.createPlaylist("My Playlist");
        player.addSongToPlaylist(0, "Song Title", "Song Artist");

        try {
            player.savePlaylist(0);
            assertTrue(Files.exists(Paths.get("playlist_My Playlist.txt")), "Playlist file should be created");
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(Paths.get("playlist_My Playlist.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void loadPlaylist() {
        try {
            // Create a test playlist file
            Files.write(Paths.get("playlist_test.txt"), "Song Title - Song Artist\n".getBytes());
            player.loadPlaylist("playlist_test.txt");

            assertFalse(player.getPlaylists().isEmpty(), "Playlist should be loaded");
            assertEquals("playlist_test", player.getPlaylists().get(0).getName(), "Playlist name should match the filename");
            assertEquals("Song Title", player.getPlaylists().get(0).getSongs().get(0).getTitle(), "Song title should match");
            assertEquals("Song Artist", player.getPlaylists().get(0).getSongs().get(0).getArtist(), "Song artist should match");
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(Paths.get("playlist_test.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
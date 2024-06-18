import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Song {
    private String title;
    private String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}

class Playlist {
    private String name;
    private List<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(int index) {
        if (index >= 0 && index < songs.size()) {
            songs.remove(index);
        } else {
            System.out.println("Песня с указанным номером не найдена.");
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Плейлист: ").append(name).append("\n");
        for (int i = 0; i < songs.size(); i++) {
            result.append(i+1).append(". ").append(songs.get(i)).append("\n");
        }
        return result.toString();
    }
}

class Player {
    private List<Playlist> playlists;
    private Playlist currentPlaylist;
    private int currentTrackIndex;

    public Player() {
        playlists = new ArrayList<>();
        currentPlaylist = null;
        currentTrackIndex = -1;
    }

    public void showMenu() {
        System.out.println("0 - выйти");
        System.out.println("1 - показать список песен");
        System.out.println("2 - создать плейлист (по имени)");
        System.out.println("3 - включить плейлист (по номеру)");
        System.out.println("4 - удалить плейлист (по номеру)");
        System.out.println("5 - добавить песню в плейлист (по номеру, по названию, по исполнителю)");
        System.out.println("6 - показать весь плейлист");
        System.out.println("7 - убрать песню из плейлиста (по номеру)");
        System.out.println("8 - включить предыдущий трек");
        System.out.println("9 - включить следующий трек");
        System.out.println("10 - повторить текущий трек");
        System.out.println("11 - сохранить плейлист (по номеру)");
        System.out.println("12 - загрузить плейлист");
    }

    public void showSongs() {
        boolean hasSongs = false;
        for (Playlist playlist : playlists) {
            if (!playlist.getSongs().isEmpty()) {
                System.out.println(playlist);
                hasSongs = true;
            }
        }
        if (!hasSongs) {
            System.out.println("Песни не найдены в добавленных плейлистах.");
        }
    }

    public void createPlaylist(String name) {
        playlists.add(new Playlist(name));
        System.out.println("Плейлист " + name + " создан.");
    }

    public void selectPlaylist(int index) {
        if (index >= 0 && index < playlists.size()) {
            currentPlaylist = playlists.get(index);
            currentTrackIndex = -1;
            System.out.println("Плейлист " + currentPlaylist.getName() + " выбран.");
        } else {
            System.out.println("Плейлист с указанным номером не найден.");
        }
    }

    public void removePlaylist(int index) {
        if (index >= 0 && index < playlists.size()) {
            playlists.remove(index);
            System.out.println("Плейлист удален.");
        } else {
            System.out.println("Плейлист с указанным номером не найден.");
        }
    }

    public void addSongToPlaylist(int playlistIndex, String title, String artist) {
        if (playlistIndex >= 0 && playlistIndex < playlists.size()) {
            Song song = new Song(title, artist);
            playlists.get(playlistIndex).addSong(song);
            System.out.println("Песня добавлена в плейлист.");
        } else {
            System.out.println("Плейлист с указанным номером не найден.");
        }
    }

    public void showCurrentPlaylist() {
        if (currentPlaylist != null) {
            System.out.println(currentPlaylist);
        } else {
            System.out.println("Плейлист не выбран.");
        }
    }

    public void removeSongFromPlaylist(int index) {
        if (currentPlaylist != null) {
            currentPlaylist.removeSong(index);
        } else {
            System.out.println("Плейлист не выбран.");
        }
    }

    public void playPreviousTrack() {
        if (currentPlaylist != null) {
            if (currentTrackIndex > 0) {
                currentTrackIndex--;
                System.out.println("Включен предыдущий трек.");
            } else {
                System.out.println("Это первый трек в плейлисте.");
            }
        } else {
            System.out.println("Плейлист не выбран.");
        }
    }

    public void playNextTrack() {
        if (currentPlaylist != null) {
            if (currentTrackIndex < currentPlaylist.getSongs().size() - 1) {
                currentTrackIndex++;
                System.out.println("Включен следующий трек.");
            } else {
                System.out.println("Это последний трек в плейлисте.");
            }
        } else {
            System.out.println("Плейлист не выбран.");
        }
    }

    public void repeatCurrentTrack() {
        if (currentPlaylist != null && currentTrackIndex != -1 && currentTrackIndex < currentPlaylist.getSongs().size()) {
            System.out.println("Текущий трек: " + currentPlaylist.getSongs().get(currentTrackIndex) + " повторяется.");
        } else {
            System.out.println("Плейлист не выбран или трек не воспроизводится.");
        }
    }

    // Добавляем исключение IOException в сигнатуру метода
    public void savePlaylist(int index) throws IOException {
        if (index >= 0 && index < playlists.size()) {
            Playlist playlistToSave = playlists.get(index);
            String filename = "playlist_" + playlistToSave.getName() + ".txt"; // Создаем имя файла на основе названия плейлиста
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                // Записываем каждую песню из плейлиста в файл
                for (Song song : playlistToSave.getSongs()) {
                    writer.write(song.getTitle() + " - " + song.getArtist());
                    writer.newLine(); // Добавляем перевод строки для каждой песни
                }
            }
            System.out.println("Плейлист " + playlistToSave.getName() + " сохранен в файл " + filename);
        } else {
            System.out.println("Плейлист с указанным номером не найден.");
        }
    }

    public void loadPlaylist(String filename) throws IOException {
        Playlist playlist = new Playlist(filename.replaceFirst("[.][^.]+$", "")); // Получаем название плейлиста из имени файла
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String title = parts[0];
                    String artist = parts[1];
                    Song song = new Song(title, artist);
                    playlist.addSong(song);
                }
            }
        }
        playlists.add(playlist); // Добавляем загруженный плейлист в список плейлистов
        System.out.println("Плейлист " + playlist.getName() + " загружен из файла " + filename);
    }


    public List<Playlist> getPlaylists() {       // для тестов
        return playlists;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Player player = new Player();

        while (true) {
            System.out.println();
            System.out.print("Введите любой символ чтобы продолжить ");
            scanner.nextLine();


            player.showMenu();
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 0:
                    System.out.println("Выход из программы.");
                    return;
                case 1:
                    player.showSongs();
                    break;
                case 2:
                    System.out.print("Введите название плейлиста: ");
                    String playlistName = scanner.nextLine();
                    player.createPlaylist(playlistName);
                    break;
                case 3:
                    System.out.print("Введите номер плейлиста: ");
                    int playlistIndex = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    player.selectPlaylist(playlistIndex - 1);
                    break;
                case 4:
                    System.out.print("Введите номер плейлиста: ");
                    playlistIndex = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    player.removePlaylist(playlistIndex - 1);
                    break;
                case 5:
                    System.out.print("Введите номер плейлиста: ");
                    playlistIndex = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.print("Введите название песни: ");
                    String title = scanner.nextLine();
                    System.out.print("Введите исполнителя: ");
                    String artist = scanner.nextLine();
                    player.addSongToPlaylist(playlistIndex - 1, title, artist);
                    break;
                case 6:
                    player.showCurrentPlaylist();
                    break;
                case 7:
                    System.out.print("Введите номер песни: ");
                    int songIndex = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    player.removeSongFromPlaylist(songIndex - 1);
                    break;
                case 8:
                    player.playPreviousTrack();
                    break;
                case 9:
                    player.playNextTrack();
                    break;
                case 10:
                    player.repeatCurrentTrack();
                    break;
                case 11:
                    System.out.print("Введите номер плейлиста: ");
                    playlistIndex = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    try {
                        player.savePlaylist(playlistIndex - 1);
                    } catch (IOException e) {
                        System.out.println("Ошибка при сохранении плейлиста: " + e.getMessage());
                    }
                    break;
                case 12:
                    System.out.print("Введите имя файла для загрузки плейлиста: ");
                    String filename = scanner.nextLine();
                    try {
                        player.loadPlaylist(filename);
                    } catch (IOException e) {
                        System.out.println("Ошибка при загрузке плейлиста: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Некорректный выбор.");
                    break;
            }
        }
    }
}
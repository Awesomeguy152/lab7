package org.example.server.manager;

import org.example.server.handlers.HashHandler;
import org.example.data.*;
import org.example.server.system.Receiver;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

public class DataBaseManager {
    private static String URL = "jdbc:postgresql://pg:5432/studs";
    private static String username = "s439689";
    private static String password = "iVtsidmTUv0fp9fK";
    private static Connection connection;
    private static DataBaseManager instance = null;
    private static final String ADD_USER_REQUEST = "INSERT INTO users (login, password) VALUES (?, ?)";
    private static final String GET_USER_BY_USERNAME = "SELECT id, login, password FROM users WHERE login = ?";
    private static final String INSERT_MUSICBAND = "INSERT INTO musicband (name, owner_id, coordinates_x, coordinates_y, creation_date, " +
            "number_of_participants, genre, front_man_name, front_man_birthday, front_man_weight, front_man_eye_color) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String GET_OWNER_BY_KEY = "SELECT owner_id FROM musicband WHERE id = ?";
    private static final String REMOVE_MUSICBAND = "DELETE FROM musicband WHERE id = ? AND owner_id=?";
    private static final String CLEAR_MUSICBAND = "DELETE FROM musicband WHERE owner_id=?";
    private static final String UPDATE_MUSICBAND_BY_ID = "UPDATE musicband SET " +
            "name = ?, owner_id = ?, coordinates_x = ?, coordinates_y = ?, creation_date = ?, number_of_participants = ?," +
            " genre = ?, front_man_name = ?, front_man_birthday = ?, front_man_weight = ?, front_man_eye_color = ? WHERE id = ?";


    public DataBaseManager() {
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
        // DriverManager(JDBC(библиотека
    }

    public static void connectToDataBase() {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Connection is ready");
        } catch (SQLException e) {
            System.out.println("Error while connecting to database");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    // корректность имени + пароля
    public static boolean checkUser(String login, String password) {
        try {
            PreparedStatement getStatement = connection.prepareStatement(GET_USER_BY_USERNAME);
            getStatement.setString(1, login);
            ResultSet rs = getStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(HashHandler.encryptString(password));
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // регистрация пользователя
    public static boolean insertUser(String username, String password) {
        try (PreparedStatement addStatement = connection.prepareStatement(ADD_USER_REQUEST)) {
            addStatement.setString(1, username);
            addStatement.setString(2, HashHandler.encryptString(password));
            addStatement.executeUpdate();
            addStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't register user. Reason: " + e.getMessage());
            return false;
        }
    }

    public static int getUserId(String login) {
        try {
            PreparedStatement getStatement = connection.prepareStatement(GET_USER_BY_USERNAME);
            getStatement.setString(1, login);
            ResultSet rs = getStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }


    private static void insertMusicBandDataIntoStatement(MusicBand musicBand, PreparedStatement statement) throws SQLException {
        statement.setString(1, musicBand.getName());
        statement.setDouble(3, musicBand.getCoordinates().getX());
        statement.setDouble(4, musicBand.getCoordinates().getY());

        statement.setDate(5, Date.valueOf(musicBand.getCreationDate().toLocalDate()));
        statement.setInt(6, musicBand.getNumberOfParticipants());
        statement.setString(7, musicBand.getGenre().toString());
        statement.setString(8, musicBand.getFrontMan().getName());
        statement.setDate(9, Date.valueOf(musicBand.getCreationDate().toLocalDate()));
        statement.setInt(10, musicBand.getFrontMan().getWeight());
        statement.setString(11, musicBand.getFrontMan().getEyeColor().toString());
    }

    private static MusicBand extractMusicBandFromEntry(ResultSet rs) throws SQLException {
        MusicBand musicBand = new MusicBand();
        musicBand.setId(rs.getInt("id"));
        musicBand.setName(rs.getString("name"));
        musicBand.setCoordinates(new Coordinates((long) rs.getDouble("coordinates_x"), (long) rs.getDouble("coordinates_y")));
        // musicBand.setCreationDate(ZonedDateTime.from(rs.getDate("creation_date").toLocalDate()));
        musicBand.setNumberOfParticipants(rs.getInt("number_of_participants"));
        musicBand.setGenre(MusicGenre.valueOf(rs.getString("genre")));
        Person person = new Person();
        person.setName(rs.getString("front_man_name"));
        person.setWeight(rs.getInt("front_man_weight"));
        person.setEyeColor(Color.valueOf(rs.getString("front_man_eye_color")));
        person.setBirthday(rs.getDate("front_man_birthday"));
        musicBand.setFrontMan(person);
        return musicBand;
    }

    public static void getDataFromDatabase(CollectionManager collectionManager) {
        collectionManager.clear();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM musicband");
            while (rs.next()) {
                try {
                    MusicBand musicBand = extractMusicBandFromEntry(rs);
                    collectionManager.addMusicBand(musicBand);

                } catch (Exception e) {
                    System.out.println("Invalid music band entry in DB. Reason: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Couldn't load data from DB. Reason: " + e.getMessage());
            System.exit(-1);
        }
    }


    public static boolean removeMusicBandByName(String login, String key, CollectionManager collectionManager) {
        int userId = getUserId(login); // получаем id пользователя, от которого пришел запрос
        if (userId == getOwnerId(key)) {
            try (PreparedStatement statement = connection.prepareStatement(REMOVE_MUSICBAND)) {
                statement.setInt(1, Integer.parseInt(key)); // Используйте setInt для int значений
                statement.setInt(2, getOwnerId(key));

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    getDataFromDatabase(collectionManager); // Обновление коллекции
                    return true; // Возвращает true, если удаление произошло
                } else {
                    System.out.println("Музыкальная группа не найдена");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Couldn't remove music band. Reason: " + e.getMessage());
                return false;
            }
        } else {
            return false; // Неправильный владелец
        }
    }


    public static int getOwnerId(String key) {
        try {
            // GET_OWNER_BY_KEY = "SELECT owner_id FROM musicband WHERE id = ?"
            PreparedStatement getStatement = connection.prepareStatement(GET_OWNER_BY_KEY);
            getStatement.setInt(1, Integer.parseInt(key));
            ResultSet rs = getStatement.executeQuery();
            while (rs.next()) {
                return rs.getInt("owner_id");
            }
            return -1;
        } catch (Exception e) {
            return -2;
        }
    }

    public static boolean updateMusicBandById(int id, MusicBand musicBand, String login) {
        int userId = getUserId(login);
        if (userId == getOwnerId(String.valueOf(id))) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_MUSICBAND_BY_ID)) {
                insertMusicBandDataIntoStatement(musicBand, statement);
                statement.setInt(2, userId);
                statement.setInt(12, id);
                statement.executeUpdate();
                statement.close();
                return true;
            } catch (SQLException e) {
                System.out.println("Couldn't update music band. Reason: " + e.getMessage());
                return false;
            }
        } else return false;
    }

    public static int insertMusicBand(MusicBand musicBand, String login) {
        int id = getUserId(login);
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_MUSICBAND);
            insertMusicBandDataIntoStatement(musicBand, statement);
            statement.setInt(2, id); // Получаем owner_id из объекта Owner
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("id");
            }
            return 0;
        } catch (SQLException e) {
            System.out.println("Couldn't add organization. Reason: " + e.getMessage());
            return 0;
        }
    }

    public static void clear(CollectionManager collectionManager, String login) {
        int id = getUserId(login);
        try {
            PreparedStatement statement = connection.prepareStatement(CLEAR_MUSICBAND);
            statement.setInt(1, id); // Получаем owner_id из объекта Owner
            statement.executeUpdate();
            getDataFromDatabase(collectionManager);
        } catch (SQLException e) {
            System.out.println("Couldn't clear " + e.getMessage());
        }
    }

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        DataBaseManager.URL = URL;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DataBaseManager.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DataBaseManager.password = password;
    }


}

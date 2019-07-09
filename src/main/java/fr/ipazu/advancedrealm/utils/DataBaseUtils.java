package fr.ipazu.advancedrealm.utils;

import org.bukkit.Bukkit;

import java.sql.*;

public class DataBaseUtils {

    private Connection connection = null;
    private String irl, pers, mum;
    public DataBaseUtils(){

    }
    private void setAuth(String url, String user, String pass){
        this.irl = url;
        this.pers = user;
        this.mum = pass;
    }

    private void connect() throws SQLException {
        if(connection != null)
            disconnect();
        if(irl != null && pers != null && mum != null){
            connection = DriverManager.getConnection(irl, pers, mum);
            info("Connexion database établie avec succès!");
        }else err("Connexion impossible : il manque des informations.", "Connexion Database");

    }
    private void connectionDatabase(){
        String url =  "jdbc:mysql://154.49.211.66:3306/market";
        String user = "root";
        String pass = "grossemaman";
        setAuth(url, user, pass);
        try{
            connect();
        }catch (SQLException e){
            err(e.getMessage(), "Connexion database");
        }
    }
    public void checkLicense(){
       connectionDatabase();
        try {
            if (getLicenceActivation() == 0) {
                System.out.println("[advancedrealm] Your license is not activated, please contact iPazu@3982 on discord.");
                Bukkit.shutdown();
            }
            else
                System.out.println("[advancedrealm] License successfully activated");

        }catch (SQLException e){
            System.out.println("Your license is not activated, please contact iPazu@3982 on discord.");
            Bukkit.shutdown();
        }
    }
    private void info(String info){
        System.out.println("[Database] " + info);
    }

    private void err(String err, String type){
        info("Erreur (" + type + ") : " + err);
    }
    private Connection getConnection(){
        return connection;
    }

    private void disconnect() throws SQLException {
        if(!connection.isClosed()){
            connection.close();
             info("Connexion database fermée avec succès!");
        }
    }

    private PreparedStatement createStatement(String query) throws SQLException {
        if(connection.isClosed())
            connect();
        return connection.prepareStatement(query);
    }

    private int getLicenceActivation() throws SQLException {
        int activated = 0;
        PreparedStatement statement = createStatement("SELECT activated FROM licence WHERE licence = ?");
        statement.setString(1, "advancedrealm_hBt5DeMCPdbFWpt6");
        statement.executeQuery();
        ResultSet rs = statement.executeQuery();
        if(!rs.next()) {
            return activated;
        }
        activated = rs.getInt("activated");
        statement.close();
        return activated;
    }

}

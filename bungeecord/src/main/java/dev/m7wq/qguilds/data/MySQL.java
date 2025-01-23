package dev.m7wq.qguilds.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.roles.Role;
import net.md_5.bungee.config.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import dev.m7wq.qguilds.Plugin;
import org.checkerframework.checker.units.qual.A;

public class MySQL {

    HikariDataSource dataSource = new HikariDataSource();
    HikariConfig dataSourceConfig = new HikariConfig();

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public boolean connect(Configuration configuration) throws SQLException {
        boolean isConnected = initDatasource(configuration);
        if (isConnected)
            initializeDatabase();
        return isConnected;
    }

    public boolean isClosed() {
        return (this.dataSource == null || !this.dataSource.isRunning() || this.dataSource.isClosed());
    }

    public void disconnect() {
        if (isClosed())
            System.out.println("Guilds - Database is already closed!");
        this.dataSource.close();
        System.out.println("Guilds - Successfully disconnected from the database.");
    }

    public void initializeDatabase() {
        try (Connection connection = getConnection()) {
            String initSql = "CREATE TABLE IF NOT EXISTS guilds (" +
                    "    name VARCHAR(25)," +
                    "    tag VARCHAR(3)," +
                    "    tag_color VARCHAR(2)" +
                    ");";
            connection.createStatement().execute(initSql);

            // final load
            String initPlrsGuildsSQL = "CREATE TABLE IF NOT EXISTS guilds_players (" +
                    "    name VARCHAR(20),"+
                    "    ign VARCHAR(40)," +
                    "    role VARCHAR(25)," +
                    "    muted TINYINT(0)" +
                    ");";
            connection.createStatement().execute(initPlrsGuildsSQL);

            String statsSQL = "CREATE TABLE IF NOT EXISTS guilds_stats  (" +
                    "name VARCHAR(20)," +
                    "kills INT," +
                    "points INT" +
                    ");";
            connection.createStatement().execute(statsSQL);


            String rolesSQL = "CREATE TABLE IF NOT EXISTS guilds_roles (" +
                    "name VARCHAR(20)," +
                    "role VARCHAR(15)," +
                    "color VARCHAR(2)" +
                    ");";

            connection.createStatement().execute(rolesSQL);

            String permissionsSQL = "CREATE TABLE IF NOT EXISTS guilds_roles_permissions (" +
                    "name VARCHAR(20)," +
                    "role VARCHAR(15)," +
                    "permission VARCHAR(20)" +
                    ");";

            connection.createStatement().execute(permissionsSQL);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCacheData(Map<String, Guild> guildMap) {
        try (Connection connection = getConnection()) {



            for (Guild guild : guildMap.values()) {
                List<Member> memberList;
                if (guild.getOldName() == null) {
                    memberList = getMembers(guild.getGuildName());
                }else {
                    memberList = getMembers(guild.getOldName());
                }

                for (Member member : memberList){
                    if (!guildMap.get(guild.getGuildName()).getPlayersList().contains(member)) {
                        removePlayer(member.getIgn());
                    }else if(guildMap.get(guild.getGuildName()).getPlayersList().contains(member)){
                        for (Member mapMember : guild.getPlayersList()){
                            if (
                                    member.getIgn().equalsIgnoreCase(mapMember.getIgn()) &&
                                            !member.getRole().getName().equalsIgnoreCase(mapMember.getRole().getName()) ||
                                            member.isMuted() != mapMember.isMuted()
                            ){
                                updateData(mapMember);
                                break;
                            }
                        }
                    }
                }

                for (Member member : guild.getPlayersList()) {
                    String checkQuery = "SELECT * FROM guilds_players WHERE ign = ?";
                    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                        checkStmt.setString(1, member.getIgn());
                        ResultSet memberResultSet = checkStmt.executeQuery();


                        if (memberResultSet.next()) {


                            memberResultSet.close();
                            continue;
                        }

                        String insertQuery = "INSERT INTO guilds_players (name, ign, role, muted) VALUES (?,?,?,?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, guild.getGuildName());
                            insertStmt.setString(2, member.getIgn());
                            insertStmt.setString(3, member.getRole().getName());
                            insertStmt.setBoolean(4, member.isMuted());
                            insertStmt.executeUpdate();
                        }

                    }



                }

                try (PreparedStatement tagStatement = connection.prepareStatement("SELECT tag FROM guilds WHERE name = ?");
                      PreparedStatement tagColorStatement = connection.prepareStatement("SELECT tag_color FROM guilds WHERE name = ?");){

                    String guildName;

                    if(guild.getOldName() == null){
                        guildName = guild.getGuildName();
                    }else {
                        guildName = guild.getOldName();
                    }

                    tagColorStatement.setString(1,guildName);
                    tagStatement.setString(1,guildName);


                    if (tagStatement.executeQuery().next())
                        if (!tagStatement.executeQuery().getString("tag").equalsIgnoreCase(guild.getGuildTag()))
                            changeTag(guild.getGuildTag(),guildName);

                    if (tagColorStatement.executeQuery().next())
                        if (!tagColorStatement.executeQuery().getString("tag_color").equalsIgnoreCase(guild.getTagColor()))
                            changeTagColor(guild.getTagColor(),guildName);

                    if (guild.getOldName() != null){
                        renameTheGuild(guild.getOldName(),guild.getGuildName());
                    }


                }

                // handle roles & permission
                // and new and removed guilds
            }

            System.out.println("Guilds - SAVED CACHE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeGuild(String name){
        try (Connection connection = getConnection(); PreparedStatement statement= connection.prepareStatement("DELETE FROM guilds WHERE name = ?")){
            statement.setString(1,name);
            statement.addBatch();
            connection.setAutoCommit(false);
            statement.executeBatch();
            connection.commit();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateData(Member member) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE guilds_players SET muted = ? , role = ? WHERE ign = ?")) {
            statement.setBoolean(1, member.isMuted());
            statement.setString(2, member.getRole().getName());
            statement.setString(3, member.getIgn());
            statement.addBatch();
            connection.setAutoCommit(false);
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Guild> getGuilds() {
        HashMap<String, Guild> guildsMap = new HashMap<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM guilds");
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                String name = results.getString("name");
                String tag = results.getString("tag");

                String tagColor = results.getString("tag_color");


                guildsMap.put(name, new Guild(getRoles(name),tagColor, tag, name, getMembers(name)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guildsMap;
    }

    public List<Role> getRoles(String guildName){
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM guilds_roles WHERE name = ?")) {


            List<Role> roles = new ArrayList<>();


            statement.setString(1,guildName);

            ResultSet results = statement.executeQuery();

            while (results.next()){
                roles.add(new Role(
                        results.getString("role"),
                        results.getString("color"),
                        getPermissions(guildName,results.getString("role"))
                ));
            }


        return roles;

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

    }

    public List<Permission> getPermissions(String guildName, String role){
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM guilds_roles_permissions WHERE role = ?, name = ?")){

            preparedStatement.setString(1,role);
            preparedStatement.setString(2,guildName);

            ResultSet set = preparedStatement.executeQuery();

            List<Permission> permissions = new ArrayList<>();

            while (set.next()){
                permissions.add(Permission.getByName(set.getString("permission")));
            }

            return permissions;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void removePlayer(String ign){
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM guilds_players WHERE ign = ?")){
            statement.setString(1,ign);
            statement.addBatch();
            connection.setAutoCommit(false);
            statement.executeBatch();
            connection.commit();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Member> getMembers(String guild_name) {
        List<Member> playersList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM guilds_players WHERE name = ?")) {

            statement.setString(1, guild_name);
            ResultSet results = statement.executeQuery();


            while (results.next()) {
                String roleName = results.getString("role");
                Role role = new Role(
                        roleName,
                        getRoleColor(roleName,guild_name),
                        getPermissions(guild_name,roleName)


                );
                playersList.add(new Member(role, results.getString("ign"), results.getBoolean("muted")));
            }


            results.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playersList;
    }

    public String getRoleColor(String roleName, String guildName){
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT color FROM guilds_roles WHERE role = ?, name = ?")){
            statement.setString(1,roleName);
            statement.setString(2,guildName);

            ResultSet set = statement.executeQuery();


            if (set.next()) {

                return set.getString("color");

            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public void createGuild(Member creator, Guild guild) {



        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO guilds(name, tag, tag_color) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, guild.getGuildName());

            statement.setString(2, guild.getGuildTag());
            statement.setString(3, guild.getTagColor());

            statement.addBatch();
            connection.setAutoCommit(false);
            statement.executeBatch();
            connection.commit();

            String insertCreatorSql = "INSERT INTO guilds_players(name, ign, role, muted) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertCreatorStmt = connection.prepareStatement(insertCreatorSql)) {
                insertCreatorStmt.setString(1, guild.getGuildName());
                insertCreatorStmt.setString(2, creator.getIgn());
                insertCreatorStmt.setString(3, creator.getRole().getName());
                insertCreatorStmt.setBoolean(4, creator.isMuted());

                insertCreatorStmt.addBatch();
                connection.setAutoCommit(false);
                insertCreatorStmt.executeBatch();
                connection.commit();
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeTag(String tag,String guildName){



        String updateQuery = "UPDATE guilds SET tag = ? WHERE name = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setString(1,tag);
            statement.setString(2,guildName);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void changeTagColor(String tagColor,String guildName){



        String updateQuery = "UPDATE guilds SET tag_color = ? WHERE name = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setString(1,tagColor);
            statement.setString(2,guildName);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void renameTheGuild(String oldName, String newName){


        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();




        try (Connection connection = getConnection();
        PreparedStatement guildsMainQuery = connection.prepareStatement("UPDATE guilds SET name = ? WHERE name = ?");
        PreparedStatement playerGuildsQuery = connection.prepareStatement("UPDATE guilds_players SET name = ? WHERE name = ?")){

        guildsMainQuery.setString(1,newName);
        guildsMainQuery.setString(2,oldName);
        guildsMainQuery.executeUpdate();

        playerGuildsQuery.setString(1,newName);
        playerGuildsQuery.setString(2,oldName);
        playerGuildsQuery.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }


    }

    public boolean initDatasource(Configuration c) {
        try {
            String driver = c.getString("DATABASE.driver");
            String host = c.getString("DATABASE.host");
            int port = c.getInt("DATABASE.port");
            String username = c.getString("DATABASE.username");
            String password = c.getString("DATABASE.password");
            String database = c.getString("DATABASE.database");
            String databaseProperties = c.getString("DATABASE.databaseProperties");
            int maximumPoolSize = c.getInt("DATABASE.maximumPoolSize");

            this.dataSourceConfig.setPoolName("Guilds");
            this.dataSourceConfig.setMaximumPoolSize(maximumPoolSize);
            this.dataSourceConfig.setIdleTimeout(0L);
            this.dataSourceConfig.setUsername(username);
            this.dataSourceConfig.setPassword(password);
            this.dataSourceConfig.setJdbcUrl(driver + host + ":" + port + "/" + database + databaseProperties);
            this.dataSource = new HikariDataSource(this.dataSourceConfig);
            System.out.println("Guilds - Connected to database!");
            return true;
        } catch (Exception e) {
            System.out.println("Guilds - Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

package net.minebr.armazem.database.datasource;

import com.zaxxer.hikari.HikariConfig;
import lombok.val;
import net.minebr.armazem.database.datamanager.GenericDataManager;
import net.minebr.armazem.database.exception.DatabaseException;
import net.minebr.armazem.database.util.Utils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class HikariDataSource extends AbstractDataSource {

    private com.zaxxer.hikari.HikariDataSource dataSource;

    public HikariDataSource(String ip, String database, String user, String password) throws DatabaseException {
        openConnection(ip, database, user, password);
    }

    private void openConnection(String ip, String database, String user, String password) throws DatabaseException {
        try {

            String url = "jdbc:mysql://" + ip + "/" + database + "?autoReconnect=true";
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
            hikariConfig.setMaximumPoolSize(3);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            this.dataSource = new com.zaxxer.hikari.HikariDataSource(hikariConfig);
            Utils.debug(Utils.LogType.INFO, "conexão com mysql hikari inicializada com sucesso");
        } catch (Exception e) {
            throw new DatabaseException("não foi possivel iniciar conexão com banco de dados hikari", e);
        }
    }

    @Override
    public <V> void insert(String key, V value, boolean async, String tableName) {
        Runnable runnable = () -> {
            try (Connection connection = dataSource.getConnection(); val preparedStatement = connection.prepareStatement("INSERT INTO `" + tableName + "`(`key`, `json`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `json` = VALUES(`json`)")) {
                preparedStatement.setString(1, key);
                preparedStatement.setString(2, gson.toJson(value));
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) executor.submit(runnable);
        else runnable.run();
    }

    @Override
    public void delete(String key, boolean async, String tableName) {
        Runnable runnable = () -> {
            try (Connection connection = dataSource.getConnection();
                 val preparedStatement = connection.prepareStatement("DELETE FROM `" + tableName + "` WHERE `key` = ?")) {
                preparedStatement.setString(1, key);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) executor.submit(runnable);
        else runnable.run();
    }

    @Override
    public <V> V find(String key, String tableName, Class<V> vClass) {
        try (Connection connection = dataSource.getConnection();
             val preparedStatement = connection.prepareStatement("SELECT * FROM `" + tableName + "` WHERE `key` = ?")) {
            preparedStatement.setString(1, key);
            val resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return gson.fromJson(resultSet.getString("json"), vClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <V> List<V> findAll(String tableName, Class<V> vClass) {
        List<V> values = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             val preparedStatement = connection.prepareStatement("SELECT * FROM `" + tableName + "`")) {
            val resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                values.add(gson.fromJson(resultSet.getString("json"), vClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public boolean exists(String key, String tableName) {
        try (Connection connection = dataSource.getConnection();
             val preparedStatement = connection.prepareStatement("SELECT * FROM `" + tableName + "` WHERE `key` = ?")) {
            preparedStatement.setString(1, key);
            return preparedStatement.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void createTable(GenericDataManager<?, ?> dao) {
        try (Connection connection = dataSource.getConnection();
             val preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + dao.getTableName() + "`(`key` VARCHAR(64) NOT NULL, `json` TEXT NOT NULL, PRIMARY KEY (`key`))")) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (dataSource != null) {
                dataSource.close();
            }
        } catch (Exception e) {
            throw new DatabaseException("não foi possivel fechar conexão com mysql", e);
        }
    }
}
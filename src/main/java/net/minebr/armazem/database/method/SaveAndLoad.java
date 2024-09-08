package net.minebr.armazem.database.method;

import lombok.val;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.database.datamanager.DataManager;
import net.minebr.armazem.database.datasource.HikariDataSource;
import net.minebr.armazem.database.datasource.MySQLDataSource;
import net.minebr.armazem.database.datasource.SQLiteDataSource;
import net.minebr.armazem.database.exception.DatabaseException;
import net.minebr.armazem.database.util.Utils;
import org.bukkit.Bukkit;

public class SaveAndLoad {

    public static void saveAll(ArmazemMain main) {
        try {
            if (main.getAbstractDataSource() == null) return;
            main.getMainDataManager().saveCached(false);
            main.getAbstractDataSource().close();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public static boolean prepareDatabase(ArmazemMain main) {
        try {
            val databaseType = main.getConfig().getString("Database.type");
            if (databaseType.equalsIgnoreCase("MYSQL_FAST")) {
                main.setAbstractDataSource(new HikariDataSource(main.getConfig().getString("Database.host"), main.getConfig().getString("Database.database"), main.getConfig().getString("Database.user"), main.getConfig().getString("Database.pass")));
            } else if (databaseType.equalsIgnoreCase("MYSQL")) {
                main.setAbstractDataSource(new MySQLDataSource(main.getConfig().getString("Database.host"), main.getConfig().getString("Database.database"), main.getConfig().getString("Database.user"), main.getConfig().getString("Database.pass")));
            } else {
                main.setAbstractDataSource(new SQLiteDataSource());
            }
            main.setMainDataManager(new DataManager(main.getAbstractDataSource()));

            return true;
        } catch (DatabaseException e) {
            Utils.debug(Utils.LogType.INFO, "Erro ao inicializar conexão com banco de dados.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(main);
        }
        return false;
    }

}

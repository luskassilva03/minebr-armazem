package net.minebr.armazem;

import br.com.ystoreplugins.product.yrankup.RankupAPIHolder;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import lombok.Getter;
import lombok.Setter;
import net.minebr.armazem.api.ConfigAPI;
import net.minebr.armazem.api.VaultAPI;
import net.minebr.armazem.command.ArmazemAdminCommand;
import net.minebr.armazem.command.ArmazemCommand;
import net.minebr.armazem.database.datamanager.DataManager;
import net.minebr.armazem.database.datasource.AbstractDataSource;
import net.minebr.armazem.database.method.AutoSave;
import net.minebr.armazem.database.method.SaveAndLoad;
import net.minebr.armazem.database.util.Utils;
import net.minebr.armazem.listener.BreakFarms;
import net.minebr.armazem.listener.Interact;
import net.minebr.armazem.listener.SpawnEvent;
import net.minebr.armazem.listener.TrafficPlayer;
import net.minebr.armazem.loader.*;
import net.minebr.armazem.loader.menus.Armazem;
import net.minebr.armazem.loader.menus.PlantationsMenu;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.settings.*;
import net.minebr.armazem.settings.menus.ArmazemMenuConfig;
import net.minebr.armazem.settings.menus.PlantationMenuConfig;
import net.minebr.armazem.tasks.BoosterUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ArmazemMain extends JavaPlugin {

    @Getter
    private static ArmazemMain plugin;
    @Setter
    private AbstractDataSource abstractDataSource;
    @Setter
    private DataManager mainDataManager;

    private BoosterUpdater boosterUpdater;

    public ConfigAPI configuration;
    public ConfigAPI plantations;
    public ConfigAPI boosters;
    public ConfigAPI bonuses;
    public ConfigAPI hoes;

    public ConfigAPI plantationMenu;
    public ConfigAPI armazemMenu;

    private final Map<String, PlantationConfig> plantationsType = new HashMap<>();
    private final Map<String, BoosterConfig> boostersType = new HashMap<>();
    private final Map<String, BonusesConfig> bonusesType = new HashMap<>();
    private final Map<String, HoeConfig> hoesType = new HashMap<>();

    private final Map<String, PlantationMenuConfig> plantationMenuConfig = new HashMap<>();
    private final Map<String, ArmazemMenuConfig> armazemMenuConfig = new HashMap<>();

    public HashMap<String, UpgradeConfig> upgradeConfig = new HashMap<>();


    @Override
    public void onEnable() {
        plugin = this;
        VaultAPI.hook();

        configuration = new ConfigAPI(null, "config.yml", false);
        plantations = new ConfigAPI(null, "plantations.yml", false);
        boosters = new ConfigAPI(null, "boosters.yml", false);
        bonuses = new ConfigAPI(null, "bonuses.yml", false);
        hoes = new ConfigAPI(null, "hoes.yml", false);

        plantationMenu = new ConfigAPI("menus", "plantations.yml", false);
        armazemMenu = new ConfigAPI("menus", "armazem.yml", false);

        Utils.DEBUGGING = configuration.getBoolean("Database.Debug");
        if (!SaveAndLoad.prepareDatabase(this)) return;
        PlayerManager.loadAll(this.mainDataManager.USERS);

        new TrafficPlayer(this);
        new BreakFarms(this);
        new SpawnEvent(this);
        new Interact(this);

        (new Plantations()).load();
        (new Boosters()).load();
        (new Bonuses()).load();
        (new Hoes()).load();

        (new PlantationsMenu()).load();
        (new Armazem()).load();

        (new Upgrade()).load();

        new ArmazemCommand(this);
        new ArmazemAdminCommand(this);

        registerInventorys();

        new AutoSave(this, mainDataManager);

        boosterUpdater = new BoosterUpdater(this);
        boosterUpdater.start();

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) §ffoi inicializado com sucesso!");
        Bukkit.getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {

        if (boosterUpdater != null) {
            boosterUpdater.cancel();
        }

        SaveAndLoad.saveAll(this);
    }

    public void registerInventorys() {
        InventoryManager.enable(this);
    }


    public static RankupAPIHolder getRankupAPI() {
        try {
            RegisteredServiceProvider<RankupAPIHolder> rsp = Bukkit.getServer().getServicesManager().getRegistration(RankupAPIHolder.class);
            return rsp == null ? null : (RankupAPIHolder) rsp.getProvider();
        } catch (Throwable var1) {
            return null;
        }
    }

}
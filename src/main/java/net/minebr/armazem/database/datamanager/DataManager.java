package net.minebr.armazem.database.datamanager;

import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.database.datasource.AbstractDataSource;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public final CachedDataManager<String, PlayerManager> USERS;

    @SuppressWarnings("rawtypes")
    private List<CachedDataManager> daos;

    public DataManager(AbstractDataSource abstractDataSource) {
        this.daos = new ArrayList<>();
        daos.add(USERS = new CachedDataManager<>(abstractDataSource, "armazem_player", PlayerManager.class));
    }

    public int saveCached(boolean async) {
        daos.forEach(dao -> dao.saveCached(async));
        return daos.stream().mapToInt(dao -> dao.getCached().size()).sum();
    }
}
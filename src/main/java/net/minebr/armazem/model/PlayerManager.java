package net.minebr.armazem.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.val;
import net.minebr.armazem.database.Keyable;
import net.minebr.armazem.database.datamanager.CachedDataManager;
import net.minebr.armazem.database.util.Utils;

@Getter
public class PlayerManager implements Keyable<String> {
    private final String playerId; // Unique identifier for the player
    @Getter
    private int armazemLevel; // Level do armazém do jogador

    @Getter
    private final Map<String, Double> plantations; // Map to store plantation types and their quantities
    @Getter
    private final Map<String, BoosterInfo> boosters; // Map to store booster types and their information
    @Getter
    private final Set<String> friends; // Set to store friends' player IDs

    public PlayerManager(String playerId) {
        this.playerId = playerId;
        this.armazemLevel = 1; // Definir nível inicial do armazém
        this.plantations = new HashMap<>();
        this.boosters = new HashMap<>();
        this.friends = new HashSet<>();
    }

    @Override
    public String getKey() {
        return playerId;
    }

    public void addPlantation(String type, double quantity) {
        plantations.put(type, quantity);
    }

    public void removePlantation(String type) {
        plantations.remove(type);
    }

    public void addBooster(String type, double multiply, int time) {
        BoosterInfo boosterInfo = new BoosterInfo(multiply, time);
        if (boosterInfo.getActivationTime() == null) {
            System.out.println("Erro ao criar BoosterInfo: activationTime é null");
        }
        boosters.put(type, boosterInfo);
    }

    public void removeBooster(String type) {
        boosters.remove(type);
    }

    public void addFriend(String friendId) {
        friends.add(friendId);
    }

    public void removeFriend(String friendId) {
        friends.remove(friendId);
    }

    public void setArmazemLevel(int level) {
        this.armazemLevel = level;
    }

    public BoosterInfo getActiveBooster() {
        return boosters.values().stream().findFirst().orElse(null);
    }

    public String getActiveBoosterType() {
        return boosters.keySet().stream().findFirst().orElse(null);
    }

    public static void loadAll(CachedDataManager<String, PlayerManager> dao) {
        Utils.measureTime(() -> {
            int i = 0;
            for (PlayerManager playerManager : dao.getAll()) {
                if (dao.isCached(playerManager.getPlayerId())) continue;
                load(playerManager.getPlayerId(), dao);
                i++;
            }
            return "Carregado " + i + " objetos em {time}";
        });
    }

    public static void load(String string, CachedDataManager<String, PlayerManager> dao) {
        if (dao.exists(string)) {
            val account = dao.find(string);
            dao.cache(account);
        }
    }

    @Getter
    public static class BoosterInfo {
        private final double multiply;
        private final int time; // Tempo em segundos
        private final Instant activationTime;

        public BoosterInfo(double multiply, int time) {
            this.multiply = multiply;
            this.time = time;
            this.activationTime = Instant.now(); // Define o tempo de ativação
        }

        public double getMultiply() {
            return multiply;
        }

        public int getTime() {
            return time;
        }

        public Instant getActivationTime() {
            return activationTime;
        }

        public int getRemainingTime() {
            if (activationTime == null) {
                throw new IllegalStateException("Activation time is null");
            }
            long elapsed = Instant.now().getEpochSecond() - activationTime.getEpochSecond();
            int remaining = time - (int) elapsed;
            return Math.max(remaining, 0);
        }
    }
}
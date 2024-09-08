package net.minebr.armazem.inventories;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.api.builder.ItemBuilder;
import net.minebr.armazem.api.builder.SkullBuilder;
import net.minebr.armazem.functions.BonusCalculate;
import net.minebr.armazem.functions.SellDrops;
import net.minebr.armazem.functions.UpgradeArmazem;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.settings.UpgradeConfig;
import net.minebr.armazem.settings.menus.ArmazemMenuConfig;
import net.minebr.armazem.settings.menus.PlantationMenuConfig;
import net.minebr.armazem.utils.NumberFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ArmazemInventory extends SimpleInventory {
    private final ArmazemMain main;

    public ArmazemInventory(ArmazemMain main) {
        super(
                "minebr.armazem",
                "&8Armazém",
                9 * 5 // Ajuste o tamanho do inventário conforme necessário
        );
        this.main = main;
        configuration(configuration -> {
            configuration.secondUpdate(1); // Definir o tempo de atualização do inventário (não configure isso caso não queira que ele atualize automaticamente)
        });
    }

    @Override
    protected void update(Viewer viewer, InventoryEditor editor) {
        Player viewerPlayer = viewer.getPlayer();
        SellDrops sellDrops = new SellDrops();
        String playerId = viewerPlayer.getUniqueId().toString(); // Obtém o ID do jogador

        // Obtém a instância de PlayerManager usando o ID do jogador
        PlayerManager playerManager = ArmazemMain.getPlugin().getMainDataManager().USERS.getCached(viewerPlayer.getName());

        if (playerManager == null) {
            viewerPlayer.sendMessage("§cNão foi possível encontrar as informações do jogador.");
            return;
        }

        // Use playerManager conforme necessário
        int armazemLevel = playerManager.getArmazemLevel();
        Map<String, Double> plantations = playerManager.getPlantations();
        Set<String> friends = playerManager.getFriends();
        Map<String, PlayerManager.BoosterInfo> boosters = playerManager.getBoosters();

        // Continue com a lógica do inventário, como definir itens e painéis
        setGlassPanels(editor);

        // Defina os itens no inventário com base nas informações do playerManager
        main.getArmazemMenuConfig().values().stream()
                .sorted(Comparator.comparingInt(ArmazemMenuConfig::getSlot)) // Se necessário, ordenar por slot
                .forEach(menuConfig -> {
                    ItemBuilder itemBuilder = new ItemBuilder();
                    SkullBuilder skullBuilder = new SkullBuilder();

                    UpgradeConfig upgradeConfig = main.getUpgradeConfig().get("Upgrade");

                    double valorAtual = playerManager.getArmazemLevel() * upgradeConfig.getAddLevel();
                    double novoValor = (playerManager.getArmazemLevel() + 1) * upgradeConfig.getAddLevel();
                    double costUpgradeCoins = (upgradeConfig.getPriceCoins() * upgradeConfig.getMultiplyCoins()) * playerManager.getArmazemLevel();
                    double costUpgradeTokens = (upgradeConfig.getPriceTokens() * upgradeConfig.getMultiplyTokens()) * playerManager.getArmazemLevel();

                    if (menuConfig.isUseSkull() && !menuConfig.getSkullUrl().isEmpty()) {
                        skullBuilder.setTexture(menuConfig.getSkullUrl());
                        skullBuilder.setName(menuConfig.getDisplay());
                        skullBuilder.setLore(menuConfig.getLore().stream().map(line -> line.
                                replace("{level}", NumberFormat.numberFormat(playerManager.getArmazemLevel())).
                                replace("{capacity}", NumberFormat.decimalFormat(valorAtual)).
                                replace("{newcapacity}", NumberFormat.decimalFormat(novoValor)).
                                replace("{costcoins}", NumberFormat.decimalFormat(costUpgradeCoins)).
                                replace("{costtokens}", NumberFormat.decimalFormat(costUpgradeTokens))).collect(Collectors.toList()));
                        ItemStack itemStack = skullBuilder.build();
                        InventoryItem inventoryItem = InventoryItem.of(itemStack);
                        editor.setItem(menuConfig.getSlot(), inventoryItem);
                    } else {
                        itemBuilder.setType(menuConfig.getMaterial());
                        itemBuilder.setName(menuConfig.getDisplay().replace("{level}", NumberFormat.numberFormat(playerManager.getArmazemLevel())));
                        itemBuilder.setLore(menuConfig.getLore().stream().map(line -> line.
                                replace("{level}", NumberFormat.numberFormat(playerManager.getArmazemLevel())).
                                replace("{capacity}", NumberFormat.decimalFormat(valorAtual)).
                                replace("{newcapacity}", NumberFormat.decimalFormat(novoValor)).
                                replace("{costcoins}", NumberFormat.decimalFormat(costUpgradeCoins)).
                                replace("{costtokens}", NumberFormat.decimalFormat(costUpgradeTokens))).collect(Collectors.toList()));
                        ItemStack itemStack = itemBuilder.build();
                        InventoryItem inventoryItem = InventoryItem.of(itemStack);
                        editor.setItem(menuConfig.getSlot(), inventoryItem.defaultCallback(event -> {
                            viewerPlayer.closeInventory();
                            UpgradeArmazem.upgradeArmazem(viewerPlayer, playerManager, costUpgradeCoins, costUpgradeTokens, novoValor);
                        }));
                    }
                });

        // Exemplo de como você pode usar as informações do playerManager para definir itens relacionados às plantações
        PlayerManager finalPlayerManager = playerManager;
        main.getPlantationsType().forEach((plantationKey, plantationConfig) -> {
            PlantationMenuConfig plantationMenuConfig = main.getPlantationMenuConfig().get(plantationKey);

            if (plantationMenuConfig != null) {
                // Obtenha a quantidade de plantações
                double amount = finalPlayerManager.getPlantations().getOrDefault(plantationKey, 0.0);

                // Calcular o custo total para cada provedor
                double totalMoney = plantationConfig.getPrices().values().stream()
                        .filter(priceConfig -> "money".equals(priceConfig.getProvider()))
                        .mapToDouble(priceConfig -> amount * priceConfig.getPrice())
                        .sum();

                double totalCash = plantationConfig.getPrices().values().stream()
                        .filter(priceConfig -> "cash".equals(priceConfig.getProvider()))
                        .mapToDouble(priceConfig -> amount * priceConfig.getPrice())
                        .sum();

                double totalTokens = plantationConfig.getPrices().values().stream()
                        .filter(priceConfig -> "tokens".equals(priceConfig.getProvider()))
                        .mapToDouble(priceConfig -> amount * priceConfig.getPrice())
                        .sum();

                double totalLimite = plantationConfig.getPrices().values().stream()
                        .filter(priceConfig -> "limite".equals(priceConfig.getProvider()))
                        .mapToDouble(priceConfig -> amount * priceConfig.getPrice())
                        .sum();

                // Obter o bônus do jogador
                double bonus = BonusCalculate.getMaxBonus(viewerPlayer, main.getBonusesType());

                // Calcular os valores com bônus
                double totalMoneyWithBonus = totalMoney * (1 + bonus / 100);
                double totalCashWithBonus = totalCash * (1 + bonus / 100);
                double totalTokensWithBonus = totalTokens * (1 + bonus / 100);
                double totalLimiteWithBonus = totalLimite * (1 + bonus / 100);

                double bonusPlayer = BonusCalculate.getMaxBonusMultiplier(viewerPlayer.getPlayer());

                // Criação do item para o menu
                InventoryItem plantationItemStack = InventoryItem.of((new ItemBuilder())
                                .setType(Material.valueOf(plantationKey)) // Tipo padrão, ajuste conforme necessário
                                .setName(plantationMenuConfig.getDisplay().replace("&", "§"))
                                .setLore(plantationMenuConfig.getLore().stream()
                                        .map(line -> line.replace("{amount}", NumberFormat.numberFormat(amount))
                                                .replace("{money}", NumberFormat.decimalFormat(totalMoney))
                                                .replace("{money_with_bonus}", NumberFormat.decimalFormat(totalMoneyWithBonus))
                                                .replace("{cash}", NumberFormat.decimalFormat(totalCash))
                                                .replace("{cash_with_bonus}", NumberFormat.decimalFormat(totalCashWithBonus))
                                                .replace("{tokens}", NumberFormat.decimalFormat(totalTokens))
                                                .replace("{tokens_with_bonus}", NumberFormat.decimalFormat(totalTokensWithBonus))
                                                .replace("{limite}", NumberFormat.decimalFormat(totalLimite))
                                                .replace("{limite_with_bonus}", NumberFormat.decimalFormat(totalLimiteWithBonus))
                                                .replace("{bonus}", NumberFormat.decimalFormat(bonusPlayer)))
                                        .collect(Collectors.toList()))
                                .build())
                        .defaultCallback(event -> {
                            viewerPlayer.closeInventory();
                            // Lógica para vender plantações (ajuste conforme necessário)
                            sellDrops.sellDrops(viewerPlayer, new ItemStack(Material.valueOf(plantationKey)), plantationMenuConfig.getDisplay(), amount, totalMoneyWithBonus, totalTokensWithBonus, totalCashWithBonus, totalLimiteWithBonus);
                        });

                editor.setItem(plantationMenuConfig.getSlot(), plantationItemStack);
            }
        });


    }

    private void setGlassPanels(InventoryEditor editor) {
        // Define o painel de vidro (ID 7) ao redor do inventário
        int[] borderSlots = {
                // Topo
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                // Laterais (esquerda e direita)
                9, 18, 27, 36,
                17, 26, 35, 44,
                // Adicionando a linha inferior e os dois slots de cada lado
                37, 38, 39, 40, 41, 42, 43
        };

        ItemStack glassPanel = createGlassPanel("§ewww.minebr.net");

        for (int slot : borderSlots) {
            // Verifica se o slot está dentro dos limites válidos
            if (slot < getSize() && (editor.getInventory().getItem(slot) == null || editor.getInventory().getItem(slot).getType() == Material.AIR)) {
                editor.setItem(slot, InventoryItem.of(glassPanel));
            }
        }
    }

    private ItemStack createGlassPanel(String name) {
        return new ItemBuilder()
                .setType(Material.STAINED_GLASS_PANE) // Tipo do material
                .setData((byte) 7) // Cor do vidro
                .setName(name) // Nome do item
                .build();
    }
}

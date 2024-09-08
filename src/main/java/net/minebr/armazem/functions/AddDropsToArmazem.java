package net.minebr.armazem.functions;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.settings.UpgradeConfig;
import net.minebr.armazem.utils.ActionBar;
import net.minebr.armazem.utils.NumberFormat;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddDropsToArmazem {

    public void addDropsToArmazem(ArmazemMain main, Player player, ItemStack block) {
        // Inicializa a quantidade base com 1
        double amountWithEnchant = 1;

        // Verifica se o bloco não é Cactus e se a ferramenta é uma enxada com encantamento
        if (block.getType() != Material.CACTUS && isHoe(player.getItemInHand().getType())) {
            int enchantmentLevel = player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

            // Se o nível de Fortune for maior que 0, utiliza o nível da Fortune
            if (enchantmentLevel > 0) {
                amountWithEnchant = enchantmentLevel;
            }
        }

        // Obtém o PlayerManager e a configuração de upgrade
        PlayerManager user = ArmazemMain.getPlugin().getMainDataManager().USERS.getCached(player.getName());

        if (user == null) return;

        UpgradeConfig upgradeConfig = ArmazemMain.getPlugin().getUpgradeConfig().get("Upgrade");
        double capacityArmazemPlayer = user.getArmazemLevel() * upgradeConfig.getAddLevel();

        // Tipo da plantação (usa o tipo de bloco como chave)
        String plantationKey = block.getType().toString();

        // Calcula o total de plantações do mesmo tipo
        double currentAmount = user.getPlantations().getOrDefault(plantationKey, 0.0);

        // Calcula o total de plantações do jogador
        double totalDrops = user.getPlantations().values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        PlayerManager.BoosterInfo booster = user.getActiveBooster();
        double boosterMultiplier = 1.0;
        String boosterInfo = "";
        if (booster != null) {
            boosterMultiplier = booster.getMultiply();
            amountWithEnchant *= boosterMultiplier;
            int remainingTimeSeconds = booster.getRemainingTime(); // Obtém o tempo restante
            int minutes = remainingTimeSeconds / 60;
            int seconds = remainingTimeSeconds % 60;
            boosterInfo = String.format("§a[Booster x%s %d:%02d]", boosterMultiplier, minutes, seconds);
        }

        // Adiciona a quantidade ao armazém se houver capacidade suficiente
        if (capacityArmazemPlayer >= totalDrops + amountWithEnchant) {
            user.getPlantations().put(plantationKey, currentAmount + amountWithEnchant);

            if (!(block.getType() == Material.CACTUS)) {
                // Envia uma mensagem para o jogador com o status do armazém, incluindo o multiplicador do booster, se aplicável
                ActionBar.sendActionText(player, String.format(
                        "§6Armazém ➟ §f§l+§e%s drop(s) §f%s§7/§f%s %s",
                        NumberFormat.decimalFormat(amountWithEnchant),
                        NumberFormat.decimalFormat(totalDrops + amountWithEnchant),
                        NumberFormat.decimalFormat(capacityArmazemPlayer),
                        boosterInfo
                ));
            }
        } else {
            if (!(block.getType() == Material.CACTUS)) {
                ActionBar.sendActionText(player, String.format(
                        "§cArmazém Cheio ➟ §f%s§7/§f%s %s",
                        NumberFormat.decimalFormat(totalDrops),
                        NumberFormat.decimalFormat(capacityArmazemPlayer),
                        boosterInfo
                ));
            }
        }
    }

    public boolean isHoe(Material material) {
        return material == Material.WOOD_HOE ||
                material == Material.STONE_HOE ||
                material == Material.IRON_HOE ||
                material == Material.GOLD_HOE ||
                material == Material.DIAMOND_HOE;
    }

    public boolean isCropWithSeeds(Material cropMaterial) {
        return cropMaterial == Material.CROPS ||
                cropMaterial == Material.POTATO ||
                cropMaterial == Material.NETHER_WARTS ||
                cropMaterial == Material.CARROT;
    }

    public ItemStack getSeedMaterial(Material cropMaterial) {
        switch (cropMaterial) {
            case CROPS:
                return new ItemStack(Material.WHEAT);
            case POTATO:
                return new ItemStack(Material.POTATO);
            case CARROT:
                return new ItemStack(Material.CARROT);
            case NETHER_WARTS:
                return new ItemStack(Material.NETHER_STALK);
            default:
                return null;
        }
    }
}

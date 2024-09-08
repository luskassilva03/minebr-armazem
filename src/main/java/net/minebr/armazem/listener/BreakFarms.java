package net.minebr.armazem.listener;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.functions.AddDropsToArmazem;
import net.minebr.armazem.registery.ListenerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class BreakFarms extends ListenerRegistry {

    private final AddDropsToArmazem addDropsToArmazem;

    public BreakFarms(ArmazemMain main) {
        super(main);
        this.addDropsToArmazem = new AddDropsToArmazem();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Plot plot = new PlotAPI().getPlot(block.getLocation());
        Material blockType = block.getType();

        // Verifica se o bloco quebrado é um tipo de cultivo com sementes e se o jogador está usando uma enxada
        if (!addDropsToArmazem.isCropWithSeeds(blockType) || !addDropsToArmazem.isHoe(player.getItemInHand().getType())) {
            return;
        }

        ItemStack seedMaterial = addDropsToArmazem.getSeedMaterial(blockType);

        // Verifica se o material de semente é válido e se o tipo de plantação é reconhecido
        if (plot == null || !plot.hasOwner() || seedMaterial == null || !main.getPlantationsType().containsKey(seedMaterial.getType().toString())) {
            return;
        }
        Player ownerPlot = Bukkit.getPlayer(plot.getOwners().stream().findFirst().orElse(null));

        BlockState blockState = block.getState();
        int cropStage = getCropStage(blockState);

        // Cancela o evento e replantar a semente se necessário
        event.setCancelled(true);
        if ((seedMaterial.getType() == Material.NETHER_STALK && cropStage == 3) || cropStage == 7) {
            addDropsToArmazem.addDropsToArmazem(main, ownerPlot, seedMaterial);
            replantSeed(blockState);
            /*double fragmento = 0.5;
            String commandBasico = "fragmentos give Farms " + player.getName() + " 1"; // Substitua pelo comando desejado

            if (fragmento > 0.00 && ThreadLocalRandom.current().nextDouble() * 100 < fragmento) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandBasico);
            } */
        }
    }

    private int getCropStage(BlockState blockState) {
        return blockState.getData().getData();
    }

    private void replantSeed(BlockState blockState) {
        // Define o estágio de crescimento da planta para o início e atualiza o estado do bloco
        blockState.getData().setData((byte) 0);
        blockState.update(true);
    }
}

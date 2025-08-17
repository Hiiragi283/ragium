package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTSolarGeneratorBlockEntity(pos: BlockPos, state: BlockState) : HTGeneratorBlockEntity(HTGeneratorVariant.SOLAR, pos, state) {
    override val inventory: HTItemHandler = HTItemStackHandler.EMPTY

    override fun serverTickPre(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 空のない次元では停止
        if (!level.dimensionType().hasSkyLight) return TriState.FALSE
        // 日中出ない場合はスキップ
        if (!level.isDay) return TriState.DEFAULT
        // 現在地から空が見えない場合はスキップ
        if (level.canSeeSky(pos)) return TriState.DEFAULT
        // 発電を行う
        val generated: Int = handleEnergy(network)
        return when {
            generated > 0 -> TriState.TRUE
            else -> TriState.FALSE
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null
}

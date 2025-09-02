package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTSolarGeneratorBlockEntity(pos: BlockPos, state: BlockState) : HTGeneratorBlockEntity(HTGeneratorVariant.SOLAR, pos, state) {
    override fun openGui(player: Player, title: Component): InteractionResult = InteractionResult.PASS

    override fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): Boolean {
        // 空のない次元では停止
        if (!level.dimensionType().hasSkyLight) return false
        // 日中出ない場合はスキップ
        if (!level.isDay) return false
        // 現在地から空が見えない場合はスキップ
        if (level.canSeeSky(pos)) return false
        // 発電を行う
        val generated: Int = network.receiveEnergy(energyUsage, false)
        return false
    }
}

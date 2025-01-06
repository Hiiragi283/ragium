package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineProvider
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * マルチブロックの構造のパターンを持つインターフェース
 */
interface HTMultiblockProvider : HTMultiblockBuilder.Consumer {
    val multiblockManager: HTMultiblockManager

    /**
     * [HTMultiblockManager.updateValidation]の前に呼び出されます。
     */
    fun beforeBuild(world: World?, pos: BlockPos, player: PlayerEntity?) {}

    /**
     * [HTMultiblockManager.updateValidation]の後に呼び出されます。
     */
    fun afterBuild(
        world: World?,
        pos: BlockPos,
        player: PlayerEntity?,
        stateMap: Map<BlockPos, Pair<BlockPos, BlockState>>,
    ) {
    }

    interface Machine :
        HTMultiblockProvider,
        HTMachineProvider {
        override fun buildMultiblock(builder: HTMultiblockBuilder) {
            machineKey
                .getEntryOrNull()
                ?.get(HTMachinePropertyKeys.MULTIBLOCK_PATTERN)
                ?.buildMultiblock(builder)
        }
    }
}

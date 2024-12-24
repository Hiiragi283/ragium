package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * マルチブロックの構造のパターンを持つインターフェース
 */
interface HTMultiblockProvider {
    val multiblockManager: HTMultiblockManager

    /**
     * [HTMultiblockManager.updateValidation]の前に呼び出されます。
     */
    fun beforeBuild(world: World?, pos: BlockPos, player: PlayerEntity?) {}

    /**
     * [builder]にマルチブロックの構造を提供します。
     */
    fun buildMultiblock(builder: HTMultiblockBuilder)

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

    interface Machine : HTMultiblockProvider {
        override fun buildMultiblock(builder: HTMultiblockBuilder) {
            (this as? HTMachineBlockEntityBase)
                ?.key
                ?.getEntryOrNull()
                ?.get(HTMachinePropertyKeys.MULTIBLOCK_PATTERN)
                ?.invoke(builder)
        }
    }
}

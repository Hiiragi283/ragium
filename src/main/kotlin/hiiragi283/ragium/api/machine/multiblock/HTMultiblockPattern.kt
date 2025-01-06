package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.block.BlockState
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * マルチブロックを構成する各要素
 */
interface HTMultiblockPattern {
    /**
     * この要素の名前
     * @see [HTMultiblockManager.add]
     */
    val text: MutableText

    /**
     * 指定された[world]の[pos]が条件に一致するか判定します。
     */
    fun checkState(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean

    /**
     * 指定された[world]の[pos]から，コマンドでの設置に使用する[BlockState]を返します。
     */
    fun getPlacementState(world: World, pos: BlockPos, provider: HTMultiblockProvider): BlockState?
}

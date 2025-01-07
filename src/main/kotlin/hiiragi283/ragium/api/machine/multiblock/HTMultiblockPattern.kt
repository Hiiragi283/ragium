package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

/**
 * マルチブロックを構成する各要素
 */
interface HTMultiblockPattern {
    /**
     * この要素の名前
     * @see [HTMultiblockManager.add]
     */
    val text: Text

    /**
     * 指定された[world]の[pos]が条件に一致するか判定します。
     */
    fun checkState(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean

    /**
     * 指定された[world]の[pos]から，コマンドでの設置に使用する[BlockState]を返します。
     */
    fun getPlacementState(world: World, pos: BlockPos, provider: HTMultiblockProvider): BlockState?

    /**
     * 座標と[HTMultiblockPattern]のデータを表すクラス
     */
    data class Data(
        val x: Int,
        val y: Int,
        val z: Int,
        val pattern: HTMultiblockPattern,
    ) {
        constructor(pos: Vec3i, pattern: HTMultiblockPattern) : this(pos.x, pos.y, pos.z, pattern)
    }
}

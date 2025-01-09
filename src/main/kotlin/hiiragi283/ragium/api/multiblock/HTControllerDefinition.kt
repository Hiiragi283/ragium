package hiiragi283.ragium.api.multiblock

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * マルチブロックのコントローラとなる[BlockEntity]のデータをまとめたクラス
 * @param world コントローラが存在するワールド
 * @param pos コントローラが存在する座標
 * @param front コントローラの正面の向き
 */
data class HTControllerDefinition(val world: World, val pos: BlockPos, val front: Direction) {
    val state: BlockState
        get() = world.getBlockState(pos)
    val blockEntity: BlockEntity?
        get() = world.getBlockEntity(pos)

    fun <A : Any> find(lookup: BlockApiLookup<A, *>): A? = lookup.find(world, pos, null)
}

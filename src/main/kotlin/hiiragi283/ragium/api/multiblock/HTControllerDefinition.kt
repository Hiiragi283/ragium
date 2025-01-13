package hiiragi283.ragium.api.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

/**
 * マルチブロックのコントローラとなる[BlockEntity]のデータをまとめたクラス
 * @param level コントローラが存在するワールド
 * @param pos コントローラが存在する座標
 * @param front コントローラの正面の向き
 */
data class HTControllerDefinition(val level: Level, val pos: BlockPos, val front: Direction) {
    val state: BlockState
        get() = level.getBlockState(pos)
    val blockEntity: BlockEntity?
        get() = level.getBlockEntity(pos)
}

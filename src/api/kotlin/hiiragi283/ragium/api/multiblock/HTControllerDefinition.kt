package hiiragi283.ragium.api.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * マルチブロックのコントローラのデータをまとめたクラス
 * @param level コントローラが存在するワールド
 * @param pos コントローラが存在する座標
 * @param front コントローラの正面の向き
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class HTControllerDefinition(val level: Level, val pos: BlockPos, val front: Direction) : BlockGetter by level {
    val state: BlockState get() = level.getBlockState(pos)
}

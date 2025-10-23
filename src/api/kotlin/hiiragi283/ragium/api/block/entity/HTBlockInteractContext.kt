package hiiragi283.ragium.api.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

@JvmRecord
data class HTBlockInteractContext(
    val level: Level,
    val pos: BlockPos,
    val state: BlockState,
    val player: Player,
    val hitResult: BlockHitResult,
)

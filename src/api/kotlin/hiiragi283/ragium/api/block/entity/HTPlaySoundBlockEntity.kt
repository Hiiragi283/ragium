package hiiragi283.ragium.api.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

fun interface HTPlaySoundBlockEntity {
    fun playSound(level: Level, pos: BlockPos)
}

package hiiragi283.ragium.common.machine

import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMultiblockValidator(val world: World, val pos: BlockPos, val player: PlayerEntity? = null) : HTMultiblockBuilder {
    var isValid: Boolean = true
        private set

    override fun add(
        x: Int,
        y: Int,
        z: Int,
        predicate: HTBlockPredicate,
    ): HTMultiblockBuilder = apply {
        val pos1: BlockPos = pos.add(x, y, z)
        if (isValid) {
            isValid = predicate.test(world, pos1)
            if (!isValid) {
                player?.sendMessage(
                    Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_ERROR, predicate.toString(), pos1.toString()),
                    false,
                )
            }
        }
    }
}

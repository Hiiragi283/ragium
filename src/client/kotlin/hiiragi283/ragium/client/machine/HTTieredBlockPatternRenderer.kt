package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.common.machine.HTTieredBlockPattern
import net.minecraft.block.BlockState
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTTieredBlockPatternRenderer : HTMultiblockPatternRenderer<HTTieredBlockPattern> {
    override fun render(
        x: Int,
        y: Int,
        z: Int,
        pattern: HTTieredBlockPattern,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    ) {
        val state: BlockState =
            HTMachineTier.SIDED_LOOKUP
                .find(world, BlockPos(x, y, z), null)
                ?.let(pattern.blockGetter)
                ?.defaultState ?: return
        renderState(state, x, y, z, pattern, world, matrix, consumerProvider, random)
    }
}

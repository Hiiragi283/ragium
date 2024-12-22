package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.common.machine.HTBlockTagPattern
import net.minecraft.block.BlockState
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTBlockTagPatternRenderer : HTMultiblockPatternRenderer<HTBlockTagPattern> {
    override fun render(
        provider: HTMultiblockProvider,
        x: Int,
        y: Int,
        z: Int,
        pattern: HTBlockTagPattern,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    ) {
        val state: BlockState = pattern.getCurrentState(world) ?: return
        renderState(state, x, y, z, pattern, world, matrix, consumerProvider, random)
    }
}

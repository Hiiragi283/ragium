package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.common.machine.HTSimpleBlockPattern
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTSimpleBlockPatternRenderer : HTMultiblockPatternRenderer<HTSimpleBlockPattern> {
    override fun render(
        provider: HTMultiblockProvider,
        x: Int,
        y: Int,
        z: Int,
        pattern: HTSimpleBlockPattern,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    ) {
        renderState(
            pattern.getPlacementState(world, BlockPos.ORIGIN, provider),
            x,
            y,
            z,
            pattern,
            world,
            matrix,
            consumerProvider,
            random,
        )
    }
}

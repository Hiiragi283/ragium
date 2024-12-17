package hiiragi283.ragium.api.render

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

fun interface HTMultiblockPatternRenderer<T : HTMultiblockPattern> {
    fun render(
        x: Int,
        y: Int,
        z: Int,
        pattern: T,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    )
}

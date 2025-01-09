package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
data class HTMultiblockRenderer(
    val world: World,
    val matrix: MatrixStack,
    val consumerProvider: VertexConsumerProvider,
    val controller: HTControllerDefinition,
) {
    companion object {
        @JvmField
        val DUMMY_POS = BlockPos(0, 260, 0)

        @JvmField
        val RANDOM: Random = Random.create()
    }

    /*override fun add(
        x: Int,
        y: Int,
        z: Int,
        pattern: HTMultiblockPattern,
    ) {
        // HTMultiblockPatternRendererRegistry.render(controller, x, y, z, pattern, world, matrix, consumerProvider, RANDOM)
    }*/
}

package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.client.util.translate
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
data class HTMultiblockRenderer(val world: World, val matrix: MatrixStack, val consumerProvider: VertexConsumerProvider) :
    HTMultiblockBuilder {
    companion object {
        @JvmField
        val DUMMY_POS = BlockPos(0, 260, 0)
    }

    override fun add(
        x: Int,
        y: Int,
        z: Int,
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder = apply {
        val blockRenderManager: BlockRenderManager = MinecraftClient.getInstance().blockRenderManager
        matrix.push()
        matrix.translate(x, y, z)
        matrix.translate(0.125, 0.125, 0.125)
        matrix.scale(0.75f, 0.75f, 0.75f)
        component.getPreviewState(world)?.let { state: BlockState ->
            val consumer: VertexConsumer = consumerProvider.getBuffer(RenderLayers.getBlockLayer(state))
            blockRenderManager.renderBlock(state, DUMMY_POS, world, matrix, consumer, false, Random.create())
        }
        matrix.pop()
    }
}

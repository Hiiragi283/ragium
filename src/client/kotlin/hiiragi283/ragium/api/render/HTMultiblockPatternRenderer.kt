package hiiragi283.ragium.api.render

import hiiragi283.ragium.api.extension.translate
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.client.renderer.HTMultiblockRenderer
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

fun interface HTMultiblockPatternRenderer<T : HTMultiblockPattern> {
    fun render(
        provider: HTMultiblockProvider,
        x: Int,
        y: Int,
        z: Int,
        pattern: T,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    )

    fun renderState(
        state: BlockState,
        x: Int,
        y: Int,
        z: Int,
        pattern: T,
        world: World,
        matrix: MatrixStack,
        consumerProvider: VertexConsumerProvider,
        random: Random,
    ) {
        val blockRenderManager: BlockRenderManager = MinecraftClient.getInstance().blockRenderManager
        matrix.push()
        matrix.translate(x, y, z)
        matrix.translate(0.125, 0.125, 0.125)
        matrix.scale(0.75f, 0.75f, 0.75f)
        val consumer: VertexConsumer = consumerProvider.getBuffer(RenderLayers.getBlockLayer(state))
        blockRenderManager.renderBlock(
            state,
            HTMultiblockRenderer.DUMMY_POS,
            world,
            matrix,
            consumer,
            false,
            Random.create(),
        )
        matrix.pop()
    }
}

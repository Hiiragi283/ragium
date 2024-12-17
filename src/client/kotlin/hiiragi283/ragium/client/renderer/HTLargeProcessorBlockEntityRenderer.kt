package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.renderMultiblock
import hiiragi283.ragium.common.block.machine.process.HTLargeProcessorBlockEntity
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTLargeProcessorBlockEntityRenderer : BlockEntityRenderer<HTLargeProcessorBlockEntity> {
    override fun render(
        entity: HTLargeProcessorBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val world: World = entity.world ?: return
        // render machine
        val state: BlockState = when (entity.isDefault) {
            true -> RagiumBlocks.LARGE_PROCESSOR.defaultState
            false ->
                entity.key.entry
                    .block
                    .getTierState(entity.tier)
        }
        MinecraftClient
            .getInstance()
            .blockRenderManager
            .renderBlock(
                state,
                entity.pos,
                world,
                matrices,
                vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
                true,
                Random.create(),
            )
        // render multiblock
        renderMultiblock(entity, matrices, vertexConsumers)
    }
}

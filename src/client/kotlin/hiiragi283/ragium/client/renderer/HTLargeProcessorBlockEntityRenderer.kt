package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.machine.process.HTLargeProcessorBlockEntity
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
        val key: HTMachineKey = entity.key
        val tier: HTMachineTier = entity.tier
        val state: BlockState = key.entry.getBlock(tier).defaultState
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
        HTMultiblockMachineBlockEntityRenderer.render(entity, tickDelta, matrices, vertexConsumers, light, overlay)
    }
}

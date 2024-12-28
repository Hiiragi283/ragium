package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.orElse
import hiiragi283.ragium.api.extension.renderMultiblock
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.common.block.machine.process.HTExtendedProcessorBlockEntity
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTExtendedProcessorBlockEntityRenderer : BlockEntityRenderer<HTExtendedProcessorBlockEntity> {
    override fun render(
        entity: HTExtendedProcessorBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val world: World = entity.world ?: return
        // render machine
        val state: BlockState =
            entity.machineKey
                .useEntry { entry: HTMachineRegistry.Entry -> entry.block.getTierState(entity.tier) }
                .orElse(RagiumBlocks.EXTENDED_PROCESSOR.get().defaultState)
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

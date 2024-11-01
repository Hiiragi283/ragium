package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.common.block.entity.HTFireboxBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object HTFireboxBlockEntityRenderer : BlockEntityRenderer<HTFireboxBlockEntity> {
    override fun render(
        entity: HTFireboxBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val pos: BlockPos = entity.pos
        val world: World = entity.world ?: return
        val baseBlock: Block = world
            .getMachineEntity(pos.up())
            ?.tier
            ?.getBaseBlock()
            ?: Blocks.BRICKS
        val state: BlockState = baseBlock.defaultState
        MinecraftClient.getInstance().blockRenderManager
            .renderBlock(
                state,
                pos,
                world,
                matrices,
                vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
                false,
                Random.create()
            )
    }
}

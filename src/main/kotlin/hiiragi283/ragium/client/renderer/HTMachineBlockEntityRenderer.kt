package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.shape.HTBlockPredicate
import hiiragi283.ragium.common.shape.HTMultiMachineShape
import hiiragi283.ragium.common.util.getOrNull
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
class HTMachineBlockEntityRenderer(
    context: BlockEntityRendererFactory.Context,
) : BlockEntityRenderer<HTMachineBlockEntity> {
    override fun render(
        entity: HTMachineBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        if (!entity.showPreview) return
        val direction: Direction? = entity.cachedState.getOrNull(Properties.HORIZONTAL_FACING)
        val world: World = entity.world ?: return
        val multiShape: HTMultiMachineShape = entity.multiShape ?: return
        val dummyPos = BlockPos(0, 260, 0)
        val blockRenderManager: BlockRenderManager = MinecraftClient.getInstance().blockRenderManager
        multiShape.getAbsolutePattern(BlockPos.ORIGIN, direction)
            .forEach { (pos: BlockPos, predicate: HTBlockPredicate) ->
                predicate.getPreviewState(world)?.let { state ->
                    matrices.push()
                    matrices.translate(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                    matrices.translate(0.125, 0.125, 0.125)
                    matrices.scale(0.75f, 0.75f, 0.75f)
                    val consumer: VertexConsumer = vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state))
                    blockRenderManager.renderBlock(state, dummyPos, world, matrices, consumer, false, Random.create())
                    matrices.pop()
                }
            }

    }
}
package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.common.block.entity.machine.HTMultiMachineBlockEntity
import hiiragi283.ragium.common.util.getOrDefault
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
object HTMultiMachineBlockEntityRenderer : BlockEntityRenderer<HTMultiMachineBlockEntity> {
    override fun render(
        entity: HTMultiMachineBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        if (!entity.showPreview) return
        val direction: Direction = entity.cachedState.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        val world: World = entity.world ?: return
        entity.buildMultiblock(HTMultiblockRenderer(world, matrices, vertexConsumers).rotate(direction))
        /*val blockRenderManager: BlockRenderManager = MinecraftClient.getInstance().blockRenderManager
        entity.multiShape
            .getAbsolutePattern(BlockPos.ORIGIN, direction)
            .forEach { (pos: BlockPos, predicate: HTBlockPredicate) ->
                predicate.getPreviewState(world)?.let { state: BlockState ->
                    matrices.push()
                    matrices.translate(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                    matrices.translate(0.125, 0.125, 0.125)
                    matrices.scale(0.75f, 0.75f, 0.75f)
                    val consumer: VertexConsumer = vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state))
                    blockRenderManager.renderBlock(state, DUMMY_POS, world, matrices, consumer, false, Random.create())
                    matrices.pop()
                }
            }*/
    }

    override fun rendersOutsideBoundingBox(blockEntity: HTMultiMachineBlockEntity): Boolean = true
}

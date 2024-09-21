package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.util.getOrDefault
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.world.World

@Environment(EnvType.CLIENT)
class HTMultiMachineBlockEntityRenderer<T>(context: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<T> where T : BlockEntity, T : HTMultiblockController {
    override fun render(
        entity: T,
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
    }
}

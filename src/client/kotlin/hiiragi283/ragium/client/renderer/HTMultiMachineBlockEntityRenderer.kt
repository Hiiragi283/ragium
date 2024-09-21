package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.client.util.renderMultiblock
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack

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
        renderMultiblock(entity, matrices, vertexConsumers)
    }
}

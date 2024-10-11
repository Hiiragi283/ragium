package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.util.getOrNull
import hiiragi283.ragium.client.util.renderMultiblock
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties

@Environment(EnvType.CLIENT)
object HTMetaMachineBlockEntityRenderer : BlockEntityRenderer<HTMetaMachineBlockEntity> {
    override fun render(
        entity: HTMetaMachineBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        (entity.machineEntity as? HTMultiblockController)?.let {
            renderMultiblock(
                it,
                entity.world,
                entity.cachedState.getOrNull(Properties.HORIZONTAL_FACING),
                matrices,
                vertexConsumers,
            )
        }
    }
}

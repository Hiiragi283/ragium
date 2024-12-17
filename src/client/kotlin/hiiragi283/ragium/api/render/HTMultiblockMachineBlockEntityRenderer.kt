package hiiragi283.ragium.api.render

import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.renderMultiblock
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack

@Environment(EnvType.CLIENT)
object HTMultiblockMachineBlockEntityRenderer : BlockEntityRenderer<HTMachineBlockEntityBase> {
    override fun render(
        entity: HTMachineBlockEntityBase,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        if (entity is HTMultiblockProvider) {
            renderMultiblock(entity, matrices, vertexConsumers)
        }
    }
}

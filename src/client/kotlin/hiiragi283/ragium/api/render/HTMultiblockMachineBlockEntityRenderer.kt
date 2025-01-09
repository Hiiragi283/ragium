package hiiragi283.ragium.api.render

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.renderMultiblock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.random.Random

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
        entity.renderMultiblock(matrices, vertexConsumers, Random.create())
    }
}

package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.client.util.renderItem
import hiiragi283.ragium.common.block.entity.HTItemDisplayBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d

object HTItemDisplayBlockEntityRenderer : BlockEntityRenderer<HTItemDisplayBlockEntity> {
    override fun render(
        entity: HTItemDisplayBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        renderItem(entity.world, Vec3d.ZERO, entity.getStack(0), matrices, vertexConsumers, 0.75f)
    }
}

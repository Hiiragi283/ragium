package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.client.extension.renderItem
import hiiragi283.ragium.common.block.storage.HTCrateBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object HTCrateBlockEntityRenderer : BlockEntityRenderer<HTCrateBlockEntity> {
    override fun render(
        entity: HTCrateBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val front: Direction = entity.cachedState.get(Properties.FACING)
        renderItem(
            entity.world,
            Vec3d.ZERO.add(0.0, 0.5, 0.0),
            entity.previewStack,
            matrices,
            vertexConsumers,
            light = light,
        )
    }
}

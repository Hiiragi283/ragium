package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.client.extension.renderItem
import hiiragi283.ragium.common.block.storage.HTCreativeCrateBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object HTCreativeCrateBlockEntityRenderer : BlockEntityRenderer<HTCreativeCrateBlockEntity> {
    override fun render(
        entity: HTCreativeCrateBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        val front: Direction = entity.cachedState.get(Properties.HORIZONTAL_FACING)
        val yaw: Float = when (front) {
            Direction.SOUTH -> 180f
            Direction.WEST -> 90f
            Direction.EAST -> 270f
            else -> 0f
        }
        // render holding item
        renderItem(
            entity.world,
            Vec3d.ZERO.offset(front, 0.5).add(0.0, 0.5, 0.0),
            entity.itemStorage.resource.toStack(),
            matrices,
            vertexConsumers,
            scale = 0.6f,
            yaw = yaw,
        )
    }
}

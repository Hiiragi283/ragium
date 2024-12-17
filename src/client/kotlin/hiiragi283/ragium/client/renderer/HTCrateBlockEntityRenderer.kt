package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.renderItem
import hiiragi283.ragium.common.block.storage.HTCrateBlockEntity
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import java.text.NumberFormat
import kotlin.math.PI

class HTCrateBlockEntityRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<HTCrateBlockEntity> {
    val textRenderer: TextRenderer = context.textRenderer

    override fun render(
        entity: HTCrateBlockEntity,
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
            entity.itemStorage.variant.toStack(),
            matrices,
            vertexConsumers,
            scale = 0.6f,
            yaw = yaw,
        )
        // render count
        val amount: String = NumberFormat.getNumberInstance().format(entity.itemStorage.amount)
        matrices.push()
        when (front) {
            Direction.NORTH -> matrices.translate(0.5, 0.2, 0.0)
            Direction.SOUTH -> matrices.translate(0.5, 0.2, 1.0)
            Direction.WEST -> matrices.translate(0.0, 0.2, 0.5)
            Direction.EAST -> matrices.translate(1.0, 0.2, 0.5)
            else -> {}
        }
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw).rotateY(PI.toFloat()))
        val scale = 0.015625f
        matrices.scale(scale, -scale, scale)
        textRenderer.draw(
            amount,
            (-textRenderer.getWidth(amount)) / 2f,
            1f,
            0,
            false,
            matrices.peek().positionMatrix,
            vertexConsumers,
            TextRenderer.TextLayerType.POLYGON_OFFSET,
            0,
            light,
        )
        matrices.pop()
    }
}

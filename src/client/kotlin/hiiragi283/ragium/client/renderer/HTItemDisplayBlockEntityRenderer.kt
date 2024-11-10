package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.client.extension.renderItem
import hiiragi283.ragium.common.block.entity.HTItemDisplayBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d

@Environment(EnvType.CLIENT)
object HTItemDisplayBlockEntityRenderer : BlockEntityRenderer<HTItemDisplayBlockEntity> {
    override fun render(
        entity: HTItemDisplayBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        renderItem(
            entity.world,
            Vec3d.ZERO.add(0.0, 0.5, 0.0),
            entity.asInventory().getStack(0),
            matrices,
            vertexConsumers,
            0.75f,
        )
    }
}

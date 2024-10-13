package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.client.util.renderItem
import hiiragi283.ragium.client.util.renderMultiblock
import hiiragi283.ragium.common.block.entity.HTAlchemicalInfuserBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Vec3d

@Environment(EnvType.CLIENT)
object HTAlchemicalInfuserBlockEntityRenderer : BlockEntityRenderer<HTAlchemicalInfuserBlockEntity> {
    override fun render(
        entity: HTAlchemicalInfuserBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
    ) {
        // render items
        renderItem(entity.world, Vec3d(-2.0, 1.0, -2.0), entity.getStack(0), matrices, vertexConsumers)
        renderItem(entity.world, Vec3d(2.0, 1.0, -2.0), entity.getStack(1), matrices, vertexConsumers)
        renderItem(entity.world, Vec3d(2.0, 1.0, 2.0), entity.getStack(2), matrices, vertexConsumers)
        renderItem(entity.world, Vec3d(-2.0, 1.0, 2.0), entity.getStack(3), matrices, vertexConsumers)
        // render preview
        renderMultiblock(
            entity,
            entity.world,
            entity.cachedState.getOrNull(Properties.HORIZONTAL_FACING),
            matrices,
            vertexConsumers,
        )
    }
}

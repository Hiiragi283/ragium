package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.client.util.translate
import hiiragi283.ragium.common.block.entity.HTAlchemicalInfuserBlockEntity
import hiiragi283.ragium.common.util.getOrDefault
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

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
        if (!entity.showPreview) return
        val direction: Direction = entity.cachedState.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        val world: World = entity.world ?: return
        entity.buildMultiblock(HTMultiblockRenderer(world, matrices, vertexConsumers).rotate(direction))
        renderItem(world, Vec3i(-2, 1, -2), entity.getStack(0), light, matrices, vertexConsumers)
        renderItem(world, Vec3i(-2, 1, 2), entity.getStack(1), light, matrices, vertexConsumers)
        renderItem(world, Vec3i(2, 1, -2), entity.getStack(2), light, matrices, vertexConsumers)
        renderItem(world, Vec3i(2, 1, 2), entity.getStack(3), light, matrices, vertexConsumers)
    }

    private fun renderItem(
        world: World,
        pos: Vec3i,
        stack: ItemStack,
        light: Int,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
    ) {
        matrices.push()
        matrices.translate(pos)
        MinecraftClient.getInstance().itemRenderer.renderItem(
            stack,
            ModelTransformationMode.FIXED,
            light,
            OverlayTexture.DEFAULT_UV,
            matrices,
            vertexConsumers,
            world,
            1,
        )
        matrices.pop()
    }
}

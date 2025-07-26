package hiiragi283.ragium.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import hiiragi283.ragium.common.block.entity.HTTickAwareBlockEntity
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTChargerRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<HTTickAwareBlockEntity> {
    private val itemRenderer: ItemRenderer = context.itemRenderer

    override fun render(
        blockEntity: HTTickAwareBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        poseStack.pushPose()
        poseStack.translate(0.5, 0.75, 0.5)
        poseStack.scale(0.75f, 0.75f, 0.75f)
        poseStack.mulPose(Axis.YP.rotationDegrees((partialTick + 200) * 3f))
        itemRenderer.renderStatic(
            ItemStack.EMPTY,
            ItemDisplayContext.FIXED,
            LightTexture.FULL_BRIGHT,
            packedOverlay,
            poseStack,
            bufferSource,
            blockEntity.level,
            0,
        )
        poseStack.popPose()
    }
}

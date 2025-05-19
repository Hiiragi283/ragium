package hiiragi283.ragium.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import hiiragi283.ragium.common.block.entity.HTChargerBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.world.item.ItemDisplayContext

class HTChargerRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<HTChargerBlockEntity> {
    private val itemRenderer: ItemRenderer = context.itemRenderer

    override fun render(
        blockEntity: HTChargerBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        poseStack.pushPose()
        poseStack.translate(0.5, 0.75, 0.5)
        poseStack.scale(0.75f, 0.75f, 0.75f)
        poseStack.mulPose(Axis.YP.rotationDegrees((partialTick + blockEntity.totalTick) * 3f))
        itemRenderer.renderStatic(
            blockEntity.getStackInSlot(0),
            ItemDisplayContext.FIXED,
            15728880,
            packedOverlay,
            poseStack,
            bufferSource,
            blockEntity.level,
            0,
        )
        poseStack.popPose()
    }
}

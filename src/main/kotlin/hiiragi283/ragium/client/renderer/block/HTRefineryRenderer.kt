package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.stack.isLighterThanAir
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.block.entity.consumer.HTRefineryBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlasSprite

class HTRefineryRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTRefineryBlockEntity>(context) {
    override fun render(
        blockEntity: HTRefineryBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        blockEntity.getFluidTank(0, blockEntity.getFluidSideFor())?.let { tank: HTFluidTank ->
            poseStack.pushPose()
            poseStack.translate(0.51f, 0f, 0.51f)
            drawFluid(tank, poseStack, bufferSource, packedLight, packedOverlay)
            poseStack.popPose()
        }
        blockEntity.getFluidTank(1, blockEntity.getFluidSideFor())?.let { tank: HTFluidTank ->
            poseStack.pushPose()
            poseStack.translate(0.01f, 0f, 0.51f)
            drawFluid(tank, poseStack, bufferSource, packedLight, packedOverlay)
            poseStack.popPose()
        }
    }

    private fun drawFluid(
        tank: HTFluidTank,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        light: Int,
        overlay: Int,
    ) {
        val stack: ImmutableFluidStack = tank.getStack() ?: return
        val sprite: TextureAtlasSprite = HTSpriteRenderHelper.getFluidSprite(stack) ?: return
        val level: Float = tank.getStoredLevelAsFloat()
        if (stack.isLighterThanAir()) {
            poseStack.translate(0f, 1f - level, 0f)
        }
        poseStack.scale(0.48f, level, 0.48f)
        HTSpriteRenderHelper.drawFluidBox(
            poseStack,
            bufferSource,
            sprite,
            stack.getTintColor(),
            light,
            overlay,
        )
    }
}

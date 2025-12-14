package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlasSprite

class HTTankRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTTankBlockEntity>(context) {
    override fun render(
        blockEntity: HTTankBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val tank: HTFluidTank = blockEntity.tank
        val stack: ImmutableFluidStack = tank.getStack() ?: return
        val sprite: TextureAtlasSprite = HTSpriteRenderHelper.getFluidSprite(stack) ?: return

        poseStack.pushPose()
        poseStack.translate(2.5f / 16f, 0f, 2.5f / 16f)
        val level: Float = tank.getStoredLevel().toFloat()
        if (stack.fluidType().isLighterThanAir) {
            poseStack.translate(0f, 1f - (level / 2f), 0f)
        }
        poseStack.scale(11 / 16f, level, 11 / 16f)
        HTSpriteRenderHelper.drawFluidBox(
            poseStack,
            bufferSource,
            sprite,
            stack.getTintColor(),
            packedLight,
            packedOverlay,
        )
        poseStack.popPose()
    }
}

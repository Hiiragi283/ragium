package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlasSprite

class HTSingleFluidMachineRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTBlockEntity>(context) {
    override fun render(
        blockEntity: HTBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val tank: HTFluidTank = blockEntity.getFluidTank(0, blockEntity.getFluidSideFor()) ?: return
        val stack: ImmutableFluidStack = tank.getStack() ?: return
        val sprite: TextureAtlasSprite = HTSpriteRenderHelper.getFluidSprite(stack) ?: return

        poseStack.pushPose()
        poseStack.translate(1 / 16f, 1 / 2f, 1 / 16f)
        val level: Float = tank.getStoredLevel().toFloat()
        if (stack.fluidType().isLighterThanAir) {
            poseStack.translate(0f, 1f - (level / 2f), 0f)
        }
        poseStack.scale(15 / 16f, level / 2f, 15 / 16f)
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

package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.extension.scale
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.stack.isLighterThanAir
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.block.entity.machine.HTConsumerBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlasSprite

class HTSingleFluidMachineRenderer(context: BlockEntityRendererProvider.Context) :
    HTBlockEntityRenderer<HTConsumerBlockEntity>(context) {
    override fun render(
        blockEntity: HTConsumerBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val tank: HTFluidTank = blockEntity.getFluidTank(0, blockEntity.getFluidSideFor()) ?: return
        val stack: ImmutableFluidStack = tank.getStack()
        val sprite: TextureAtlasSprite = HTSpriteRenderHelper.getFluidSprite(stack) ?: return

        poseStack.pushPose()
        poseStack.translate(0.05f, 0.5f, 0.05f)
        val level: Float = tank.getStoredLevelAsFloat(stack)
        if (stack.isLighterThanAir()) {
            poseStack.translate(0f, 1f - (level / 2f), 0f)
        }
        poseStack.scale(1f, level / 2f, 1f)
        poseStack.scale(0.9f)
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

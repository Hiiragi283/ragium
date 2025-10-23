package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.client.model.HTFuelGeneratorModel
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see mekanism.client.render.tileentity.RenderEnergyCube
 */
@OnlyIn(Dist.CLIENT)
class HTFuelGeneratorRenderer(context: BlockEntityRendererProvider.Context) :
    HTModelBlockEntityRenderer<HTFuelGeneratorBlockEntity, HTFuelGeneratorModel>(::HTFuelGeneratorModel, context) {
    override fun render(
        blockEntity: HTFuelGeneratorBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val renderType: RenderType = model.renderType(blockEntity.variant) ?: return
        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        poseStack.scale(-1f, -1f, 1f)
        val time: Float = when {
            blockEntity.isActive -> blockEntity.ticks + partialTick
            else -> 0f
        }
        model.render(poseStack, bufferSource.getBuffer(renderType), packedLight, packedOverlay, time, blockEntity.upgradeHandler.getTier())
        poseStack.popPose()
    }
}

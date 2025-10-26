package hiiragi283.ragium.client.renderer.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import hiiragi283.ragium.client.model.HTFuelGeneratorModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

object HTFuelGeneratorItemRenderer : HTItemRenderer() {
    private var model: HTFuelGeneratorModel? = null

    override fun onResourceManagerReload(resourceManager: ResourceManager) {
        model = HTFuelGeneratorModel(getEntityModels())
    }

    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val renderType: RenderType = model?.renderType(stack.itemHolder) ?: return
        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f))
        model?.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, packedOverlay)
        poseStack.popPose()
    }
}

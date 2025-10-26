package hiiragi283.ragium.client.renderer.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import hiiragi283.ragium.client.model.HTFuelGeneratorModel
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class HTFuelGeneratorItemRenderer private constructor(private val variant: HTGeneratorVariant.Fuel) : HTItemRenderer() {
    companion object {
        @JvmField
        val RENDERERS: Map<HTGeneratorVariant.Fuel, HTFuelGeneratorItemRenderer> =
            HTGeneratorVariant.Fuel.entries.associateWith(::HTFuelGeneratorItemRenderer)
    }

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
        val renderType: RenderType = model?.renderType(variant) ?: return
        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f))
        model?.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, packedOverlay)
        poseStack.popPose()
    }
}

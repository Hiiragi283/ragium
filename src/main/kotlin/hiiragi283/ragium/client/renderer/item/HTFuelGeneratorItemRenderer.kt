package hiiragi283.ragium.client.renderer.item

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.client.model.HTFuelGeneratorModel
import hiiragi283.ragium.common.item.block.HTGeneratorBlockItem
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

object HTFuelGeneratorItemRenderer : HTItemRenderer() {
    @JvmField
    val ITEM_EXTENSION: IClientItemExtensions = object : IClientItemExtensions {
        override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer = HTFuelGeneratorItemRenderer
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
        renderItemAngle(poseStack) {
            val variant: HTGeneratorVariant = (stack.item as? HTGeneratorBlockItem)?.variant ?: return
            val renderType: RenderType = model?.renderType(variant) ?: return
            model?.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, packedOverlay)
        }
    }
}

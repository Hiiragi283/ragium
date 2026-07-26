package hiiragi283.ragium.client.render

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.client.render.block.HTBlockEntityRenderer
import hiiragi283.core.client.render.item.HTItemRenderer
import hiiragi283.core.support.capability.HTEnergyCapabilities
import hiiragi283.ragium.common.block.entity.storage.HTBatteryBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
data object HTBatteryRenderer {
    @JvmStatic
    fun renderCube(
        view: HTAmountView,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
    }

    @OnlyIn(Dist.CLIENT)
    class BlockRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTBatteryBlockEntity<*>>(context) {
        override fun render(
            blockEntity: HTBatteryBlockEntity<*>,
            partialTick: Float,
            poseStack: PoseStack,
            bufferSource: MultiBufferSource,
            packedLight: Int,
            packedOverlay: Int,
        ) {
            renderCube(blockEntity.handler, poseStack, bufferSource, packedLight, packedOverlay)
        }
    }

    @OnlyIn(Dist.CLIENT)
    data object ItemRenderer : HTItemRenderer() {
        override fun onResourceManagerReload(resourceManager: ResourceManager) {}

        override fun renderByItem(
            stack: ItemStack,
            displayContext: ItemDisplayContext,
            poseStack: PoseStack,
            buffer: MultiBufferSource,
            packedLight: Int,
            packedOverlay: Int,
        ) {
            val view: HTAmountView = HTEnergyCapabilities.getHandler(stack) ?: return
            renderCube(view, poseStack, buffer, packedLight, packedOverlay)
            renderBlockItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
        }
    }
}

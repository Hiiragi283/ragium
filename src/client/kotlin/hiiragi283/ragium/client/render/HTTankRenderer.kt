package hiiragi283.ragium.client.render

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.getStillTexture
import hiiragi283.core.api.storage.fluid.getTintColor
import hiiragi283.core.client.render.block.HTBlockEntityRenderer
import hiiragi283.core.client.render.item.HTItemRenderer
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
data object HTTankRenderer {
    @JvmStatic
    fun renderFluid(
        view: HTFluidView,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val resource: HTFluidResourceType = view.getResource() ?: return
        val textureId: ResourceLocation = resource.getStillTexture() ?: return
        val sprite: TextureAtlasSprite = Minecraft.getInstance().getTextureAtlas(HTConst.BLOCK_ATLAS).apply(textureId) ?: return

        val level: Float = view.getLevelAsFloat()
        if (level <= 0f) return
        poseStack.pushPose()
        poseStack.translate(2.5f / 16f, 0f, 2.5f / 16f)
        if (resource.fluidType().isLighterThanAir) {
            poseStack.translate(0f, 1f - (level / 2f), 0f)
        }
        poseStack.scale(11 / 16f, level, 11 / 16f)
        HTSpriteRenderHelper.drawFluidBox(
            poseStack,
            bufferSource.getBuffer(Sheets.translucentCullBlockSheet()),
            sprite,
            resource.getTintColor(),
            packedLight,
            packedOverlay,
        )
        poseStack.popPose()
    }

    @OnlyIn(Dist.CLIENT)
    class BlockRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTTankBlockEntity>(context) {
        override fun render(
            blockEntity: HTTankBlockEntity,
            partialTick: Float,
            poseStack: PoseStack,
            bufferSource: MultiBufferSource,
            packedLight: Int,
            packedOverlay: Int,
        ) {
            renderFluid(blockEntity.tank, poseStack, bufferSource, packedLight, packedOverlay)
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
            val view: HTFluidView = HTFluidCapabilities.getFluidView(stack, 0) ?: return
            renderFluid(view, poseStack, buffer, packedLight, packedOverlay)
            renderBlockItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
        }
    }
}

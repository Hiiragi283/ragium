package hiiragi283.ragium.client.render.block

import com.google.common.primitives.Ints
import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.client.render.block.HTBlockEntityRenderer
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class HTCrateRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTCrateBlockEntity>(context) {
    private val itemRenderer: ItemRenderer = context.itemRenderer

    override fun render(
        blockEntity: HTCrateBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val view: HTItemView = blockEntity.slot
        val stack: ItemStack = view.getResource()?.toStack() ?: return

        poseStack.pushPose()
        poseStack.translate(0.5f, 0.5f, 0.5f)
        itemRenderer.renderStatic(
            stack,
            ItemDisplayContext.FIXED,
            packedLight,
            packedOverlay,
            poseStack,
            bufferSource,
            blockEntity.level,
            blockEntity.blockPos.asLong().let(Ints::saturatedCast),
        )
        poseStack.popPose()
    }
}

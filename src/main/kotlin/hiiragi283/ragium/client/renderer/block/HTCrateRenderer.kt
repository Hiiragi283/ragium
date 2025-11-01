package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.block.attribute.HTDirectionalBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttribute
import hiiragi283.ragium.api.extension.translate
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

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
        val level: Level = blockEntity.level ?: return
        val state: BlockState = level.getBlockState(blockEntity.blockPos)
        val attribute: HTDirectionalBlockAttribute = state.getAttribute<HTDirectionalBlockAttribute>() ?: return
        val front: Direction = attribute.getDirection(state)
        poseStack.pushPose()
        poseStack.translate(0.5f)
        poseStack.mulPose(front.rotation)
        poseStack.translate(front.stepX, front.stepY, front.stepZ)
        itemRenderer.renderStatic(
            blockEntity.getStackInSlot(0, blockEntity.getItemSideFor()),
            ItemDisplayContext.FIXED,
            packedLight,
            packedOverlay,
            poseStack,
            bufferSource,
            level,
            -1,
        )
        poseStack.popPose()
    }
}

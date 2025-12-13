package hiiragi283.ragium.client.renderer.block

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.client.renderer.scale
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.Level

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
        val stack: ImmutableItemStack = blockEntity.slot.getStack() ?: return

        val ticks: Float = blockEntity.ticks + partialTick
        poseStack.pushPose()
        val x: Float = Mth.sin(ticks / 10f) * 0.1f
        poseStack.translate(8 / 16f, x + 0.5f, 8 / 16f)
        poseStack.mulPose(Axis.YP.rotation(ticks / 20f))
        itemRenderer.renderStatic(
            stack.unwrap(),
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

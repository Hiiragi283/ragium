@file:OnlyIn(Dist.CLIENT)

package hiiragi283.ragium.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.model.data.ModelData
import org.joml.Quaternionf

//    PoseStack    //

fun PoseStack.scale(i: Float) {
    scale(i, i, i)
}

fun PoseStack.translate(i: Float) {
    translate(i, i, i)
}

fun PoseStack.translate(x: Number, y: Number, z: Number) {
    translate(x.toDouble(), y.toDouble(), z.toDouble())
}

//    Rendering    //

fun renderItem(
    itemRenderer: ItemRenderer,
    stack: ItemStack,
    level: Level,
    itemY: Double,
    blockY: Double,
    poseStack: PoseStack,
    bufferSource: MultiBufferSource,
    packedLight: Int,
    packedOverlay: Int,
    xOffset: Double = 0.5,
    zOffset: Double = 0.5,
) {
    if (stack.isEmpty) return
    poseStack.pushPose()
    // 平面アイテムだけスケールを変更する
    val model: BakedModel = itemRenderer.itemModelShaper.getItemModel(stack)
    val quads: List<BakedQuad> = model.getQuads(null, null, level.random, ModelData.EMPTY, null)
    if (!quads.isEmpty()) {
        poseStack.translate(xOffset, itemY, zOffset)
        poseStack.mulPose(Quaternionf().rotateX(Mth.DEG_TO_RAD * 90))
        poseStack.scale(0.5f)
    } else {
        poseStack.translate(xOffset, blockY, zOffset)
    }
    // 保持しているアイテムを描画する
    itemRenderer.renderStatic(
        stack,
        ItemDisplayContext.FIXED,
        packedLight,
        packedOverlay,
        poseStack,
        bufferSource,
        level,
        0,
    )
    poseStack.popPose()
}

/*fun HTMultiblockController.renderMultiblock(
    poseStack: PoseStack,
    bufferSource: MultiBufferSource,
    packedLight: Int,
    packedOverlay: Int,
) {
    if (!showPreview) return
    val controller: HTControllerDefinition = getDefinition() ?: return
    val absoluteMap: HTMultiblockMap.Absolute =
        getMultiblockMap()?.convertAbsolute(BlockPos.ZERO, controller.front) ?: return
    if (absoluteMap.isEmpty()) return
    for ((pos: BlockPos, component: HTMultiblockComponent) in absoluteMap.entries) {
        val state: BlockState = component.getPlacementState(controller) ?: continue
        val blockRenderer: BlockRenderDispatcher = Minecraft.getInstance().blockRenderer
        poseStack.pushPose()
        poseStack.translate(pos)
        poseStack.translate(0.125, 0.125, 0.125)
        poseStack.scale(0.75f, 0.75f, 0.75f)
        blockRenderer.renderSingleBlock(
            state,
            poseStack,
            bufferSource,
            packedLight,
            packedOverlay,
        )
        poseStack.popPose()
    }
}*/

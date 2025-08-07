@file:OnlyIn(Dist.CLIENT)

package hiiragi283.ragium.api.extension

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.Vec3i
import net.minecraft.network.chat.Component
import net.minecraft.util.FastColor
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.model.data.ModelData
import org.joml.Matrix4f
import org.joml.Quaternionf

//    PoseStack    //

fun PoseStack.translate(x: Number, y: Number, z: Number) {
    translate(x.toDouble(), y.toDouble(), z.toDouble())
}

fun PoseStack.translate(pos: Vec3i) {
    translate(pos.x, pos.y, pos.z)
}

fun PoseStack.translate(pos: Vec3) {
    translate(pos.x, pos.y, pos.z)
}

fun PoseStack.scale(x: Number, y: Number, z: Number) {
    scale(x.toFloat(), y.toFloat(), z.toFloat())
}

fun PoseStack.scale(pos: Vec3) {
    scale(pos.x, pos.y, pos.z)
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
        poseStack.scale(0.5, 0.5, 0.5)
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

inline fun setShaderColor(color: Int, action: () -> Unit) {
    val red: Float = FastColor.ARGB32.red(color) / 255f
    val green: Float = FastColor.ARGB32.green(color) / 255f
    val blue: Float = FastColor.ARGB32.blue(color) / 255f
    val alpha: Float = FastColor.ARGB32.alpha(color) / 255f
    RenderSystem.setShaderColor(red, green, blue, alpha)
    action()
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
}

/**
 * @see [me.desht.pneumaticcraft.client.util.GuiUtils.drawFluidTexture]
 */
fun drawQuad(
    guiGraphics: GuiGraphics,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    minU: Float,
    minV: Float,
    maxU: Float,
    maxV: Float,
) {
    val matrix4f: Matrix4f = guiGraphics.pose().last().pose()
    Tesselator
        .getInstance()
        .begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        .apply {
            addVertex(matrix4f, x, y + height, 0f).setUv(minU, maxV)
            addVertex(matrix4f, x + width, y + height, 0f).setUv(maxU, maxV)
            addVertex(matrix4f, x + width, y, 0f).setUv(maxU, minV)
            addVertex(matrix4f, x, y, 0f).setUv(minU, minV)
        }.buildOrThrow()
        .let(BufferUploader::drawWithShader)
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

//    Font    //

fun Font.renderText(
    text: Component,
    x: Number,
    y: Number,
    matrix: Matrix4f,
    bufferSource: MultiBufferSource.BufferSource,
    color: Int = ChatFormatting.GRAY.color!!,
    shadow: Boolean = false,
    mode: Font.DisplayMode = Font.DisplayMode.NORMAL,
) {
    drawInBatch(
        text,
        x.toFloat(),
        y.toFloat(),
        color,
        shadow,
        matrix,
        bufferSource,
        mode,
        0,
        LightTexture.FULL_BRIGHT,
    )
}

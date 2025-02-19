package hiiragi283.ragium.api.extension

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

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

//    TextureAtlasSprite    //

fun FluidStack.getSpriteAndColor(): Pair<TextureAtlasSprite, Int> {
    val extension: IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(fluid)
    val sprite: TextureAtlasSprite = Minecraft
        .getInstance()
        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
        .apply(extension.getStillTexture(this))
    return sprite to extension.getTintColor(this)
}

//    Rendering    //

@Suppress("DEPRECATION")
fun HTMultiblockController.renderMultiblock(
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
}

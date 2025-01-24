package hiiragi283.ragium.api.extension

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.ragium.api.client.renderer.HTMultiblockComponentRendererRegistry
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions

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

fun Fluid.getSpriteAndColor(): Pair<TextureAtlasSprite, Int> {
    val extension: IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this)
    val sprite: TextureAtlasSprite = Minecraft
        .getInstance()
        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
        .apply(extension.stillTexture)
    return sprite to extension.tintColor
}

//    Rendering    //

fun HTControllerHolder.renderMultiblock(
    poseStack: PoseStack,
    bufferSource: MultiBufferSource,
    packedLight: Int,
    packedOverlay: Int,
) {
    if (!showPreview) return
    val controller: HTControllerDefinition = getController() ?: return
    val level: Level = controller.level
    val absoluteMap: HTMultiblockMap.Absolute =
        getMultiblockMap()?.convertAbsolute(BlockPos.ZERO, controller.front) ?: return
    if (absoluteMap.isEmpty()) return
    for ((pos: BlockPos, component: HTMultiblockComponent) in absoluteMap.entries) {
        HTMultiblockComponentRendererRegistry.render(
            controller,
            level,
            pos,
            component,
            poseStack,
            bufferSource,
            packedLight,
            packedOverlay,
        )
    }
}

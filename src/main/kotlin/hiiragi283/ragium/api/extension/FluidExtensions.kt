package hiiragi283.ragium.api.extension

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions

//    Color    //

/**
 * 指定した[color]を[Float]値に変換します。
 */
fun toFloatColor(color: Int): Triple<Float, Float, Float> {
    val red: Float = (color shr 16 and 255) / 255.0f
    val green: Float = (color shr 8 and 255) / 255.0f
    val blue: Float = (color and 255) / 255.0f
    return Triple(red, green, blue)
}

//    Fluid    //

val Fluid.name: Component
    get() = fluidType.description

fun Fluid.getSpriteAndColor(): Pair<TextureAtlasSprite, Int> {
    val extension: IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this)
    val sprite: TextureAtlasSprite = Minecraft
        .getInstance()
        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
        .apply(extension.stillTexture)
    return sprite to extension.tintColor
}

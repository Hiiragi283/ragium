package hiiragi283.ragium.api.extension

import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid

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

/**
 * [Fluid]の名前を[net.neoforged.neoforge.fluids.FluidType.getDescription]から返します。
 */
val Fluid.name: Component get() = fluidType.description

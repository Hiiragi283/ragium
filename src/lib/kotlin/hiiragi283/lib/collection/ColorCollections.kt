package hiiragi283.lib.collection

import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.ColorCollection

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
fun <T : Any> ColorCollection(init: (color: DyeColor) -> T): ColorCollection<T> = ColorCollection.VALUES.map(init)

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
operator fun <T : Any> ColorCollection<T>.get(color: DyeColor): T = this.pick(color)

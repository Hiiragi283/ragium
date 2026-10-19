package hiiragi283.lib.recipe.display

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.item.crafting.display.SlotDisplay

/**
 * 複数の[SlotDisplay]を一つの[SlotDisplay]にまとめます。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun Iterable<SlotDisplay>.toDisplay(): SlotDisplay {
    if (this is List<SlotDisplay>) {
        return when (this.size) {
            0 -> SlotDisplay.Empty.INSTANCE
            1 -> this.first()
            else -> SlotDisplay.Composite(this)
        }
    }

    val it: Iterator<SlotDisplay> = this.iterator()
    if (!it.hasNext()) {
        return SlotDisplay.Empty.INSTANCE
    }
    val element: SlotDisplay = it.next()
    if (!it.hasNext()) {
        return element
    }
    val dst = ObjectArrayList<SlotDisplay>()
    dst.add(element)
    while (it.hasNext()) dst.add(it.next())
    return SlotDisplay.Composite(dst)
}

/**
 * 複数の[SlotDisplay]を一つの[SlotDisplay]にまとめます。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun Sequence<SlotDisplay>.toDisplay(): SlotDisplay {
    val it: Iterator<SlotDisplay> = this.iterator()
    if (!it.hasNext()) {
        return SlotDisplay.Empty.INSTANCE
    }
    val element: SlotDisplay = it.next()
    if (!it.hasNext()) {
        return element
    }
    val dst = ObjectArrayList<SlotDisplay>()
    dst.add(element)
    while (it.hasNext()) dst.add(it.next())
    return SlotDisplay.Composite(dst)
}

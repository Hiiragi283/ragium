package hiiragi283.lib.recipe.display

import net.minecraft.world.item.crafting.display.SlotDisplay

/**
 * 複数の[SlotDisplay]を一つの[SlotDisplay]にまとめます。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmName("SlotDisplayComposite")
fun SlotDisplay(children: Iterable<SlotDisplay>): SlotDisplay = when (children.count()) {
    0 -> SlotDisplay.Empty.INSTANCE
    1 -> children.first()
    else -> SlotDisplay.Composite(children.toList())
}

/**
 * 複数の[SlotDisplay]を一つの[SlotDisplay]にまとめます。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmName("SlotDisplayCompositeArray")
fun SlotDisplay(children: Array<out SlotDisplay>): SlotDisplay = when (children.size) {
    0 -> SlotDisplay.Empty.INSTANCE
    1 -> children.first()
    else -> SlotDisplay.Composite(children.toList())
}

/**
 * 複数の[SlotDisplay]を一つの[SlotDisplay]にまとめます。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmName("SlotDisplayCompositeSequence")
fun SlotDisplay(children: Sequence<SlotDisplay>): SlotDisplay = SlotDisplay(children.toList())

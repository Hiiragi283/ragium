package hiiragi283.lib.recipe.display

import net.minecraft.world.item.crafting.display.SlotDisplay

@JvmName("SlotDisplayComposite")
fun SlotDisplay(children: Iterable<SlotDisplay>): SlotDisplay = when (children.count()) {
    0 -> SlotDisplay.Empty.INSTANCE
    1 -> children.first()
    else -> SlotDisplay.Composite(children.toList())
}

@JvmName("SlotDisplayCompositeArray")
fun SlotDisplay(children: Array<out SlotDisplay>): SlotDisplay = when (children.size) {
    0 -> SlotDisplay.Empty.INSTANCE
    1 -> children.first()
    else -> SlotDisplay.Composite(children.toList())
}

@JvmName("SlotDisplayCompositeSequence")
fun SlotDisplay(children: Sequence<SlotDisplay>): SlotDisplay = SlotDisplay(children.toList())

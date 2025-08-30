package hiiragi283.ragium.api.inventory.slot

import net.minecraft.world.Container

/**
 * @see [mekanism.common.inventory.container.slot.HotBarSlot]
 */
class HTHotBarSlot(
    container: Container,
    slot: Int,
    x: Int,
    y: Int,
) : HTInventorySlot(container, slot, x, y)

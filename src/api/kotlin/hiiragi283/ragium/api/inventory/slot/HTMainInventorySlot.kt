package hiiragi283.ragium.api.inventory.slot

import net.minecraft.world.Container

/**
 * @see [mekanism.common.inventory.container.slot.MainInventorySlot]
 */
class HTMainInventorySlot(
    container: Container,
    slot: Int,
    x: Int,
    y: Int,
) : HTInventorySlot(container, slot, x, y)

package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTContentListener

/**
 * @see [mekanism.common.inventory.slot.OutputInventorySlot]
 */
class HTOutputItemStackSlot private constructor(listener: HTContentListener?, x: Int, y: Int) :
    HTItemStackSlot(
        RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
        ALWAYS_TRUE,
        INTERNAL_ONLY,
        ImmutableStack.alwaysTrue(),
        listener,
        x,
        y,
        HTContainerItemSlot.Type.OUTPUT,
    ) {
        companion object {
            @JvmStatic
            fun create(listener: HTContentListener?, x: Int, y: Int): HTOutputItemStackSlot = HTOutputItemStackSlot(listener, x, y)
        }
    }

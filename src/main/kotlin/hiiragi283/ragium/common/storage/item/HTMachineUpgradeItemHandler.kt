package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.material.HTTierType
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.RagiumMaterialVariants
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

class HTMachineUpgradeItemHandler(private val listener: HTContentListener?) : HTItemHandler {
    private val slots: List<HTItemStackSlot> = (0..3).map { i ->
        when (i) {
            3 -> ComponentSlot(listener)

            else
            -> HTItemStackSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(7),
                HTSlotHelper.getSlotPosX(i),
                canExtract = HTItemStackSlot.MANUAL_ONLY,
                canInsert = HTItemStackSlot.MANUAL_ONLY,
            )
        }
    }

    val componentTier: HTTierType? get() = slots[3].getStack().let(ComponentSlot::getTier)

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

    override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    //    Component Slot    //

    class ComponentSlot(listener: HTContentListener?) :
        HTItemStackSlot(
            1,
            MANUAL_ONLY,
            MANUAL_ONLY,
            { stack: ItemStack -> getTier(stack) != null },
            listener,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosX(3),
        ) {
        companion object {
            @JvmStatic
            fun getTier(stack: ItemStack): HTTierType? {
                for ((tier: HTMaterialType, item: HTDeferredItem<*>) in RagiumItems.MATERIALS.row(RagiumMaterialVariants.COMPONENT)) {
                    if (tier !is HTTierType) return null
                    if (item.isOf(stack)) {
                        return tier
                    }
                }
                return null
            }
        }
    }
}

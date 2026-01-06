package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.processing.HTCrusherBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTCrusherMenu(containerId: Int, inventory: Inventory, context: HTCrusherBlockEntity) :
    HTUpgradableContainerMenu<HTCrusherBlockEntity>(RagiumMenuTypes.CRUSHER, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.inputSlot, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0.5)))

        val outputSlots: List<HTBasicItemSlot> = context.outputSlots
        for (i: Int in outputSlots.indices) {
            val slot: HTBasicItemSlot = outputSlots[i]
            addSlot(
                HTContainerItemSlot.output(
                    slot,
                    HTSlotHelper.getSlotPosX(6.5 + i % 2),
                    HTSlotHelper.getSlotPosY(0.5 + i / 2),
                ),
            )
        }

        addUpgradeSlots()
    }
}

package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.processing.HTPyrolyzerBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTPyrolyzerMenu(containerId: Int, inventory: Inventory, context: HTPyrolyzerBlockEntity) :
    HTMachineContainerMenu<HTPyrolyzerBlockEntity>(RagiumMenuTypes.PYROLYZER, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.inputSlot, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0.5)))

        val outputSlots: List<HTBasicItemSlot> = context.outputSlots
        for (i: Int in outputSlots.indices) {
            val slot: HTBasicItemSlot = outputSlots[i]
            addSlot(
                HTContainerItemSlot.output(
                    slot,
                    HTSlotHelper.getSlotPosX(5.5 + i % 2),
                    HTSlotHelper.getSlotPosY(0.5 + i / 2),
                ),
            )
        }

        addUpgradeSlots()
    }
}

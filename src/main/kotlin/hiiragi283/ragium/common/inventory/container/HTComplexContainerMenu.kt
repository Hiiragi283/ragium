package hiiragi283.ragium.common.inventory.container

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.ragium.common.block.entity.processing.HTAbstractComplexBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTDryerBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTComplexContainerMenu<BE : HTAbstractComplexBlockEntity<*>>(
    menuType: HTDeferredMenuType.WithContext<*, BE>,
    containerId: Int,
    inventory: Inventory,
    context: BE,
) : HTMachineContainerMenu<BE>(menuType, containerId, inventory, context) {
    companion object {
        @JvmStatic
        fun dryer(containerId: Int, inventory: Inventory, context: HTDryerBlockEntity): HTComplexContainerMenu<HTDryerBlockEntity> =
            HTComplexContainerMenu(RagiumMenuTypes.DRYER, containerId, inventory, context)
    }

    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.inputSlot, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0.5)))

        addSlot(HTContainerItemSlot.output(context.outputSlot, HTSlotHelper.getSlotPosX(6.5), HTSlotHelper.getSlotPosY(1)))

        addUpgradeSlots()
    }
}

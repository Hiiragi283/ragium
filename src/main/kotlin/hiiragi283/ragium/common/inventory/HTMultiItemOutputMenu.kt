package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.processing.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTPyrolyzerBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTMultiItemOutputMenu<BE : HTProcessorBlockEntity>(
    private val inputGetter: (BE) -> HTBasicItemSlot,
    private val outputsGetter: (BE) -> List<HTBasicItemSlot>,
    menuType: HTDeferredMenuType.WithContext<*, BE>,
    containerId: Int,
    inventory: Inventory,
    context: BE,
) : HTMachineContainerMenu<BE>(menuType, containerId, inventory, context) {
    companion object {
        @JvmStatic
        fun crusher(containerId: Int, inventory: Inventory, context: HTCrusherBlockEntity): HTMultiItemOutputMenu<HTCrusherBlockEntity> =
            HTMultiItemOutputMenu(
                HTCrusherBlockEntity::inputSlot,
                HTCrusherBlockEntity::outputSlots,
                RagiumMenuTypes.CRUSHER,
                containerId,
                inventory,
                context,
            )

        @JvmStatic
        fun pyrolyzer(
            containerId: Int,
            inventory: Inventory,
            context: HTPyrolyzerBlockEntity,
        ): HTMultiItemOutputMenu<HTPyrolyzerBlockEntity> = HTMultiItemOutputMenu(
            HTPyrolyzerBlockEntity::inputSlot,
            HTPyrolyzerBlockEntity::outputSlots,
            RagiumMenuTypes.PYROLYZER,
            containerId,
            inventory,
            context,
        )
    }

    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(inputGetter(context), HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0.5)))

        val outputSlots: List<HTBasicItemSlot> = outputsGetter(context)
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

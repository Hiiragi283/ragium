package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTCombineProcessMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
    slotPos: DoubleArray,
) : HTDefinitionContainerMenu(menuType, containerId, inventory, pos, definition) {
    companion object {
        @JvmStatic
        private fun empty(
            menuType: HTDeferredMenuType<*>,
            containerId: Int,
            inventory: Inventory,
            registryBuf: RegistryFriendlyByteBuf?,
            slotPos: DoubleArray,
        ): HTCombineProcessMenu = HTCombineProcessMenu(
            menuType,
            containerId,
            inventory,
            decodePos(registryBuf),
            HTMenuDefinition.empty(6),
            slotPos,
        )

        @JvmField
        val ALLOY_POS: DoubleArray = doubleArrayOf(1.5, 0.0, 2.5, 0.0)

        @JvmField
        val PRESS_POS: DoubleArray = doubleArrayOf(2.0, 0.0, 2.0, 2.0)

        @JvmStatic
        fun alloy(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTCombineProcessMenu =
            empty(RagiumMenuTypes.ALLOY_SMELTER, containerId, inventory, registryBuf, ALLOY_POS)

        @JvmStatic
        fun press(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTCombineProcessMenu =
            empty(RagiumMenuTypes.FORMING_PRESS, containerId, inventory, registryBuf, PRESS_POS)
    }

    init {
        // inputs
        addSlot(0, HTSlotHelper.getSlotPosX(slotPos[0]), HTSlotHelper.getSlotPosY(slotPos[1]))
        addSlot(1, HTSlotHelper.getSlotPosX(slotPos[2]), HTSlotHelper.getSlotPosY(slotPos[3]))
        // upgrades
        addUpgradeSlots()
        // outputs
        addOutputSlot(2, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }

    override val inputSlots: IntRange = 0..5
    override val outputSlots: IntRange = 6..6
}

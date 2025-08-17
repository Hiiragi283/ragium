package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTItemToItemMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTDefinitionContainerMenu(menuType, containerId, inventory, pos, definition) {
    companion object {
        @JvmStatic
        private fun empty(
            menuType: HTDeferredMenuType<*>,
            containerId: Int,
            inventory: Inventory,
            registryBuf: RegistryFriendlyByteBuf?,
        ): HTItemToItemMenu = HTItemToItemMenu(
            menuType,
            containerId,
            inventory,
            decodePos(registryBuf),
            HTMenuDefinition.empty(2),
        )

        @JvmStatic
        fun compressor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTItemToItemMenu =
            empty(RagiumMenuTypes.COMPRESSOR, containerId, inventory, registryBuf)

        @JvmStatic
        fun extractor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTItemToItemMenu =
            empty(RagiumMenuTypes.EXTRACTOR, containerId, inventory, registryBuf)

        @JvmStatic
        fun pulverizer(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTItemToItemMenu =
            empty(RagiumMenuTypes.PULVERIZER, containerId, inventory, registryBuf)

        @JvmStatic
        fun smelter(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTItemToItemMenu =
            empty(RagiumMenuTypes.SMELTER, containerId, inventory, registryBuf)
    }

    init {
        // inputs
        addInputSlot(0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // upgrades
        addUpgradeSlots()
        // outputs
        addOutputSlot(1, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        // player inventory
        addPlayerInv(inventory)
        // register property
        addDataSlots(definition.containerData)
    }
}

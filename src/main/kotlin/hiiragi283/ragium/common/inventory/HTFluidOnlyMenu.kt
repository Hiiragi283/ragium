package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTFluidOnlyMenu(
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
        ): HTFluidOnlyMenu = HTFluidOnlyMenu(menuType, containerId, inventory, decodePos(registryBuf), HTMenuDefinition.empty(0))

        @JvmStatic
        fun collector(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTFluidOnlyMenu =
            empty(RagiumMenuTypes.FLUID_COLLECTOR, containerId, inventory, registryBuf)

        @JvmStatic
        fun drum(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTFluidOnlyMenu =
            empty(RagiumMenuTypes.DRUM, containerId, inventory, registryBuf)

        @JvmStatic
        fun refinery(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTFluidOnlyMenu =
            empty(RagiumMenuTypes.REFINERY, containerId, inventory, registryBuf)
    }

    init {
        // upgrades
        addUpgradeSlots()
        // player inventory
        addPlayerInv(inventory)
        // register property
        addDataSlots(definition.containerData)
    }
}

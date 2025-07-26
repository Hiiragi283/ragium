package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTSingleProcessMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTDefinitionContainerMenu(
        menuType,
        containerId,
        inventory,
        pos,
        definition,
    ) {
    companion object {
        @JvmStatic
        private fun empty(
            menuType: HTDeferredMenuType<*>,
            containerId: Int,
            inventory: Inventory,
            registryBuf: RegistryFriendlyByteBuf?,
        ): HTSingleProcessMenu = HTSingleProcessMenu(menuType, containerId, inventory, decodePos(registryBuf), HTMenuDefinition.empty(2))

        @JvmStatic
        fun infuser(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?): HTSingleProcessMenu =
            empty(RagiumMenuTypes.INFUSER, containerId, inventory, registryBuf)
    }

    init {
        // inputs
        addSlot(0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0.5))
        // upgrades
        addUpgradeSlots()
        // outputs
        addOutputSlot(1, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = 5..5
}

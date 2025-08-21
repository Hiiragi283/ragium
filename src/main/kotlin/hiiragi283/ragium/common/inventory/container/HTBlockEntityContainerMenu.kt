package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.world.entity.player.Inventory

open class HTBlockEntityContainerMenu<BE : HTBlockEntity>(
    menuType: HTDeferredMenuType<*, BE>,
    containerId: Int,
    inventory: Inventory,
    context: BE,
) : HTContainerWithContextMenu<BE>(
        menuType,
        containerId,
        inventory,
        context,
    ) {
    companion object {
        @JvmStatic
        fun <BE : HTBlockEntity> create(menuType: HTDeferredMenuType<*, BE>): HTContainerFactory<HTBlockEntityContainerMenu<BE>, BE> =
            HTContainerFactory {
                containerId: Int,
                inventory: Inventory,
                context: BE,
                ->
                HTBlockEntityContainerMenu(menuType, containerId, inventory, context)
            }
    }

    init {
        // inputs
        context.addInputSlot(::addInputSlot)
        // outputs
        context.addOutputSlot(::addOutputSlot)
        // player inventory
        addPlayerInv(inventory)
        // container data
        context.addContainerData(::addDataSlots)
    }
}

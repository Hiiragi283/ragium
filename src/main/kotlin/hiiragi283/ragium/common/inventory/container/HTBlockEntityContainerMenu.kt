package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

open class HTBlockEntityContainerMenu<BE : HTBlockEntity>(
    menuType: HTDeferredMenuType<*>,
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
        fun <BE : HTBlockEntity> create(menuType: HTDeferredMenuType<*>): HTContainerFactory<HTBlockEntityContainerMenu<BE>, BE> =
            HTContainerFactory {
                containerId: Int,
                inventory: Inventory,
                context: BE,
                ->
                HTBlockEntityContainerMenu(menuType, containerId, inventory, context)
            }
    }

    init {
        (context as? HTMachineBlockEntity)?.let { blockEntity: HTMachineBlockEntity ->
            blockEntity
                .getItemSlots(blockEntity.getItemSideFor())
                .mapNotNull(HTItemSlot::createContainerSlot)
                .forEach(::addSlot)
            addDataSlots(blockEntity.containerData)
        }
        // player inventory
        addPlayerInv(inventory)
    }

    override fun stillValid(player: Player): Boolean = !context.isRemoved && context.level?.isInWorldBounds(context.blockPos) == true
}

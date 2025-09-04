package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.block.entity.HTBlockEntity
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
    init {
        context.let { blockEntity: HTBlockEntity ->
            blockEntity
                .getItemSlots(blockEntity.getItemSideFor())
                .mapNotNull(HTItemSlot::createContainerSlot)
                .forEach(::addSlot)
        }
        // player inventory
        addPlayerInv(inventory)
    }

    override fun stillValid(player: Player): Boolean = !context.isRemoved && context.level?.isInWorldBounds(context.blockPos) == true
}

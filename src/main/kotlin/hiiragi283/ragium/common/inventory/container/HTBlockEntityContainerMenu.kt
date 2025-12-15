package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

open class HTBlockEntityContainerMenu<BE : HTBlockEntity>(
    menuType: HTDeferredMenuType.WithContext<*, BE>,
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
        // tracking slots
        context.addMenuTrackers(this)
        // block entity slots
        addSlots(context)
        if (context is HTUpgradableBlockEntity) {
            addSlots(context.getUpgradeSlots())
        }
        // player inventory
        addPlayerInv(inventory)
    }

    override fun stillValid(player: Player): Boolean = !context.isRemoved && context.level?.isInWorldBounds(context.blockPos) == true
}

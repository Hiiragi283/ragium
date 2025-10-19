package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import net.minecraft.world.entity.player.Inventory

open class HTGeneratorContainerMenu<BE : HTGeneratorBlockEntity>(
    menuType: HTDeferredMenuType.WithContext<*, BE>,
    containerId: Int,
    inventory: Inventory,
    context: BE,
) : HTBlockEntityContainerMenu<BE>(
        menuType,
        containerId,
        inventory,
        context,
    ) {
    init {
        addSlots(context.upgradeHandler)
    }
}

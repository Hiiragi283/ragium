package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import net.minecraft.world.entity.player.Inventory

open class HTMachineContainerMenu<BE : HTMachineBlockEntity>(
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
        if (context is HTProcessorBlockEntity<*, *>) {
            addDataSlots(context.containerData)
        }
    }
}

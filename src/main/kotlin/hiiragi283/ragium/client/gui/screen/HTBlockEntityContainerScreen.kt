package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

abstract class HTBlockEntityContainerScreen<BE : HTBlockEntity>(
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTContainerScreen<HTBlockEntityContainerMenu<BE>>(
        menu,
        inventory,
        title,
    )

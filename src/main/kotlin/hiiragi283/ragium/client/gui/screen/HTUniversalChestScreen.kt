package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.client.gui.screen.HTContainerScreen
import hiiragi283.ragium.common.inventory.HTUniversalChestMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTUniversalChestScreen(menu: HTUniversalChestMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTUniversalChestMenu>(
        vanillaId("textures", "gui", "container", "shulker_box.png"),
        menu,
        inventory,
        title,
    )

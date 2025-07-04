package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.screen.HTDefinitionContainerScreen
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTBasicMachineScreen(
    override val texture: ResourceLocation,
    menu: HTDefinitionContainerMenu,
    inventory: Inventory,
    title: Component,
) : HTDefinitionContainerScreen<HTDefinitionContainerMenu>(menu, inventory, title) {
    companion object {
        @JvmStatic
        fun create(texture: ResourceLocation): MenuScreens.ScreenConstructor<HTDefinitionContainerMenu, HTBasicMachineScreen> =
            MenuScreens.ScreenConstructor { menu: HTDefinitionContainerMenu, inventory: Inventory, title: Component ->
                HTBasicMachineScreen(texture, menu, inventory, title)
            }
    }

    override val progressPosX: Int = HTSlotHelper.getSlotPosX(3.5)
    override val progressPosY: Int = HTSlotHelper.getSlotPosY(1)
}

package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.gui.screen.HTDefinitionContainerScreen
import hiiragi283.ragium.api.gui.screen.HTProgressBar
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTMachineScreen<T : HTDefinitionContainerMenu>(
    override val texture: ResourceLocation,
    menu: T,
    inventory: Inventory,
    title: Component,
) : HTDefinitionContainerScreen<T>(menu, inventory, title) {
    companion object {
        @JvmStatic
        fun create(
            texture: ResourceLocation,
        ): MenuScreens.ScreenConstructor<HTDefinitionContainerMenu, HTMachineScreen<HTDefinitionContainerMenu>> =
            MenuScreens.ScreenConstructor { menu: HTDefinitionContainerMenu, inventory: Inventory, title: Component ->
                HTMachineScreen(texture, menu, inventory, title)
            }
    }

    override val progressBar: HTProgressBar =
        HTProgressBar.arrow(HTSlotHelper.getSlotPosX(3.5), HTSlotHelper.getSlotPosY(1))
}

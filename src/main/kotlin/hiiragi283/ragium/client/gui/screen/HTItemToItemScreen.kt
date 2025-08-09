package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTProgressBar
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.HTItemToItemMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTItemToItemScreen(
    texture: ResourceLocation,
    factory: (Int, Int) -> HTProgressBar,
    menu: HTItemToItemMenu,
    inventory: Inventory,
    title: Component,
) : HTMachineScreen<HTItemToItemMenu>(texture, menu, inventory, title) {
    companion object {
        @JvmStatic
        fun compressor(menu: HTItemToItemMenu, inventory: Inventory, title: Component): HTItemToItemScreen = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/compressor.png"),
            HTProgressBar::infuse,
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun extractor(menu: HTItemToItemMenu, inventory: Inventory, title: Component): HTItemToItemScreen = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/extractor.png"),
            HTProgressBar::arrow,
            menu,
            inventory,
            title,
        )
    }

    override val progressBar: HTProgressBar = factory(HTSlotHelper.getSlotPosX(3.5), HTSlotHelper.getSlotPosY(1))
}

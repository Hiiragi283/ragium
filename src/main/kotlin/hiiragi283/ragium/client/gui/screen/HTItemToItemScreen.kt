package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTProgressWidget
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
    private val factory: (() -> Float, Int, Int) -> HTProgressWidget,
    menu: HTItemToItemMenu,
    inventory: Inventory,
    title: Component,
) : HTMachineScreen<HTItemToItemMenu>(texture, menu, inventory, title) {
    companion object {
        @JvmStatic
        fun compressor(menu: HTItemToItemMenu, inventory: Inventory, title: Component): HTItemToItemScreen = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/compressor.png"),
            HTProgressWidget::infuse,
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun extractor(menu: HTItemToItemMenu, inventory: Inventory, title: Component): HTItemToItemScreen = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/extractor.png"),
            HTProgressWidget::arrow,
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun pulverizer(menu: HTItemToItemMenu, inventory: Inventory, title: Component): HTItemToItemScreen = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/pulverizer.png"),
            HTProgressWidget::arrow,
            menu,
            inventory,
            title,
        )
    }

    override fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {
        consumer(factory(menu::progress, startX + HTSlotHelper.getSlotPosX(3.5), startY + HTSlotHelper.getSlotPosY(1)))
    }
}

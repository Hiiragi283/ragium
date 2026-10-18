package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag

class HTEnergySlotWidgetRenderer(gui: HTGuiAccess, widget: HTEnergySlotWidget) :
    HTSpriteWidgetRenderer<HTEnergySlotWidget>(gui, widget) {
    companion object {
        @JvmField
        val SPRITE: Identifier = RagiumAPI.id("energy_slot")

        @JvmField
        val BACKGROUND: Identifier = RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "energy_slot_background.png")
    }

    override fun getBackgroundId(): Identifier = BACKGROUND

    override fun shouldRender(): Boolean = !widget.isEmpty

    override fun getFilledLevel(): Float = widget.filledLevelAsFloat

    override fun getSpriteId(): Identifier = SPRITE

    override fun collectTooltips(consumer: (Text) -> Unit, context: Item.TooltipContext, flag: TooltipFlag) {
        HTStorageHelper.addEnergyTooltip(widget, consumer, flag.isCreative)
    }
}

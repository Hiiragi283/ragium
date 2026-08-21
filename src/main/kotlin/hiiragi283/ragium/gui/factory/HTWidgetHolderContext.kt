package hiiragi283.ragium.gui.factory

import hiiragi283.lib.gui.widget.HTWidgetHolder
import net.minecraft.world.entity.player.Player

interface HTWidgetHolderContext {
    fun setup(player: Player, widgetHolder: HTWidgetHolder)

    fun stillValid(player: Player): Boolean
}

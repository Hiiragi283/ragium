package hiiragi283.ragium.client.gui

import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class HTProcessorScreen(gui: HTProcessorScreenHandler, player: PlayerEntity, title: Text) :
    CottonInventoryScreen<HTProcessorScreenHandler>(gui, player, title) {
    constructor(gui: HTProcessorScreenHandler, inventory: PlayerInventory, title: Text) :
        this(gui, inventory.player, title)
}

package hiiragi283.ragium.client.gui

import hiiragi283.ragium.common.screen.HTGeneratorScreenHandler
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class HTGeneratorScreen(gui: HTGeneratorScreenHandler, player: PlayerEntity, title: Text) :
    CottonInventoryScreen<HTGeneratorScreenHandler>(gui, player, title) {
    constructor(gui: HTGeneratorScreenHandler, inventory: PlayerInventory, title: Text) :
            this(gui, inventory.player, title)
}

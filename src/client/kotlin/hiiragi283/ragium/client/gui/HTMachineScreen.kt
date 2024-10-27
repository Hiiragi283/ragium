package hiiragi283.ragium.client.gui

import hiiragi283.ragium.common.screen.HTProcessorScreenHandlerBase
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class HTMachineScreen(gui: HTProcessorScreenHandlerBase, player: PlayerEntity, title: Text) :
    CottonInventoryScreen<HTProcessorScreenHandlerBase>(gui, player, title) {
    constructor(gui: HTProcessorScreenHandlerBase, inventory: PlayerInventory, title: Text) :
        this(gui, inventory.player, title)
}

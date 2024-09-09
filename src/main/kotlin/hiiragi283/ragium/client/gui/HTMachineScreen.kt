package hiiragi283.ragium.client.gui

import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class HTMachineScreen(
    gui: HTMachineScreenHandler,
    player: PlayerEntity,
    title: Text,
) : CottonInventoryScreen<HTMachineScreenHandler>(gui, player, title) {

    constructor(gui: HTMachineScreenHandler, inventory: PlayerInventory, title: Text) :
            this(gui, inventory.player, title)

}
package hiiragi283.ragium.client.gui

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class HTMachineScreen(gui: SyncedGuiDescription, player: PlayerEntity, title: Text) :
    CottonInventoryScreen<SyncedGuiDescription>(gui, player, title) {
    constructor(gui: SyncedGuiDescription, inventory: PlayerInventory, title: Text) :
        this(gui, inventory.player, title)
}

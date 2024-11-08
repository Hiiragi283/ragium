package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
abstract class HTMachineScreenBase<T : HTMachineScreenHandlerBase>(handler: T, inventory: PlayerInventory, title: Text) :
    HTScreenBase<T>(handler, inventory, title) {
    abstract val fluidCache: Array<FluidVariant>
    abstract val amountCache: LongArray
}

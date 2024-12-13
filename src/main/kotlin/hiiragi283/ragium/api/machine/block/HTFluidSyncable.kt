package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.storage.HTFluidVariantStack
import net.minecraft.server.network.ServerPlayerEntity

/**
 * A handler to sync [HTFluidVariantStack] on [net.minecraft.screen.ScreenHandler]
 */
fun interface HTFluidSyncable {
    /**
     * @param player a player which is opening [hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase]
     */
    fun sendPacket(player: ServerPlayerEntity, handler: Handler)

    fun interface Handler {
        fun send(player: ServerPlayerEntity, index: Int, stack: HTFluidVariantStack)
    }
}

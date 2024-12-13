package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.storage.HTFluidVariantStack
import net.minecraft.server.network.ServerPlayerEntity

fun interface HTFluidSyncable {
    fun sendPacket(player: ServerPlayerEntity, handler: Handler)

    fun interface Handler {
        fun send(player: ServerPlayerEntity, index: Int, stack: HTFluidVariantStack)
    }
}

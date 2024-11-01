package hiiragi283.ragium.api.machine.entity

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.server.network.ServerPlayerEntity

fun interface HTFluidSyncable {
    fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit)
}

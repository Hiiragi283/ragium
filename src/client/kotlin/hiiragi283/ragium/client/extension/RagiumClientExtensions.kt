package hiiragi283.ragium.client.extension

import hiiragi283.ragium.api.extension.networkMap
import hiiragi283.ragium.api.world.HTEnergyNetwork
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.world.ClientWorld
import net.minecraft.network.packet.CustomPayload
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

//    Network    //

fun <T : CustomPayload> CustomPayload.Id<T>.registerClientReceiver(handler: ClientPlayNetworking.PlayPayloadHandler<T>) {
    ClientPlayNetworking.registerGlobalReceiver(this, handler)
}

val ClientPlayNetworking.Context.world: ClientWorld?
    get() = client().world

fun ClientPlayNetworking.Context.getBlockEntity(pos: BlockPos): BlockEntity? = world?.getBlockEntity(pos)

//    PersistentState    //

val CLIENT_NETWORK_MAP: Map<RegistryKey<World>, HTEnergyNetwork>
    get() = MinecraftClient.getInstance().server?.networkMap ?: mapOf()

val ClientWorld.energyNetwork: HTEnergyNetwork?
    get() = CLIENT_NETWORK_MAP[registryKey]

package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.world.HTBackpackManager
import hiiragi283.ragium.api.world.HTEnergyNetwork
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.entity.Entity
import net.minecraft.registry.RegistryKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

//    PlayerEntity    //

/**
 * Cast as [this] entity as [ServerPlayerEntity], or null if failed
 */
fun Entity.asServerPlayer(): ServerPlayerEntity? = this as? ServerPlayerEntity

/**
 * Cast as [this] world as [ServerWorld], or null if failed
 */
fun World.asServerWorld(): ServerWorld? = this as? ServerWorld

//    World    //

/**
 * Run [action] if [this] world is [ServerWorld]
 */
fun World.ifServer(action: ServerWorld.() -> Unit): World = apply {
    asServerWorld()?.let(action)
}

fun <T : Any> World?.mapIfServer(transform: (ServerWorld) -> T): DataResult<T> =
    this?.asServerWorld()?.let(transform).toDataResult { "Target world is not ServerWorld!" }

//    PersistentState    //

/**
 * Get or create a new [PersistentState] instance named with [id] from [world]
 */
fun <T : PersistentState> getState(world: ServerWorld, type: PersistentState.Type<T>, id: Identifier): T = world.persistentStateManager
    .getOrCreate(type, id.splitWith('_'))
    .apply(PersistentState::markDirty)

/**
 * Get or create a new [PersistentState] instance named with [id] from [world], or null if not on server-side
 */
fun <T : PersistentState> getState(world: World, type: PersistentState.Type<T>, id: Identifier): T? {
    val key: RegistryKey<World> = world.registryKey
    val server: MinecraftServer = world.server ?: return null
    return getState(server, key, type, id)
}

/**
 * Get or create a new [PersistentState] instance named with [id] from [server] and [key], or null if there is not [ServerWorld] bound with [key]
 */
fun <T : PersistentState> getState(
    server: MinecraftServer,
    key: RegistryKey<World>,
    type: PersistentState.Type<T>,
    id: Identifier,
): T? = server.getWorld(key)?.let { getState(it, type, id) }

/**
 * Get or create a new [PersistentState] instance named with [id] in overworld
 */
fun <T : PersistentState> getStateFromServer(server: MinecraftServer, type: PersistentState.Type<T>, id: Identifier): T =
    getState(server.overworld, type, id)

/**
 * Get or create a new [PersistentState] instance named with [id] in overworld, or null if not on server-side
 */
fun <T : PersistentState> getStateFromServer(world: ServerWorld, type: PersistentState.Type<T>, id: Identifier): T =
    getStateFromServer(world.server, type, id)

// Backpack
val MinecraftServer.backpackManager: HTBackpackManager
    get() = getStateFromServer(this, HTBackpackManager.TYPE, HTBackpackManager.ID)

val WorldAccess.backpackManager: DataResult<HTBackpackManager>
    get() = server
        ?.backpackManager
        .toDataResult { "Failed to find backpack manager!" }

// Energy Network
val MinecraftServer.networkMap: Map<RegistryKey<World>, HTEnergyNetwork>
    get() = worlds.associate { it.registryKey to it.energyNetwork }

val ServerWorld.energyNetwork: HTEnergyNetwork
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID)

val World.energyNetwork: DataResult<HTEnergyNetwork>
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID).toDataResult { "Failed to find energy network!" }

fun World.processEnergy(flag: HTEnergyNetwork.Flag, amount: Long, parent: TransactionContext? = null): Boolean =
    flag.processAmount(energyNetwork.getOrNull(), amount, parent)

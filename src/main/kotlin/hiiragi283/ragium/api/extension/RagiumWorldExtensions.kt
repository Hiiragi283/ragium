package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.world.HTDataDriveManager
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.api.world.HTHardModeManager
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import net.minecraft.world.WorldView
import kotlin.jvm.optionals.getOrNull

//    Views    //

fun BlockView.getMachineEntity(pos: BlockPos): HTMachineEntity? = (getBlockEntity(pos) as? HTMetaMachineBlockEntity)?.machineEntity

fun BlockView.getMultiblockController(pos: BlockPos): HTMultiblockController? =
    (getBlockEntity(pos) as? HTMultiblockController) ?: getMachineEntity(pos) as? HTMultiblockController

fun <T : Any> WorldView.getEntry(registryKey: RegistryKey<Registry<T>>, key: RegistryKey<T>): RegistryEntry<T>? =
    registryManager.get(registryKey).getEntry(key).getOrNull()

fun WorldView.getEnchantment(key: RegistryKey<Enchantment>): RegistryEntry<Enchantment>? = getEntry(RegistryKeys.ENCHANTMENT, key)

//    World    //

fun dropStackAt(entity: Entity, stack: ItemStack) {
    dropStackAt(entity.world, entity.blockPos, stack)
}

fun dropStackAt(world: World, pos: BlockPos, stack: ItemStack) {
    val itemEntity = ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, stack)
    itemEntity.setPickupDelay(0)
    world.spawnEntity(itemEntity)
}

//    PersistentState    //

fun <T : PersistentState> getState(world: ServerWorld, type: PersistentState.Type<T>, id: Identifier): T = world.persistentStateManager
    .getOrCreate(type, id.splitWith('_'))
    .apply { markDirty() }

fun <T : PersistentState> getState(world: World, type: PersistentState.Type<T>, id: Identifier): T? {
    val key: RegistryKey<World> = world.registryKey
    val server: MinecraftServer = world.server ?: return null
    return getState(server, key, type, id)
}

fun <T : PersistentState> getState(
    server: MinecraftServer,
    key: RegistryKey<World>,
    type: PersistentState.Type<T>,
    id: Identifier,
): T? = server.getWorld(key)?.let { getState(it, type, id) }

// Data Drive
val MinecraftServer.dataDriveManager: HTDataDriveManager
    get() = getState(overworld, HTDataDriveManager.TYPE, HTDataDriveManager.ID)

val ServerWorld.dataDriveManager: HTDataDriveManager
    get() = server.dataDriveManager

val World.dataDriveManager: HTDataDriveManager?
    get() = server?.dataDriveManager

// Energy Network
val MinecraftServer.networkMap: Map<ServerWorld, HTEnergyNetwork>
    get() = worlds.associateWith { it.energyNetwork }

val ServerWorld.energyNetwork: HTEnergyNetwork
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID)

val World.energyNetwork: HTEnergyNetwork?
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID)

// Hard Mode
val MinecraftServer.hardModeManager: HTHardModeManager
    get() = getState(overworld, HTHardModeManager.TYPE, HTHardModeManager.ID)

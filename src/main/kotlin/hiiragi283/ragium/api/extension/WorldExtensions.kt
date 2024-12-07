package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.world.HTBackpackManager
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.*

//    Views    //

fun BlockView.getMachineEntity(pos: BlockPos): HTMachineBlockEntityBase? = (getBlockEntity(pos) as? HTMachineBlockEntityBase)

fun BlockView.getMultiblockController(pos: BlockPos): HTMultiblockController? = getBlockEntity(pos) as? HTMultiblockController

fun <T : Any> WorldView.getEntry(registryKey: RegistryKey<Registry<T>>, key: RegistryKey<T>): RegistryEntry<T>? =
    registryManager.get(registryKey).getEntryOrNull(key)

fun WorldView.getEnchantment(key: RegistryKey<Enchantment>): RegistryEntry<Enchantment>? = getEntry(RegistryKeys.ENCHANTMENT, key)

//    World    //

fun dropStackAt(entity: Entity, item: ItemConvertible, count: Int = 1): Boolean = dropStackAt(entity, ItemStack(item, count))

fun dropStackAt(entity: Entity, stack: ItemStack): Boolean = dropStackAt(entity.world, entity.blockPos, stack)

fun dropStackAt(world: World, pos: BlockPos, stack: ItemStack): Boolean {
    val itemEntity = ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, stack)
    itemEntity.velocity = Vec3d.ZERO
    itemEntity.setPickupDelay(0)
    return world.spawnEntity(itemEntity)
}

fun World.ifServer(action: ServerWorld.() -> Unit): World = apply {
    (this as? ServerWorld)?.let(action)
}

fun World.ifSClient(action: World.() -> Unit): World = apply {
    if (this.isClient) this.action()
}

fun <T : Any> World.mapIfServer(transform: (ServerWorld) -> T): DataResult<T> =
    (this as? ServerWorld)?.let(transform).toDataResult { "Target world is not ServerWorld!" }

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

fun <T : PersistentState> getStateFromServer(server: MinecraftServer, type: PersistentState.Type<T>, id: Identifier): T =
    getState(server.overworld, type, id)

fun <T : PersistentState> getStateFromServer(world: ServerWorld, type: PersistentState.Type<T>, id: Identifier): T =
    getStateFromServer(world.server, type, id)

// Backpack
val MinecraftServer.backpackManager: HTBackpackManager
    get() = getStateFromServer(this, HTBackpackManager.TYPE, HTBackpackManager.ID)

val WorldAccess.backpackManager: DataResult<HTBackpackManager>
    get() = server
        ?.backpackManager
        .toDataResult { "Failed to find backpack manager!" }

fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
    openBackpackScreen(world, player, player.getStackInHand(hand))

fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, stack: ItemStack): TypedActionResult<ItemStack> =
    if (stack.contains(RagiumComponentTypes.COLOR)) {
        openBackpackScreen(world, player, stack.getOrDefault(RagiumComponentTypes.COLOR, DyeColor.WHITE))
        TypedActionResult.success(stack, world.isClient)
    } else {
        TypedActionResult.pass(stack)
    }

fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, color: DyeColor) {
    world.backpackManager
        .map { it[color] }
        .ifSuccess { inventory: SimpleInventory ->
            player.openHandledScreen(
                SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x6(syncId, playerInv, inventory)
                }, RagiumItems.BACKPACK.name),
            )
            player.playSound(SoundEvents.BLOCK_VAULT_OPEN_SHUTTER, 1.0f, 1.0f)
        }
}

// Energy Network
val MinecraftServer.networkMap: Map<RegistryKey<World>, HTEnergyNetwork>
    get() = worlds.associate { it.registryKey to it.energyNetwork }

val ServerWorld.energyNetwork: HTEnergyNetwork
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID)

val World.energyNetwork: DataResult<HTEnergyNetwork>
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID).toDataResult { "Failed to find energy network!" }

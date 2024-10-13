package hiiragi283.ragium.api.extension

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.*
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView
import java.text.NumberFormat
import java.util.*
import kotlin.jvm.optionals.getOrNull

//    Either    //

fun <T : Any> Either<out T, out T>.mapCast(): T = map({ it }, { it })

//    BlockPos    //

fun BlockPos.getAroundPos(filter: (BlockPos) -> Boolean): List<BlockPos> = Direction.entries.map(this::offset).filter(filter)

//    FabricLoader    //

fun isModLoaded(modId: String): Boolean = FabricLoader.getInstance().isModLoaded(modId)

//    Identifier    //

fun Identifier.splitWith(splitter: Char): String = "${namespace}${splitter}$path"

//    Inventory    //

fun openEnderChest(world: World, player: PlayerEntity) {
    if (!world.isClient) {
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                GenericContainerScreenHandler.createGeneric9x3(
                    syncId,
                    playerInv,
                    playerInv.player.enderChestInventory,
                )
            }, Text.translatable("container.enderchest")),
        )
    }
    world.playSound(
        null,
        player.x,
        player.y,
        player.z,
        SoundEvents.BLOCK_ENDER_CHEST_OPEN,
        SoundCategory.BLOCKS,
        0.5f,
        1.0f,
    )
}

//    Registry    //

@JvmField
val STATIC_LOOKUP: RegistryWrapper.WrapperLookup = RegistryWrapper.WrapperLookup.of(
    Registries.REGISTRIES.stream().map(Registry<*>::getReadOnlyWrapper),
)

fun <T : Any> createEntry(registry: Registry<T>, key: RegistryKey<T>): RegistryEntry.Reference<T> {
    registry.entryOf(key)
    return RegistryEntry.Reference.standAlone(registry.entryOwner, key)
}

fun <T : Any> createEntry(registry: Registry<T>, id: Identifier): RegistryEntry.Reference<T> =
    createEntry(registry, RegistryKey.of(registry.key, id))

fun <T : Any> createEntry(registry: Registry<T>, namespace: String, path: String): RegistryEntry.Reference<T> =
    createEntry(registry, Identifier.of(namespace, path))

//    ScreenHandler    //

fun ScreenHandlerContext.machineInventory(size: Int): Inventory = get { world: World, pos: BlockPos ->
    world.getMachineEntity(pos)?.parent ?: SimpleInventory(size)
}.orElseGet { SimpleInventory(size) }

//    Table    //

fun <R : Any, C : Any, V : Any> hashTableOf(): Table<R, C, V> = HashBasedTable.create()

fun <R : Any, C : Any, V : Any> buildTable(builderAction: ImmutableTable.Builder<R, C, V>.() -> Unit): ImmutableTable<R, C, V> =
    ImmutableTable.builder<R, C, V>().apply(builderAction).build()

fun <R : Any, C : Any, V : Any> Table<R, C, V>.forEach(action: (R, C, V) -> Unit) {
    cellSet().forEach { action(it.rowKey, it.columnKey, it.value) }
}

//    Transaction    //

inline fun <R> useTransaction(action: (Transaction) -> R): R = Transaction.openOuter().use(action)

//    Text    //

fun longText(value: Long): MutableText = Text.literal(NumberFormat.getNumberInstance().format(value))

//    World    //

fun BlockView.getMachineEntity(pos: BlockPos): HTMachineEntity? = (getBlockEntity(pos) as? HTMetaMachineBlockEntity)?.machineEntity

fun <T : Any> WorldView.getEntry(registryKey: RegistryKey<Registry<T>>, key: RegistryKey<T>): RegistryEntry<T>? =
    registryManager.get(registryKey).getEntry(key).getOrNull()

fun WorldView.getEnchantment(key: RegistryKey<Enchantment>): RegistryEntry<Enchantment>? = getEntry(RegistryKeys.ENCHANTMENT, key)

/*fun breakRangedBlock(
    world: World,
    pos: BlockPos,
    range: Int,
    breaker: Entity,
    tool: ItemStack
) {
    breakRangedBlock(world, pos, breaker.facing, range, breaker, tool::isSuitableFor)
}

fun breakRangedBlock(
    world: World,
    pos: BlockPos,
    direction: Direction,
    range: Int,
    breaker: Entity?,
    predicate: (BlockState) -> Boolean
) {
    when (direction.axis) {
        Direction.Axis.X -> BlockPos.iterate(pos.add(0, range, -range), pos.add(0, -range, range))
        Direction.Axis.Y -> BlockPos.iterate(pos.add(-range, 0, -range), pos.add(range, 0, range))
        Direction.Axis.Z -> BlockPos.iterate(pos.add(-range, range, 0), pos.add(range, -range, 0))
        else -> listOf()
    }
        .filter { predicate(world.getBlockState(it)) }
        .forEach {
            world.setBlockState(it, Blocks.BEDROCK.defaultState)
            // world.breakBlock(it, true, breaker)
        }
}*/

fun dropStackAt(entity: Entity, stack: ItemStack) {
    dropStackAt(entity.world, entity.blockPos, stack)
}

fun dropStackAt(world: World, pos: BlockPos, stack: ItemStack) {
    val itemEntity = ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, stack)
    itemEntity.setPickupDelay(0)
    world.spawnEntity(itemEntity)
}

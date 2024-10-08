package hiiragi283.ragium.common.util

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import hiiragi283.ragium.common.init.RagiumComponentTypes
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.component.Component
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentHolder
import net.minecraft.component.ComponentMap
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.*
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.State
import net.minecraft.state.property.Property
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import net.minecraft.world.WorldView
import java.text.NumberFormat
import java.util.stream.Collectors
import kotlin.jvm.optionals.getOrNull

//    Either    //

fun <T : Any> Either<out T, out T>.mapCast(): T = map({ it }, { it })

//    Block    //

fun blockSettings(): AbstractBlock.Settings = AbstractBlock.Settings.create()

fun blockSettings(block: AbstractBlock): AbstractBlock.Settings = AbstractBlock.Settings.copy(block)

fun <T : BlockEntity> blockEntityType(factory: BlockEntityType.BlockEntityFactory<T>, vararg blocks: Block): BlockEntityType<T> =
    BlockEntityType.Builder.create(factory, *blocks).build()

fun World.modifyBlockState(pos: BlockPos, mapping: (BlockState) -> BlockState): Boolean {
    val stateIn: BlockState = getBlockState(pos)
    return setBlockState(pos, mapping(stateIn))
}

fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrNull(property: Property<T>): T? = when (contains(property)) {
    true -> get(property)
    false -> null
}

fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrDefault(property: Property<T>, defaultValue: T): T = when (contains(property)) {
    true -> get(property)
    false -> defaultValue
}

operator fun <O : Any, S : Any> State<O, S>.contains(property: Property<*>): Boolean = contains(property)

operator fun <O : Any, S : Any, T : Comparable<T>, U : State<O, S>> U.set(property: Property<T>, value: T): U = apply {
    with(property, value)
}

//    BlockApiLookup    //

fun <A, C> BlockApiLookup<A, C>.createCache(world: ServerWorld, pos: BlockPos): BlockApiCache<A, C> = BlockApiCache.create(this, world, pos)

fun <A, C> BlockApiLookup<A, C>.createCacheOrNull(world: World, pos: BlockPos): BlockApiCache<A, C>? =
    (world as? ServerWorld)?.let { createCache(it, pos) }

fun <A, C> BlockApiCache<A, C>.findOrDefault(context: C, defaultValue: A, state: BlockState? = null): A =
    find(state, context) ?: defaultValue

//    BlockPos    //

fun BlockPos.getAroundPos(filter: (BlockPos) -> Boolean): List<BlockPos> = Direction.entries.map(this::offset).filter(filter)

//    Component    //

val ComponentHolder.machineType: HTMachineType
    get() = getOrDefault(RagiumComponentTypes.MACHINE_TYPE, HTMachineType.DEFAULT)

val ComponentMap.machineType: HTMachineType
    get() = getOrDefault(RagiumComponentTypes.MACHINE_TYPE, HTMachineType.DEFAULT)

val ComponentHolder.machineTier: HTMachineTier
    get() = getOrDefault(RagiumComponentTypes.MACHINE_TIER, HTMachineTier.PRIMITIVE)

val ComponentMap.machineTier: HTMachineTier
    get() = getOrDefault(RagiumComponentTypes.MACHINE_TIER, HTMachineTier.PRIMITIVE)

fun ComponentMap.asString(): String = "{${stream().map(Component<*>::toString).collect(Collectors.joining(", "))}}"

//    Enchantment    //

fun hasEnchantment(enchantment: RegistryKey<Enchantment>, world: World, stack: ItemStack): Boolean =
    EnchantmentHelper.getLevel(world.getEntry(RegistryKeys.ENCHANTMENT, enchantment), stack) > 0

//    Item    //

fun itemSettings(): Item.Settings = Item.Settings()

fun Item.Settings.element(element: RagiumContents.Element): Item.Settings = component(RagiumComponentTypes.ELEMENT, element)

fun Item.Settings.machineType(type: HTMachineConvertible): Item.Settings = component(RagiumComponentTypes.MACHINE_TYPE, type.asMachine())

fun Item.Settings.tier(tier: HTMachineTier): Item.Settings = component(RagiumComponentTypes.MACHINE_TIER, tier)

fun buildItemStack(item: ItemConvertible?, count: Int = 1, builderAction: ComponentChanges.Builder.() -> Unit = {}): ItemStack {
    if (item == null) return ItemStack.EMPTY
    val item1: Item = item.asItem()
    if (item1 == Items.AIR) return ItemStack.EMPTY
    val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(item1)
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}

fun ItemStack.hasEnchantment(world: WorldView, key: RegistryKey<Enchantment>): Boolean = world
    .getEnchantment(key)
    ?.let(EnchantmentHelper.getEnchantments(this)::getLevel)
    ?.let { it > 0 }
    ?: false

//    ItemUsageContext    //

val ItemUsageContext.blockState: BlockState
    get() = world.getBlockState(blockPos)

val ItemUsageContext.blockEntity: BlockEntity?
    get() = world.getBlockEntity(blockPos)

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

//    PacketCodec    //

fun <B : ByteBuf, V : Any> PacketCodec<B, V>.toList(): PacketCodec<B, List<V>> = collect(PacketCodecs.toList())

//    Player    //

fun ServerPlayerEntity.sendTitle(title: Text) {
    networkHandler.sendPacket(TitleS2CPacket(title))
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

//    ScreenHandler    //

fun ScreenHandlerContext.machineInventory(size: Int): Inventory = get { world: World, pos: BlockPos ->
    world.getMachineEntity(pos)?.parent ?: SimpleInventory(size)
}.orElseGet { SimpleInventory(size) }

//    StringIdentifiable    //

inline fun <reified T : StringIdentifiable> List<T>.createCodec(): Codec<T> =
    StringIdentifiable.BasicCodec(this.toTypedArray(), this::matchName, this::indexOf)

fun <T : StringIdentifiable> Iterable<T>.matchName(name: String): T = first { it.asString() == name }

fun <T : StringIdentifiable> Iterable<T>.matchNameOrNull(name: String): T? = firstOrNull { it.asString() == name }

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

fun World.sendPacketForPlayers(action: (ServerPlayerEntity) -> Unit) {
    (this as? ServerWorld)?.let(PlayerLookup::world)?.forEach(action)
}

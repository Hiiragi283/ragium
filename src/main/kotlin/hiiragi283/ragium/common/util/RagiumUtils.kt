package hiiragi283.ragium.common.util

import com.google.common.collect.Table
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.machine.HTMachineTier
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
import net.minecraft.component.ComponentChanges
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.State
import net.minecraft.state.property.Property
import net.minecraft.text.Text
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.Function

//    Either    //

fun <T : Any> Either<out T, out T>.mapCast(): T = map(Function.identity(), Function.identity())

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

//    Energy    //

//    Fluid    //

//    Item    //

fun itemSettings(): Item.Settings = Item.Settings()

fun Item.Settings.element(element: RagiElement): Item.Settings = component(RagiumComponentTypes.ELEMENT, element)

fun Item.Settings.tier(tier: HTMachineTier): Item.Settings = component(RagiumComponentTypes.TIER, tier)

fun buildItemStack(item: ItemConvertible?, count: Int = 1, builderAction: ComponentChanges.Builder.() -> Unit = {}): ItemStack {
    if (item == null) return ItemStack.EMPTY
    val item1: Item = item.asItem()
    if (item1 == Items.AIR) return ItemStack.EMPTY
    val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(item1)
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}

//    PacketCodec    //

fun <B : ByteBuf, V : Any> PacketCodec<B, V>.toList(): PacketCodec<B, List<V>> = collect(PacketCodecs.toList())

//    Player    //

fun ServerPlayerEntity.sendTitle(title: Text) {
    networkHandler.sendPacket(TitleS2CPacket(title))
}

//    StringIdentifiable    //

inline fun <reified T : StringIdentifiable> List<T>.createCodec(): Codec<T> =
    StringIdentifiable.BasicCodec(this.toTypedArray(), this::matchName, this::indexOf)

fun <T : StringIdentifiable> Iterable<T>.matchName(name: String): T = first { it.asString() == name }

fun <T : StringIdentifiable> Iterable<T>.matchNameOrNull(name: String): T? = firstOrNull { it.asString() == name }

//    Table    //

fun <R : Any, C : Any, V : Any> Table<R, C, V>.forEach(action: (R, C, V) -> Unit) {
    cellSet().forEach { action(it.rowKey, it.columnKey, it.value) }
}

//    Transaction    //

inline fun <R : Any> useTransaction(action: (Transaction) -> R): R = Transaction.openOuter().use(action)

//    Recipe    //

//    Reflection    //

/*inline fun <reified T : Any> Any.getFilteredInstances(): List<T> = this::class.java.declaredFields
    .onEach { it.isAccessible = true }
    .map { it.get(this) }
    .filterIsInstance<T>()*/

//    World    //

fun dropStackAt(player: PlayerEntity, stack: ItemStack) {
    dropStackAt(player.world, player.blockPos, stack)
}

fun dropStackAt(world: World, pos: BlockPos, stack: ItemStack) {
    val itemEntity = ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
    itemEntity.setPickupDelay(0)
    world.spawnEntity(itemEntity)
}

fun World.sendPacketForPlayers(action: (ServerPlayerEntity) -> Unit) {
    (this as? ServerWorld)?.let(PlayerLookup::world)?.forEach(action)
}

package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.content.HTContent
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.State
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun blockSettings(): AbstractBlock.Settings = AbstractBlock.Settings.create()

@Suppress("DEPRECATION")
fun blockSettings(block: AbstractBlock, isShallow: Boolean = false): AbstractBlock.Settings = when (isShallow) {
    true -> AbstractBlock.Settings.copyShallow(block)
    false -> AbstractBlock.Settings.copy(block)
}

fun <T : BlockEntity> blockEntityType(factory: BlockEntityType.BlockEntityFactory<T>, vararg blocks: Block): BlockEntityType<T> =
    BlockEntityType.Builder.create(factory, *blocks).build()

fun World.modifyBlockState(pos: BlockPos, mapping: (BlockState) -> BlockState?): Boolean {
    val stateIn: BlockState = getBlockState(pos)
    return mapping(stateIn)?.let { setBlockState(pos, it) } == true
}

//    BlockState    //

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

fun BlockState.isOf(content: HTContent<Block>): Boolean = isOf(content.value)

//    BlockApiLookup    //

fun <A, C> BlockApiLookup<A, C>.createCache(world: ServerWorld, pos: BlockPos): BlockApiCache<A, C> = BlockApiCache.create(this, world, pos)

fun <A, C> BlockApiLookup<A, C>.createCacheOrNull(world: World, pos: BlockPos): BlockApiCache<A, C>? =
    (world as? ServerWorld)?.let { createCache(it, pos) }

fun <A, C> BlockApiCache<A, C>.findOrDefault(context: C, defaultValue: A, state: BlockState? = null): A =
    find(state, context) ?: defaultValue

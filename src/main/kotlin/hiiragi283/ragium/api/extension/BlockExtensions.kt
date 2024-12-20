package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.util.HTRegistryEntryList
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.state.State
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Create a new [AbstractBlock.Settings] instance
 */
fun blockSettings(): AbstractBlock.Settings = AbstractBlock.Settings.create()

/**
 * Create a new [AbstractBlock.Settings] instance with copied [block] settings
 */
@Suppress("DEPRECATION")
fun blockSettings(block: AbstractBlock, isShallow: Boolean = false): AbstractBlock.Settings = when (isShallow) {
    true -> AbstractBlock.Settings.copyShallow(block)
    false -> AbstractBlock.Settings.copy(block)
}

/**
 * Try to replace [BlockState] in [this] world at [pos]
 * @param doBreak break block if true, or replace if false
 * @param mapping transform current [BlockState] into new one (may replace) or null (not replaced)
 * @return true if successfully replaced on server side, or false if failed to replace or on client side
 */
fun World.replaceBlockState(pos: BlockPos, doBreak: Boolean = false, mapping: (BlockState) -> BlockState?): Boolean {
    val stateIn: BlockState = getBlockState(pos)
    return when {
        isClient -> false
        else -> mapping(stateIn)?.let { state: BlockState ->
            if (doBreak) breakBlock(pos, false)
            setBlockState(pos, state)
        } == true
    }
}

//    BlockState    //

/**
 * Get [property] value if [this] state has the [property], or null if not
 */
fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrNull(property: Property<T>): T? = when (contains(property)) {
    true -> get(property)
    false -> null
}

/**
 * Get [property] value if [this] state has the [property], or [defaultValue] if not
 */
fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrDefault(property: Property<T>, defaultValue: T): T = when (contains(property)) {
    true -> get(property)
    false -> defaultValue
}

/**
 * Check if [this] block state has the same block from [content]
 */
fun BlockState.isOf(content: HTBlockContent): Boolean = isOf(content.get())

/**
 * Check if [this] block state has the same block from [entryList]
 */
fun BlockState.isIn(entryList: HTRegistryEntryList<Block>): Boolean = entryList.storage.map(this::isIn, this::isOf)

//    BlockEntity    //

/**
 * Run [action] if [this] block entity has [BlockEntity.world]
 * @return return [action] result, or null if [this] doesn't have [BlockEntity.world]
 */
fun <T : Any> BlockEntity.ifPresentWorld(action: (World) -> T): T? = world?.let(action)

/**
 * Create a new [ScreenHandlerContext] instance based on [this] block entity
 * @return return a new instance, or [ScreenHandlerContext.EMPTY] if [this] doesn't have [BlockEntity.world]
 */
fun BlockEntity.createContext(): ScreenHandlerContext = world?.let { ScreenHandlerContext.create(it, pos) } ?: ScreenHandlerContext.EMPTY

//    BlockEntityType    //

/**
 * Create a new [BlockEntityType] instance with [factory] and [blocks]
 */
fun <T : BlockEntity> blockEntityType(factory: BlockEntityType.BlockEntityFactory<T>, vararg blocks: Block): BlockEntityType<T> =
    BlockEntityType.Builder.create(factory, *blocks).build()

fun BlockEntityType<*>.add(content: HTBlockContent) {
    addSupportedBlock(content.get())
}

fun BlockEntityType<*>.addAll(vararg blocks: Block) {
    blocks.forEach(this::addSupportedBlock)
}

fun BlockEntityType<*>.addAll(blocks: Iterable<Block>) {
    blocks.forEach(this::addSupportedBlock)
}

fun BlockEntityType<*>.addAll(blocks: Iterable<HTBlockContent>) {
    blocks.forEach(this::add)
}

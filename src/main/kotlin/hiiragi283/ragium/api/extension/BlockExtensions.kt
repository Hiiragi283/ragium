package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.util.HTRegistryEntryList
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
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

fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrNull(property: Property<T>): T? = when (contains(property)) {
    true -> get(property)
    false -> null
}

fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrDefault(property: Property<T>, defaultValue: T): T = when (contains(property)) {
    true -> get(property)
    false -> defaultValue
}

// operator fun <O : Any, S : Any> State<O, S>.contains(property: Property<*>): Boolean = contains(property)

operator fun <O : Any, S : Any, T : Comparable<T>, U : State<O, S>> U.set(property: Property<T>, value: T): U = apply {
    with(property, value)
}

fun BlockState.isOf(content: HTContent<Block>): Boolean = isOf(content.value)

fun BlockState.isIn(entryList: HTRegistryEntryList<Block>): Boolean = entryList.storage.map(this::isIn, this::isOf)

//    BlockApiLookup    //

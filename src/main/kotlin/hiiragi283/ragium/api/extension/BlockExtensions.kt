package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
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
 * [AbstractBlock.Settings]を返します。
 * @return [AbstractBlock.Settings.create]
 */
fun blockSettings(): AbstractBlock.Settings = AbstractBlock.Settings.create()

/**
 * 指定した[block]の設定をコピーした[AbstractBlock.Settings]を返します。
 * @return [isShallow]がtrueの場合は[AbstractBlock.Settings.copyShallow]，それ以外の場合は[AbstractBlock.Settings.copy]
 */
@Suppress("DEPRECATION")
fun blockSettings(block: AbstractBlock, isShallow: Boolean = false): AbstractBlock.Settings = when (isShallow) {
    true -> AbstractBlock.Settings.copyShallow(block)
    false -> AbstractBlock.Settings.copy(block)
}

/**
 * 指定した[pos]に存在する[BlockState]を置き換えようとします。
 * @param doBreak trueの場合は[World.breakBlock]，それ以外の場合は[World.setBlockState]
 * @return サーバー側で置換に成功したらtrue，それ以外の場合はfalse
 */
fun World.replaceBlockState(pos: BlockPos, doBreak: Boolean = false, transform: (BlockState) -> BlockState?): Boolean {
    val stateIn: BlockState = getBlockState(pos)
    return when {
        isClient -> false
        else -> transform(stateIn)?.let { state: BlockState ->
            if (doBreak) breakBlock(pos, false)
            setBlockState(pos, state)
        } == true
    }
}

//    BlockState    //

val BlockState.tier: HTMachineTier
    get() = getOrNull(HTMachineTier.PROPERTY) ?: (block as? HTMachineTierProvider)?.tier ?: HTMachineTier.PRIMITIVE

/**
 * 指定した[property]の値を返します。
 * @return [property]が含まれていない場合はnull
 */
fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrNull(property: Property<T>): T? = when (contains(property)) {
    true -> get(property)
    false -> null
}

/**
 * 指定した[property]の値を返します。
 * @return [property]が含まれていない場合は[defaultValue]
 */
fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrDefault(property: Property<T>, defaultValue: T): T = when (contains(property)) {
    true -> get(property)
    false -> defaultValue
}

/**
 * 指定した[property]の値を返します。
 * @return [property]が含まれていない場合はnull
 */
fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getResult(property: Property<T>): Result<T> = runCatching { get(property) }

/**
 * 指定した[content]とブロックが一致するか判定します。
 */
fun BlockState.isOf(content: HTBlockContent): Boolean = isOf(content.get())

//    BlockEntity    //

/**
 * [BlockEntity.hasWorld]がtrueの場合のみに[action]を実行します。
 * @param T 戻り値のクラス
 * @return [action]を実行した場合はその戻り値を，それ以外の場合はnull
 */
fun <T : Any> BlockEntity.ifPresentWorld(action: (World) -> T): T? = world?.let(action)

/**
 * [ScreenHandlerContext]を返します。
 * @return [BlockEntity.hasWorld]がfalseの場合は[ScreenHandlerContext.EMPTY]
 */
fun BlockEntity?.createContext(): ScreenHandlerContext =
    this?.ifPresentWorld { ScreenHandlerContext.create(it, pos) } ?: ScreenHandlerContext.EMPTY

//    BlockEntityType    //

/**
 * 指定した[factory]と[blocks]から[BlockEntityType]を返します。
 */
fun <T : BlockEntity> blockEntityType(factory: BlockEntityType.BlockEntityFactory<T>, vararg blocks: Block): BlockEntityType<T> =
    BlockEntityType.Builder.create(factory, *blocks).build()

/**
 * 指定した[content]を[BlockEntityType]に追加します。
 */
fun BlockEntityType<*>.add(content: HTBlockContent) {
    addSupportedBlock(content.get())
}

/**
 * 指定した[blocks]を[BlockEntityType]に追加します。
 */
fun BlockEntityType<*>.addAll(vararg blocks: Block) {
    blocks.forEach(this::addSupportedBlock)
}

/**
 * 指定した[blocks]を[BlockEntityType]に追加します。
 */
fun BlockEntityType<*>.addAll(blocks: Iterable<Block>) {
    blocks.forEach(this::addSupportedBlock)
}

/**
 * 指定した[contents]を[BlockEntityType]に追加します。
 */
fun BlockEntityType<*>.addAllContents(contents: Iterable<HTBlockContent>) {
    contents.forEach(this::add)
}

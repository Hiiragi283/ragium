package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTAmountView
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension

/**
 * [HTAmountView]を取得する[HTMultiCapability]の拡張インターフェース
 */
interface HTAmountViewCapability<HANDLER : Any, ITEM_HANDLER : HANDLER, N> :
    HTMultiCapability<HANDLER, ITEM_HANDLER>,
    HTAmountViewProvider<HANDLER, Direction, N>
    where N : Number, N : Comparable<N> {
    //    Block    //

    /**
     * 指定した引数から[HTAmountView]の一覧を返します。
     * @return [HTAmountView]の[List]
     */
    fun getCapabilityViews(level: ILevelExtension, pos: BlockPos, side: Direction?): List<HTAmountView<N>> =
        getCapability(level, pos, side)?.let { apply(it, side) } ?: listOf()

    /**
     * 指定した引数から[index]に対応する[HTAmountView]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilityView(
        level: ILevelExtension,
        pos: BlockPos,
        side: Direction?,
        index: Int,
    ): HTAmountView<N>? = getCapabilityViews(level, pos, side).getOrNull(index)

    //    Entity    //

    fun getCapabilityViews(entity: Entity, side: Direction?): List<HTAmountView<N>> =
        getCapability(entity, side)?.let { apply(it, side) } ?: listOf()

    fun getCapabilityView(entity: Entity, side: Direction?, index: Int): HTAmountView<N>? =
        getCapabilityViews(entity, side).getOrNull(index)

    fun getCapabilityAmounts(entity: Entity, side: Direction?): List<N> = getCapabilityViews(entity, side).map(HTAmountView<N>::getAmount)

    fun getCapabilityAmount(entity: Entity, side: Direction?, index: Int): N? = getCapabilityView(entity, side, index)?.getAmount()

    //    Item    //

    /**
     * 指定した引数から[HTAmountView]の一覧を返します。
     * @return [HTAmountView]の[List]
     */
    fun getCapabilityViews(stack: IItemStackExtension): List<HTAmountView<N>> = getCapability(stack)?.let { apply(it, null) } ?: listOf()

    /**
     * 指定した引数から[index]に対応する[HTAmountView]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilityView(stack: IItemStackExtension, index: Int): HTAmountView<N>? = getCapabilityViews(stack).getOrNull(index)

    fun getCapabilityAmounts(stack: IItemStackExtension): List<N> = getCapabilityViews(stack).map(HTAmountView<N>::getAmount)

    fun getCapabilityAmount(stack: IItemStackExtension, index: Int): N? = getCapabilityView(stack, index)?.getAmount()

    // HTItemStorageStack

    fun getCapabilityViews(stack: ImmutableItemStack?): List<HTAmountView<N>> = getCapability(stack)?.let { apply(it, null) } ?: listOf()

    fun getCapabilityView(stack: ImmutableItemStack?, index: Int): HTAmountView<N>? = getCapabilityViews(stack).getOrNull(index)

    fun getCapabilityAmounts(stack: ImmutableItemStack?): List<N> = getCapabilityViews(stack).map(HTAmountView<N>::getAmount)

    fun getCapabilityAmount(stack: ImmutableItemStack?, index: Int): N? = getCapabilityView(stack, index)?.getAmount()

    interface Simple<HANDLER : Any, N> : HTAmountViewCapability<HANDLER, HANDLER, N> where N : Number, N : Comparable<N>
}

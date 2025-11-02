package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStackView
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension

/**
 * [HTStackView]を取得する[HTMultiCapability]の拡張インターフェース
 */
interface HTStackViewCapability<HANDLER : Any, ITEM_HANDLER : HANDLER, STACK : ImmutableStack<*, STACK>> :
    HTMultiCapability<HANDLER, ITEM_HANDLER>,
    HTStackViewProvider<HANDLER, Direction, STACK> {
    //    Block    //

    /**
     * 指定した引数から[HTStackView]の一覧を返します。
     * @return [HTStackView]の[List]
     */
    fun getCapabilityViews(level: ILevelExtension, pos: BlockPos, side: Direction?): List<HTStackView<STACK>> =
        getCapability(level, pos, side)?.let { apply(it, side) } ?: listOf()

    /**
     * 指定した引数から[index]に対応する[HTStackView]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilityView(
        level: ILevelExtension,
        pos: BlockPos,
        side: Direction?,
        index: Int,
    ): HTStackView<STACK>? = getCapabilityViews(level, pos, side).getOrNull(index)

    //    Entity    //

    fun getCapabilityViews(entity: Entity, side: Direction?): List<HTStackView<STACK>> =
        getCapability(entity, side)?.let { apply(it, side) } ?: listOf()

    fun getCapabilityView(entity: Entity, side: Direction?, index: Int): HTStackView<STACK>? =
        getCapabilityViews(entity, side).getOrNull(index)

    fun getCapabilityStacks(entity: Entity, side: Direction?): List<STACK?> =
        getCapabilityViews(entity, side).map(HTStackView<STACK>::getStack)

    fun getCapabilityStack(entity: Entity, side: Direction?, index: Int): STACK? = getCapabilityView(entity, side, index)?.getStack()

    //    Item    //

    /**
     * 指定した引数から[HTStackView]の一覧を返します。
     * @return [HTStackView]の[List]
     */
    fun getCapabilityViews(stack: IItemStackExtension): List<HTStackView<STACK>> = getCapability(stack)?.let { apply(it, null) } ?: listOf()

    /**
     * 指定した引数から[index]に対応する[HTStackView]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilityView(stack: IItemStackExtension, index: Int): HTStackView<STACK>? = getCapabilityViews(stack).getOrNull(index)

    fun getCapabilityStacks(stack: IItemStackExtension): List<STACK?> = getCapabilityViews(stack).map(HTStackView<STACK>::getStack)

    fun getCapabilityStack(stack: IItemStackExtension, index: Int): STACK? = getCapabilityView(stack, index)?.getStack()

    // HTItemStorageStack

    fun getCapabilityViews(stack: ImmutableItemStack?): List<HTStackView<STACK>> = getCapability(stack)?.let { apply(it, null) } ?: listOf()

    fun getCapabilityView(stack: ImmutableItemStack?, index: Int): HTStackView<STACK>? = getCapabilityViews(stack).getOrNull(index)

    fun getCapabilityStacks(stack: ImmutableItemStack?): List<STACK?> = getCapabilityViews(stack).map(HTStackView<STACK>::getStack)

    fun getCapabilityStack(stack: ImmutableItemStack?, index: Int): STACK? = getCapabilityView(stack, index)?.getStack()

    interface Simple<HANDLER : Any, STACK : ImmutableStack<*, STACK>> : HTStackViewCapability<HANDLER, HANDLER, STACK>
}

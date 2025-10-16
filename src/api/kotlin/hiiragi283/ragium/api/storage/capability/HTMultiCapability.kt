package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension

/**
 * 複数のキャパビリティを束ねるインターフェース
 * @param HANDLER キャパビリティのインターフェース
 * @param ITEM_HANDLER アイテムにおけるキャパビリティのインターフェース
 * @param SLOTTED_HANDLER [SLOT]に基づいた[HANDLER]の拡張インターフェース
 * @param SLOT 単一の値を保持するスロットのインターフェース
 * @see [mekanism.common.capabilities.IMultiTypeCapability]
 */
interface HTMultiCapability<HANDLER : Any, ITEM_HANDLER : HANDLER, SLOTTED_HANDLER : HANDLER, SLOT : Any> {
    fun blockCapability(): BlockCapability<HANDLER, Direction?>

    fun itemCapability(): ItemCapability<ITEM_HANDLER, Void?>

    //    Block    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): HANDLER? = level.getCapability(blockCapability(), pos, side)

    /**
     * 指定した引数から[SLOTTED_HANDLER]を返します。
     * @return [SLOTTED_HANDLER]が見つからない場合は`null`
     */
    fun getSlottedCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): SLOTTED_HANDLER?

    /**
     * 指定した引数から[SLOT]の一覧を返します。
     * @return [SLOT]の[List]
     */
    fun getCapabilitySlots(level: ILevelExtension, pos: BlockPos, side: Direction?): List<SLOT>

    /**
     * 指定した引数から[index]に対応する[SLOT]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilitySlot(
        level: ILevelExtension,
        pos: BlockPos,
        side: Direction?,
        index: Int,
    ): SLOT? = getCapabilitySlots(level, pos, side).getOrNull(index)

    //    Item    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(stack: IItemStackExtension): ITEM_HANDLER? = stack.getCapability(itemCapability())

    /**
     * 指定した引数から[SLOTTED_HANDLER]を返します。
     * @return [SLOTTED_HANDLER]が見つからない場合は`null`
     */
    fun getSlottedCapability(stack: IItemStackExtension): SLOTTED_HANDLER?

    /**
     * 指定した引数から[SLOT]の一覧を返します。
     * @return [SLOT]の[List]
     */
    fun getCapabilitySlots(stack: IItemStackExtension): List<SLOT>

    /**
     * 指定した引数から[index]に対応する[SLOT]を返します。
     * @return 見つからない場合は`null`
     */
    fun getCapabilitySlot(stack: IItemStackExtension, index: Int): SLOT? = getCapabilitySlots(stack).getOrNull(index)

    fun hasCapability(stack: IItemStackExtension): Boolean = getCapability(stack) != null

    // HTItemStorageStack
    fun getCapability(stack: ImmutableItemStack): ITEM_HANDLER? = getCapability(stack.stack)

    fun getSlottedCapability(stack: ImmutableItemStack): SLOTTED_HANDLER? = getSlottedCapability(stack.stack)

    fun getCapabilitySlots(stack: ImmutableItemStack): List<SLOT> = getCapabilitySlots(stack.stack)

    fun getCapabilitySlot(stack: ImmutableItemStack, index: Int): SLOT? = getCapabilitySlot(stack.stack, index)
}

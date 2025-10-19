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
 * @see [mekanism.common.capabilities.IMultiTypeCapability]
 */
interface HTMultiCapability<HANDLER : Any, ITEM_HANDLER : HANDLER> {
    fun blockCapability(): BlockCapability<HANDLER, Direction?>

    fun itemCapability(): ItemCapability<ITEM_HANDLER, Void?>

    //    Block    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): HANDLER? = level.getCapability(blockCapability(), pos, side)

    //    Item    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(stack: IItemStackExtension): ITEM_HANDLER? = stack.getCapability(itemCapability())

    fun hasCapability(stack: IItemStackExtension): Boolean = getCapability(stack) != null

    // HTItemStorageStack
    fun getCapability(stack: ImmutableItemStack): ITEM_HANDLER? = getCapability(stack.stack)

    fun hasCapability(stack: ImmutableItemStack): Boolean = hasCapability(stack.stack)
}

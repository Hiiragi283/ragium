package hiiragi283.ragium.api.storage.capability

import com.google.common.util.concurrent.Runnables
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension
import java.util.function.BooleanSupplier

/**
 * 複数のキャパビリティを束ねるインターフェース
 * @param HANDLER キャパビリティのインターフェース
 * @param ITEM_HANDLER アイテムにおけるキャパビリティのインターフェース
 * @see mekanism.common.capabilities.IMultiTypeCapability
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
interface HTMultiCapability<HANDLER : Any, ITEM_HANDLER : HANDLER> {
    val block: BlockCapability<HANDLER, Direction?>
    val entity: EntityCapability<HANDLER, Direction?>
    val item: ItemCapability<ITEM_HANDLER, Void?>

    //    Block    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): HANDLER? = level.getCapability(block, pos, side)

    fun createCache(
        level: ServerLevel,
        pos: BlockPos,
        side: Direction?,
        validator: BooleanSupplier = BooleanSupplier { true },
        listener: Runnable = Runnables.doNothing(),
    ): BlockCapabilityCache<HANDLER, Direction?> = BlockCapabilityCache.create(
        block,
        level,
        pos,
        side,
        validator,
        listener,
    )

    //    Entity    //

    fun getCapability(entity: Entity, side: Direction?): HANDLER? = entity.getCapability(this@HTMultiCapability.entity, side)

    //    Item    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(stack: IItemStackExtension): ITEM_HANDLER? = stack.getCapability(item)

    fun hasCapability(stack: IItemStackExtension): Boolean = getCapability(stack) != null

    // HTItemStorageStack
    fun getCapability(stack: ImmutableItemStack?): ITEM_HANDLER? = stack?.getCapability(item)

    fun hasCapability(stack: ImmutableItemStack): Boolean = getCapability(stack) != null

    interface Simple<HANDLER : Any> : HTMultiCapability<HANDLER, HANDLER>
}

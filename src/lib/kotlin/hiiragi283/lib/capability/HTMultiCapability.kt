package hiiragi283.lib.capability

import com.google.common.util.concurrent.Runnables
import java.util.function.BooleanSupplier
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.transfer.access.ItemAccess

/**
 * 複数のキャパビリティを束ねるインターフェース
 *
 * 参照 : [Mekanism - IMultiTypeCapability](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/capabilities/IMultiTypeCapability.java)
 * @param HANDLER キャパビリティのインターフェース
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTMultiCapability<HANDLER : Any> {
    val block: BlockCapability<HANDLER, Direction?>
    val entity: EntityCapability<HANDLER, Direction?>
    val item: ItemCapability<HANDLER, ItemAccess>

    //    Block    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(level: Level, pos: BlockPos, side: Direction?): HANDLER? = level.getCapability(block, pos, side)

    fun createCache(level: ServerLevel, pos: BlockPos, side: Direction?): BlockCapabilityCache<HANDLER, Direction?> = BlockCapabilityCache.create(block, level, pos, side)

    fun createCache(level: ServerLevel, pos: BlockPos, side: Direction?, validator: BooleanSupplier = BooleanSupplier { true }, listener: Runnable = Runnables.doNothing()): BlockCapabilityCache<HANDLER, Direction?> = BlockCapabilityCache.create(block, level, pos, side, validator, listener)

    //    Entity    //

    fun getCapability(entity: Entity, side: Direction?): HANDLER? = entity.getCapability(this@HTMultiCapability.entity, side)

    //    Item    //

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(access: ItemAccess): HANDLER? = access.getCapability(item)

    fun hasCapability(access: ItemAccess): Boolean = getCapability(access) != null

    /**
     * 指定した引数から[HANDLER]を返します。
     * @return [HANDLER]が見つからない場合は`null`
     */
    fun getCapability(stack: ItemStack): HANDLER? = ItemAccess.forStack(stack).let(::getCapability)

    fun hasCapability(stack: ItemStack): Boolean = getCapability(stack) != null
}

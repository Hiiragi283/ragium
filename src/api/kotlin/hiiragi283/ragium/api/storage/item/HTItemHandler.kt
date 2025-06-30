package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.extension.dropStackAt
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.items.IItemHandler
import kotlin.collections.forEach

interface HTItemHandler :
    IItemHandler,
    INBTSerializable<CompoundTag> {
    val isEmpty: Boolean
    val slotRange: IntRange get() = (0 until slots)

    fun forEachStacks(action: (ItemStack) -> Unit) {
        slotRange.map(this::getStackInSlot).forEach(action)
    }

    fun forEachIndexedStacks(action: (Int, ItemStack) -> Unit) {
        slotRange.forEach { index: Int -> action(index, getStackInSlot(index)) }
    }

    fun dropStacksAt(entity: Entity) {
        forEachStacks { stack: ItemStack -> dropStackAt(entity, stack) }
    }

    fun dropStacksAt(level: Level, pos: BlockPos) {
        forEachStacks { stack: ItemStack -> dropStackAt(level, pos, stack) }
    }

    fun consumeStackInSlot(slot: Int, count: Int)
}

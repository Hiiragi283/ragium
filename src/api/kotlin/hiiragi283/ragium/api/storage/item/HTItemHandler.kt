package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.extension.dropStackAt
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.items.IItemHandler

interface HTItemHandler :
    IItemHandler,
    INBTSerializable<CompoundTag> {
    val isEmpty: Boolean
    val slotRange: IntRange get() = (0 until slots)

    fun dropStacksAt(entity: Entity) {
        for (stack: ItemStack in getStackView()) {
            dropStackAt(entity, stack)
        }
    }

    fun dropStacksAt(level: Level, pos: BlockPos) {
        for (stack: ItemStack in getStackView()) {
            dropStackAt(level, pos, stack)
        }
    }

    fun consumeStackInSlot(slot: Int, count: Int)

    fun getStackView(): Iterable<ItemStack>
}

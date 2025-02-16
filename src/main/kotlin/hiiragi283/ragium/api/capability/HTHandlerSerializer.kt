package hiiragi283.ragium.api.capability

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.logError
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import org.slf4j.Logger

/**
 * 指定された[items]と[fluids]を[Codec]に基づいて読み書きするクラス
 */
class HTHandlerSerializer private constructor(val items: List<HTSlotHandler<ItemStack>>, val fluids: List<HTSlotHandler<FluidStack>>) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        /**
         * 空のインスタンス
         */
        @JvmField
        val EMPTY = HTHandlerSerializer(listOf(), listOf())

        /**
         * 指定した[items]と[fluids]から新しいインスタンスを返します。
         * @return [items]と[fluids]が共に空の場合は[HTHandlerSerializer.EMPTY]
         */
        @JvmStatic
        fun of(items: List<HTSlotHandler<ItemStack>>, fluids: List<HTSlotHandler<FluidStack>>): HTHandlerSerializer {
            if (items.isEmpty() && fluids.isEmpty()) {
                return EMPTY
            }
            return HTHandlerSerializer(items, fluids)
        }

        /**
         * 指定した[items]から新しいインスタンスを返します。
         * @return [items]が空の場合は[HTHandlerSerializer.EMPTY]
         */
        @JvmStatic
        fun ofItem(items: List<HTSlotHandler<ItemStack>>): HTHandlerSerializer =
            if (items.isEmpty()) EMPTY else HTHandlerSerializer(items, listOf())

        /**
         * 指定した[fluids]から新しいインスタンスを返します。
         * @return [fluids]が空の場合は[HTHandlerSerializer.EMPTY]
         */
        @JvmStatic
        fun ofFluid(fluids: List<HTSlotHandler<FluidStack>>): HTHandlerSerializer =
            if (fluids.isEmpty()) EMPTY else HTHandlerSerializer(listOf(), fluids)
    }

    /**
     * 指定した[nbt]に値を書き込みます。
     */
    fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        ItemStack.OPTIONAL_CODEC
            .listOf(1, Int.MAX_VALUE)
            .encodeStart(dynamicOps, items.map(HTSlotHandler<ItemStack>::stack))
            .ifSuccess { nbt.put("items", it) }
            .logError(LOGGER)
        FluidStack.OPTIONAL_CODEC
            .listOf(1, Int.MAX_VALUE)
            .encodeStart(dynamicOps, fluids.map(HTSlotHandler<FluidStack>::stack))
            .ifSuccess { nbt.put("fluids", it) }
            .logError(LOGGER)
    }

    /**
     * 指定した[nbt]から値を読み取ります。
     */
    fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        ItemStack.OPTIONAL_CODEC
            .listOf(1, Int.MAX_VALUE)
            .parse(dynamicOps, nbt.get("items"))
            .ifSuccess { stacks: List<ItemStack> ->
                stacks.forEachIndexed { index: Int, stack: ItemStack ->
                    items[index].stack = stack
                }
            }.logError(LOGGER)
        FluidStack.OPTIONAL_CODEC
            .listOf(1, Int.MAX_VALUE)
            .parse(dynamicOps, nbt.get("fluids"))
            .ifSuccess { stacks: List<FluidStack> ->
                stacks.forEachIndexed { index: Int, stack: FluidStack ->
                    fluids[index].stack = stack
                }
            }.logError(LOGGER)
    }

    fun dropItems(level: Level, pos: BlockPos) {
        items.forEach { slot: HTSlotHandler<ItemStack> ->
            dropStackAt(level, pos, slot.stack)
        }
    }
}

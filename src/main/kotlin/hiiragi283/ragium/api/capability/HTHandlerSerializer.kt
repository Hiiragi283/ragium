package hiiragi283.ragium.api.capability

import com.mojang.logging.LogUtils
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import org.slf4j.Logger

class HTHandlerSerializer private constructor(val items: List<HTSlotHandler<ItemStack>>, val fluids: List<HTSlotHandler<FluidStack>>) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        @JvmField
        val EMPTY = HTHandlerSerializer(listOf(), listOf())

        @JvmStatic
        fun of(items: List<HTSlotHandler<ItemStack>>, fluids: List<HTSlotHandler<FluidStack>>): HTHandlerSerializer {
            if (items.isEmpty() && fluids.isEmpty()) {
                return EMPTY
            }
            return HTHandlerSerializer(items, fluids)
        }

        @JvmStatic
        fun ofItem(items: List<HTSlotHandler<ItemStack>>): HTHandlerSerializer =
            if (items.isEmpty()) EMPTY else HTHandlerSerializer(items, listOf())

        @JvmStatic
        fun ofFluid(fluids: List<HTSlotHandler<FluidStack>>): HTHandlerSerializer =
            if (fluids.isEmpty()) EMPTY else HTHandlerSerializer(listOf(), fluids)
    }

    fun writeNbt(nbt: CompoundTag, provider: HolderLookup.Provider) {
        val dynamicOps: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)
        ItemStack.OPTIONAL_CODEC
            .listOf()
            .encodeStart(dynamicOps, items.map(HTSlotHandler<ItemStack>::stack))
            .ifSuccess { nbt.put("items", it) }
            .ifError { LOGGER.error(it.message()) }
        FluidStack.OPTIONAL_CODEC
            .listOf()
            .encodeStart(dynamicOps, fluids.map(HTSlotHandler<FluidStack>::stack))
            .ifSuccess { nbt.put("fluids", it) }
            .ifError { LOGGER.error(it.message()) }
    }

    fun readNbt(nbt: CompoundTag, provider: HolderLookup.Provider) {
        val dynamicOps: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)
        ItemStack.OPTIONAL_CODEC
            .listOf()
            .parse(dynamicOps, nbt.get("items"))
            .ifSuccess { stacks: List<ItemStack> ->
                stacks.forEachIndexed { index: Int, stack: ItemStack ->
                    items[index].stack = stack
                }
            }.ifError { LOGGER.error(it.message()) }
        FluidStack.OPTIONAL_CODEC
            .listOf()
            .parse(dynamicOps, nbt.get("fluids"))
            .ifSuccess { stacks: List<FluidStack> ->
                stacks.forEachIndexed { index: Int, stack: FluidStack ->
                    fluids[index].stack = stack
                }
            }.ifError { LOGGER.error(it.message()) }
    }
}

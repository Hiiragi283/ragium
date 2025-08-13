package hiiragi283.ragium.integration.jade.base

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTProgressData private constructor(
    val progress: Int,
    val total: Int,
    val items: List<ItemStack>,
    val fluids: List<FluidStack>,
) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTProgressData> = BiCodec.composite(
            BiCodec.INT.fieldOf("progress"),
            HTProgressData::progress,
            BiCodec.INT.fieldOf("total"),
            HTProgressData::total,
            BiCodecs.ITEM_STACK.listOf().fieldOf("items"),
            HTProgressData::items,
            BiCodecs.FLUID_STACK.listOf().fieldOf("fluids"),
            HTProgressData::fluids,
            ::HTProgressData,
        )

        @JvmStatic
        fun builder(): Builder = Builder()
    }

    fun getItem(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): FluidStack = fluids.getOrNull(index) ?: FluidStack.EMPTY

    class Builder {
        private val items: MutableList<ItemStack> = mutableListOf()
        private val fluids: MutableList<FluidStack> = mutableListOf()

        fun addEmpty(): Builder = addItem(ItemStack.EMPTY)

        fun addItem(stack: ItemStack): Builder = apply {
            items.add(stack.copy())
        }

        fun addFluid(stack: FluidStack): Builder = apply {
            fluids.add(stack.copy())
        }

        fun build(progress: Int, total: Int): HTProgressData = HTProgressData(progress, total, items, fluids)
    }
}

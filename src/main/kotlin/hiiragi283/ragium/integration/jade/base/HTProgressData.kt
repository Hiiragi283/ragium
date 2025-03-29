package hiiragi283.ragium.integration.jade.base

import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTProgressData(
    val progress: Int,
    val total: Int,
    val items: List<ItemStack>,
    val fluids: List<FluidStack>,
) {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTProgressData> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTProgressData::progress,
            ByteBufCodecs.VAR_INT,
            HTProgressData::total,
            ItemStack.OPTIONAL_STREAM_CODEC.listOf(),
            HTProgressData::items,
            FluidStack.OPTIONAL_STREAM_CODEC.listOf(),
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

        fun addEmpty(): Builder = addItem(null)

        fun addItem(slot: HTItemSlot?): Builder = addItem(slot?.stack ?: ItemStack.EMPTY)

        fun addItem(stack: ItemStack): Builder = apply {
            items.add(stack.copy())
        }

        fun addFluid(tank: HTFluidTank?): Builder = addFluid(tank?.stack ?: FluidStack.EMPTY)

        fun addFluid(stack: FluidStack): Builder = apply {
            fluids.add(stack.copy())
        }

        fun build(progress: Int, total: Int): HTProgressData = HTProgressData(progress, total, items, fluids)
    }
}

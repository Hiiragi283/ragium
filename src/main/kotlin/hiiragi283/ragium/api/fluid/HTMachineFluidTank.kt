package hiiragi283.ragium.api.fluid

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.extension.logError
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import org.slf4j.Logger

open class HTMachineFluidTank(capacity: Int, val callback: () -> Unit) : FluidTank(capacity) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        /**
         * 指定した[tanks]の値を[nbt]に書き込みます。
         */
        @JvmStatic
        fun writeToNBT(tanks: Array<out FluidTank>, nbt: CompoundTag, provider: HolderLookup.Provider) {
            FluidStack.OPTIONAL_CODEC
                .listOf()
                .encodeStart(
                    provider.createSerializationContext(NbtOps.INSTANCE),
                    tanks.map(FluidTank::getFluid),
                ).ifSuccess { result: Tag -> nbt.put("Fluids", result) }
                .logError(LOGGER)
        }

        /**
         * 指定した[tanks]の容量を[newTier]で更新しつつ，[nbt]から読み取ります。
         */
        @JvmStatic
        fun readFromNBT(tanks: Array<out FluidTank>, nbt: CompoundTag, provider: HolderLookup.Provider) {
            FluidStack.OPTIONAL_CODEC
                .listOf()
                .parse(
                    provider.createSerializationContext(NbtOps.INSTANCE),
                    nbt.get("Fluids"),
                ).ifSuccess { stacks: List<FluidStack> ->
                    stacks.forEachIndexed { index: Int, stack: FluidStack ->
                        val tank: FluidTank = tanks[index]
                        tank.fluid = stack
                    }
                }.logError(LOGGER)
        }
    }

    override fun onContentsChanged() {
        callback()
    }
}

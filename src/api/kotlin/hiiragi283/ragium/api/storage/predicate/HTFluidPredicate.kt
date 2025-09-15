package hiiragi283.ragium.api.storage.predicate

import hiiragi283.ragium.api.extension.negate
import hiiragi283.ragium.api.extension.partially1
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

fun interface HTFluidPredicate : Predicate<FluidStack> {
    companion object {
        @JvmField
        val TRUE = HTFluidPredicate { true }

        @JvmField
        val FALSE = HTFluidPredicate { false }

        @JvmField
        val EMPTY = HTFluidPredicate(FluidStack::isEmpty)

        @JvmField
        val NOT_EMPTY = HTFluidPredicate(FluidStack::isEmpty.negate())

        @JvmStatic
        fun byFluid(fluid: Fluid): HTFluidPredicate = HTFluidPredicate { stack: FluidStack -> stack.`is`(fluid) }

        @JvmStatic
        fun byFluid(other: FluidStack): HTFluidPredicate = FluidStack::isSameFluid.partially1(other).let(::HTFluidPredicate)

        @JvmStatic
        fun byFluidAndComponent(other: FluidStack): HTFluidPredicate =
            FluidStack::isSameFluidSameComponents.partially1(other).let(::HTFluidPredicate)

        @JvmStatic
        fun byTag(tagKey: TagKey<Fluid>): HTFluidPredicate = HTFluidPredicate { stack: FluidStack -> stack.`is`(tagKey) }

        @JvmStatic
        fun byHolder(holder: Holder<Fluid>): HTFluidPredicate = HTFluidPredicate { stack: FluidStack -> stack.`is`(holder) }

        @JvmStatic
        fun byHolderSet(holderSet: HolderSet<Fluid>): HTFluidPredicate = HTFluidPredicate { stack: FluidStack -> stack.`is`(holderSet) }
    }
}

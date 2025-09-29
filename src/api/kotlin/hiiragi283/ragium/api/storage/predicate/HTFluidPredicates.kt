package hiiragi283.ragium.api.storage.predicate

import hiiragi283.ragium.api.extension.partially1
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

object HTFluidPredicates {
    @JvmField
    val TRUE: Predicate<FluidStack> = Predicate { true }

    @JvmField
    val FALSE: Predicate<FluidStack> = TRUE.negate()

    @JvmField
    val EMPTY: Predicate<FluidStack> = Predicate(FluidStack::isEmpty)

    @JvmField
    val NOT_EMPTY: Predicate<FluidStack> = EMPTY.negate()

    @JvmStatic
    fun byFluid(fluid: Fluid): Predicate<FluidStack> = Predicate { stack: FluidStack -> stack.`is`(fluid) }

    @JvmStatic
    fun byFluid(other: FluidStack): Predicate<FluidStack> = Predicate(FluidStack::isSameFluid.partially1(other))

    @JvmStatic
    fun byFluidAndComponent(other: FluidStack): Predicate<FluidStack> = Predicate(FluidStack::isSameFluidSameComponents.partially1(other))

    @JvmStatic
    fun byTag(tagKey: TagKey<Fluid>): Predicate<FluidStack> = Predicate { stack: FluidStack -> stack.`is`(tagKey) }

    @JvmStatic
    fun byHolder(holder: Holder<Fluid>): Predicate<FluidStack> = Predicate { stack: FluidStack -> stack.`is`(holder) }

    @JvmStatic
    fun byHolderSet(holderSet: HolderSet<Fluid>): Predicate<FluidStack> = Predicate { stack: FluidStack -> stack.`is`(holderSet) }
}

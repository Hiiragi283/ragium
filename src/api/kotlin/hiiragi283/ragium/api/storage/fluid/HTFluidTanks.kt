package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import net.neoforged.neoforge.fluids.FluidStack

typealias HTFluidView = HTStackView<ImmutableFluidStack>

fun HTFluidView.getFluidStack(): FluidStack = this.getStack()?.unwrap() ?: FluidStack.EMPTY

fun HTFluidTank.isValid(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::isValid) ?: false

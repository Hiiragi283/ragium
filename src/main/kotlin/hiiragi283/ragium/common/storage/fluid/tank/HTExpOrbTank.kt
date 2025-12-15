package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.util.HTExperienceHelper
import net.minecraft.world.entity.ExperienceOrb

class HTExpOrbTank(private val orb: ExperienceOrb) :
    HTFluidTank.Basic(),
    HTValueSerializable.Empty {
    override fun setStack(stack: ImmutableFluidStack?) {
        if (stack == null) {
            orb.value = 0
        } else if (isValid(stack)) {
            orb.value = HTExperienceHelper.expAmountFromFluid(stack.amount())
        }
    }

    override fun updateAmount(stack: ImmutableFluidStack, amount: Int) {
        orb.value = HTExperienceHelper.expAmountFromFluid(amount)
    }

    override fun isValid(stack: ImmutableFluidStack): Boolean = RagiumFluidContents.EXPERIENCE.isOf(stack)

    override fun getStack(): ImmutableFluidStack? = orb
        .value
        .let(HTExperienceHelper::fluidAmountFromExp)
        .let(RagiumFluidContents.EXPERIENCE::toImmutableStack)

    override fun getCapacity(stack: ImmutableFluidStack?): Int = Int.MAX_VALUE

    override fun onContentsChanged() {
        if (orb.value <= 0) {
            orb.discard()
        }
    }
}

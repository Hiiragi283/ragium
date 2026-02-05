package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTMutableFluidTank
import hiiragi283.core.api.toFraction
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.util.HTExperienceHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

class HTExperienceTankBlockEntity(pos: BlockPos, state: BlockState) : HTTankBlockEntity(TODO(), pos, state) {
    private var expLevel: Fraction = Fraction.ZERO

    private inner class ExperienceTank :
        HTMutableFluidTank(),
        HTContentListener.Empty {
        override fun setResource(resource: HTFluidResourceType?) {}

        override fun setAmount(amount: Int) {
            expLevel = HTExperienceHelper.expAmountFromFluid(amount).toFraction(1)
        }

        override fun getAmount(): Int = HTExperienceHelper.fluidAmountFromExp(expLevel).toInt()

        override fun getResource(): HTFluidResourceType? = HCFluids.EXPERIENCE.toResource()

        override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

        override fun isValid(resource: HTFluidResourceType): Boolean = resource.isOf(HCFluids.EXPERIENCE)

        override fun serialize(output: HTValueOutput) {
            output.write("exp_level", BiCodecs.NON_NEGATIVE_FRACTION, expLevel)
        }

        override fun deserialize(input: HTValueInput) {
            val expLevel: Fraction = input.read("exp_level", BiCodecs.NON_NEGATIVE_FRACTION) ?: return
            this@HTExperienceTankBlockEntity.expLevel = expLevel
        }
    }
}

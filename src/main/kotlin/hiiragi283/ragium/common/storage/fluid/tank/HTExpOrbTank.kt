package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.entity.ExperienceOrb

class HTExpOrbTank(private val expOrb: ExperienceOrb) :
    HTFluidTank.Basic(),
    HTValueSerializable.Empty {
    private val multiplier: Int get() = RagiumConfig.COMMON.expCollectorMultiplier.asInt

    override fun getStack(): ImmutableFluidStack? = RagiumFluidContents.EXPERIENCE.toStorageStack(expOrb.value * multiplier)

    override fun getCapacity(stack: ImmutableFluidStack?): Int = when {
        stack == null || !isValid(stack) -> 0
        else -> Int.MAX_VALUE
    }

    override fun isValid(stack: ImmutableFluidStack): Boolean = RagiumFluidContents.EXPERIENCE.isOf(stack)

    override fun onContentsChanged() {
        if (expOrb.value <= 0) {
            expOrb.discard()
        }
    }

    override fun setStack(stack: ImmutableFluidStack?) {
        expOrb.value = when (stack) {
            null -> 0
            else -> stack.amountAsInt() / multiplier
        }
    }
}

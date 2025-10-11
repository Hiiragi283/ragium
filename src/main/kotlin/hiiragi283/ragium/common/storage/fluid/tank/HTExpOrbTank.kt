package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.fluid.HTFluidStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.entity.ExperienceOrb

class HTExpOrbTank(private val expOrb: ExperienceOrb) :
    HTFluidTank.Mutable(),
    HTValueSerializable.Empty {
    private val multiplier: Int get() = RagiumConfig.COMMON.expCollectorMultiplier.asInt

    override fun getStack(): HTFluidStorageStack = RagiumFluidContents.EXPERIENCE.toStorageStack(expOrb.value * multiplier)

    override fun getCapacityAsLong(stack: HTFluidStorageStack): Long = when (isValid(stack)) {
        true -> Long.MAX_VALUE
        false -> 0
    }

    override fun isValid(stack: HTFluidStorageStack): Boolean = RagiumFluidContents.EXPERIENCE.isOf(stack)

    override fun onContentsChanged() {
        if (expOrb.value <= 0) {
            expOrb.discard()
        }
    }

    override fun setStack(stack: HTFluidStorageStack) {
        expOrb.value = stack.amountAsInt() / multiplier
    }
}

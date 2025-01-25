package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.common.init.RagiumFluids
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

data class HTGeneratorFuel(val tagKey: TagKey<Fluid>, val amount: Int) {
    constructor(fluid: RagiumFluids, amount: Int) : this(fluid.commonTag, amount)

    val isEmpty: Boolean = amount <= 0

    fun isAcceptable(stack: FluidStack): Boolean = stack.`is`(tagKey) && stack.amount >= amount

    fun isAcceptableWithoutAmount(stack: FluidStack): Boolean = stack.`is`(tagKey)
}

package hiiragi283.ragium.client.integration.jade

import net.neoforged.neoforge.fluids.FluidStack
import snownee.jade.api.fluid.JadeFluidObject

fun FluidStack.toJade(): JadeFluidObject = JadeFluidObject.of(this.fluid, this.amount.toLong(), this.componentsPatch)

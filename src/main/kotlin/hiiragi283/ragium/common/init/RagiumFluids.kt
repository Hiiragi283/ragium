package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.fluid.HTFlowableFluid
import hiiragi283.ragium.common.fluid.HTFluidContent

object RagiumFluids {
    @JvmField
    val OIL: HTFluidContent = register("oil")

    @JvmStatic
    fun init() {
        OIL.registerAttributes {
            viscosity = 6000
        }
    }

    private fun register(name: String, builderAction: HTFlowableFluid.Settings.() -> Unit = {}): HTFluidContent =
        HTFluidContent.create(Ragium.id(name), builderAction)
}

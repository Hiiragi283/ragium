package hiiragi283.ragium.common.unused

import hiiragi283.ragium.common.Ragium

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

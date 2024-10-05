package hiiragi283.ragium.common.unused

import hiiragi283.ragium.api.RagiumAPI

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
        HTFluidContent.create(RagiumAPI.id(name), builderAction)
}

package hiiragi283.ragium.common.data.oreSlurry

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.data.oreSlurry.HTOreSlurryFluidAccess
import hiiragi283.ragium.common.fluid.RagiumFluids

class RagiumOreSlurryFluidAccess : HTOreSlurryFluidAccess {
    override val fluidContent: HTFluidContent get() = RagiumFluids.ORE_SLURRY
}

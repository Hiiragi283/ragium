package hiiragi283.ragium.common.data.recipe

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.data.recipe.HTOreSlurryFluidAccess
import hiiragi283.ragium.common.fluid.RagiumFluids

class RagiumOreSlurryFluidAccess : HTOreSlurryFluidAccess {
    override val fluidContent: HTFluidContent get() = RagiumFluids.ORE_SLURRY
}

package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.advancement.HTBuildMultiblockCriterion
import net.minecraft.advancement.criterion.Criterion
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumAdvancementCriteria {
    @JvmField
    val BUILD_MULTIBLOCK: HTBuildMultiblockCriterion = register("build_multiblock", HTBuildMultiblockCriterion)

    private fun <T : Criterion<*>> register(name: String, criterion: T): T = Registry.register(
        Registries.CRITERION,
        Ragium.id(name),
        criterion,
    )
}

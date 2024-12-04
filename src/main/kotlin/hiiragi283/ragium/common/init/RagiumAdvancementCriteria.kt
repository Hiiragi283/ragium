package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.advancement.HTDrankFluidCriterion
import hiiragi283.ragium.common.advancement.HTInteractMachineCriterion
import net.minecraft.advancement.criterion.Criterion
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumAdvancementCriteria {
    @JvmField
    val BUILT_MACHINE: HTInteractMachineCriterion = register("interact_machine", HTInteractMachineCriterion)

    @JvmField
    val DRANK_FLUID: HTDrankFluidCriterion = register("drank_fluid", HTDrankFluidCriterion)

    @JvmStatic
    private fun <T : Criterion<*>> register(name: String, criterion: T): T = Registry.register(
        Registries.CRITERION,
        RagiumAPI.id(name),
        criterion,
    )
}

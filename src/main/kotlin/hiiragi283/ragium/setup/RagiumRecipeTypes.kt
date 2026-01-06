package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTListItemRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTListItemRecipeInput, HTAlloyingRecipe> = REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTCrushingRecipe> = REGISTER.registerType(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTCuttingRecipe> = REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val DRYING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTDryingRecipe> = REGISTER.registerType(RagiumConst.DRYING)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTMixingRecipe> = REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTPlantingRecipe.Input, HTPlantingRecipe> = REGISTER.registerType(RagiumConst.PLANTING)

    @JvmField
    val PRESSING: HTDeferredRecipeType<HTListItemRecipeInput, HTPressingRecipe> = REGISTER.registerType(RagiumConst.PRESSING)

    @JvmField
    val PYROLYZING: HTDeferredRecipeType<SingleRecipeInput, HTPyrolyzingRecipe> = REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTSingleFluidRecipeInput, HTRefiningRecipe> = REGISTER.registerType(RagiumConst.REFINING)

    @JvmField
    val SIMULATING: HTDeferredRecipeType<HTSimulatingRecipe.Input, HTSimulatingRecipe<*>> = REGISTER.registerType(RagiumConst.SIMULATING)

    @JvmField
    val SOLIDIFYING: HTDeferredRecipeType<HTItemAndFluidRecipeInput, HTSolidifyingRecipe> = REGISTER.registerType(RagiumConst.SOLIDIFYING)
}

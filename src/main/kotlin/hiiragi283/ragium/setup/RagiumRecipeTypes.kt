package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RTEnchantingRecipe
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import net.minecraft.world.item.crafting.Recipe

data object RagiumRecipeTypes {
    @JvmStatic
    val allTypes: Set<HTRecipeType<*>> field: MutableSet<HTRecipeType<*>> = mutableSetOf()

    @JvmStatic
    private fun <T : Recipe<*>> create(name: String): HTRecipeType<T> = HTRecipeType<T>(RagiumAPI.id(name)).also(allTypes::add)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTRecipeType<HTAlloyingRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val ASSEMBLING: HTRecipeType<HTAssemblingRecipe> = create(RagiumConst.ASSEMBLING)

    @JvmField
    val CUTTING: HTRecipeType<HTCuttingRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val COMPRESSING: HTRecipeType<HTCompressingRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val PLANTING: HTRecipeType<RTPlantingRecipe> = create(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTRecipeType<HTFreezingRecipe> = create(RagiumConst.FREEZING)

    @JvmField
    val IMPLODING: HTRecipeType<HTImplodingRecipe> = create(RagiumConst.IMPLODING)

    @JvmField
    val MELTING: HTRecipeType<HTMeltingRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTRecipeType<HTPyrolyzingRecipe> = create(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeType<HTRefiningRecipe> = create(RagiumConst.REFINING)

    @JvmField
    val WASHING: HTRecipeType<HTWashingRecipe> = create(RagiumConst.WASHING)

    // Machine - Elite
    @JvmField
    val BATHING: HTRecipeType<HTBathingRecipe> = create(RagiumConst.BATHING)

    @JvmField
    val CHEMICAL_REACTING: HTRecipeType<HTChemicalReactingRecipe> = create(RagiumConst.CHEMICAL_REACTING)

    @JvmField
    val MIXING: HTRecipeType<HTMixingRecipe> = create(RagiumConst.MIXING)

    // Machine - Ultimate
    @JvmField
    val ENCHANTING: HTRecipeType<RTEnchantingRecipe> = create(RagiumConst.ENCHANTING)
}

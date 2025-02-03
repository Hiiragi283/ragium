package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipe
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

object RagiumJEIRecipeTypes {
    @JvmField
    val MATERIAL_INFO: RecipeType<HTTypedMaterial> =
        RecipeType.create(RagiumAPI.MOD_ID, "material_info", HTTypedMaterial::class.java)

    @JvmField
    val EXTRACTOR: RecipeType<HTExtractorRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "extractor", HTExtractorRecipe::class.java)

    @JvmStatic
    private val RECIPE_TYPE_MAP: MutableMap<HTMachineKey, RecipeType<RecipeHolder<HTMachineRecipe>>> =
        mutableMapOf()

    @JvmStatic
    fun getRecipeType(machine: HTMachineKey): RecipeType<RecipeHolder<HTMachineRecipe>> =
        RECIPE_TYPE_MAP.computeIfAbsent(machine) { key: HTMachineKey ->
            RecipeType.createRecipeHolderType(RagiumAPI.id(key.name).withSuffix("_old"))
        }
}

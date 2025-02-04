package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipe
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

object RagiumJEIRecipeTypes {
    @JvmField
    val MATERIAL_INFO: RecipeType<HTTypedMaterial> =
        RecipeType.create(RagiumAPI.MOD_ID, "material_info", HTTypedMaterial::class.java)

    @JvmField
    val COMPRESSOR: RecipeType<HTCompressorRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "compressor", HTCompressorRecipe::class.java)

    @JvmField
    val EXTRACTOR: RecipeType<HTExtractorRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "extractor", HTExtractorRecipe::class.java)

    @JvmField
    val GRINDER: RecipeType<HTGrinderRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "grinder", HTGrinderRecipe::class.java)

    @JvmField
    val REFINERY: RecipeType<HTRefineryRecipe> =
        RecipeType.create(RagiumAPI.MOD_ID, "refinery", HTRefineryRecipe::class.java)

    @JvmStatic
    private val RECIPE_TYPE_MAP: MutableMap<HTMachineKey, RecipeType<RecipeHolder<HTMachineRecipe>>> =
        mutableMapOf()

    @JvmStatic
    fun getRecipeType(machine: HTMachineKey): RecipeType<RecipeHolder<HTMachineRecipe>> =
        RECIPE_TYPE_MAP.computeIfAbsent(machine) { key: HTMachineKey ->
            RecipeType.createRecipeHolderType(RagiumAPI.id(key.name).withSuffix("_old"))
        }
}

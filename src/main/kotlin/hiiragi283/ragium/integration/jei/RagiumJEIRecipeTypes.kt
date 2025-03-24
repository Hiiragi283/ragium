package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.integration.jei.entry.HTDeviceOutputEntry
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

object RagiumJEIRecipeTypes {
    @JvmField
    val DEVICE: RecipeType<HTDeviceOutputEntry> =
        RecipeType.create(RagiumAPI.MOD_ID, "device", HTDeviceOutputEntry::class.java)

    //    Machine    //

    @JvmStatic
    fun create(recipeType: HTMachineRecipeType): RecipeType<RecipeHolder<HTMachineRecipe>> = RecipeType.createFromVanilla(recipeType)

    @JvmField
    val CENTRIFUGING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.CENTRIFUGING)

    @JvmField
    val CRUSHING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.CRUSHING)

    @JvmField
    val EXTRACTING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.EXTRACTING)

    @JvmField
    val INFUSING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.INFUSING)

    @JvmField
    val REFINING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.REFINING)
}

package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSimulatingRecipe
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

object RagiumRecipeSerializers {
    @JvmField
    val ALLOYING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTAlloyingRecipe>> =
        create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTCompressingRecipe>> =
        create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTCrushingRecipe>> =
        create(RagiumConst.CRUSHING)

    @JvmField
    val ENCHANTING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTEnchantingRecipe>> =
        create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTExtractingRecipe>> =
        create(RagiumConst.EXTRACTING)

    @JvmField
    val FLUID_TRANSFORM: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTRefiningRecipe>> =
        create(RagiumConst.FLUID_TRANSFORM)

    @JvmField
    val MELTING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTMeltingRecipe>> =
        create(RagiumConst.MELTING)

    @JvmField
    val PULVERIZING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTPulverizingRecipe>> =
        create("pulverizing")

    @JvmField
    val SIMULATING: HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTSimulatingRecipe>> =
        create(RagiumConst.SIMULATING)

    @JvmStatic
    private fun <RECIPE : Recipe<*>> create(path: String): HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<RECIPE>> =
        HTDeferredHolder.create(Registries.RECIPE_SERIALIZER, RagiumAPI.id(path))
}

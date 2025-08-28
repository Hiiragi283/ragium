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
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder

object RagiumRecipeSerializers {
    @JvmField
    val ALLOYING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTAlloyingRecipe>> =
        create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTCompressingRecipe>> =
        create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTCrushingRecipe>> =
        create(RagiumConst.CRUSHING)

    @JvmField
    val ENCHANTING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTEnchantingRecipe>> =
        create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTExtractingRecipe>> =
        create(RagiumConst.EXTRACTING)

    @JvmField
    val FLUID_TRANSFORM: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTRefiningRecipe>> =
        create(RagiumConst.FLUID_TRANSFORM)

    @JvmField
    val MELTING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTMeltingRecipe>> =
        create(RagiumConst.MELTING)

    @JvmField
    val PULVERIZING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTPulverizingRecipe>> =
        create("pulverizing")

    @JvmStatic
    fun <RECIPE : Recipe<*>> create(path: String): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<RECIPE>> =
        DeferredHolder.create(Registries.RECIPE_SERIALIZER, RagiumAPI.id(path))
}

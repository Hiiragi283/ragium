package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMixingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSolidifyingRecipe
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
    val INFUSING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTInfusingRecipe>> =
        create(RagiumConst.INFUSING)

    @JvmField
    val MELTING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTMeltingRecipe>> =
        create(RagiumConst.MELTING)

    @JvmField
    val MIXING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTMixingRecipe>> =
        create(RagiumConst.MIXING)

    @JvmField
    val PULVERIZING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTPulverizingRecipe>> =
        create("pulverizing")

    @JvmField
    val REFINING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTRefiningRecipe>> =
        create(RagiumConst.REFINING)

    @JvmField
    val SOLIDIFYING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTSolidifyingRecipe>> =
        create(RagiumConst.SOLIDIFYING)

    @JvmStatic
    fun <R : Recipe<*>> create(path: String): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<R>> =
        DeferredHolder.create(Registries.RECIPE_SERIALIZER, RagiumAPI.id(path))
}

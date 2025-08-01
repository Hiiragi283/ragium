package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.base.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.base.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.base.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.base.HTPressingRecipe
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
    val CRUSHING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTCrushingRecipe>> =
        create(RagiumConst.CRUSHING)

    @JvmField
    val EXTRACTING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTExtractingRecipe>> =
        create(RagiumConst.EXTRACTING)

    @JvmField
    val PRESSING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTPressingRecipe>> =
        create(RagiumConst.PRESSING)

    @JvmStatic
    fun <R : Recipe<*>> create(path: String): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<R>> =
        DeferredHolder.create(Registries.RECIPE_SERIALIZER, RagiumAPI.id(path))
}

package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumRecipeTypes {
    @JvmField
    val REGISTER: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>> register(name: String): DeferredHolder<RecipeType<*>, RecipeType<T>> =
        REGISTER.register(name) { id: ResourceLocation -> RecipeType.simple<T>(id) }

    @JvmField
    val COMPRESSOR: DeferredHolder<RecipeType<*>, RecipeType<HTCompressorRecipe>> = register("compressor")

    @JvmField
    val EXTRACTOR: DeferredHolder<RecipeType<*>, RecipeType<HTExtractorRecipe>> = register("extractor")

    @JvmField
    val REFINERY: DeferredHolder<RecipeType<*>, RecipeType<HTRefineryRecipe>> = register("refinery")
}

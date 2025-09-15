package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTSimpleRecipeSerializer
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumCustomRecipeSerializers {
    @JvmField
    val REGISTER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>, S : RecipeSerializer<T>> register(name: String, serializer: S): S {
        REGISTER.register(name) { _: ResourceLocation -> serializer }
        return serializer
    }

    //    Custom    //

    @JvmField
    val ICE_CREAM_SODA: SimpleCraftingRecipeSerializer<HTIceCreamSodaRecipe> =
        register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))

    @JvmField
    val SMITHING_MODIFY: HTSimpleRecipeSerializer<HTSmithingModifyRecipe> =
        register("smithing_modify", HTSimpleRecipeSerializer(HTSmithingModifyRecipe.CODEC))
}

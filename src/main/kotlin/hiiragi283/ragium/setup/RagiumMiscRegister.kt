package hiiragi283.ragium.setup

import hiiragi283.ragium.api.codec.MapBiCodec
import hiiragi283.ragium.api.recipe.HTSimpleRecipeSerializer
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSimulatingRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeSerializer
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.common.recipe.result.HTFluidResultImpl
import hiiragi283.ragium.common.recipe.result.HTItemResultImpl
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun onRegister(event: RegisterEvent) {
        event.register(Registries.RECIPE_SERIALIZER, ::recipeSerializers)
        event.register(Registries.RECIPE_TYPE, ::recipeTypes)
    }

    @JvmStatic
    private fun recipeSerializers(helper: RegisterEvent.RegisterHelper<RecipeSerializer<*>>) {
        fun <RECIPE : Recipe<*>> register(holder: HTDeferredRecipeSerializer<RECIPE>, codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>) {
            helper.register(holder.id, HTSimpleRecipeSerializer(codec))
        }

        register(RagiumRecipeSerializers.SAWMILL, RagiumRecipeBiCodecs.SAWMILL)
        // Machine
        register(
            RagiumRecipeSerializers.ALLOYING,
            RagiumRecipeBiCodecs.combineItemToObj(::HTAlloyingRecipe, 2..3),
        )
        register(
            RagiumRecipeSerializers.COMPRESSING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResultImpl.CODEC, ::HTCompressingRecipe),
        )
        register(
            RagiumRecipeSerializers.CRUSHING,
            RagiumRecipeBiCodecs.itemToChancedItem(::HTCrushingRecipe),
        )
        register(
            RagiumRecipeSerializers.ENCHANTING,
            RagiumRecipeBiCodecs.combineItemToObj(::HTEnchantingRecipe, 1..3),
        )
        register(
            RagiumRecipeSerializers.EXTRACTING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResultImpl.CODEC, ::HTExtractingRecipe),
        )
        register(
            RagiumRecipeSerializers.FLUID_TRANSFORM,
            RagiumRecipeBiCodecs.fluidTransform(::HTRefiningRecipe),
        )
        register(
            RagiumRecipeSerializers.MELTING,
            RagiumRecipeBiCodecs.itemToObj(HTFluidResultImpl.CODEC, ::HTMeltingRecipe),
        )
        register(
            RagiumRecipeSerializers.PULVERIZING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResultImpl.CODEC, ::HTPulverizingRecipe),
        )
        register(
            RagiumRecipeSerializers.SIMULATING,
            RagiumRecipeBiCodecs.itemWithCatalystToItem(::HTSimulatingRecipe),
        )
    }

    @JvmStatic
    private fun recipeTypes(helper: RegisterEvent.RegisterHelper<RecipeType<*>>) {
        fun <I : RecipeInput, R : Recipe<I>> register(holder: HTDeferredRecipeType<I, R>) {
            helper.register(holder.id, RecipeType.simple<R>(holder.id))
        }

        register(RagiumRecipeTypes.SAWMILL)
        // Machine
        register(RagiumRecipeTypes.ALLOYING)
        register(RagiumRecipeTypes.COMPRESSING)
        register(RagiumRecipeTypes.CRUSHING)
        register(RagiumRecipeTypes.ENCHANTING)
        register(RagiumRecipeTypes.EXTRACTING)
        register(RagiumRecipeTypes.FLUID_TRANSFORM)
        register(RagiumRecipeTypes.MELTING)
        register(RagiumRecipeTypes.SIMULATING)
    }
}

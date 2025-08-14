package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.MapBiCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMixingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSolidifyingRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun onRegister(event: RegisterEvent) {
        event.register(Registries.RECIPE_SERIALIZER, ::recipeSerializers)
        event.register(Registries.RECIPE_TYPE, ::recipeTypes)
    }

    @JvmStatic
    private fun recipeSerializers(helper: RegisterEvent.RegisterHelper<RecipeSerializer<*>>) {
        fun <R : Recipe<*>> register(
            holder: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<R>>,
            codec: MapBiCodec<RegistryFriendlyByteBuf, R>,
        ) {
            helper.register(
                holder.id,
                object : RecipeSerializer<R> {
                    override fun codec(): MapCodec<R> = codec.codec

                    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, R> = codec.streamCodec
                },
            )
        }

        register(
            RagiumRecipeSerializers.ALLOYING,
            RagiumRecipeBiCodecs.combineItemToItem(::HTAlloyingRecipe, 2..3),
        )
        register(
            RagiumRecipeSerializers.COMPRESSING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResult.CODEC, ::HTCompressingRecipe),
        )
        register(
            RagiumRecipeSerializers.CRUSHING,
            RagiumRecipeBiCodecs.itemToChancedItem(::HTCrushingRecipe),
        )
        register(
            RagiumRecipeSerializers.EXTRACTING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResult.CODEC, ::HTExtractingRecipe),
        )
        register(
            RagiumRecipeSerializers.INFUSING,
            RagiumRecipeBiCodecs.itemWithFluidToObj(HTItemResult.CODEC, ::HTInfusingRecipe),
        )
        register(
            RagiumRecipeSerializers.MELTING,
            RagiumRecipeBiCodecs.itemToObj(HTFluidResult.CODEC, ::HTMeltingRecipe),
        )
        register(
            RagiumRecipeSerializers.MIXING,
            RagiumRecipeBiCodecs.itemWithFluidToObj(HTFluidResult.CODEC, ::HTMixingRecipe),
        )
        register(
            RagiumRecipeSerializers.PULVERIZING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResult.CODEC, ::HTPulverizingRecipe),
        )
        register(
            RagiumRecipeSerializers.REFINING,
            RagiumRecipeBiCodecs.fluidToObj(::HTRefiningRecipe),
        )
        register(
            RagiumRecipeSerializers.SOLIDIFYING,
            RagiumRecipeBiCodecs.fluidWithCatalystToObj(HTItemResult.CODEC, ::HTSolidifyingRecipe),
        )
    }

    @JvmStatic
    private fun recipeTypes(helper: RegisterEvent.RegisterHelper<RecipeType<*>>) {
        fun <I : RecipeInput, R : Recipe<I>> register(holder: HTDeferredRecipeType<I, R>) {
            helper.register(holder.id, RecipeType.simple<R>(holder.id))
        }

        register(RagiumRecipeTypes.ALLOYING)
        register(RagiumRecipeTypes.CRUSHING)
        register(RagiumRecipeTypes.COMPRESSING)
        register(RagiumRecipeTypes.EXTRACTING)
        register(RagiumRecipeTypes.INFUSING)
        register(RagiumRecipeTypes.MELTING)
        register(RagiumRecipeTypes.MIXING)
        register(RagiumRecipeTypes.REFINING)
        register(RagiumRecipeTypes.SOLIDIFYING)
    }
}

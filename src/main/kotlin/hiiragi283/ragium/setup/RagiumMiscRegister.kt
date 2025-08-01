package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypesNew
import hiiragi283.ragium.api.recipe.base.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.base.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.base.HTExtractingRecipe
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
            codec: MapCodec<R>,
            streamCodec: StreamCodec<RegistryFriendlyByteBuf, R>,
        ) {
            helper.register(
                holder.id,
                object : RecipeSerializer<R> {
                    override fun codec(): MapCodec<R> = codec

                    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, R> = streamCodec
                },
            )
        }

        register(
            RagiumRecipeSerializers.ALLOYING,
            RagiumRecipeCodecs.combineItemToItem(::HTAlloyingRecipe),
            RagiumRecipeStreamCodecs.combineItemToItem(::HTAlloyingRecipe),
        )
        register(
            RagiumRecipeSerializers.CRUSHING,
            RagiumRecipeCodecs.itemToChancedItem(::HTCrushingRecipe),
            RagiumRecipeStreamCodecs.itemToChancedItem(::HTCrushingRecipe),
        )
        register(
            RagiumRecipeSerializers.EXTRACTING,
            RagiumRecipeCodecs.itemToItem(::HTExtractingRecipe),
            RagiumRecipeStreamCodecs.itemToItem(::HTExtractingRecipe),
        )
    }

    @JvmStatic
    private fun recipeTypes(helper: RegisterEvent.RegisterHelper<RecipeType<*>>) {
        fun <I : RecipeInput, R : Recipe<I>> register(holder: HTDeferredRecipeType<I, R>) {
            helper.register(holder.id, RecipeType.simple<R>(holder.id))
        }

        register(RagiumRecipeTypesNew.ALLOYING)
        register(RagiumRecipeTypesNew.CRUSHING)
        register(RagiumRecipeTypesNew.EXTRACTING)
    }
}

package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.recipe.*
import hiiragi283.ragium.common.recipe.custom.HTBucketExtractingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBucketFillingRecipe
import hiiragi283.ragium.common.recipe.custom.HTIceCreamSodaRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>, S : RecipeSerializer<T>> register(name: String, serializer: S): DeferredHolder<RecipeSerializer<*>, S> =
        REGISTER.register(name) { _: ResourceLocation -> serializer }

    @JvmStatic
    private fun <T : Recipe<*>> register(
        name: String,
        codec: MapCodec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<T>> = REGISTER.register(name) { _: ResourceLocation ->
        object : RecipeSerializer<T> {
            override fun codec(): MapCodec<T> = codec

            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
        }
    }

    @JvmStatic
    private fun <T : Recipe<*>> registerUnit(name: String, recipe: T): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<T>> =
        register(name, MapCodec.unit(recipe), StreamCodec.unit(recipe))

    //    Machine    //

    @JvmField
    val CRUSHING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTCrushingRecipe>> =
        register("crushing", HTCrushingRecipe.CODEC, HTCrushingRecipe.STREAM_CODEC)

    @JvmField
    val EXTRACTING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTExtractingRecipe>> =
        register("extracting", HTExtractingRecipe.CODEC, HTExtractingRecipe.STREAM_CODEC)

    @JvmField
    val INFUSING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTInfusingRecipe>> =
        register("infusing", HTInfusingRecipe.CODEC, HTInfusingRecipe.STREAM_CODEC)

    @JvmField
    val REFINING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTRefiningRecipe>> =
        register("refining", HTRefiningRecipe.CODEC, HTRefiningRecipe.STREAM_CODEC)

    @JvmField
    val SOLIDIFYING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTSolidifyingRecipe>> =
        register("solidifying", HTSolidifyingRecipe.CODEC, HTSolidifyingRecipe.STREAM_CODEC)

    //    Custom    //

    @JvmField
    val BUCKET_EXTRACTING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTBucketExtractingRecipe>> =
        registerUnit("bucket_extracting", HTBucketExtractingRecipe)

    @JvmField
    val BUCKET_FILLING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTBucketFillingRecipe>> =
        registerUnit("bucket_filling", HTBucketFillingRecipe)

    @JvmField
    val ICE_CREAM_SODA: DeferredHolder<RecipeSerializer<*>, SimpleCraftingRecipeSerializer<HTIceCreamSodaRecipe>> =
        register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))
}

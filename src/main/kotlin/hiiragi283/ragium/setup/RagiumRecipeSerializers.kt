package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTDefinitionRecipe
import hiiragi283.ragium.common.recipe.*
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
    private fun <T : HTDefinitionRecipe<*>> register(
        name: String,
        factory: HTDefinitionRecipe.Factory<T>,
    ): DeferredHolder<RecipeSerializer<*>, HTDefinitionRecipe.Serializer<T>> = register(name, HTDefinitionRecipe.Serializer(name, factory))

    @JvmStatic
    private fun <T : Recipe<*>> registerUnit(name: String, recipe: T): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<T>> =
        register(name, MapCodec.unit(recipe), StreamCodec.unit(recipe))

    //    Machine    //

    @JvmField
    val CRUSHING: DeferredHolder<RecipeSerializer<*>, HTDefinitionRecipe.Serializer<HTCrushingRecipe>> =
        register("crushing", RagiumRecipeFactories::crushing)

    @JvmField
    val EXTRACTING: DeferredHolder<RecipeSerializer<*>, HTDefinitionRecipe.Serializer<HTExtractingRecipe>> =
        register("extracting", RagiumRecipeFactories::extracting)

    @JvmField
    val INFUSING: DeferredHolder<RecipeSerializer<*>, HTDefinitionRecipe.Serializer<HTInfusingRecipe>> =
        register("infusing", RagiumRecipeFactories::infusing)

    @JvmField
    val REFINING: DeferredHolder<RecipeSerializer<*>, HTDefinitionRecipe.Serializer<HTRefiningRecipe>> =
        register("refining", RagiumRecipeFactories::refining)

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

    @JvmField
    val MATERIAL_CRUSHING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTMaterialCrushingRecipe>> =
        register("material_crushing", HTMaterialCrushingRecipe.CODEC, HTMaterialCrushingRecipe.STREAM_CODEC)
}

package hiiragi283.ragium.setup

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTTransmuteRecipe
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBeehiveRecipe
import hiiragi283.ragium.common.recipe.HTBlockInteractingRecipeImpl
import hiiragi283.ragium.common.recipe.HTCauldronDroppingRecipeImpl
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBucketExtractingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBucketFillingRecipe
import hiiragi283.ragium.common.recipe.custom.HTEternalTicketRecipe
import hiiragi283.ragium.common.recipe.custom.HTIceCreamSodaRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>, S : RecipeSerializer<T>> register(name: String, serializer: S): Supplier<S> =
        REGISTER.register(name) { _: ResourceLocation -> serializer }

    @JvmStatic
    private fun <T : Recipe<*>> register(
        name: String,
        codec: MapCodec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ): Supplier<RecipeSerializer<T>> = REGISTER.register(name) { _: ResourceLocation ->
        object : RecipeSerializer<T> {
            override fun codec(): MapCodec<T> = codec

            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
        }
    }

    @JvmStatic
    private fun <T : Recipe<*>> registerUnit(name: String, recipe: T): Supplier<RecipeSerializer<T>> =
        register(name, MapCodec.unit(recipe), StreamCodec.unit(recipe))

    //    Machine    //

    @JvmField
    val ALLOYING: Supplier<RecipeSerializer<HTAlloyingRecipe>> =
        register(RagiumConstantValues.ALLOYING, HTAlloyingRecipe.CODEC, HTAlloyingRecipe.STREAM_CODEC)

    @JvmField
    val BEE_HIVE: Supplier<RecipeSerializer<HTBeehiveRecipe>> =
        register(RagiumConstantValues.BEE_HIVE, HTBeehiveRecipe.CODEC, HTBeehiveRecipe.STREAM_CODEC)

    @JvmField
    val BLOCK_INTERACTING: Supplier<RecipeSerializer<HTBlockInteractingRecipeImpl>> =
        register(RagiumConstantValues.BLOCK_INTERACTING, HTBlockInteractingRecipeImpl.CODEC, HTBlockInteractingRecipeImpl.STREAM_CODEC)

    @JvmField
    val CAULDRON_DROPPING: Supplier<RecipeSerializer<HTCauldronDroppingRecipeImpl>> =
        register(RagiumConstantValues.CAULDRON_DROPPING, HTCauldronDroppingRecipeImpl.CODEC, HTCauldronDroppingRecipeImpl.STREAM_CODEC)

    @JvmField
    val CRUSHING: Supplier<RecipeSerializer<HTCrushingRecipe>> =
        register(RagiumConstantValues.CRUSHING, HTCrushingRecipe.CODEC, HTCrushingRecipe.STREAM_CODEC)

    @JvmField
    val EXTRACTING: Supplier<RecipeSerializer<HTExtractingRecipe>> =
        register(RagiumConstantValues.EXTRACTING, HTExtractingRecipe.CODEC, HTExtractingRecipe.STREAM_CODEC)

    @JvmField
    val INFUSING: Supplier<RecipeSerializer<HTInfusingRecipe>> =
        register(RagiumConstantValues.INFUSING, HTInfusingRecipe.CODEC, HTInfusingRecipe.STREAM_CODEC)

    // val PRESSING: Supplier<RecipeSerializer<HTPressingRecipe>>

    @JvmField
    val REFINING: Supplier<RecipeSerializer<HTRefiningRecipe>> =
        register(RagiumConstantValues.REFINING, HTRefiningRecipe.CODEC, HTRefiningRecipe.STREAM_CODEC)

    @JvmField
    val SOLIDIFYING: Supplier<RecipeSerializer<HTSolidifyingRecipe>> =
        register(RagiumConstantValues.SOLIDIFYING, HTSolidifyingRecipe.CODEC, HTSolidifyingRecipe.STREAM_CODEC)

    //    Custom    //

    @Suppress("DEPRECATION")
    @JvmField
    val TRANSMUTE: Supplier<RecipeSerializer<HTTransmuteRecipe>> = register(
        "transmute",
        RecipeSerializer.SHAPELESS_RECIPE
            .codec()
            .xmap(::HTTransmuteRecipe, HTTransmuteRecipe::internalRecipe)
            .validate { recipe: HTTransmuteRecipe ->
                if (recipe.ingredients.size != 2) {
                    return@validate DataResult.error { "Transmute Recipe requires only 2 ingredients!" }
                }
                DataResult.success(recipe)
            },
        RecipeSerializer.SHAPELESS_RECIPE.streamCodec().map(::HTTransmuteRecipe, HTTransmuteRecipe::internalRecipe),
    )

    @JvmField
    val BUCKET_EXTRACTING: Supplier<RecipeSerializer<HTBucketExtractingRecipe>> =
        registerUnit("bucket_extracting", HTBucketExtractingRecipe)

    @JvmField
    val BUCKET_FILLING: Supplier<RecipeSerializer<HTBucketFillingRecipe>> =
        registerUnit("bucket_filling", HTBucketFillingRecipe)

    @JvmField
    val ETERNAL_TICKET: Supplier<RecipeSerializer<HTEternalTicketRecipe>> =
        registerUnit("eternal_ticket", HTEternalTicketRecipe)

    @JvmField
    val ICE_CREAM_SODA: Supplier<SimpleCraftingRecipeSerializer<HTIceCreamSodaRecipe>> =
        register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))
}

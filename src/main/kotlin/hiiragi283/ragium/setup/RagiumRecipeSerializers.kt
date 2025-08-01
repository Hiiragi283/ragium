package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBlastChargeRecipe
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

    //    Machine    //

    @JvmField
    val ALLOYING: Supplier<RecipeSerializer<HTAlloyingRecipe>> =
        register(RagiumConst.ALLOYING, HTAlloyingRecipe.CODEC, HTAlloyingRecipe.STREAM_CODEC)

    @JvmField
    val INFUSING: Supplier<RecipeSerializer<HTInfusingRecipe>> =
        register(RagiumConst.INFUSING, HTInfusingRecipe.CODEC, HTInfusingRecipe.STREAM_CODEC)

    @JvmField
    val MELTING: Supplier<RecipeSerializer<HTMeltingRecipe>> =
        register(RagiumConst.MELTING, HTMeltingRecipe.CODEC, HTMeltingRecipe.STREAM_CODEC)

    @JvmField
    val PRESSING: Supplier<RecipeSerializer<HTPressingRecipe>> =
        register(RagiumConst.PRESSING, HTPressingRecipe.CODEC, HTPressingRecipe.STREAM_CODEC)

    @JvmField
    val REFINING: Supplier<RecipeSerializer<HTRefiningRecipe>> =
        register(RagiumConst.REFINING, HTRefiningRecipe.CODEC, HTRefiningRecipe.STREAM_CODEC)

    @JvmField
    val SOLIDIFYING: Supplier<RecipeSerializer<HTSolidifyingRecipe>> =
        register(RagiumConst.SOLIDIFYING, HTSolidifyingRecipe.CODEC, HTSolidifyingRecipe.STREAM_CODEC)

    //    Custom    //

    @JvmField
    val BLAST_CHARGE: Supplier<RecipeSerializer<HTBlastChargeRecipe>> =
        register("blast_charge", SimpleCraftingRecipeSerializer(::HTBlastChargeRecipe))

    @JvmField
    val ETERNAL_TICKET: Supplier<SimpleCraftingRecipeSerializer<HTEternalTicketRecipe>> =
        register("eternal_ticket", SimpleCraftingRecipeSerializer(::HTEternalTicketRecipe))

    @JvmField
    val ICE_CREAM_SODA: Supplier<SimpleCraftingRecipeSerializer<HTIceCreamSodaRecipe>> =
        register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))
}

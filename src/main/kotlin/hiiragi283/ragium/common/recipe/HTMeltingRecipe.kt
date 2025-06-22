package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTMeltingRecipe(val ingredient: SizedIngredient, val output: HTFluidOutput) : HTUniversalRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMeltingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC
                        .fieldOf(RagiumConstantValues.ITEM_INPUT)
                        .forGetter(HTMeltingRecipe::ingredient),
                    HTFluidOutput.CODEC
                        .fieldOf(RagiumConstantValues.FLUID_OUTPUT)
                        .forGetter(HTMeltingRecipe::output),
                ).apply(instance, ::HTMeltingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMeltingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTMeltingRecipe::ingredient,
            HTFluidOutput.STREAM_CODEC,
            HTMeltingRecipe::output,
            ::HTMeltingRecipe,
        )
    }

    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean = ingredient.test(input.getItem(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()
}

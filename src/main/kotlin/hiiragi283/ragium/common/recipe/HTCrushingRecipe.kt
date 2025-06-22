package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.extension.listOrElement
import hiiragi283.ragium.api.recipe.HTItemOutput
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

class HTCrushingRecipe(val ingredient: SizedIngredient, val outputs: List<HTItemOutput>) : HTUniversalRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrushingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC
                        .fieldOf(RagiumConstantValues.ITEM_INPUT)
                        .forGetter(HTCrushingRecipe::ingredient),
                    HTItemOutput.CODEC
                        .listOrElement()
                        .fieldOf(RagiumConstantValues.ITEM_OUTPUT)
                        .forGetter(HTCrushingRecipe::outputs),
                ).apply(instance, ::HTCrushingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCrushingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTCrushingRecipe::ingredient,
            HTItemOutput.STREAM_CODEC.listOf(),
            HTCrushingRecipe::outputs,
            ::HTCrushingRecipe,
        )
    }

    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean = ingredient.test(input.getItem(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}

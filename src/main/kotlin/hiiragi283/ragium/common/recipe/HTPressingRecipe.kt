package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTPressingRecipe(val ingredient: SizedIngredient, val catalyst: Ingredient, val output: HTItemOutput) : HTUniversalRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTPressingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC
                        .fieldOf(RagiumConstantValues.FLUID_INPUT)
                        .forGetter(HTPressingRecipe::ingredient),
                    Ingredient.CODEC
                        .optionalFieldOf("catalyst", Ingredient.EMPTY)
                        .forGetter(HTPressingRecipe::catalyst),
                    HTItemOutput.CODEC
                        .fieldOf(RagiumConstantValues.ITEM_OUTPUT)
                        .forGetter(HTPressingRecipe::output),
                ).apply(instance, ::HTPressingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTPressingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTPressingRecipe::ingredient,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTPressingRecipe::catalyst,
            HTItemOutput.STREAM_CODEC,
            HTPressingRecipe::output,
            ::HTPressingRecipe,
        )
    }

    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean {
        val bool1: Boolean = ingredient.test(input.getItem(0))
        val catalystStack: ItemStack = input.getItem(1)
        val bool2: Boolean = when {
            catalyst.isEmpty -> catalystStack.isEmpty
            else -> catalyst.test(catalystStack)
        }
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PRESSING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PRESSING.get()
}

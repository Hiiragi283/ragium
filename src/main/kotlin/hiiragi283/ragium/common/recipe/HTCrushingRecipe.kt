package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.extension.listOrElement
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTCrushingRecipe(val ingredient: SizedIngredient, val outputs: List<HTItemOutput>) : Recipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrushingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTCrushingRecipe::ingredient),
                    HTItemOutput.CODEC
                        .listOrElement()
                        .fieldOf("outputs")
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

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}

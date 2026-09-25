package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTBiomeCondition
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.fluids.FluidStack

class RTResourceExtractingRecipe(
    val condition: HTBiomeCondition,
    val result: HTFluidResult,
    override val progressData: HTProgressData
) : HTRecipePredicate<RTResourceExtractingRecipe.Input>,
    HTRecipeFactory<RTResourceExtractingRecipe.Input, FluidStack>,
    HTProgressRecipe.Simple<RTResourceExtractingRecipe.Input>,
    HTSerializableRecipe<RTResourceExtractingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTResourceExtractingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTBiomeCondition.CODEC.fieldOf("condition").forGetter(RTResourceExtractingRecipe::condition),
                HTFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTResourceExtractingRecipe::result),
                HTProgressData.CODEC.forGetter(RTResourceExtractingRecipe::progressData)
            ).apply(instance, ::RTResourceExtractingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTResourceExtractingRecipe> = StreamCodec.composite(
            HTBiomeCondition.STREAM_CODEC,
            RTResourceExtractingRecipe::condition,
            HTFluidResult.STREAM_CODEC,
            RTResourceExtractingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTResourceExtractingRecipe::progressData,
            ::RTResourceExtractingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTResourceExtractingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun matches(input: Input): Boolean = condition.test(input.biome)

    override fun produce(input: Input): FluidStack = result.create()

    override fun getSerializer(): RecipeSerializer<RTResourceExtractingRecipe> =
        RagiumRecipeSerializers.RESOURCE_EXTRACTING

    override fun getType(): HTRecipeType<RTResourceExtractingRecipe> = RagiumRecipeTypes.RESOURCE_EXTRACTING

    @JvmRecord
    data class Input(val biome: Holder<Biome>) : RecipeInput {
        constructor(getter: Level, pos: BlockPos) : this(getter.getBiome(pos))

        override fun getItem(index: Int): ItemStack = error("No item for index: $index")

        override fun size(): Int = 0
    }
}

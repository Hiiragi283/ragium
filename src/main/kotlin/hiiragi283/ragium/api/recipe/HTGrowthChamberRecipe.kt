package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCodecs
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTGrowthChamberRecipe(
    group: String,
    val seed: Ingredient,
    val soil: Ingredient,
    val waterAmount: Int,
    private val crop: HTItemResult,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTGrowthChamberRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    Ingredient.CODEC_NONEMPTY.fieldOf("seed").forGetter(HTGrowthChamberRecipe::seed),
                    Ingredient.CODEC_NONEMPTY.fieldOf("soil").forGetter(HTGrowthChamberRecipe::soil),
                    Codec
                        .intRange(0, Int.MAX_VALUE)
                        .optionalFieldOf("water", 0)
                        .forGetter(HTGrowthChamberRecipe::waterAmount),
                    HTRecipeCodecs.itemResult(),
                ).apply(instance, ::HTGrowthChamberRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTGrowthChamberRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTGrowthChamberRecipe::getGroup,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTGrowthChamberRecipe::seed,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTGrowthChamberRecipe::soil,
            ByteBufCodecs.INT,
            HTGrowthChamberRecipe::waterAmount,
            HTItemResult.STREAM_CODEC,
            HTGrowthChamberRecipe::crop,
            ::HTGrowthChamberRecipe,
        )
    }

    override val itemResults: List<HTItemResult> = listOf(crop)

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        val bool1: Boolean = seed.test(input.getItem(0))
        val bool2: Boolean = soil.test(input.getItem(1))
        val bool3: Boolean = if (waterAmount > 0) {
            SizedFluidIngredient.of(Tags.Fluids.WATER, waterAmount).test(input.getFluid(0))
        } else {
            true
        }
        return bool1 && bool2 && bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.GROWTH_CHAMBER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.GROWTH_CHAMBER.get()
}

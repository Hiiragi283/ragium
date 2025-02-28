package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTGrowthChamberRecipe(
    group: String,
    val seed: Ingredient,
    val soil: Ingredient,
    val waterAmount: Int,
    val crop: HTItemOutput,
) : HTMachineRecipe(group) {
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
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTGrowthChamberRecipe::crop),
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
            HTItemOutput.STREAM_CODEC,
            HTGrowthChamberRecipe::crop,
            ::HTGrowthChamberRecipe,
        )
    }

    private val waterIngredient: SizedFluidIngredient? = when (waterAmount > 0) {
        true -> SizedFluidIngredient.of(Tags.Fluids.WATER, waterAmount)
        false -> null
    }

    override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = seed.test(context.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = soil.test(context.getItemStack(HTStorageIO.INPUT, 1))
        val bool3: Boolean = waterIngredient?.test(context.getFluidStack(HTStorageIO.INPUT, 0)) != false
        return bool1 && bool2 && bool3
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output

        // Input
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output

        // Input
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.GROWTH_CHAMBER
}

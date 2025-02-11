package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTDistilleryRecipe(
    group: String,
    val input: SizedFluidIngredient,
    val firstOutput: FluidStack,
    val secondOutput: FluidStack,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTDistilleryRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.FLUID_INPUT.forGetter(HTDistilleryRecipe::input),
                    FluidStack.CODEC.fieldOf("first_fluid_output").forGetter(HTDistilleryRecipe::firstOutput),
                    FluidStack.CODEC.fieldOf("second_fluid_output").forGetter(HTDistilleryRecipe::secondOutput),
                ).apply(instance, ::HTDistilleryRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTDistilleryRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTDistilleryRecipe::getGroup,
            SizedFluidIngredient.STREAM_CODEC,
            HTDistilleryRecipe::input,
            FluidStack.STREAM_CODEC,
            HTDistilleryRecipe::firstOutput,
            FluidStack.STREAM_CODEC,
            HTDistilleryRecipe::secondOutput,
            ::HTDistilleryRecipe,
        )
    }

    override val itemResults: List<HTItemResult> = listOf()

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input.getFluid(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.DISTILLERY.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.DISTILLERY.get()
}

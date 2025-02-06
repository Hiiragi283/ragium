package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTInfuserRecipe(
    group: String,
    val itemInput: SizedIngredient,
    val fluidInput: SizedFluidIngredient,
    itemOutput: Optional<ItemStack>,
    fluidOutput: Optional<FluidStack>,
) : HTFluidOutputRecipe(group, itemOutput, fluidOutput) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTInfuserRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.ITEM_INPUT.forGetter(HTInfuserRecipe::itemInput),
                        HTRecipeCodecs.FLUID_INPUT.forGetter(HTInfuserRecipe::fluidInput),
                        ItemStack.CODEC.optionalFieldOf("item_output").forGetter(HTInfuserRecipe::itemOutput),
                        FluidStack.CODEC.optionalFieldOf("fluid_output").forGetter(HTInfuserRecipe::fluidOutput),
                    ).apply(instance, ::HTInfuserRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTInfuserRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTInfuserRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTInfuserRecipe::itemInput,
            SizedFluidIngredient.STREAM_CODEC,
            HTInfuserRecipe::fluidInput,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC),
            HTInfuserRecipe::itemOutput,
            ByteBufCodecs.optional(FluidStack.STREAM_CODEC),
            HTInfuserRecipe::fluidOutput,
            ::HTInfuserRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean =
        itemInput.test(input.getItem(0)) && fluidInput.test(input.getFluid(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INFUSER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.INFUSER.get()
}

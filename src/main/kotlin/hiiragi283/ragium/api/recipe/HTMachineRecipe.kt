package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTMachineRecipe(
    val definition: HTMachineDefinition,
    val itemInputs: List<SizedIngredient>,
    val fluidInputs: List<SizedFluidIngredient>,
    val catalyst: Optional<Ingredient>,
    val itemOutputs: List<ItemStack>,
    val fluidOutputs: List<FluidStack>,
) : Recipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineDefinition.CODEC.forGetter(HTMachineRecipe::definition),
                    SizedIngredient.NESTED_CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTMachineRecipe::itemInputs),
                    SizedFluidIngredient.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTMachineRecipe::fluidInputs),
                    Ingredient.CODEC.optionalFieldOf("catalyst").forGetter(HTMachineRecipe::catalyst),
                    ItemStack.CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTMachineRecipe::itemOutputs),
                    FluidStack.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTMachineRecipe::fluidOutputs),
                ).apply(instance, ::HTMachineRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipe> = StreamCodec.composite(
            HTMachineDefinition.STREAM_CODEC,
            HTMachineRecipe::definition,
            SizedIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidInputs,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
            HTMachineRecipe::catalyst,
            ItemStack.STREAM_CODEC.toList(),
            HTMachineRecipe::itemOutputs,
            FluidStack.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidOutputs,
            ::HTMachineRecipe,
        )
    }

    override fun matches(input: HTMachineInput, level: Level): Boolean = false

    override fun assemble(input: HTMachineInput, registries: HolderLookup.Provider): ItemStack = itemOutputs.getOrNull(0) ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<out Recipe<HTMachineInput>> = RagiumRecipes.MACHINE_SERIALIZER.get()

    override fun getType(): RecipeType<out Recipe<HTMachineInput>> = RagiumRecipes.MACHINE_TYPE.get()

    override fun placementInfo(): PlacementInfo = PlacementInfo.NOT_PLACEABLE

    override fun recipeBookCategory(): RecipeBookCategory = RagiumRecipes.MACHINE_CATEGORY.get()
}

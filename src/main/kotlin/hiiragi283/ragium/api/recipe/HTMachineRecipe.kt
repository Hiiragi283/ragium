package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
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
        val CODEC: MapCodec<HTMachineRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTMachineDefinition.CODEC.forGetter(HTMachineRecipe::definition),
                        SizedIngredient.FLAT_CODEC
                            .listOf()
                            .optionalFieldOf("item_inputs", listOf())
                            .forGetter(HTMachineRecipe::itemInputs),
                        SizedFluidIngredient.FLAT_CODEC
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
            }.validate(::validate)

        @JvmStatic
        private fun validate(recipe: HTMachineRecipe): DataResult<HTMachineRecipe> {
            val entry: HTMachineRegistry.Entry = recipe.machineKey.getEntryOrNull()
                ?: return DataResult.error { "Unregistered machine key: ${recipe.machineKey}" }
            val validator: HTMachineRecipeValidator = entry.getOrDefault(HTMachinePropertyKeys.RECIPE_VALIDATOR)
            return validator.validate(recipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipe> = StreamCodec.composite(
            HTMachineDefinition.STREAM_CODEC,
            HTMachineRecipe::definition,
            SizedIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidInputs,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC),
            HTMachineRecipe::catalyst,
            ItemStack.STREAM_CODEC.toList(),
            HTMachineRecipe::itemOutputs,
            FluidStack.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidOutputs,
            ::HTMachineRecipe,
        )
    }

    val machineKey: HTMachineKey = definition.key
    val machineTier: HTMachineTier = definition.tier

    override fun matches(input: HTMachineInput, level: Level): Boolean = false

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun assemble(input: HTMachineInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = itemOutputs.getOrNull(0) ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<out Recipe<HTMachineInput>> = RagiumRecipes.MACHINE_SERIALIZER.get()

    override fun getType(): RecipeType<out Recipe<HTMachineInput>> = RagiumRecipes.MACHINE_TYPE.get()
}

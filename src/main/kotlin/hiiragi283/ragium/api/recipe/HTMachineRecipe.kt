package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.extension.toSet
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
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

class HTMachineRecipe(
    val definition: HTMachineDefinition,
    val itemInputs: List<SizedIngredient>,
    val fluidInputs: List<SizedFluidIngredient>,
    private val itemOutputs: List<ItemStack>,
    private val fluidOutputs: List<FluidStack>,
    val conditions: Set<HTMachineRecipeCondition>,
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
                        ItemStack.CODEC
                            .listOf()
                            .optionalFieldOf("item_outputs", listOf())
                            .forGetter(HTMachineRecipe::itemOutputs),
                        FluidStack.CODEC
                            .listOf()
                            .optionalFieldOf("fluid_outputs", listOf())
                            .forGetter(HTMachineRecipe::fluidOutputs),
                        HTMachineRecipeCondition.SET_CODEC
                            .optionalFieldOf("machine_conditions", setOf())
                            .forGetter(HTMachineRecipe::conditions),
                    ).apply(instance, ::HTMachineRecipe)
            }.validate(::validate)

        @JvmStatic
        private fun validate(recipe: HTMachineRecipe): DataResult<HTMachineRecipe> =
            recipe.machineKey.getEntryData().flatMap { entry: HTMachineRegistry.Entry ->
                entry.getOrDefault(HTMachinePropertyKeys.RECIPE_VALIDATOR).validate(recipe)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipe> = StreamCodec.composite(
            HTMachineDefinition.STREAM_CODEC,
            HTMachineRecipe::definition,
            SizedIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidInputs,
            ItemStack.STREAM_CODEC.toList(),
            HTMachineRecipe::itemOutputs,
            FluidStack.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidOutputs,
            HTMachineRecipeCondition.STREAM_CODEC.toSet(),
            HTMachineRecipe::conditions,
            ::HTMachineRecipe,
        )
    }

    val machineKey: HTMachineKey = definition.key
    val machineTier: HTMachineTier = definition.tier

    fun getItemOutput(index: Int): ItemStack? = itemOutputs.getOrNull(index)?.copy()

    fun getFluidOutput(index: Int): FluidStack? = fluidOutputs.getOrNull(index)?.copy()

    //    Recipe    //

    override fun matches(input: HTMachineInput, level: Level): Boolean {
        // Machine Definition
        if (input.key != this.machineKey) return false
        if (input.tier < this.machineTier) return false
        // Item
        input.itemInputs.forEachIndexed { slot: Int, stack: ItemStack ->
            if (stack.`is`(RagiumItems.SLOT_LOCK)) return@forEachIndexed
            val ingredient: SizedIngredient = itemInputs.getOrNull(slot) ?: return@forEachIndexed
            if (!ingredient.test(stack)) {
                return false
            }
        }
        // Fluid
        input.fluidInputs.forEachIndexed { slot: Int, stack: FluidStack ->
            val ingredient: SizedFluidIngredient = fluidInputs.getOrNull(slot) ?: return@forEachIndexed
            if (!ingredient.test(stack)) {
                return false
            }
        }
        // Condition
        for (condition: HTMachineRecipeCondition in conditions) {
            if (!condition.test(level, input.pos)) return false
        }
        return return true
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun assemble(input: HTMachineInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = getItemOutput(0) ?: ItemStack.EMPTY

    override fun getIngredients(): NonNullList<Ingredient> {
        val list: NonNullList<Ingredient> = NonNullList.create()
        itemInputs.map(SizedIngredient::ingredient).forEach(list::add)
        return list
    }

    override fun getToastSymbol(): ItemStack = machineKey.getEntryOrNull()?.get()?.let(::ItemStack) ?: super.getToastSymbol()

    override fun getSerializer(): RecipeSerializer<out Recipe<HTMachineInput>> = RagiumRecipes.MACHINE_SERIALIZER.get()

    override fun getType(): RecipeType<out Recipe<HTMachineInput>> = RagiumRecipes.MACHINE_TYPE.get()
}

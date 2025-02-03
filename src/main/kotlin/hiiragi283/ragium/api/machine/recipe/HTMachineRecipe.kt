package hiiragi283.ragium.api.machine.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
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
    private val type: HTMachineRecipeType,
    val itemInputs: List<SizedIngredient>,
    val fluidInputs: List<SizedFluidIngredient>,
    private val itemOutputs: List<ItemStack>,
    private val fluidOutputs: List<FluidStack>,
    val condition: Optional<HTMachineRecipeCondition>,
) : Recipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTMachineRecipeType.CODEC.fieldOf("type").forGetter(HTMachineRecipe::type),
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
                        HTMachineRecipeCondition.CODEC
                            .optionalFieldOf("condition")
                            .forGetter(HTMachineRecipe::condition),
                    ).apply(instance, ::HTMachineRecipe)
            }.validate(::validate)

        @JvmStatic
        private fun validate(recipe: HTMachineRecipe): DataResult<HTMachineRecipe> = recipe.machineKey
            .getProperty()
            .getOrDefault(HTMachinePropertyKeys.RECIPE_VALIDATOR)(recipe)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipe> = StreamCodec.composite(
            HTMachineRecipeType.STREAM_CODEC,
            HTMachineRecipe::type,
            SizedIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidInputs,
            ItemStack.STREAM_CODEC.toList(),
            HTMachineRecipe::itemOutputs,
            FluidStack.STREAM_CODEC.toList(),
            HTMachineRecipe::fluidOutputs,
            ByteBufCodecs.optional(HTMachineRecipeCondition.STREAM_CODEC),
            HTMachineRecipe::condition,
            ::HTMachineRecipe,
        )
    }

    private val machineKey: HTMachineKey = type.machine

    fun getItemOutputs(): List<ItemStack> = itemOutputs.map(ItemStack::copy)

    fun getFluidOutputs(): List<FluidStack> = fluidOutputs.map(FluidStack::copy)

    //    Recipe    //

    override fun matches(input: HTMachineInput, level: Level): Boolean {
        // Machine Definition
        if (input.key != this.machineKey) return false
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
        return condition.map { it.test(level, input.pos) }.orElse(true)
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun assemble(input: HTMachineInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = getItemOutputs().getOrNull(0) ?: ItemStack.EMPTY

    override fun getIngredients(): NonNullList<Ingredient> {
        val list: NonNullList<Ingredient> = NonNullList.create()
        itemInputs.map(SizedIngredient::ingredient).forEach(list::add)
        return list
    }

    override fun getToastSymbol(): ItemStack = machineKey.getBlockOrNull()?.get()?.let(::ItemStack) ?: super.getToastSymbol()

    override fun getSerializer(): RecipeSerializer<out Recipe<HTMachineInput>> = type

    override fun getType(): RecipeType<out Recipe<HTMachineInput>> = type
}

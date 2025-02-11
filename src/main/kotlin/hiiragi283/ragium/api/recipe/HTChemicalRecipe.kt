package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCondition
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTChemicalRecipe(
    group: String,
    val itemInputs: List<SizedIngredient>,
    val fluidInputs: List<SizedFluidIngredient>,
    private val itemOutputs: List<ItemStack>,
    private val fluidOutputs: List<FluidStack>,
    val condition: Optional<HTMachineRecipeCondition>,
) : HTMachineRecipeBase(group) {
    /*companion object {
        @JvmField
        val CODEC: MapCodec<HTChemicalRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        SizedIngredient.FLAT_CODEC
                            .listOf()
                            .optionalFieldOf("item_inputs", listOf())
                            .forGetter(HTChemicalRecipe::itemInputs),
                        SizedFluidIngredient.FLAT_CODEC
                            .listOf()
                            .optionalFieldOf("fluid_inputs", listOf())
                            .forGetter(HTChemicalRecipe::fluidInputs),
                        ItemStack.CODEC
                            .listOf()
                            .optionalFieldOf("item_outputs", listOf())
                            .forGetter(HTChemicalRecipe::itemOutputs),
                        FluidStack.CODEC
                            .listOf()
                            .optionalFieldOf("fluid_outputs", listOf())
                            .forGetter(HTChemicalRecipe::fluidOutputs),
                        HTMachineRecipeCondition.CODEC
                            .optionalFieldOf("condition")
                            .forGetter(HTChemicalRecipe::condition),
                    ).apply(instance, ::HTChemicalRecipe)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTChemicalRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTChemicalRecipe::getGroup,
            SizedIngredient.STREAM_CODEC.toList(),
            HTChemicalRecipe::itemInputs,
            SizedFluidIngredient.STREAM_CODEC.toList(),
            HTChemicalRecipe::fluidInputs,
            ItemStack.STREAM_CODEC.toList(),
            HTChemicalRecipe::itemOutputs,
            FluidStack.STREAM_CODEC.toList(),
            HTChemicalRecipe::fluidOutputs,
            ByteBufCodecs.optional(HTMachineRecipeCondition.STREAM_CODEC),
            HTChemicalRecipe::condition,
            ::HTChemicalRecipe,
        )
    }*/

    fun getItemOutputs(): List<ItemStack> = itemOutputs.map(ItemStack::copy)

    fun getFluidOutputs(): List<FluidStack> = fluidOutputs.map(FluidStack::copy)

    override val itemResults: List<HTItemResult> = listOf()

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        // Item
        input.items.forEachIndexed { slot: Int, stack: ItemStack ->
            if (stack.`is`(RagiumItems.SLOT_LOCK)) return@forEachIndexed
            val ingredient: SizedIngredient = itemInputs.getOrNull(slot) ?: return@forEachIndexed
            if (!ingredient.test(stack)) {
                return false
            }
        }
        // Fluid
        input.fluids.forEachIndexed { slot: Int, stack: FluidStack ->
            val ingredient: SizedFluidIngredient = fluidInputs.getOrNull(slot) ?: return@forEachIndexed
            if (!ingredient.test(stack)) {
                return false
            }
        }
        // Condition
        return true
        // return condition.map { it.test(level, input.pos) }.orElse(true)
    }

    override fun getSerializer(): RecipeSerializer<*> = TODO()

    override fun getType(): RecipeType<*> = TODO()
}

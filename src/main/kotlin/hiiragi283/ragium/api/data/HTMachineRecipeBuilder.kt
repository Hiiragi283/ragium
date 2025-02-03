package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipe
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipeType
import net.minecraft.advancements.Criterion
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTMachineRecipeBuilder private constructor(private val recipeType: HTMachineRecipeType) :
    HTMachineRecipeBuilderBase<HTMachineRecipeBuilder>() {
        companion object {
            @JvmStatic
            fun create(recipeType: HTMachineRecipeType, tier: HTMachineTier): HTMachineRecipeBuilder = create(recipeType).catalyst(tier)

            @JvmStatic
            fun create(recipeType: HTMachineRecipeType): HTMachineRecipeBuilder = HTMachineRecipeBuilder(recipeType)
        }

        private val itemInputs: MutableMap<Ingredient, Int> = mutableMapOf()
        private val fluidInputs: MutableMap<FluidIngredient, Int> = mutableMapOf()
        private val itemOutputs: MutableList<ItemStack> = mutableListOf()
        private val fluidOutputs: MutableList<FluidStack> = mutableListOf()
        private var condition: HTMachineRecipeCondition? = null

        //    Input    //

        override fun itemInput(ingredient: Ingredient, count: Int): HTMachineRecipeBuilder = apply {
            check(itemInputs.put(ingredient, count) == null) { "Same ingredient is not supported!" }
        }

        override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTMachineRecipeBuilder = apply {
            check(fluidInputs.put(ingredient, amount) == null) { "Same ingredient is not supported!" }
        }

        //    Machine Condition    //

        fun condition(condition: HTMachineRecipeCondition): HTMachineRecipeBuilder = apply {
            check(this.condition == null) { "Machine condition is already registered!" }
            this.condition = condition
        }

        //    Output    //

        override fun itemOutput(stack: ItemStack): HTMachineRecipeBuilder = apply {
            check(!stack.isEmpty) { "Empty ItemStack is not allowed!" }
            itemOutputs.add(stack.copy())
        }

        override fun fluidOutput(stack: FluidStack): HTMachineRecipeBuilder = apply {
            check(!stack.isEmpty) { "Empty FluidStack is not allowed!" }
            fluidOutputs.add(stack.copy())
        }

        //    RecipeBuilder    //

        override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

        override fun group(groupName: String?): RecipeBuilder = this

        override fun getResult(): Item = itemOutputs.getOrNull(0)?.item ?: error("Empty item outputs")

        override fun getPrimalId(): ResourceLocation {
            val firstItem: Item? = itemOutputs.getOrNull(0)?.item
            if (firstItem != null) {
                return RecipeBuilder.getDefaultRecipeId(result)
            }
            val firstFluid: Fluid? = fluidOutputs.getOrNull(0)?.fluid
            if (firstFluid != null) {
                return BuiltInRegistries.FLUID.getKey(firstFluid)
            }
            return error("There is no output!")
        }

        override val prefix: String = recipeType.machine.name

        override fun saveInternal(output: RecipeOutput, id: ResourceLocation) {
            output.accept(id, createRecipe(), null)
        }

        fun export(): RecipeHolder<HTMachineRecipe> = export(getPrimalId())

        fun export(id: ResourceLocation): RecipeHolder<HTMachineRecipe> = RecipeHolder(fixId(id), createRecipe())

        fun exportPrefixed(prefix: String): RecipeHolder<HTMachineRecipe> = export(getPrimalId().withPrefix(prefix))

        fun exportSuffixed(suffix: String): RecipeHolder<HTMachineRecipe> = export(getPrimalId().withSuffix(suffix))

        private fun fixId(id: ResourceLocation): ResourceLocation = RagiumAPI.wrapId(id.withPrefix("$prefix/"))

        private fun createRecipe(): HTMachineRecipe = HTMachineRecipe(
            recipeType,
            itemInputs.map { (ingredient: Ingredient, count: Int) ->
                SizedIngredient(ingredient, count)
            },
            fluidInputs.map { (ingredient: FluidIngredient, count: Int) ->
                SizedFluidIngredient(ingredient, count)
            },
            itemOutputs,
            fluidOutputs,
            Optional.ofNullable(condition),
        )
    }

package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.recipe.condition.HTProcessorCatalystCondition
import hiiragi283.ragium.common.recipe.condition.HTTierCondition
import net.minecraft.advancements.Criterion
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*
import java.util.function.Supplier

class HTMachineRecipeBuilder private constructor(private val machine: HTMachineKey) : RecipeBuilder {
    companion object {
        @JvmStatic
        fun create(machine: HTMachineKey, tier: HTMachineTier): HTMachineRecipeBuilder = create(machine).apply {
            if (tier != HTMachineTier.BASIC) machineConditions(HTTierCondition(tier))
        }

        @JvmStatic
        fun create(machine: HTMachineKey): HTMachineRecipeBuilder = HTMachineRecipeBuilder(machine)
    }

    private val itemInputs: MutableMap<Ingredient, Int> = mutableMapOf()
    private val fluidInputs: MutableMap<FluidIngredient, Int> = mutableMapOf()
    private val itemOutputs: MutableList<ItemStack> = mutableListOf()
    private val fluidOutputs: MutableList<FluidStack> = mutableListOf()
    private var machineCondition: HTMachineRecipeCondition? = null

    private val conditions: MutableList<ICondition> = mutableListOf()

    //    Input    //

    // fun itemInput(item: Supplier<out ItemLike>, count: Int = 1): HTMachineRecipeBuilder = itemInput(item.get(), count)

    fun itemInput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder = itemInput(Ingredient.of(item), count)

    fun itemInput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): HTMachineRecipeBuilder =
        itemInput(prefix.createTag(material), count)

    fun itemInput(provider: HTMaterialProvider, count: Int = 1): HTMachineRecipeBuilder = itemInput(provider.prefixedTagKey, count)

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeBuilder = itemInput(Ingredient.of(tagKey), count)

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): HTMachineRecipeBuilder = itemInput(ingredient.toVanilla(), count)

    fun itemInput(ingredient: SizedIngredient): HTMachineRecipeBuilder = itemInput(ingredient.ingredient(), ingredient.count())

    fun itemInput(ingredient: Ingredient, count: Int = 1): HTMachineRecipeBuilder = apply {
        check(itemInputs.put(ingredient, count) == null) { "Same ingredient is not supported!" }
    }

    // fun fluidInput(fluid: Supplier<out Fluid>, count: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = fluidInput(fluid.get(), count)

    fun fluidInput(content: RagiumFluids, amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder =
        fluidInput(content.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder =
        fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder =
        fluidInput(FluidIngredient.tag(tagKey), amount)

    fun fluidInput(ingredient: FluidIngredient, amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = apply {
        check(fluidInputs.put(ingredient, amount) == null) { "Same ingredient is not supported!" }
    }

    fun waterInput(amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = fluidInput(Tags.Fluids.MILK, amount)

    //    Machine Condition    //

    fun catalyst(prefix: HTTagPrefix, material: HTMaterialKey): HTMachineRecipeBuilder = catalyst(prefix.createTag(material))

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeBuilder = catalyst(Ingredient.of(tagKey))

    fun catalyst(item: ItemLike): HTMachineRecipeBuilder = catalyst(Ingredient.of(item))

    fun catalyst(ingredient: Ingredient): HTMachineRecipeBuilder = machineConditions(HTProcessorCatalystCondition(ingredient))

    fun machineConditions(condition: HTMachineRecipeCondition): HTMachineRecipeBuilder = apply {
        check(this.machineCondition == null) { "Machine condition is already registered!" }
        this.machineCondition = condition
    }

    //    Output    //

    fun itemOutput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder = itemOutput(ItemStack(item, count))

    fun itemOutput(stack: ItemStack): HTMachineRecipeBuilder = apply {
        check(!stack.isEmpty) { "Empty ItemStack is not allowed!" }
        itemOutputs.add(stack)
    }

    fun fluidOutput(fluid: Supplier<out Fluid>, count: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder =
        fluidOutput(fluid.get(), count)

    fun fluidOutput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = fluidOutput(FluidStack(fluid, amount))

    fun fluidOutput(stack: FluidStack): HTMachineRecipeBuilder = apply {
        check(!stack.isEmpty) { "Empty FluidStack is not allowed!" }
        fluidOutputs.add(stack)
    }

    fun waterOutput(amount: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = fluidOutput(Fluids.WATER, amount)

    //    Condition    //

    fun conditions(conditions: Iterable<ICondition>): HTMachineRecipeBuilder = apply {
        this.conditions.addAll(conditions)
    }

    fun conditions(vararg conditions: ICondition): HTMachineRecipeBuilder = apply {
        this.conditions.addAll(conditions)
    }

    //    RecipeBuilder    //

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = this

    override fun getResult(): Item = itemOutputs.getOrNull(0)?.item ?: error("Empty item outputs")

    private fun getPrimalId(): ResourceLocation {
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

    fun savePrefixed(recipeOutput: RecipeOutput, prefix: String) {
        save(recipeOutput, getPrimalId().withPrefix(prefix))
    }

    fun saveSuffixed(recipeOutput: RecipeOutput, suffix: String) {
        save(recipeOutput, getPrimalId().withSuffix(suffix))
    }

    override fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, getPrimalId())
    }

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(fixId(id), createRecipe(), null, *conditions.toTypedArray())
    }

    fun export(): RecipeHolder<HTMachineRecipe> = export(getPrimalId())

    fun export(id: ResourceLocation): RecipeHolder<HTMachineRecipe> = RecipeHolder(fixId(id), createRecipe())

    fun exportPrefixed(prefix: String): RecipeHolder<HTMachineRecipe> = export(getPrimalId().withPrefix(prefix))

    fun exportSuffixed(suffix: String): RecipeHolder<HTMachineRecipe> = export(getPrimalId().withSuffix(suffix))

    private fun fixId(id: ResourceLocation): ResourceLocation = RagiumAPI.wrapId(id.withPrefix(machine.name + '/'))

    private fun createRecipe(): HTMachineRecipe = HTMachineRecipe(
        machine,
        itemInputs.map { (ingredient: Ingredient, count: Int) ->
            SizedIngredient(ingredient, count)
        },
        fluidInputs.map { (ingredient: FluidIngredient, count: Int) ->
            SizedFluidIngredient(ingredient, count)
        },
        itemOutputs,
        fluidOutputs,
        Optional.ofNullable(machineCondition),
    )
}

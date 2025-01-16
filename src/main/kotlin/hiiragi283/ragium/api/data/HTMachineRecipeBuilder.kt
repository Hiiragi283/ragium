package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumFluids
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*
import java.util.function.Supplier

class HTMachineRecipeBuilder private constructor(private val definition: HTMachineDefinition) : RecipeBuilder {
    companion object {
        @JvmStatic
        fun create(machine: HTMachineKey, tier: HTMachineTier = HTMachineTier.BASIC): HTMachineRecipeBuilder =
            HTMachineRecipeBuilder(HTMachineDefinition(HTMachineKey.validate(machine).orThrow, tier))
    }

    private val itemInputs: MutableMap<Ingredient, Int> = mutableMapOf()
    private val fluidInputs: MutableMap<FluidIngredient, Int> = mutableMapOf()
    private var catalyst: Ingredient? = null
    private val itemOutputs: MutableList<ItemStack> = mutableListOf()
    private val fluidOutputs: MutableList<FluidStack> = mutableListOf()

    private val conditions: MutableList<ICondition> = mutableListOf()

    //    Input    //

    // fun itemInput(item: Supplier<out ItemLike>, count: Int = 1): HTMachineRecipeBuilder = itemInput(item.get(), count)

    fun itemInput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder = itemInput(Ingredient.of(item), count)

    fun itemInput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): HTMachineRecipeBuilder =
        itemInput(prefix.createTag(material), count)

    fun itemInput(provider: HTMaterialProvider, count: Int = 1): HTMachineRecipeBuilder = itemInput(provider.prefixedTagKey, count)

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeBuilder = itemInput(Ingredient.of(tagKey), count)

    fun itemInput(ingredient: Ingredient, count: Int = 1): HTMachineRecipeBuilder = apply {
        check(itemInputs.put(ingredient, count) == null) { "Same ingredient is not supported!" }
    }

    // fun fluidInput(fluid: Supplier<out Fluid>, count: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = fluidInput(fluid.get(), count)

    fun fluidInput(content: RagiumFluids, count: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder =
        fluidInput(content.commonTag, count)

    fun fluidInput(fluid: Fluid, count: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder =
        fluidInput(FluidIngredient.of(fluid), count)

    fun fluidInput(tagKey: TagKey<Fluid>, count: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder =
        fluidInput(FluidIngredient.tag(tagKey), count)

    fun fluidInput(ingredient: FluidIngredient, count: Int = FluidType.BUCKET_VOLUME): HTMachineRecipeBuilder = apply {
        check(fluidInputs.put(ingredient, count) == null) { "Same ingredient is not supported!" }
    }

    //    Catalyst    //

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeBuilder = catalyst(Ingredient.of(tagKey))

    fun catalyst(item: ItemLike): HTMachineRecipeBuilder = catalyst(Ingredient.of(item))

    fun catalyst(ingredient: Ingredient): HTMachineRecipeBuilder = apply {
        this.catalyst = ingredient
    }

    //    Output    //

    fun itemOutput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder = itemOutput(ItemStack(item, count))

    fun itemOutput(stack: ItemStack): HTMachineRecipeBuilder = apply {
        check(!stack.isEmpty) { "Empty ItemStack is not allowed!" }
        itemOutputs.add(stack)
    }

    fun fluidOutput(fluid: Supplier<out Fluid>, count: Int = 1): HTMachineRecipeBuilder = fluidOutput(fluid.get(), count)

    fun fluidOutput(fluid: Fluid, count: Int = 1): HTMachineRecipeBuilder = fluidOutput(FluidStack(fluid, count))

    fun fluidOutput(stack: FluidStack): HTMachineRecipeBuilder = apply {
        check(!stack.isEmpty) { "Empty FluidStack is not allowed!" }
        fluidOutputs.add(stack)
    }

    //    Condition    //

    fun conditions(vararg conditions: ICondition): HTMachineRecipeBuilder = apply {
        this.conditions.addAll(conditions)
    }

    //    RecipeBuilder    //

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = this

    override fun getResult(): Item = itemOutputs.getOrNull(0)?.item ?: error("Empty item outputs")

    fun savePrefixed(recipeOutput: RecipeOutput, prefix: String) {
        save(recipeOutput, RecipeBuilder.getDefaultRecipeId(result).withPrefix(prefix))
    }

    fun saveSuffixed(recipeOutput: RecipeOutput, suffix: String) {
        save(recipeOutput, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix))
    }

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        val fixedId: ResourceLocation = RagiumAPI.id(id.withPrefix(definition.key.name + '/').path)
        recipeOutput.accept(
            fixedId,
            HTMachineRecipe(
                definition,
                itemInputs.map { (ingredient: Ingredient, count: Int) ->
                    SizedIngredient(ingredient, count)
                },
                fluidInputs.map { (ingredient: FluidIngredient, count: Int) ->
                    SizedFluidIngredient(ingredient, count)
                },
                Optional.ofNullable(catalyst),
                itemOutputs,
                fluidOutputs,
            ),
            null,
            *conditions.toTypedArray(),
        )
    }
}

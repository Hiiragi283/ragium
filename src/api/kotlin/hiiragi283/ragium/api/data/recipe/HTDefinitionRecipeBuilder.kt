package hiiragi283.ragium.api.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.registry.HTFluidContent
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.function.Supplier

class HTDefinitionRecipeBuilder<R : Recipe<*>>(private val prefix: String, private val factory: (HTRecipeDefinition) -> R) :
    HTRecipeBuilder,
    HTItemOutputRecipeBuilder<HTDefinitionRecipeBuilder<R>> {
    private val itemInputs: MutableList<SizedIngredient> = mutableListOf()
    private val fluidInputs: MutableList<SizedFluidIngredient> = mutableListOf()
    private var catalyst: Ingredient = Ingredient.EMPTY
    private val itemOutputs: MutableList<HTItemOutput> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidOutput> = mutableListOf()
    private val conditions: MutableList<ICondition> = mutableListOf()

    //    Item Input    //

    fun itemInput(item: ItemLike, count: Int = 1): HTDefinitionRecipeBuilder<R> = itemInput(SizedIngredient.of(item, count))

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTDefinitionRecipeBuilder<R> = itemInput(SizedIngredient.of(tagKey, count))

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): HTDefinitionRecipeBuilder<R> =
        itemInput(SizedIngredient(ingredient.toVanilla(), count))

    fun itemInput(ingredient: Ingredient, count: Int = 1): HTDefinitionRecipeBuilder<R> = itemInput(SizedIngredient(ingredient, count))

    private fun itemInput(ingredient: SizedIngredient): HTDefinitionRecipeBuilder<R> = apply {
        itemInputs.add(ingredient)
    }

    //    Fluid Input    //

    fun fluidInput(fluid: HTFluidContent<*, *, *>, amount: Int = 1000): HTDefinitionRecipeBuilder<R> = fluidInput(fluid.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = 1000): HTDefinitionRecipeBuilder<R> = fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = 1000): HTDefinitionRecipeBuilder<R> =
        fluidInput(FluidIngredient.tag(tagKey), amount)

    fun fluidInput(ingredient: FluidIngredient, amount: Int = 1000): HTDefinitionRecipeBuilder<R> =
        fluidInput(SizedFluidIngredient(ingredient, amount))

    private fun fluidInput(ingredient: SizedFluidIngredient): HTDefinitionRecipeBuilder<R> = apply {
        fluidInputs.add(ingredient)
    }

    fun waterInput(amount: Int = 1000): HTDefinitionRecipeBuilder<R> = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = 1000): HTDefinitionRecipeBuilder<R> = fluidInput(Tags.Fluids.MILK, amount)

    //    Catalyst    //

    fun catalyst(item: ItemLike): HTDefinitionRecipeBuilder<R> = catalyst(Ingredient.of(item))

    fun catalyst(tagKey: TagKey<Item>): HTDefinitionRecipeBuilder<R> = catalyst(Ingredient.of(tagKey))

    fun catalyst(ingredient: ICustomIngredient): HTDefinitionRecipeBuilder<R> = catalyst(ingredient.toVanilla())

    fun catalyst(catalyst: Ingredient): HTDefinitionRecipeBuilder<R> = apply {
        check(!catalyst.isEmpty) { "Setting empty ingredient is not allowed!" }
        this.catalyst = catalyst
    }

    //    Item Output    //

    override fun itemOutput(stack: ItemStack, chance: Float): HTDefinitionRecipeBuilder<R> = apply {
        if (stack.isEmpty) {
            error("Empty ItemStack is not allowed for HTItemOutput!")
        }
        validateChance(chance)
        itemOutputs.add(
            HTItemOutput(
                Either.left(stack.itemHolder.idOrThrow),
                stack.count,
                stack.componentsPatch,
                chance,
            ),
        )
    }

    override fun itemOutput(
        tagKey: TagKey<Item>,
        count: Int,
        chance: Float,
        appendCondition: Boolean,
    ): HTDefinitionRecipeBuilder<R> = apply {
        validateChance(chance)
        itemOutputs.add(
            HTItemOutput(
                Either.right(tagKey),
                count,
                DataComponentPatch.EMPTY,
                chance,
            ),
        )
        if (appendCondition) {
            conditions.add(NotCondition(TagEmptyCondition(tagKey)))
        }
    }

    //    Fluid Output    //

    fun fluidOutput(fluid: Supplier<out Fluid>, amount: Int = 1000): HTDefinitionRecipeBuilder<R> = fluidOutput(fluid.get(), amount)

    fun fluidOutput(fluid: Fluid, amount: Int = 1000): HTDefinitionRecipeBuilder<R> = fluidOutput(FluidStack(fluid, amount))

    fun fluidOutput(stack: FluidStack): HTDefinitionRecipeBuilder<R> = apply {
        if (stack.isEmpty) {
            error("Empty FluidStack is not allowed for HTFluidOutput!")
        }
        fluidOutputs.add(
            HTFluidOutput(
                Either.left(stack.fluidHolder.idOrThrow),
                stack.amount,
                stack.componentsPatch,
            ),
        )
    }

    fun fluidOutput(tagKey: TagKey<Fluid>, amount: Int = 1000): HTDefinitionRecipeBuilder<R> = apply {
        fluidOutputs.add(
            HTFluidOutput(
                Either.right(tagKey),
                amount,
                DataComponentPatch.EMPTY,
            ),
        )
    }

    //    ICondition    //

    fun condition(condition: ICondition): HTDefinitionRecipeBuilder<R> = apply {
        this.conditions.add(condition)
    }

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = itemOutputs.firstOrNull()?.id
        ?: fluidOutputs.firstOrNull()?.id
        ?: error("Either one item or fluid output required at least!")

    override fun group(groupName: String?): RecipeBuilder = throw UnsupportedOperationException()

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            id.withPrefix("$prefix/"),
            factory(
                HTRecipeDefinition(
                    itemInputs,
                    fluidInputs,
                    catalyst,
                    itemOutputs,
                    fluidOutputs,
                ),
            ),
            null,
            *conditions.toTypedArray(),
        )
    }
}

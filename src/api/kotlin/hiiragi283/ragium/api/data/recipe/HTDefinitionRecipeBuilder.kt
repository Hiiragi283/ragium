package hiiragi283.ragium.api.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.registry.HTFluidContent
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
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

class HTDefinitionRecipeBuilder(private val contexts: List<Context>) :
    HTRecipeBuilder,
    HTItemOutputRecipeBuilder<HTDefinitionRecipeBuilder> {
    companion object {
        @JvmStatic
        fun create(prefix: String, factory: (HTRecipeDefinition) -> Recipe<*>): HTDefinitionRecipeBuilder =
            HTDefinitionRecipeBuilder(listOf(Context(prefix, factory)))
    }

    private val itemInputs: MutableList<SizedIngredient> = mutableListOf()
    private val fluidInputs: MutableList<SizedFluidIngredient> = mutableListOf()
    private var catalyst: Ingredient = Ingredient.EMPTY
    private val itemOutputs: MutableList<HTItemOutput> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidOutput> = mutableListOf()
    private val conditions: MutableList<ICondition> = mutableListOf()

    //    Item Input    //

    fun itemInput(item: ItemLike, count: Int = 1): HTDefinitionRecipeBuilder = itemInput(SizedIngredient.of(item, count))

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTDefinitionRecipeBuilder = itemInput(SizedIngredient.of(tagKey, count))

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): HTDefinitionRecipeBuilder =
        itemInput(SizedIngredient(ingredient.toVanilla(), count))

    fun itemInput(ingredient: Ingredient, count: Int = 1): HTDefinitionRecipeBuilder = itemInput(SizedIngredient(ingredient, count))

    private fun itemInput(ingredient: SizedIngredient): HTDefinitionRecipeBuilder = apply {
        itemInputs.add(ingredient)
    }

    //    Fluid Input    //

    fun fluidInput(fluid: HTFluidContent<*, *, *>, amount: Int = 1000): HTDefinitionRecipeBuilder = fluidInput(fluid.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = 1000): HTDefinitionRecipeBuilder = fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = 1000): HTDefinitionRecipeBuilder = fluidInput(FluidIngredient.tag(tagKey), amount)

    fun fluidInput(ingredient: FluidIngredient, amount: Int = 1000): HTDefinitionRecipeBuilder =
        fluidInput(SizedFluidIngredient(ingredient, amount))

    private fun fluidInput(ingredient: SizedFluidIngredient): HTDefinitionRecipeBuilder = apply {
        fluidInputs.add(ingredient)
    }

    fun waterInput(amount: Int = 1000): HTDefinitionRecipeBuilder = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = 1000): HTDefinitionRecipeBuilder = fluidInput(Tags.Fluids.MILK, amount)

    //    Catalyst    //

    fun catalyst(item: ItemLike): HTDefinitionRecipeBuilder = catalyst(Ingredient.of(item))

    fun catalyst(tagKey: TagKey<Item>): HTDefinitionRecipeBuilder = catalyst(Ingredient.of(tagKey))

    fun catalyst(ingredient: ICustomIngredient): HTDefinitionRecipeBuilder = catalyst(ingredient.toVanilla())

    fun catalyst(catalyst: Ingredient): HTDefinitionRecipeBuilder = apply {
        check(!catalyst.isEmpty) { "Setting empty ingredient is not allowed!" }
        this.catalyst = catalyst
    }

    //    Item Output    //

    override fun itemOutput(
        id: ResourceLocation,
        count: Int,
        component: DataComponentPatch,
        chance: Float,
    ): HTDefinitionRecipeBuilder = apply {
        validateChance(chance)
        itemOutputs.add(
            HTItemOutput(
                Either.left(id),
                count,
                component,
                chance,
            ),
        )
    }

    override fun itemOutput(
        tagKey: TagKey<Item>,
        count: Int,
        chance: Float,
        appendCondition: Boolean,
    ): HTDefinitionRecipeBuilder = apply {
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

    fun fluidOutput(fluid: Supplier<out Fluid>, amount: Int = 1000): HTDefinitionRecipeBuilder = fluidOutput(fluid.get(), amount)

    fun fluidOutput(fluid: Fluid, amount: Int = 1000): HTDefinitionRecipeBuilder = fluidOutput(FluidStack(fluid, amount))

    fun fluidOutput(stack: FluidStack): HTDefinitionRecipeBuilder = apply {
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

    fun fluidOutput(tagKey: TagKey<Fluid>, amount: Int = 1000): HTDefinitionRecipeBuilder = apply {
        fluidOutputs.add(
            HTFluidOutput(
                Either.right(tagKey),
                amount,
                DataComponentPatch.EMPTY,
            ),
        )
    }

    //    ICondition    //

    fun condition(condition: ICondition): HTDefinitionRecipeBuilder = apply {
        this.conditions.add(condition)
    }

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = itemOutputs.firstOrNull()?.id
        ?: fluidOutputs.firstOrNull()?.id
        ?: error("Either one item or fluid output required at least!")

    override fun group(groupName: String?): RecipeBuilder = throw UnsupportedOperationException()

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        val definition = HTRecipeDefinition(
            itemInputs,
            fluidInputs,
            catalyst,
            itemOutputs,
            fluidOutputs,
        )
        for (context: Context in contexts) {
            context.save(recipeOutput, id, definition, *conditions.toTypedArray())
        }
    }

    //    Context    //

    data class Context(private val prefix: String, private val factory: (HTRecipeDefinition) -> Recipe<*>) {
        fun save(
            recipeOutput: RecipeOutput,
            id: ResourceLocation,
            definition: HTRecipeDefinition,
            vararg conditions: ICondition,
        ) {
            recipeOutput.accept(id.withPrefix("$prefix/"), factory(definition), null, *conditions)
        }
    }
}

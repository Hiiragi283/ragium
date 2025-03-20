package hiiragi283.ragium.api.data.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.registry.HTDeferredFluid
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.function.Supplier

class HTMachineRecipeBuilder<R : HTMachineRecipe>(val factory: (HTRecipeDefinition) -> DataResult<R>) : HTRecipeBuilder<R> {
    private val itemInputs: MutableList<SizedIngredient> = mutableListOf()
    private val fluidInputs: MutableList<SizedFluidIngredient> = mutableListOf()
    private val itemOutputs: MutableList<HTItemOutput> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidOutput> = mutableListOf()

    //    Item Input    //

    fun itemInput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder<R> = itemInput(SizedIngredient.of(item, count))

    fun itemInput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): HTMachineRecipeBuilder<R> =
        itemInput(prefix.createTag(material), count)

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeBuilder<R> = itemInput(SizedIngredient.of(tagKey, count))

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): HTMachineRecipeBuilder<R> =
        itemInput(SizedIngredient(ingredient.toVanilla(), count))

    private fun itemInput(ingredient: SizedIngredient): HTMachineRecipeBuilder<R> = apply {
        itemInputs.add(ingredient)
    }

    //    Fluid Input    //

    fun fluidInput(fluid: HTDeferredFluid<*>, amount: Int = 1000): HTMachineRecipeBuilder<R> = fluidInput(fluid.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = 1000): HTMachineRecipeBuilder<R> = fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = 1000): HTMachineRecipeBuilder<R> = fluidInput(FluidIngredient.tag(tagKey), amount)

    fun fluidInput(ingredient: FluidIngredient, amount: Int = 1000): HTMachineRecipeBuilder<R> =
        fluidInput(SizedFluidIngredient(ingredient, amount))

    private fun fluidInput(ingredient: SizedFluidIngredient): HTMachineRecipeBuilder<R> = apply {
        fluidInputs.add(ingredient)
    }

    fun waterInput(amount: Int = 1000): HTMachineRecipeBuilder<R> = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = 1000): HTMachineRecipeBuilder<R> = fluidInput(Tags.Fluids.MILK, amount)

    //    Item Output    //

    fun itemOutput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder<R> = itemOutput(HTItemOutput.of(item, count))

    fun itemOutput(stack: ItemStack): HTMachineRecipeBuilder<R> = itemOutput(HTItemOutput.of(stack))

    private fun itemOutput(output: HTItemOutput): HTMachineRecipeBuilder<R> = apply {
        itemOutputs.add(output)
    }

    //    Fluid Output    //

    fun fluidOutput(fluid: Supplier<out Fluid>, count: Int = 1000): HTMachineRecipeBuilder<R> = fluidOutput(fluid.get(), count)

    fun fluidOutput(fluid: Fluid, amount: Int = 1000): HTMachineRecipeBuilder<R> = fluidOutput(HTFluidOutput.of(fluid, amount))

    private fun fluidOutput(output: HTFluidOutput): HTMachineRecipeBuilder<R> = apply {
        fluidOutputs.add(output)
    }

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = itemOutputs.firstOrNull()?.id
        ?: fluidOutputs.firstOrNull()?.id
        ?: error("Either one item or fluid output required at least!")

    override val prefix: String get() = throw UnsupportedOperationException()

    override fun createRecipe(): R = factory(
        HTRecipeDefinition(
            itemInputs,
            fluidInputs,
            itemOutputs,
            fluidOutputs,
        ),
    ).orThrow

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        val recipe: R = createRecipe()
        recipeOutput.accept(
            id.withPrefix("${recipe.recipeType.name}/"),
            recipe,
            null,
        )
    }

    override fun group(groupName: String?): RecipeBuilder = throw UnsupportedOperationException()
}

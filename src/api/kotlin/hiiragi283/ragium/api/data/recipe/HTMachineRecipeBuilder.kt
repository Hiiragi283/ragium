package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import net.minecraft.data.recipes.RecipeBuilder
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

class HTMachineRecipeBuilder(private val recipeType: HTMachineRecipeType) : HTRecipeBuilder<HTMachineRecipe> {
    private val itemInputs: MutableList<SizedIngredient> = mutableListOf()
    private val fluidInputs: MutableList<SizedFluidIngredient> = mutableListOf()
    private val itemOutputs: MutableList<HTItemOutput> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidOutput> = mutableListOf()

    //    Item Input    //

    fun itemInput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder = itemInput(SizedIngredient.of(item, count))

    fun itemInput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): HTMachineRecipeBuilder =
        itemInput(prefix.createItemTag(material), count)

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeBuilder = itemInput(SizedIngredient.of(tagKey, count))

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): HTMachineRecipeBuilder =
        itemInput(SizedIngredient(ingredient.toVanilla(), count))

    private fun itemInput(ingredient: SizedIngredient): HTMachineRecipeBuilder = apply {
        itemInputs.add(ingredient)
    }

    //    Fluid Input    //

    fun fluidInput(fluid: HTFluidContent<*, *, *>, amount: Int = 1000): HTMachineRecipeBuilder = fluidInput(fluid.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = 1000): HTMachineRecipeBuilder = fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = 1000): HTMachineRecipeBuilder = fluidInput(FluidIngredient.tag(tagKey), amount)

    fun fluidInput(ingredient: FluidIngredient, amount: Int = 1000): HTMachineRecipeBuilder =
        fluidInput(SizedFluidIngredient(ingredient, amount))

    private fun fluidInput(ingredient: SizedFluidIngredient): HTMachineRecipeBuilder = apply {
        fluidInputs.add(ingredient)
    }

    fun waterInput(amount: Int = 1000): HTMachineRecipeBuilder = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = 1000): HTMachineRecipeBuilder = fluidInput(Tags.Fluids.MILK, amount)

    //    Item Output    //

    fun itemOutput(item: ItemLike, count: Int = 1): HTMachineRecipeBuilder = itemOutput(HTItemOutput.of(item, count))

    fun itemOutput(stack: ItemStack): HTMachineRecipeBuilder = itemOutput(HTItemOutput.of(stack))

    private fun itemOutput(output: HTItemOutput): HTMachineRecipeBuilder = apply {
        itemOutputs.add(output)
    }

    //    Fluid Output    //

    fun fluidOutput(fluid: Supplier<out Fluid>, count: Int = 1000): HTMachineRecipeBuilder = fluidOutput(fluid.get(), count)

    fun fluidOutput(fluid: Fluid, amount: Int = 1000): HTMachineRecipeBuilder = fluidOutput(HTFluidOutput.of(fluid, amount))

    private fun fluidOutput(output: HTFluidOutput): HTMachineRecipeBuilder = apply {
        fluidOutputs.add(output)
    }

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = itemOutputs.firstOrNull()?.id
        ?: fluidOutputs.firstOrNull()?.id
        ?: error("Either one item or fluid output required at least!")

    override val prefix: String get() = recipeType.name

    override fun createRecipe(): HTMachineRecipe = recipeType
        .createRecipe(
            HTRecipeDefinition(
                itemInputs,
                fluidInputs,
                itemOutputs,
                fluidOutputs,
            ),
        ).orThrow

    override fun group(groupName: String?): RecipeBuilder = throw UnsupportedOperationException()
}

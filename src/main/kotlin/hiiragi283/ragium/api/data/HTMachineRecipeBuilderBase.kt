package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTMachineRecipeBase
import hiiragi283.ragium.common.init.RagiumItems
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
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.function.Supplier

abstract class HTMachineRecipeBuilderBase<T : HTMachineRecipeBuilderBase<T, R>, R : HTMachineRecipeBase> : RecipeBuilder {
    //    Item Input    //

    fun itemInput(item: ItemLike, count: Int = 1): T = itemInput(Ingredient.of(item), count)

    fun itemInput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): T = itemInput(prefix.createTag(material), count)

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): T = itemInput(Ingredient.of(tagKey), count)

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): T = itemInput(ingredient.toVanilla(), count)

    fun itemInput(ingredient: SizedIngredient): T = itemInput(ingredient.ingredient(), ingredient.count())

    abstract fun itemInput(ingredient: Ingredient, count: Int = 1): T

    //    Fluid Input    //

    fun fluidInput(content: HTFluidContent, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(content.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(FluidIngredient.tag(tagKey), amount)

    abstract fun fluidInput(ingredient: FluidIngredient, amount: Int = FluidType.BUCKET_VOLUME): T

    fun waterInput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(Tags.Fluids.MILK, amount)

    //    Item Output    //

    fun itemOutput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): T =
        itemOutput(RagiumItems.getMaterialItem(prefix, material), count)

    fun itemOutput(item: ItemLike, count: Int = 1): T = itemOutput(ItemStack(item, count))

    abstract fun itemOutput(stack: ItemStack): T

    //    Fluid Output    //

    fun fluidOutput(fluid: Supplier<out Fluid>, count: Int = FluidType.BUCKET_VOLUME): T = fluidOutput(fluid.get(), count)

    fun fluidOutput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): T = fluidOutput(FluidStack(fluid, amount))

    abstract fun fluidOutput(stack: FluidStack): T

    fun waterOutput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidOutput(Fluids.WATER, amount)

    //    RecipeBuilder    //

    final override fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, getPrimalId())
    }

    protected abstract fun getPrimalId(): ResourceLocation

    fun savePrefixed(recipeOutput: RecipeOutput, prefix: String) {
        save(recipeOutput, getPrimalId().withPrefix(prefix))
    }

    fun saveSuffixed(recipeOutput: RecipeOutput, suffix: String) {
        save(recipeOutput, getPrimalId().withSuffix(suffix))
    }

    final override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            fixId(id),
            createRecipe(),
            null,
        )
    }

    protected abstract val prefix: String

    private fun fixId(id: ResourceLocation): ResourceLocation = RagiumAPI.wrapId(id.withPrefix("$prefix/"))

    //    Export    //

    protected abstract fun createRecipe(): R

    fun export(id: ResourceLocation): RecipeHolder<R> = RecipeHolder(fixId(id), createRecipe())

    fun exportPrefixed(prefix: String): RecipeHolder<R> = export(getPrimalId().withPrefix(prefix))

    fun exportSuffixed(suffix: String): RecipeHolder<R> = export(getPrimalId().withSuffix(suffix))

    fun export(consumer: (R) -> Unit) {
        consumer(createRecipe())
    }
}

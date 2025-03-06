package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.base.HTFluidOutput
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMachineRecipe
import hiiragi283.ragium.api.registry.HTDeferredFluid
import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderGetter
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.function.Supplier

/**
 * 機械レシピのビルダーの基本クラス
 * @param T このクラスを継承したビルダーのクラス
 * @param R 生成する機械レシピのクラス
 * @param lookup 現在未使用
 */
abstract class HTMachineRecipeBuilder<T : HTMachineRecipeBuilder<T, R>, R : HTMachineRecipe>(val lookup: HolderGetter<Item>) :
    RecipeBuilder {
    //    Item Input    //

    fun itemInput(item: ItemLike, count: Int = 1): T = itemInput(HTItemIngredient.of(item, count))

    fun itemInput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): T = itemInput(prefix.createTag(material), count)

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): T = itemInput(HTItemIngredient.of(tagKey, count))

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): T = itemInput(HTItemIngredient.of(ingredient, count))

    abstract fun itemInput(ingredient: HTItemIngredient): T

    //    Fluid Input    //

    fun fluidInput(fluid: HTDeferredFluid<*>, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(fluid.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(FluidIngredient.tag(tagKey), amount)

    abstract fun fluidInput(ingredient: FluidIngredient, amount: Int = FluidType.BUCKET_VOLUME): T

    fun waterInput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(Tags.Fluids.MILK, amount)

    //    Item Output    //

    /*fun itemOutput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): T = itemOutput(prefix.createTag(material), count)

    fun itemOutput(tagKey: TagKey<Item>, count: Int = 1): T {
        lookup
            .get(tagKey)
            .map { HTItemOutput(it, count, DataComponentPatch.EMPTY) }
            .ifPresentOrElse(::itemOutput) { isErrored = true }
        return this as T
    }*/

    fun itemOutput(item: ItemLike, count: Int = 1): T = itemOutput(HTItemOutput.of(item, count))

    fun itemOutput(stack: ItemStack): T = itemOutput(HTItemOutput.of(stack))

    protected abstract fun itemOutput(output: HTItemOutput): T

    //    Fluid Output    //

    fun fluidOutput(fluid: Supplier<out Fluid>, count: Int = FluidType.BUCKET_VOLUME): T = fluidOutput(fluid.get(), count)

    fun fluidOutput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): T =
        fluidOutput(HTFluidOutput(fluid, amount, DataComponentPatch.EMPTY))

    protected abstract fun fluidOutput(output: HTFluidOutput): T

    fun waterOutput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidOutput(Fluids.WATER, amount)

    //    RecipeBuilder    //

    final override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = throw UnsupportedOperationException()

    final override fun getResult(): Item = throw UnsupportedOperationException()

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
        recipeOutput.accept(fixId(id), createRecipe(), null)
    }

    protected abstract val prefix: String

    private fun fixId(id: ResourceLocation): ResourceLocation = id.withPrefix("$prefix/")

    //    Export    //

    protected abstract fun createRecipe(): R

    fun export(id: ResourceLocation, consumer: (RecipeHolder<R>) -> Unit) {
        consumer(RecipeHolder(fixId(id), createRecipe()))
    }
}

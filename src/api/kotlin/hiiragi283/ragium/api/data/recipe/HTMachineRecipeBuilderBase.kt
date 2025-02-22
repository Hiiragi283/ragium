package hiiragi283.ragium.api.data.recipe

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.commonTag
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
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
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.registries.DeferredHolder
import org.slf4j.Logger
import java.util.function.Supplier

abstract class HTMachineRecipeBuilderBase<T : HTMachineRecipeBuilderBase<T, R>, R : HTMachineRecipeBase>(val lookup: HolderGetter<Item>) :
    RecipeBuilder {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    private var isErrored: Boolean = false

    //    Item Input    //

    fun itemInput(item: ItemLike, count: Int = 1): T = itemInput(HTItemIngredient.of(item, count))

    fun itemInput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): T = itemInput(prefix.createTag(material), count)

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): T = itemInput(HTItemIngredient.of(tagKey, count))

    fun itemInput(ingredient: ICustomIngredient, count: Int = 1): T = itemInput(HTItemIngredient.of(ingredient, count))

    abstract fun itemInput(ingredient: HTItemIngredient): T

    //    Fluid Input    //

    fun fluidInput(fluid: DeferredHolder<Fluid, *>, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(fluid.commonTag, amount)

    fun fluidInput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(FluidIngredient.of(fluid), amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(FluidIngredient.tag(tagKey), amount)

    abstract fun fluidInput(ingredient: FluidIngredient, amount: Int = FluidType.BUCKET_VOLUME): T

    fun waterInput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(Tags.Fluids.WATER, amount)

    fun milkInput(amount: Int = FluidType.BUCKET_VOLUME): T = fluidInput(Tags.Fluids.MILK, amount)

    //    Item Output    //

    fun itemOutput(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): T = itemOutput(prefix.createTag(material), count)

    @Suppress("UNCHECKED_CAST")
    fun itemOutput(tagKey: TagKey<Item>, count: Int = 1): T {
        lookup
            .get(tagKey)
            .map { HTItemOutput(it, count, DataComponentPatch.EMPTY) }
            .ifPresentOrElse(::itemOutput) { isErrored = true }
        return this as T
    }

    fun itemOutput(item: ItemLike, count: Int = 1): T =
        itemOutput(HTItemOutput(HolderSet.direct(item.asHolder()), count, DataComponentPatch.EMPTY))

    fun itemOutput(stack: ItemStack): T {
        check(!stack.isEmpty) { "Empty ItemStack is not allowed!" }
        return itemOutput(HTItemOutput(HolderSet.direct(stack.itemHolder), stack.count, stack.componentsPatch))
    }

    abstract fun itemOutput(output: HTItemOutput): T

    //    Fluid Output    //

    fun fluidOutput(fluid: Supplier<out Fluid>, count: Int = FluidType.BUCKET_VOLUME): T = fluidOutput(fluid.get(), count)

    fun fluidOutput(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME): T = fluidOutput(FluidStack(fluid, amount))

    abstract fun fluidOutput(stack: FluidStack): T

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

    private fun logWarn(id: ResourceLocation) {
        LOGGER.debug("Machine recipe: {} will not be saved!", id)
    }

    final override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        if (isErrored) {
            logWarn(id)
            return
        }
        recipeOutput.accept(fixId(id), createRecipe(), null)
    }

    protected abstract val prefix: String

    private fun fixId(id: ResourceLocation): ResourceLocation = id.withPrefix("$prefix/")

    //    Export    //

    protected abstract fun createRecipe(): R

    fun export(id: ResourceLocation, consumer: (RecipeHolder<R>) -> Unit) {
        if (isErrored) {
            logWarn(id)
            return
        }
        consumer(RecipeHolder(fixId(id), createRecipe()))
    }
}

package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.init.RagiumFluids
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTMachineRecipeJsonBuilder private constructor(
    private val key: HTMachineKey,
    private val tier: HTMachineTier = HTMachineTier.PRIMITIVE,
) {
    companion object {
        @JvmStatic
        fun create(key: HTMachineKey, minTier: HTMachineTier = HTMachineTier.PRIMITIVE): HTMachineRecipeJsonBuilder =
            HTMachineRecipeJsonBuilder(key, minTier)

        @JvmStatic
        fun createRecipeId(item: ItemConvertible): Identifier = CraftingRecipeJsonBuilder
            .getItemId(item)
            .path
            .let { RagiumAPI.id(it) }

        @JvmStatic
        fun createRecipeId(fluid: Fluid): Identifier = Registries.FLUID
            .getId(fluid)
            .path
            .let { RagiumAPI.id(it) }

        @JvmStatic
        fun createRecipeId(fluid: RagiumFluids): Identifier = createRecipeId(fluid.value)
    }

    private val itemInputs: MutableSet<HTItemIngredient> = mutableSetOf()
    private val fluidInputs: MutableSet<HTFluidIngredient> = mutableSetOf()
    private val itemOutputs: MutableList<HTItemResult> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidResult> = mutableListOf()
    private var catalyst: HTItemIngredient? = null

    //    Input    //

    fun itemInput(ingredient: HTItemIngredient): HTMachineRecipeJsonBuilder = apply {
        check(itemInputs.add(ingredient)) { "Duplicated item input: $ingredient found!" }
    }

    fun itemInput(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(prefix.createTag(material), count, consumeType)

    fun itemInput(
        content: HTContent.Material<*>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(content.prefixedTagKey, count, consumeType)

    fun itemInput(
        item: ItemConvertible,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(HTItemIngredient.of(item, count, consumeType))

    fun itemInput(
        tagKey: TagKey<Item>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(HTItemIngredient.of(tagKey, count, consumeType))

    fun fluidInput(ingredient: HTFluidIngredient): HTMachineRecipeJsonBuilder = apply {
        check(fluidInputs.add(ingredient)) { "Duplicated fluid input $ingredient found!" }
    }

    fun fluidInput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder =
        fluidInput(HTFluidIngredient.of(fluid, amount))

    fun fluidInput(fluid: RagiumFluids, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = fluidInput(fluid.tagKey, amount)

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder =
        fluidInput(HTFluidIngredient.of(tagKey, amount))

    //    Output    //

    fun itemOutput(
        item: ItemConvertible,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        itemOutputs.add(HTItemResult(item, count, components))
    }

    fun itemOutput(stack: ItemStack): HTMachineRecipeJsonBuilder = apply { itemOutputs.add(HTItemResult(stack)) }

    fun fluidOutput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult(fluid, amount))
    }

    fun fluidOutput(fluid: RagiumFluids, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult(fluid.value, amount))
    }

    //    Catalyst    //

    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.of(item)
    }

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.of(tagKey)
    }

    fun offerTo(exporter: RecipeExporter, output: ItemConvertible, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, output: Fluid, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, output: RagiumFluids, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        val prefix = "${key.id.path}/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        export { recipe: HTMachineRecipe -> exporter.accept(prefixedId, recipe, null) }
    }

    fun <T : Any> transform(transform: (HTMachineRecipe) -> T): T = transform(
        HTMachineRecipe(
            HTMachineDefinition(key, tier),
            itemInputs.toList(),
            fluidInputs.toList(),
            catalyst,
            itemOutputs,
            fluidOutputs,
        ),
    )

    fun export(action: (HTMachineRecipe) -> Unit) {
        transform(action)
    }
}

package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder.Companion.createRecipeId
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.*
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

/**
 * ```kotlin
 * HTMachineRecipeJsonBuilder
 *     .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
 *     .fluidInput(RagiumFluids.NITROGEN)
 *     .fluidInput(Fluids.WATER)
 *     .fluidOutput(RagiumFluids.NITRIC_ACID)
 *     .offerTo(exporter, RagiumFluids.NITRIC_ACID, "_from_nitrogen")
 *
 * ```
 */
class HTMachineRecipeJsonBuilder private constructor(
    private val key: HTMachineKey,
    private val tier: HTMachineTier = HTMachineTier.PRIMITIVE,
) {
    companion object {
        @JvmStatic
        fun create(key: HTMachineKey, minTier: HTMachineTier = HTMachineTier.PRIMITIVE): HTMachineRecipeJsonBuilder =
            HTMachineRecipeJsonBuilder(key, minTier)

        /**
         * Create new [Identifier] with [RagiumAPI.MOD_NAME] namespace and [item] path
         */
        @JvmStatic
        fun createRecipeId(item: ItemConvertible): Identifier = CraftingRecipeJsonBuilder
            .getItemId(item)
            .path
            .let { RagiumAPI.id(it) }

        /**
         * Create new [Identifier] with [RagiumAPI.MOD_NAME] namespace and [fluid] path
         */
        @JvmStatic
        fun createRecipeId(fluid: Fluid): Identifier = Registries.FLUID
            .getId(fluid)
            .path
            .let { RagiumAPI.id(it) }

        @JvmStatic
        fun createRecipeId(content: HTFluidContent): Identifier = createRecipeId(content.get())
    }

    private val itemInputs: MutableSet<HTItemIngredient> = mutableSetOf()
    private val fluidInputs: MutableSet<HTFluidIngredient> = mutableSetOf()
    private val itemOutputs: MutableList<HTItemResult> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidResult> = mutableListOf()
    private var catalyst: HTItemIngredient? = null

    //    Input    //

    /**
     * Put an item ingredient
     * @throws [IllegalStateException] when duplicated [ingredient] registered
     */
    fun itemInput(ingredient: HTItemIngredient): HTMachineRecipeJsonBuilder = apply {
        check(itemInputs.add(ingredient)) { "Duplicated item input: $ingredient found!" }
    }

    /**
     * Put an item ingredient based on [HTTagPrefix] and [HTMachineKey]
     */
    fun itemInput(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(prefix.createTag(material), count, consumeType)

    /**
     * Put an item ingredient based on [HTContent.Material]
     */
    fun itemInput(
        content: HTContent.Material<*>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(content.prefixedTagKey, count, consumeType)

    /**
     * Put an item ingredient based on [ItemConvertible]
     */
    fun itemInput(
        item: ItemConvertible,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(HTItemIngredient.of(item, count, consumeType))

    /**
     * Put an item ingredient based on [TagKey]
     */
    fun itemInput(
        tagKey: TagKey<Item>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(HTItemIngredient.of(tagKey, count, consumeType))

    /**
     * Put a fluid ingredient
     * @throws [IllegalStateException] when duplicated [ingredient] registered
     */
    fun fluidInput(ingredient: HTFluidIngredient): HTMachineRecipeJsonBuilder = apply {
        check(fluidInputs.add(ingredient)) { "Duplicated fluid input $ingredient found!" }
    }

    /**
     * Put a fluid ingredient based on [Fluid]
     */
    fun fluidInput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder =
        fluidInput(HTFluidIngredient.of(fluid, amount))

    /**
     * Put a fluid ingredient based on [HTFluidContent]
     */
    fun fluidInput(fluid: HTFluidContent, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder =
        fluidInput(fluid.tagKey, amount)

    /**
     * Put a fluid ingredient based on [TagKey]
     */
    fun fluidInput(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder =
        fluidInput(HTFluidIngredient.of(tagKey, amount))

    //    Output    //

    /**
     * Put an item output based on [ItemConvertible]
     */
    fun itemOutput(
        item: ItemConvertible,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        itemOutputs.add(HTItemResult(item, count, components))
    }

    /**
     * Put an item output based on [ItemStack]
     */
    fun itemOutput(stack: ItemStack): HTMachineRecipeJsonBuilder = apply { itemOutputs.add(HTItemResult(stack)) }

    /**
     * Put a fluid output based on [Fluid]
     */
    fun fluidOutput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult(fluid, amount))
    }

    /**
     * Put a fluid output based on [HTContent]
     */
    fun fluidOutput(fluid: HTFluidContent, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult(fluid.get(), amount))
    }

    //    Catalyst    //

    /**
     * Put a catalyst item based on [ItemConvertible]
     */
    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.of(item)
    }

    /**
     * Put a catalyst item based on [TagKey]
     */
    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.of(tagKey)
    }

    /**
     * Export built [HTMachineRecipe] to [exporter]
     * @param output used to generate recipe id by [createRecipeId]
     * @param suffix append to recipe id path by [Identifier.withSuffixedPath]
     */
    fun offerTo(exporter: RecipeExporter, output: ItemConvertible, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    /**
     * Export built [HTMachineRecipe] to [exporter]
     * @param output used to generate recipe id by [createRecipeId]
     * @param suffix append to recipe id path by [Identifier.withSuffixedPath]
     */
    fun offerTo(exporter: RecipeExporter, output: Fluid, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    /**
     * Export built [HTMachineRecipe] to [exporter]
     * @param output used to generate recipe id by [createRecipeId]
     * @param suffix append to recipe id path by [Identifier.withSuffixedPath]
     */
    fun offerTo(exporter: RecipeExporter, output: HTFluidContent, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    /**
     * Offer built [HTMachineRecipe] to [exporter] with recipe id prefixed with [HTMachineKey.id]
     */
    fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        val prefix = "${key.id.path}/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        export { recipe: HTMachineRecipe -> exporter.accept(prefixedId, recipe, null) }
    }

    /**
     * Transform built [HTMachineRecipe] into [T]
     * @return transformed value from [HTMachineRecipe]
     */
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

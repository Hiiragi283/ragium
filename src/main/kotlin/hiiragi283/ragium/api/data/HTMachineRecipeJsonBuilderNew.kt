package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.*
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

/**
 * 機械レシピを構築するビルダー
 * @see HTMachineRecipe
 */
class HTMachineRecipeJsonBuilderNew private constructor(
    private val key: HTMachineKey,
    private val factory: HTMachineRecipe.Factory<*>,
    private val tier: HTMachineTier,
) {
    companion object {
        @JvmStatic
        private val idCache: MutableMap<Identifier, Int> = mutableMapOf()

        @JvmStatic
        fun create(
            key: HTMachineKey,
            factory: HTMachineRecipe.Factory<*>,
            minTier: HTMachineTier = HTMachineTier.PRIMITIVE,
        ): HTMachineRecipeJsonBuilderNew = HTMachineRecipeJsonBuilderNew(key, factory, minTier)

        @JvmStatic
        fun create(recipeType: HTMachineRecipeType<*>, minTier: HTMachineTier = HTMachineTier.PRIMITIVE): HTMachineRecipeJsonBuilderNew =
            create(recipeType.machineKey, recipeType.factory, minTier)
    }

    private val itemInputs: MutableSet<HTItemIngredient> = mutableSetOf()
    private val fluidInputs: MutableSet<HTFluidIngredient> = mutableSetOf()
    private val itemOutputs: MutableList<HTItemResult> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidResult> = mutableListOf()
    private var catalyst: HTItemIngredient? = null

    //    Input    //

    /**
     * アイテムの材料を追加します。
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun itemInput(ingredient: HTItemIngredient): HTMachineRecipeJsonBuilderNew = apply {
        check(itemInputs.add(ingredient)) { "Duplicated item input: $ingredient found!" }
    }

    /**
     * アイテムの材料を[item]から追加します。
     * @param count 必要なアイテムの個数
     * @param consumeType アイテムを消費するときの挙動
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun itemInput(
        item: ItemConvertible,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilderNew = itemInput(HTItemIngredient.of(item, count, consumeType))

    /**
     * アイテムの材料を[items]から追加します。
     * @param count 必要なアイテムの個数
     * @param consumeType アイテムを消費するときの挙動
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun itemInput(
        vararg items: ItemConvertible,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilderNew = itemInput(items.toList(), count, consumeType)

    /**
     * アイテムの材料を[items]から追加します。
     * @param count 必要なアイテムの個数
     * @param consumeType アイテムを消費するときの挙動
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun itemInput(
        items: List<ItemConvertible>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilderNew = itemInput(HTItemIngredient.of(items, count, consumeType))

    /**
     * アイテムの材料を[prefix]と[material]から追加します。
     * @param count 必要なアイテムの個数
     * @param consumeType アイテムを消費するときの挙動
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun itemInput(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilderNew = itemInput(prefix.createTag(material), count, consumeType)

    /**
     * アイテムの材料を[provider]から追加します。
     * @param count 必要なアイテムの個数
     * @param consumeType アイテムを消費するときの挙動
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun itemInput(
        provider: HTMaterialProvider,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilderNew = itemInput(provider.prefixedTagKey, count, consumeType)

    /**
     * アイテムの材料を[tagKey]から追加します。
     * @param count 必要なアイテムの個数
     * @param consumeType アイテムを消費するときの挙動
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun itemInput(
        tagKey: TagKey<Item>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilderNew = itemInput(HTItemIngredient.of(tagKey, count, consumeType))

    /**
     * 液体の材料を追加します。
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun fluidInput(ingredient: HTFluidIngredient): HTMachineRecipeJsonBuilderNew = apply {
        check(fluidInputs.add(ingredient)) { "Duplicated fluid input $ingredient found!" }
    }

    /**
     * 液体の材料を[fluid]から追加します。
     * @param amount 必要な液体の量
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun fluidInput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew =
        fluidInput(HTFluidIngredient.of(fluid, amount))

    /**
     * 液体の材料を[fluids]から追加します。
     * @param amount 必要な液体の量
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun fluidInput(fluids: List<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew =
        fluidInput(HTFluidIngredient.of(fluids, amount))

    /**
     * 液体の材料を[content]から追加します。
     * @param amount 必要な液体の量
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun fluidInput(content: HTFluidContent, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew =
        fluidInput(content.tagKey, amount)

    /**
     * 液体の材料を[tagKey]から追加します。
     * @param amount 必要な液体の量
     * @throws IllegalStateException 重複した材料が登録された場合
     */
    fun fluidInput(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew =
        fluidInput(HTFluidIngredient.of(tagKey, amount))

    //    Output    //

    /**
     * アイテムの完成品を追加します。
     */
    fun itemOutput(
        item: ItemConvertible,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilderNew = apply {
        itemOutputs.add(HTItemResult.ofItem(item, count, components))
    }

    /**
     * アイテムの完成品を[prefix]と[material]から追加します。
     */
    fun itemOutput(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilderNew = itemOutput(prefix.createTag(material), count, components)

    /**
     * アイテムの完成品を[provider]から追加します。
     */
    fun itemOutput(
        provider: HTMaterialProvider,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilderNew = itemOutput(provider.prefixedTagKey, count, components)

    /**
     * アイテムの完成品をタグから追加します。
     */
    fun itemOutput(
        tagKey: TagKey<Item>,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilderNew = apply {
        itemOutputs.add(HTItemResult.fromTag(tagKey, count, components))
    }

    /**
     * アイテムの完成品を[stack]から追加します。
     */
    fun itemOutput(stack: ItemStack): HTMachineRecipeJsonBuilderNew = apply { itemOutputs.add(HTItemResult.fromStack(stack)) }

    /**
     * 液体の完成品を追加します。
     */
    fun fluidOutput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew = apply {
        fluidOutputs.add(HTFluidResult(fluid, amount))
    }

    /**
     * 液体の完成品を[content]から追加します。
     */
    fun fluidOutput(content: HTFluidContent, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew = apply {
        fluidOutputs.add(HTFluidResult(content.get(), amount))
    }

    //    Catalyst    //

    /**
     * 触媒アイテムを[item]から指定します。
     */
    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilderNew = apply {
        catalyst = HTItemIngredient.of(item)
    }

    /**
     * 触媒アイテムを[prefix]と[material]から指定します。
     */
    fun catalyst(prefix: HTTagPrefix, material: HTMaterialKey): HTMachineRecipeJsonBuilderNew = catalyst(prefix.createTag(material))

    /**
     * 触媒アイテムを[tagKey]から指定します。
     */
    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilderNew = apply {
        catalyst = HTItemIngredient.of(tagKey)
    }

    //    Complete    //

    fun offerTo(
        exporter: RecipeExporter,
        prefix: HTTagPrefix,
        materialKey: HTMaterialKey,
        suffix: String = "",
    ) {
        offerTo(exporter, prefix.createTag(materialKey).id.withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, tagKey: TagKey<Item>) {
        offerTo(exporter, tagKey.id)
    }

    fun offerTo(exporter: RecipeExporter, content: HTContent<*>) {
        offerTo(exporter, content.id)
    }

    fun offerTo(exporter: RecipeExporter, path: String) {
        offerTo(exporter, RagiumAPI.id(path))
    }

    /**
     * [HTMachineKey.id]で前置されたレシピIDを使用してレシピを登録します。
     */
    fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        val prefix = "${key.id.path}/"
        var prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        // avoid id duplication
        val count: Int = idCache.compute(prefixedId) { _: Identifier, value: Int? -> value?.let { it + 1 } ?: 0 }!!
        if (count > 0) {
            prefixedId = prefixedId.withSuffixedPath("_alt$count")
        }
        val recipe: HTMachineRecipe = build()
        exporter.accept(prefixedId, recipe, null)
    }

    fun build(): HTMachineRecipe {
        val data = HTMachineRecipeData(
            itemInputs.toList(),
            fluidInputs.toList(),
            catalyst,
            itemOutputs,
            fluidOutputs,
        )
        check(data.isValidOutput(false)) { "No output recipe is not allowed!" }
        return factory.create(HTMachineDefinition(key, tier), data)
    }

    fun <T> transform(transform: (HTMachineRecipe) -> T): T = build().let(transform)
}

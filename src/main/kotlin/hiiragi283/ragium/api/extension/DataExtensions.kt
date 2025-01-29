package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import net.minecraft.data.recipes.*
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

//    LanguageProvider    //

@JvmName("addBlock")
fun LanguageProvider.add(block: Supplier<out Block>, value: String) {
    add(block.get().descriptionId, value)
}

@JvmName("addItem")
fun LanguageProvider.add(block: Supplier<out Item>, value: String) {
    add(block.get().descriptionId, value)
}

fun LanguageProvider.add(tier: HTMachineTier, value: String, prefix: String) {
    add(tier.translationKey, value)
    add(tier.prefixKey, prefix)
}

fun LanguageProvider.add(machine: HTMachineKey, value: String, desc: String = "") {
    add(machine.translationKey, value)
    add(machine.descriptionKey, desc)
}

fun LanguageProvider.add(material: HTMaterialKey, value: String) {
    add(material.translationKey, value)
}

fun LanguageProvider.add(prefix: HTTagPrefix, value: String) {
    add(prefix.translationKey, value)
}

fun LanguageProvider.add(variant: HTOreVariant, value: String) {
    add(variant.translationKey, value)
}

//    ModelBuilder    //

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(content: HTContent<*>): T = getBuilder(content.id)

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(holder: DeferredHolder<*, *>): T = getBuilder(holder.id)

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(id: ResourceLocation): T = getBuilder(id.toString())

fun <T : ModelBuilder<T>> ModelProvider<T>.withExistingParent(content: HTContent<*>, parent: ResourceLocation): T =
    withExistingParent(content.id.toString(), parent)

fun <T : ModelBuilder<T>> ModelProvider<T>.withUncheckedParent(content: HTContent<*>, parent: ResourceLocation): T =
    getBuilder(content).parent(ModelFile.UncheckedModelFile(parent))

fun <T : ModelBuilder<T>> T.blockTexture(key: String, id: ResourceLocation): T = texture(key, id.withPrefix("block/"))

fun <T : ModelBuilder<T>> T.itemTexture(key: String, id: ResourceLocation): T = texture(key, id.withPrefix("item/"))

fun <T : ModelBuilder<T>> T.cutout(): T = renderType("cutout")

//    RecipeBuilder    //

private fun RecipeBuilder.savePrefixed(output: RecipeOutput, prefix: String) {
    save(output, RecipeBuilder.getDefaultRecipeId(result).withPrefix(prefix))
}

fun ShapedRecipeBuilder.defineMaterial(symbol: Char, provider: HTMaterialProvider): ShapedRecipeBuilder =
    define(symbol, provider.prefixedTagKey)

fun ShapedRecipeBuilder.define(symbol: Char, prefix: HTTagPrefix, material: HTMaterialKey): ShapedRecipeBuilder =
    define(symbol, prefix.createTag(material))

fun ShapedRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "shaped/")
}

fun ShapelessRecipeBuilder.requiresMaterial(provider: HTMaterialProvider): ShapelessRecipeBuilder = requires(provider.prefixedTagKey)

fun ShapelessRecipeBuilder.requires(prefix: HTTagPrefix, material: HTMaterialKey): ShapelessRecipeBuilder =
    requires(prefix.createTag(material))

fun ShapelessRecipeBuilder.requiresFor(times: Int, ingredient: Ingredient): ShapelessRecipeBuilder = apply {
    repeat(times) { requires(ingredient) }
}

fun ShapelessRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "shapeless/")
}

fun SingleItemRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "stonecutting/")
}

//    TagBuilder    //

fun <T : Any> TagsProvider.TagAppender<T>.add(holder: DeferredHolder<T, out T>, optional: Boolean = false): TagsProvider.TagAppender<T> =
    when (optional) {
        true -> addOptional(holder.id)
        false -> add(holder.keyOrThrow)
    }

fun <T : ItemLike> TagsProvider.TagAppender<T>.add(content: HTContent<T>, optional: Boolean = false): TagsProvider.TagAppender<T> =
    when (optional) {
        true -> addOptional(content.id)
        false -> add(content.key)
    }

fun TagsProvider.TagAppender<Item>.addItem(content: HTContent<Block>, optional: Boolean = false): TagsProvider.TagAppender<Item> =
    when (optional) {
        true -> addOptional(content.id)
        false -> add(content.asHolder().keyOrThrow)
    }

fun <T : Any> TagsProvider.TagAppender<T>.addTag(tagKey: TagKey<T>, optional: Boolean = false): TagsProvider.TagAppender<T> =
    when (optional) {
        true -> addOptionalTag(tagKey.location)
        false -> addTag(tagKey)
    }

fun <T : ItemLike> TagsProvider.TagAppender<T>.addAll(contents: Iterable<HTContent<T>>): TagsProvider.TagAppender<T> =
    addAll(contents.map(HTContent<T>::key))

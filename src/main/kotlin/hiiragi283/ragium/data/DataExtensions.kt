package hiiragi283.ragium.data

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
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

fun SimpleCookingRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "cooking/")
}

fun SingleItemRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "stonecutting/")
}

//    TagBuilder    //

fun TagBuilder.addElement(holder: DeferredHolder<*, *>, optional: Boolean = false): TagBuilder = when (optional) {
    true -> addOptionalElement(holder.id)
    false -> addElement(holder.id)
}

fun TagBuilder.addElement(content: HTContent<*>, optional: Boolean = false): TagBuilder = when (optional) {
    true -> addOptionalElement(content.id)
    false -> addElement(content.id)
}

fun TagBuilder.addTag(tagKey: TagKey<*>, optional: Boolean = false): TagBuilder = when (optional) {
    true -> addOptionalTag(tagKey.location)
    false -> addTag(tagKey.location)
}

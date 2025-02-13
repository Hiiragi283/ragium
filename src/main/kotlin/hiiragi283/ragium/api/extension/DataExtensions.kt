package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import net.minecraft.Util
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.client.model.generators.*
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

//    LanguageProvider    //

fun LanguageProvider.addFluid(fluid: Supplier<out Fluid>, value: String) {
    add(fluid.get().fluidType.descriptionId, value)
}

fun LanguageProvider.addEnchantment(key: ResourceKey<Enchantment>, value: String) {
    add(Util.makeDescriptionId("enchantment", key.location()), value)
}

fun LanguageProvider.add(machine: HTMachineType, value: String, desc: String = "") {
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

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(holder: DeferredHolder<*, *>): T = getBuilder(holder.id)

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(id: ResourceLocation): T = getBuilder(id.toString())

fun <T : ModelBuilder<T>> ModelProvider<T>.withUncheckedParent(holder: DeferredBlock<*>, parent: ResourceLocation): T =
    getBuilder(holder).parent(ModelFile.UncheckedModelFile(parent))

fun <T : ModelBuilder<T>> T.blockTexture(key: String, id: ResourceLocation): T = texture(key, id.withPrefix("block/"))

fun <T : ModelBuilder<T>> T.itemTexture(key: String, id: ResourceLocation): T = texture(key, id.withPrefix("item/"))

fun <T : ModelBuilder<T>> T.cutout(): T = renderType("cutout")

fun <T : ModelBuilder<T>> ModelProvider<T>.cutoutSimpleBlock(name: String, texture: ResourceLocation): T =
    withExistingParent(name, "block/cube_all")
        .texture("all", texture)
        .cutout()

fun BlockStateProvider.layeredBlock(holder: DeferredBlock<*>, layer0: ResourceLocation, layer1: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(
            models()
                .withExistingParent(holder.id.path, RagiumAPI.id("block/layered"))
                .texture("layer0", layer0)
                .texture("layer1", layer1)
                .cutout(),
        ),
    )
}

fun BlockStateProvider.uncheckedSimpleBlock(holder: DeferredBlock<*>) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(ModelFile.UncheckedModelFile(holder.id.withPrefix("block/"))),
    )
}

fun BlockStateProvider.cutoutSimpleBlock(holder: DeferredBlock<*>) {
    simpleBlock(
        holder.get(),
        models().cutoutSimpleBlock(holder.blockId.path, holder.blockId),
    )
}

fun ItemModelProvider.basicItem(holder: DeferredHolder<Item, *>): ItemModelBuilder = basicItem(holder.id)

fun ItemModelProvider.simpleBlockItem(holder: DeferredHolder<Block, *>): ItemModelBuilder = simpleBlockItem(holder.id)

//    RecipeBuilder    //

private fun RecipeBuilder.savePrefixed(output: RecipeOutput, prefix: String) {
    save(output, RecipeBuilder.getDefaultRecipeId(result).withPrefix(prefix))
}

fun ShapedRecipeBuilder.define(symbol: Char, prefix: HTTagPrefix, material: HTMaterialKey): ShapedRecipeBuilder =
    define(symbol, prefix.createTag(material))

fun ShapedRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "shaped/")
}

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

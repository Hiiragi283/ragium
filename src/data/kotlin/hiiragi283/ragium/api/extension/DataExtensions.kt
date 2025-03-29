package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.Util
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.client.model.generators.*
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

//    LanguageProvider    //

fun LanguageProvider.addFluid(fluid: Supplier<out Fluid>, value: String) {
    add(fluid.get().fluidType.descriptionId, value)
}

fun LanguageProvider.addEnchantment(key: ResourceKey<Enchantment>, value: String, desc: String) {
    val translationKey: String = Util.makeDescriptionId("enchantment", key.location())
    add(translationKey, value)
    add("$translationKey.desc", desc)
}

fun LanguageProvider.addMaterialKey(material: HTMaterialKey, value: String) {
    add(material.translationKey, value)
}

fun LanguageProvider.addTagPrefix(prefix: HTTagPrefix, value: String) {
    add(prefix.translationKey, value)
}

fun LanguageProvider.addOreVariant(variant: HTOreVariant, value: String) {
    add(variant.translationKey, value)
}

fun LanguageProvider.addMold(mold: RagiumItems.Molds, value: String) {
    addItem(mold.holder, value)
}

//    ModelFile    //

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

fun <T : ModelBuilder<T>> ModelProvider<T>.layeredModel(path: String, layer0: ResourceLocation, layer1: ResourceLocation): T =
    withExistingParent(path, RagiumAPI.id("block/layered"))
        .texture("layer0", layer0)
        .texture("layer1", layer1)
        .renderType("cutout")

fun <T : ModelBuilder<T>> ModelProvider<T>.layeredModel(
    holder: DeferredHolder<*, *>,
    layer0: ResourceLocation,
    layer1: ResourceLocation,
): T = layeredModel(holder.id.path, layer0, layer1)

//    BlockModelProvider    //

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(holder: DeferredHolder<*, *>): T = getBuilder(holder.id.toString())

fun BlockStateProvider.simpleBlock(holder: DeferredBlock<*>) {
    simpleBlock(holder.get())
}

fun BlockStateProvider.layeredBlock(holder: DeferredBlock<*>, layer0: ResourceLocation, layer1: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(
            models().layeredModel(holder, layer0, layer1),
        ),
    )
}

fun BlockStateProvider.simpleAltBlock(holder: DeferredBlock<*>) {
    simpleBlock(holder.get(), modelFile(holder.blockId))
}

fun BlockStateProvider.simpleAltBlock(holder: DeferredBlock<*>, all: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.id.path, all)),
    )
}

fun BlockStateProvider.cutoutSimpleBlock(holder: DeferredBlock<*>) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.id.path, holder.blockId).renderType("cutout")),
    )
}

fun BlockStateProvider.slabBlock(holder: DeferredBlock<out SlabBlock>, base: DeferredBlock<*>) {
    slabBlock(holder.get(), base.blockId, base.blockId)
}

//    ItemModelProvider    //

fun ItemModelProvider.basicItem(holder: DeferredItem<*>): ItemModelBuilder = basicItem(holder.id)

fun ItemModelProvider.simpleBlockItem(holder: DeferredBlock<*>): ItemModelBuilder = getBuilder(holder).parent(modelFile(holder.blockId))

package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.registry.HTFluidContent
import net.minecraft.Util
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.SlabBlock
import net.neoforged.neoforge.client.model.generators.*
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

//    LanguageProvider    //

fun LanguageProvider.addFluid(content: HTFluidContent<*, *, *>, value: String) {
    add(content.getType().descriptionId, value)
    if ("en_us" in this.name) {
        add(content.getBucket(), "$value Bucket")
    } else if ("ja_jp" in this.name) {
        add(content.getBucket(), "${value}入りバケツ")
    }
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

fun LanguageProvider.addItem(item: ItemLike, value: String) {
    addItem(item::asItem, value)
}

fun LanguageProvider.addItemAdvDesc(item: ItemLike, value: String) {
    addAdvDesc(item.asItemHolder().idOrThrow, value)
}

fun LanguageProvider.addAdvDesc(holder: DeferredHolder<*, *>, value: String) {
    addAdvDesc(holder.id, value)
}

private fun LanguageProvider.addAdvDesc(id: ResourceLocation, value: String) {
    add(Util.makeDescriptionId("advancements", id.withSuffix(".desc")), value)
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

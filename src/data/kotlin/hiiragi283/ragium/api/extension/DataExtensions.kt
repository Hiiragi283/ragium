package hiiragi283.ragium.api.extension

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.Util
import net.minecraft.advancements.Advancement
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.SlabBlock
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

//    Advancement    //

private fun translationKey(key: ResourceKey<Advancement>): String = Util.makeDescriptionId("advancements", key.location())

fun ResourceKey<Advancement>.titleKey(): String = "${translationKey(this)}.title"

fun ResourceKey<Advancement>.descKey(): String = "${translationKey(this)}.desc"

//    TagAppender    //

fun <T : Any, B : TagsProvider.TagAppender<T>> B.addHolder(holder: DeferredHolder<T, out T>): B = apply {
    holder.unwrapKey().ifPresent(::add)
}

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

fun LanguageProvider.addInfo(item: ItemLike, value: String) {
    add(RagiumTranslationKeys.getTooltipKey(ItemStack(item)), value)
}

fun LanguageProvider.addAdvancement(key: ResourceKey<Advancement>, title: String, desc: String) {
    add(key.titleKey(), title)
    add(key.descKey(), desc)
}

fun LanguageProvider.addMatterType(type: IMatterType, value: String) {
    add("${RagiumConstantValues.REPLICATION}.matter_type.${type.name}", value)
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

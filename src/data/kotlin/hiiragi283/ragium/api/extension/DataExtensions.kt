package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder

//    Advancement    //

fun ResourceKey<Advancement>.titleKey(): String = toDescriptionKey("advancements", "title")

fun ResourceKey<Advancement>.descKey(): String = toDescriptionKey("advancements", "desc")

//    ModelFile    //

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

fun <T : ModelBuilder<T>> ModelProvider<T>.layeredModel(path: String, layer0: ResourceLocation, layer1: ResourceLocation): T =
    withExistingParent(path, RagiumAPI.id("block/layered"))
        .texture("layer0", layer0)
        .texture("layer1", layer1)
        .renderType("cutout")

fun <T : ModelBuilder<T>> ModelProvider<T>.layeredModel(holder: DeferredBlock<*>, layer0: ResourceLocation, layer1: ResourceLocation): T =
    layeredModel(holder.id.path, layer0, layer1)

fun <T : ModelBuilder<T>> ModelProvider<T>.layeredModel(holder: HTBlockHolderLike, layer0: ResourceLocation, layer1: ResourceLocation): T =
    layeredModel(holder.id.path, layer0, layer1)

//    BlockModelProvider    //

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(holder: DeferredHolder<*, *>): T = getBuilder(holder.id.toString())

fun BlockStateProvider.layeredBlock(holder: DeferredBlock<*>, layer0: ResourceLocation, layer1: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(
            models().layeredModel(holder, layer0, layer1),
        ),
    )
}

fun BlockStateProvider.layeredBlock(holder: HTBlockHolderLike, layer0: ResourceLocation, layer1: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(
            models().layeredModel(holder, layer0, layer1),
        ),
    )
}

fun BlockStateProvider.cubeColumn(
    holder: DeferredBlock<*>,
    side: ResourceLocation = holder.blockId.withSuffix("_side"),
    end: ResourceLocation = holder.blockId.withSuffix("_top"),
) {
    simpleBlock(holder.get(), models().cubeColumn(holder.blockId.path, side, end))
}

fun BlockStateProvider.cubeColumn(holder: HTBlockHolderLike, side: ResourceLocation, top: ResourceLocation) {
    simpleBlock(holder.get(), models().cubeColumn(holder.blockId.path, side, top))
}

fun BlockStateProvider.altModelBlock(holder: DeferredBlock<*>) {
    simpleBlock(holder.get(), modelFile(holder.blockId))
}

fun BlockStateProvider.altModelBlock(holder: HTBlockHolderLike) {
    simpleBlock(holder.get(), modelFile(holder.blockId))
}

fun BlockStateProvider.altTextureBlock(holder: HTBlockHolderLike, all: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.id.path, all)),
    )
}

fun BlockStateProvider.cutoutSimpleBlock(holder: HTBlockHolderLike, texId: ResourceLocation = holder.blockId) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.id.path, texId).renderType("cutout")),
    )
}

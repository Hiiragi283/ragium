package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredBlockHolder
import hiiragi283.ragium.util.variant.HTDecorationVariant
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.registries.DeferredHolder

//    ModelFile    //

fun modelFile(path: String): ModelFile = modelFile(vanillaId(path))

fun modelFile(namespace: String, path: String): ModelFile = modelFile(ResourceLocation.fromNamespaceAndPath(namespace, path))

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

fun BlockModelProvider.layeredModel(
    holder: DeferredHolder<Block, *>,
    layer0: ResourceLocation,
    layer1: ResourceLocation,
): BlockModelBuilder = withExistingParent(holder.id.path, RagiumAPI.id("block/layered"))
    .texture("layer0", layer0)
    .texture("layer1", layer1)
    .renderType("cutout")

//    BlockModelProvider    //

val HTDecorationVariant.textureId: ResourceLocation get() = base.blockId

fun BlockStateProvider.simpleBlock(holder: DeferredHolder<Block, *>) {
    simpleBlock(holder.get())
}

fun BlockStateProvider.layeredBlock(holder: DeferredHolder<Block, *>, layer0: ResourceLocation, layer1: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(
            models().layeredModel(holder, layer0, layer1),
        ),
    )
}

fun BlockStateProvider.cubeColumn(
    holder: DeferredHolder<Block, *>,
    side: ResourceLocation = holder.blockId.withSuffix("_side"),
    end: ResourceLocation = holder.blockId.withSuffix("_top"),
) {
    simpleBlock(holder.get(), models().cubeColumn(holder.blockId.path, side, end))
}

fun BlockStateProvider.altModelBlock(holder: DeferredHolder<Block, *>, id: ResourceLocation = holder.blockId) {
    simpleBlock(holder.get(), modelFile(id))
}

fun BlockStateProvider.altTextureBlock(holder: DeferredHolder<Block, *>, all: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.id.path, all)),
    )
}

fun BlockStateProvider.cutoutSimpleBlock(holder: DeferredHolder<Block, *>, texId: ResourceLocation = holder.blockId) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.id.path, texId).renderType("cutout")),
    )
}

fun BlockStateProvider.translucentSimpleBlock(holder: DeferredHolder<Block, *>, texId: ResourceLocation = holder.blockId) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.id.path, texId).renderType("translucent")),
    )
}

//    ItemModelProvider    //

fun ItemModelProvider.simpleBlockItem(block: DeferredHolder<Block, *>): ItemModelBuilder = simpleBlockItem(block.id)

fun ItemModelProvider.basicItem(item: DeferredHolder<Item, *>): ItemModelBuilder = basicItem(item.id)

fun ItemModelProvider.basicItem(block: HTDeferredBlockHolder<*, *>): ItemModelBuilder = basicItem(block.itemHolder)

fun ItemModelProvider.handheldItem(item: DeferredHolder<Item, *>): ItemModelBuilder = handheldItem(item.id)

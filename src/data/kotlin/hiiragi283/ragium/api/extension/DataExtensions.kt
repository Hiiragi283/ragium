package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.common.variant.HTDecorationVariant
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile

//    ModelFile    //

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

fun BlockModelProvider.layeredModel(holder: HTDeferredBlock<*, *>, layer0: ResourceLocation, layer1: ResourceLocation): BlockModelBuilder =
    withExistingParent(holder.getPath(), RagiumAPI.id("block/layered"))
        .texture("layer0", layer0)
        .texture("layer1", layer1)
        .renderType("cutout")

//    BlockModelProvider    //

val HTDecorationVariant.textureId: ResourceLocation get() = base.blockId

fun BlockStateProvider.simpleBlock(holder: HTDeferredBlock<*, *>) {
    simpleBlock(holder.get())
}

fun BlockStateProvider.layeredBlock(holder: HTDeferredBlock<*, *>, layer0: ResourceLocation, layer1: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(
            models().layeredModel(holder, layer0, layer1),
        ),
    )
}

fun BlockStateProvider.cubeColumn(
    holder: HTDeferredBlock<*, *>,
    side: ResourceLocation = holder.blockId.withSuffix("_side"),
    end: ResourceLocation = holder.blockId.withSuffix("_top"),
) {
    simpleBlock(holder.get(), models().cubeColumn(holder.blockId.path, side, end))
}

fun BlockStateProvider.altModelBlock(
    holder: HTDeferredBlock<*, *>,
    id: ResourceLocation = holder.blockId,
    factory: (Block, ModelFile) -> Unit = ::simpleBlock,
) {
    factory(holder.get(), modelFile(id))
}

fun BlockStateProvider.altTextureBlock(holder: HTDeferredBlock<*, *>, all: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.getPath(), all)),
    )
}

fun BlockStateProvider.cutoutSimpleBlock(holder: HTDeferredBlock<*, *>, texId: ResourceLocation = holder.blockId) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.getPath(), texId).renderType("cutout")),
    )
}

fun BlockStateProvider.translucentSimpleBlock(holder: HTDeferredBlock<*, *>, texId: ResourceLocation = holder.blockId) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(models().cubeAll(holder.getPath(), texId).renderType("translucent")),
    )
}

//    ItemModelProvider    //

fun ItemModelProvider.simpleBlockItem(block: HTHolderLike): ItemModelBuilder = simpleBlockItem(block.getId())

fun ItemModelProvider.basicItem(item: HTHolderLike): ItemModelBuilder = basicItem(item.getId())

fun ItemModelProvider.basicItem(block: HTDeferredBlock<*, *>): ItemModelBuilder = basicItem(block.itemHolder)

fun ItemModelProvider.handheldItem(item: HTHolderLike): ItemModelBuilder = handheldItem(item.getId())

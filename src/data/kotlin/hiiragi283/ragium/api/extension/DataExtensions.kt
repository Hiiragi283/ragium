package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.util.variant.HTDecorationVariant
import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.registries.DeferredHolder

//    Advancement    //

fun ResourceKey<Advancement>.titleKey(): String = toDescriptionKey("advancements", "title")

fun ResourceKey<Advancement>.descKey(): String = toDescriptionKey("advancements", "desc")

//    ModelFile    //

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

fun <BUILDER : ModelBuilder<BUILDER>> ModelProvider<BUILDER>.layeredModel(
    path: String,
    layer0: ResourceLocation,
    layer1: ResourceLocation,
): BUILDER = withExistingParent(path, RagiumAPI.id("block/layered"))
    .texture("layer0", layer0)
    .texture("layer1", layer1)
    .renderType("cutout")

fun <BUILDER : ModelBuilder<BUILDER>> ModelProvider<BUILDER>.layeredModel(
    holder: DeferredHolder<Block, *>,
    layer0: ResourceLocation,
    layer1: ResourceLocation,
): BUILDER = layeredModel(holder.id.path, layer0, layer1)

//    BlockModelProvider    //

val HTDecorationVariant.textureId: ResourceLocation get() = base.id.withPrefix("block/")

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

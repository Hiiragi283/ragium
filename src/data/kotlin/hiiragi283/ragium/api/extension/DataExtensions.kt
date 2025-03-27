package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockStateProperties
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.Util
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.BlockState
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

//    ModelBuilder    //

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

fun activeModel(state: BlockState, active: ModelFile, inactive: ModelFile): ModelFile =
    when (state.getValue(HTBlockStateProperties.IS_ACTIVE)) {
        true -> active
        false -> inactive
    }

fun activeModel(state: BlockState, active: ResourceLocation, inactive: ResourceLocation): ModelFile =
    activeModel(state, modelFile(active), modelFile(inactive))

fun BlockModelProvider.machine(
    name: String,
    top: ResourceLocation,
    bottom: ResourceLocation,
    front: ResourceLocation,
): BlockModelBuilder = withExistingParent(name, RagiumAPI.id("block/machine_base"))
    .texture("top", top)
    .texture("bottom", bottom)
    .texture("front", front)
    .renderType("cutout")

fun <T : ModelBuilder<T>> ModelProvider<T>.getBuilder(holder: DeferredHolder<*, *>): T = getBuilder(holder.id.toString())

fun BlockStateProvider.layeredBlock(holder: DeferredBlock<*>, layer0: ResourceLocation, layer1: ResourceLocation) {
    simpleBlock(
        holder.get(),
        ConfiguredModel(
            models()
                .withExistingParent(holder.id.path, RagiumAPI.id("block/layered"))
                .texture("layer0", layer0)
                .texture("layer1", layer1)
                .renderType("cutout"),
        ),
    )
}

fun BlockStateProvider.simpleAltBlock(holder: DeferredBlock<*>) {
    simpleBlock(holder.get(), modelFile(holder.blockId))
}

fun BlockStateProvider.simpleAltBlock(holder: DeferredBlock<*>, all: String) {
    simpleAltBlock(holder, ResourceLocation.parse(all))
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
        models()
            .withExistingParent(holder.blockId.path, "block/cube_all")
            .texture("all", holder.blockId)
            .renderType("cutout"),
    )
}

fun BlockStateProvider.slabBlock(holder: DeferredBlock<out SlabBlock>, base: DeferredBlock<*>) {
    slabBlock(holder.get(), base.blockId, base.blockId)
}

fun ItemModelProvider.basicItem(holder: DeferredItem<*>): ItemModelBuilder = basicItem(holder.id)

fun ItemModelProvider.simpleBlockItem(holder: DeferredBlock<*>): ItemModelBuilder = getBuilder(holder).parent(modelFile(holder.blockId))

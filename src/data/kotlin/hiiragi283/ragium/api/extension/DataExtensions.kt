package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockStateProperties
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.registry.HTDeferredMachine
import hiiragi283.ragium.api.util.HTOreVariant
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

fun LanguageProvider.add(machine: HTDeferredMachine<*, *>, value: String) {
    addBlock(machine.blockHolder, value)
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

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

fun modelFile(machine: HTDeferredMachine<*, *>): ModelFile = modelFile(machine.blockId)

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

fun BlockModelProvider.machine(machine: HTDeferredMachine<*, *>, top: ResourceLocation, bottom: ResourceLocation): BlockModelBuilder =
    machine(
        machine.blockId.path,
        top,
        bottom,
        machine.blockId.withSuffix("_front"),
    )

fun BlockModelProvider.activeMachine(
    machine: HTDeferredMachine<*, *>,
    top: ResourceLocation,
    bottom: ResourceLocation,
): BlockModelBuilder = machine(
    machine.blockId.path + "_active",
    top,
    bottom,
    machine.blockId.withSuffix("_front_active"),
)

fun BlockModelProvider.activeMachine(
    state: BlockState,
    machine: HTDeferredMachine<*, *>,
    top: ResourceLocation,
    bottom: ResourceLocation,
): ModelFile = activeModel(
    state,
    activeMachine(machine, top, bottom),
    machine(machine, top, bottom),
)

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

package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

class RagiumBlockEntityTypeTagsProvider(context: HTDataGenContext) :
    HTTagsProvider<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, context) {
    override fun addTagsInternal(factory: BuilderFactory<BlockEntityType<*>>) {
        category(factory)

        factory
            .apply(RagiumModTags.BlockEntityTypes.CHANCED_OUTPUT_UPGRADABLE)
            .add(RagiumBlockEntityTypes.CUTTING_MACHINE)
            .add(RagiumBlockEntityTypes.CRUSHER)
            .add(RagiumBlockEntityTypes.PLANTER)
            .add(RagiumBlockEntityTypes.WASHER)

        factory
            .apply(RagiumModTags.BlockEntityTypes.EFFICIENT_CRUSH_UPGRADABLE)
            .add(RagiumBlockEntityTypes.PULVERIZER)
            .add(RagiumBlockEntityTypes.CRUSHER)
    }

    private fun category(factory: BuilderFactory<BlockEntityType<*>>) {
        factory
            .apply(RagiumModTags.BlockEntityTypes.MACHINES)
            .addTag(RagiumModTags.BlockEntityTypes.MACHINES_ELECTRIC)
            .addTag(RagiumModTags.BlockEntityTypes.DEVICES)

        factory
            .apply(RagiumModTags.BlockEntityTypes.MACHINES_ELECTRIC)
            .addTag(RagiumModTags.BlockEntityTypes.GENERATORS)
            .addTag(RagiumModTags.BlockEntityTypes.PROCESSORS)
        // Generators
        factory
            .apply(RagiumModTags.BlockEntityTypes.GENERATORS)
            // Basic
            .add(RagiumBlockEntityTypes.THERMAL_GENERATOR)
            // Advanced
            .add(RagiumBlockEntityTypes.COMBUSTION_GENERATOR)
            // Elite
            .add(RagiumBlockEntityTypes.SOLAR_PANEL_CONTROLLER)
            // Ultimate
            .add(RagiumBlockEntityTypes.ENCHANTMENT_GENERATOR)
            .add(RagiumBlockEntityTypes.NUCLEAR_REACTOR)

        // Processors
        factory
            .apply(RagiumModTags.BlockEntityTypes.PROCESSORS)
            // Basic
            .add(RagiumBlockEntityTypes.ALLOY_SMELTER)
            .add(RagiumBlockEntityTypes.BLOCK_BREAKER)
            .add(RagiumBlockEntityTypes.COMPRESSOR)
            .add(RagiumBlockEntityTypes.CUTTING_MACHINE)
            .add(RagiumBlockEntityTypes.ELECTRIC_FURNACE)
            .add(RagiumBlockEntityTypes.EXTRACTOR)
            .add(RagiumBlockEntityTypes.PULVERIZER)
            // Advanced
            .add(RagiumBlockEntityTypes.CRUSHER)
            .add(RagiumBlockEntityTypes.MELTER)
            .add(RagiumBlockEntityTypes.MIXER)
            .add(RagiumBlockEntityTypes.REFINERY)
            // Elite
            .add(RagiumBlockEntityTypes.ADVANCED_MIXER)
            .add(RagiumBlockEntityTypes.BREWERY)
            .add(RagiumBlockEntityTypes.MULTI_SMELTER)
            .add(RagiumBlockEntityTypes.PLANTER)
            .add(RagiumBlockEntityTypes.WASHER)
            // Ultimate
            .add(RagiumBlockEntityTypes.ENCHANTER)
            .add(RagiumBlockEntityTypes.SIMULATOR)

        // Devices
        factory
            .apply(RagiumModTags.BlockEntityTypes.DEVICES)
            // Basic
            .add(RagiumBlockEntityTypes.FLUID_COLLECTOR)
            .add(RagiumBlockEntityTypes.ITEM_COLLECTOR)
            // Elite
            .add(RagiumBlockEntityTypes.DIM_ANCHOR)
            .add(RagiumBlockEntityTypes.ENI)
            // Ultimate
            .add(RagiumBlockEntityTypes.TELEPAD)
            // Creative
            .add(RagiumBlockEntityTypes.CEU)
    }
}

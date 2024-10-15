package hiiragi283.ragium.data

import hiiragi283.ragium.api.data.HTMultiblockExporter
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumMultiblockPatterns
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class RagiumMultiblockPatternProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String = "Multiblock Pattern"

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {
        // blast furnace
        register(entries, RagiumMultiblockPatterns.BLAST_FURNACE) { tier: HTMachineTier ->
            addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addHollow(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addHollow(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addLayer(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            )
        }
        // distillation tower
        register(entries, RagiumMultiblockPatterns.DISTILLATION_TOWER) { tier: HTMachineTier ->
            addLayer(
                -1..1,
                -1,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            ).addHollow(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
            ).addCross4(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            ).add(
                0,
                4,
                2,
                HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
            )
        }
        // saw mill
        register(entries, RagiumMultiblockPatterns.SAW_MILL) { tier: HTMachineTier ->
            add(-1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
            add(1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
            add(-1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
            add(0, 0, 1, HTMultiblockComponent.of(Blocks.STONECUTTER))
            add(1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
            addLayer(-1..1, 0, 2..2, HTMultiblockComponent.of(tier.getHull()))
        }

        // alchemical infuser
        entries.add(
            RagiumMultiblockPatterns.ALCHEMICAL_INFUSER,
            HTMultiblockExporter.create {
                // tiles
                add(-2, -1, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                add(-2, -1, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                add(2, -1, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                add(2, -1, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILES))
                // slabs
                add(-2, 0, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                add(-2, 0, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                add(2, 0, -2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                add(2, 0, 2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                addLayer(-1..1, -1, -2..-2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                    .addLayer(-1..1, -1, 2..2, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                    .addLayer(-2..-2, -1, -1..1, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                    .addLayer(2..2, -1, -1..1, HTMultiblockComponent.of(Blocks.DEEPSLATE_TILE_SLAB))
                // obsidian
                add(-1, -1, -1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                add(-1, -1, 1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                add(1, -1, -1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                add(1, -1, 1, HTMultiblockComponent.of(Blocks.OBSIDIAN))
                // crying
                add(-1, -1, 0, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(0, -1, -1, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(0, -1, 0, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(0, -1, 1, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
                add(1, -1, 0, HTMultiblockComponent.of(Blocks.CRYING_OBSIDIAN))
            },
        )
    }

    private fun register(
        entries: Entries,
        keyMap: Map<HTMachineTier, RegistryKey<HTMultiblockPattern>>,
        builderAction: HTMultiblockExporter.(HTMachineTier) -> Unit,
    ) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            entries.add(
                keyMap[tier]!!,
                HTMultiblockExporter.create { builderAction(tier) },
            )
        }
    }
}

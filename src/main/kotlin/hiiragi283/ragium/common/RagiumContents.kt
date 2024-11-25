package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.block.HTPipeType
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

object RagiumContents {
    //    Ores    //

    enum class Ores(override val material: HTMaterialKey, val baseStone: Block) : HTContent.Material<Block> {
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE, Blocks.STONE) {
            override val delegated: HTContent<Block> =
                HTContent.ofBlock(RagiumAPI.id("raginite_ore"))
            override val tagPrefix: HTTagPrefix = HTTagPrefix.ORE
        },
        DEEP_RAGINITE(RagiumMaterialKeys.RAGINITE, Blocks.DEEPSLATE) {
            override val delegated: HTContent<Block> =
                HTContent.ofBlock(RagiumAPI.id("deepslate_raginite_ore"))
            override val tagPrefix: HTTagPrefix = HTTagPrefix.DEEP_ORE
        },
        NETHER_RAGINITE(RagiumMaterialKeys.RAGINITE, Blocks.NETHERRACK) {
            override val delegated: HTContent<Block> =
                HTContent.ofBlock(RagiumAPI.id("nether_raginite_ore"))
            override val tagPrefix: HTTagPrefix = HTTagPrefix.NETHER_ORE
        },
        END_RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL, Blocks.END_STONE) {
            override val delegated: HTContent<Block> =
                HTContent.ofBlock(RagiumAPI.id("end_ragi_crystal_ore"))
            override val tagPrefix: HTTagPrefix = HTTagPrefix.END_ORE
        },
        ;

        val dropMineral: ItemConvertible
            get() = when (this) {
                CRUDE_RAGINITE -> RawMaterials.CRUDE_RAGINITE
                DEEP_RAGINITE -> RawMaterials.RAGINITE
                NETHER_RAGINITE -> RawMaterials.RAGINITE
                END_RAGI_CRYSTAL -> Gems.RAGI_CRYSTAL
            }
    }

    //    Storage Blocks    //

    enum class StorageBlocks(override val material: HTMaterialKey) : HTContent.Material<Block> {
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        STEEL(RagiumMaterialKeys.STEEL),
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_block"))
        override val tagPrefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
    }

    //    Dusts    //

    enum class Dusts(override val material: HTMaterialKey) : HTContent.Material<Item> {
        // tier 1
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        ALKALI(RagiumMaterialKeys.ALKALI),
        ASH(RagiumMaterialKeys.ASH),
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        NITER(RagiumMaterialKeys.NITER),
        QUARTZ(RagiumMaterialKeys.QUARTZ),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        // tier 2
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        GOLD(RagiumMaterialKeys.GOLD),

        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id("${name.lowercase()}_dust"))
        override val tagPrefix: HTTagPrefix = HTTagPrefix.DUST
    }

    //    Gems    //

    enum class Gems(override val material: HTMaterialKey) : HTContent.Material<Item> {
        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        FLUORITE(RagiumMaterialKeys.FLUORITE),

        // tier 4
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id(name.lowercase()))
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEM
    }

    //    Ingots    //

    enum class Ingots(override val material: HTMaterialKey) : HTContent.Material<Item> {
        // tier 1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),

        // tier 2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier 3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id("${name.lowercase()}_ingot"))
        override val tagPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    //    Plates    //

    enum class Plates(override val material: HTMaterialKey) : HTContent.Material<Item> {
        // tier1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        IRON(RagiumMaterialKeys.IRON),
        COPPER(RagiumMaterialKeys.COPPER),
        WOOD(RagiumMaterialKeys.WOOD),

        // tier2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        GOLD(RagiumMaterialKeys.GOLD),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),

        // tier4
        NETHERITE(RagiumMaterialKeys.NETHERITE),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id("${name.lowercase()}_plate"))
        override val tagPrefix: HTTagPrefix = HTTagPrefix.PLATE
    }

    //    Raw Materials    //

    enum class RawMaterials(override val material: HTMaterialKey) : HTContent.Material<Item> {
        // tier 1
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        NITER(RagiumMaterialKeys.NITER),
        REDSTONE(RagiumMaterialKeys.REDSTONE),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        // tier2
        RAGINITE(RagiumMaterialKeys.RAGINITE),

        // tier 3
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id("raw_${name.lowercase()}"))
        override val tagPrefix: HTTagPrefix = HTTagPrefix.RAW_MATERIAL
    }

    //    Grates    //

    enum class Grates(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_grate"))
    }

    //    Casings    //

    enum class Casings(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_casing"))
    }

    //    Hulls    //

    enum class Hulls(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_hull"))
    }

    //    Coils    //

    enum class Coils(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_coil"))
    }

    //    Exporter    //

    enum class Exporters(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_exporter"))
    }

    //    Pipes    //

    enum class Pipes(override val tier: HTMachineTier, val pipeType: HTPipeType) : HTContent.Tier<Block> {
        IRON(HTMachineTier.PRIMITIVE, HTPipeType.ITEM),
        WOODEN(HTMachineTier.PRIMITIVE, HTPipeType.FLUID),
        STEEL(HTMachineTier.BASIC, HTPipeType.ITEM),
        COPPER(HTMachineTier.BASIC, HTPipeType.FLUID),
        UNIVERSAL(HTMachineTier.ADVANCED, HTPipeType.ALL),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_pipe"))
    }

    //    Drums    //

    enum class Drums(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_drum"))
    }

    //    Circuits    //

    enum class CircuitBoards(override val tier: HTMachineTier) : HTContent.Tier<Item> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id("${name.lowercase()}_circuit_board"))

        fun getCircuit(): Circuits = when (this) {
            PRIMITIVE -> Circuits.PRIMITIVE
            BASIC -> Circuits.BASIC
            ADVANCED -> Circuits.ADVANCED
        }
    }

    enum class Circuits(override val tier: HTMachineTier) : HTContent.Tier<Item> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id("${name.lowercase()}_circuit"))
    }
}

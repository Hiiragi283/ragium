package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

object RagiumContents {
    //    Ores    //

    enum class Ores(
        path: String,
        override val tagPrefix: HTTagPrefix,
        override val material: HTMaterialKey,
        val baseStone: Block,
    ) : HTContent.Material<Block> {
        CRUDE_RAGINITE("raginite_ore", HTTagPrefix.ORE, RagiumMaterialKeys.CRUDE_RAGINITE, Blocks.STONE),
        DEEP_RAGINITE("deepslate_raginite_ore", HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.RAGINITE, Blocks.DEEPSLATE),
        NETHER_RAGINITE("nether_raginite_ore", HTTagPrefix.NETHER_ORE, RagiumMaterialKeys.RAGINITE, Blocks.NETHERRACK),
        END_RAGI_CRYSTAL("end_ragi_crystal_ore", HTTagPrefix.END_ORE, RagiumMaterialKeys.RAGI_CRYSTAL, Blocks.END_STONE),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id(path))

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
        LAPIS(RagiumMaterialKeys.LAPIS),
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
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),
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
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        LAPIS(RagiumMaterialKeys.LAPIS),
        WOOD(RagiumMaterialKeys.WOOD),

        // tier2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        GOLD(RagiumMaterialKeys.GOLD),
        QUARTZ(RagiumMaterialKeys.QUARTZ),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),

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
        STONE(HTMachineTier.PRIMITIVE, HTPipeType.ITEM),
        WOODEN(HTMachineTier.PRIMITIVE, HTPipeType.FLUID),
        IRON(HTMachineTier.BASIC, HTPipeType.ITEM),
        COPPER(HTMachineTier.BASIC, HTPipeType.FLUID),
        UNIVERSAL(HTMachineTier.ADVANCED, HTPipeType.ALL),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_pipe"))
    }

    enum class CrossPipes(val pipeType: HTPipeType) :
        HTContent.Delegated<Block>,
        ItemConvertible {
        STEEL(HTPipeType.ITEM),
        GOLD(HTPipeType.FLUID),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_pipe"))

        override fun asItem(): Item = value.asItem()
    }

    enum class PipeStations(val pipeType: HTPipeType) :
        HTContent.Delegated<Block>,
        ItemConvertible {
        ITEM(HTPipeType.ITEM),
        FLUID(HTPipeType.FLUID),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_pipe_station"))

        override fun asItem(): Item = value.asItem()
    }

    enum class FilteringPipe(val pipeType: HTPipeType) :
        HTContent.Delegated<Block>,
        ItemConvertible {
        ITEM(HTPipeType.ITEM),
        FLUID(HTPipeType.FLUID),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_filtering_pipe"))

        override fun asItem(): Item = value.asItem()
    }

    //    Crate    //

    enum class Crates(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Block> =
            HTContent.ofBlock(RagiumAPI.id("${name.lowercase()}_crate"))
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

    enum class PressMold(val prefix: HTTagPrefix?) :
        HTContent.Delegated<Item>,
        ItemConvertible {
        GEAR(HTTagPrefix.GEAR),
        PIPE(null),
        PLATE(HTTagPrefix.PLATE),
        ROD(HTTagPrefix.ROD),
        ;

        override val delegated: HTContent<Item> =
            HTContent.ofItem(RagiumAPI.id("${name.lowercase()}_press_mold"))

        override fun asItem(): Item = value
    }
}

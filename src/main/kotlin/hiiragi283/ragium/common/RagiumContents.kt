package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.block.HTPipeType
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object RagiumContents : HTContentRegister {
    //    Ores    //

    enum class Ores(override val material: HTMaterialKey, val baseStone: Block) : HTContent.Material<Block> {
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE, Blocks.STONE) {
            override val key: RegistryKey<Block> = RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("raginite_ore"))
            override val enPattern: String = "Raginite Ore"
            override val jaPattern: String = "ラギナイト鉱石"
        },
        DEEP_RAGINITE(RagiumMaterialKeys.RAGINITE, Blocks.DEEPSLATE) {
            override val key: RegistryKey<Block> =
                RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("deepslate_raginite_ore"))
            override val enPattern: String = "Deep Raginite Ore"
            override val jaPattern: String = "深層ラギナイト鉱石"
        },
        NETHER_RAGINITE(RagiumMaterialKeys.RAGINITE, Blocks.NETHERRACK) {
            override val key: RegistryKey<Block> =
                RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("nether_raginite_ore"))
            override val enPattern: String = "Nether Raginite Ore"
            override val jaPattern: String = "ネザーラギナイト鉱石"
        },
        END_RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL, Blocks.END_STONE) {
            override val key: RegistryKey<Block> =
                RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("end_ragi_crystal_ore"))
            override val enPattern: String = "End Ragi-Crystal Ore"
            override val jaPattern: String = "エンドラギクリスタリル鉱石"
        }, ;

        val dropMineral: ItemConvertible
            get() = when (this) {
                CRUDE_RAGINITE -> RawMaterials.CRUDE_RAGINITE
                DEEP_RAGINITE -> RawMaterials.RAGINITE
                NETHER_RAGINITE -> RawMaterials.RAGINITE
                END_RAGI_CRYSTAL -> Gems.RAGI_CRYSTAL
            }

        override val registry: Registry<Block> = Registries.BLOCK
        override val tagPrefix: HTTagPrefix = HTTagPrefix.ORE
    }

    //    Storage Blocks    //

    enum class StorageBlocks(override val material: HTMaterialKey) : HTContent.Material<Block> {
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        STEEL(RagiumMaterialKeys.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        ;

        override val registry: Registry<Block> = Registries.BLOCK
        override val key: RegistryKey<Block> =
            RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("${name.lowercase()}_block"))
        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
        override val tagPrefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
    }

    //    Dusts    //

    enum class Dusts(override val material: HTMaterialKey) : HTContent.Material<Item> {
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),

        ASH(RagiumMaterialKeys.ASH),
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        COPPER(RagiumMaterialKeys.COPPER),
        GOLD(RagiumMaterialKeys.GOLD),
        IRON(RagiumMaterialKeys.IRON),
        NITER(RagiumMaterialKeys.NITER),
        SULFUR(RagiumMaterialKeys.SULFUR),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_dust"))
        override val enPattern: String = "%s Dust"
        override val jaPattern: String = "%sの粉"
        override val tagPrefix: HTTagPrefix = HTTagPrefix.DUST
    }

    //    Gems    //

    enum class Gems(override val material: HTMaterialKey) : HTContent.Material<Item> {
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id(name.lowercase()))
        override val enPattern: String = "%s"
        override val jaPattern: String = "%s"
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEM
    }

    //    Ingots    //

    enum class Ingots(override val material: HTMaterialKey) : HTContent.Material<Item> {
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        STEEL(RagiumMaterialKeys.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_ingot"))
        override val enPattern: String = "%s Ingot"
        override val jaPattern: String = "%sインゴット"
        override val tagPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    //    Plates    //

    enum class Plates(override val material: HTMaterialKey) : HTContent.Material<Item> {
        // tier1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        IRON(RagiumMaterialKeys.IRON),
        COPPER(RagiumMaterialKeys.COPPER),

        // tier2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        GOLD(RagiumMaterialKeys.GOLD),
        PLASTIC(RagiumMaterialKeys.PLASTIC),
        SILICON(RagiumMaterialKeys.SILICON),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        ENGINEERING_PLASTIC(RagiumMaterialKeys.ENGINEERING_PLASTIC),
        STELLA(RagiumMaterialKeys.STELLA),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_plate"))
        override val enPattern: String = "%s Plate"
        override val jaPattern: String = "%s板"
        override val tagPrefix: HTTagPrefix = HTTagPrefix.PLATE
    }

    //    Raw Materials    //

    enum class RawMaterials(override val material: HTMaterialKey) : HTContent.Material<Item> {
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("raw_${name.lowercase()}"))
        override val enPattern: String = "Raw %s"
        override val jaPattern: String = "%sの原石"
        override val tagPrefix: HTTagPrefix = HTTagPrefix.RAW_MATERIAL
    }

    //    Hulls    //

    enum class Hulls(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val registry: Registry<Block> = Registries.BLOCK
        override val key: RegistryKey<Block> =
            RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("${name.lowercase()}_hull"))
        override val enPattern: String = "%s Hull"
        override val jaPattern: String = "%s筐体"
    }

    //    Coils    //

    enum class Coils(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val registry: Registry<Block> = Registries.BLOCK
        override val key: RegistryKey<Block> =
            RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("${name.lowercase()}_coil"))
        override val enPattern: String = "%s Coil"
        override val jaPattern: String = "%sコイル"
    }

    //    Exporter    //

    enum class Exporters(override val tier: HTMachineTier) : HTContent.Tier<Block> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val registry: Registry<Block> = Registries.BLOCK
        override val key: RegistryKey<Block> =
            RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("${name.lowercase()}_exporter"))
        override val enPattern: String = "%s Exporter"
        override val jaPattern: String = "%s搬出機"
    }

    //    Pipes    //

    enum class Pipes(override val tier: HTMachineTier, val pipeType: HTPipeType) : HTContent.Tier<Block> {
        IRON(HTMachineTier.PRIMITIVE, HTPipeType.ITEM),
        WOODEN(HTMachineTier.PRIMITIVE, HTPipeType.FLUID),
        STEEL(HTMachineTier.BASIC, HTPipeType.ITEM),
        COPPER(HTMachineTier.BASIC, HTPipeType.FLUID),
        UNIVERSAL(HTMachineTier.ADVANCED, HTPipeType.ALL),
        ;

        override val registry: Registry<Block> = Registries.BLOCK
        override val key: RegistryKey<Block> =
            RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("${name.lowercase()}_pipe"))
        override val enPattern: String = "%s Pipe"
        override val jaPattern: String = "%sパイプ"
    }

    //    Circuits    //

    enum class CircuitBoards(override val tier: HTMachineTier) : HTContent.Tier<Item> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_circuit_board"))
        override val enPattern: String = "%s Circuit Board"
        override val jaPattern: String = "%s回路基板"
    }

    enum class Circuits(override val tier: HTMachineTier) : HTContent.Tier<Item> {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_circuit"))
        override val enPattern: String = "%s Circuit"
        override val jaPattern: String = "%s回路"
    }
}

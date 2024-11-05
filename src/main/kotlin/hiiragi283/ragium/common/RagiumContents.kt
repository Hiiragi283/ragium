package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.*
import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.HTTagPrefixes
import hiiragi283.ragium.common.block.HTPipeType
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ToolMaterial
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey

object RagiumContents : HTContentRegister {
    //    Ores    //

    enum class Ores(override val material: RagiumMaterials, val baseStone: Block) : HTContent.Material<Block> {
        CRUDE_RAGINITE(RagiumMaterials.CRUDE_RAGINITE, Blocks.STONE) {
            override val key: RegistryKey<Block> = RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("raginite_ore"))
            override val enPattern: String = "Raginite Ore"
            override val jaPattern: String = "ラギナイト鉱石"
        },
        DEEP_RAGINITE(RagiumMaterials.RAGINITE, Blocks.DEEPSLATE) {
            override val key: RegistryKey<Block> =
                RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("deepslate_raginite_ore"))
            override val enPattern: String = "Deep Raginite Ore"
            override val jaPattern: String = "深層ラギナイト鉱石"
        },
        NETHER_RAGINITE(RagiumMaterials.RAGINITE, Blocks.NETHERRACK) {
            override val key: RegistryKey<Block> =
                RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("nether_raginite_ore"))
            override val enPattern: String = "Nether Raginite Ore"
            override val jaPattern: String = "ネザーラギナイト鉱石"
        },
        END_RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL, Blocks.END_STONE) {
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
        override val tagPrefix: HTTagPrefix = HTTagPrefixes.ORES
    }

    //    Storage Blocks    //

    enum class StorageBlocks(override val material: RagiumMaterials) : HTContent.Material<Block> {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        ALUMINUM(RagiumMaterials.ALUMINUM),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val registry: Registry<Block> = Registries.BLOCK
        override val key: RegistryKey<Block> =
            RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id("${name.lowercase()}_block"))
        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
        override val tagPrefix: HTTagPrefix = HTTagPrefixes.STORAGE_BLOCKS
    }

    //    Dusts    //

    enum class Dusts(override val material: RagiumMaterials) : HTContent.Material<Item> {
        CRUDE_RAGINITE(RagiumMaterials.CRUDE_RAGINITE),
        RAGINITE(RagiumMaterials.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL),

        ASH(RagiumMaterials.ASH),
        BAUXITE(RagiumMaterials.BAUXITE),
        COPPER(RagiumMaterials.COPPER),
        GOLD(RagiumMaterials.GOLD),
        IRON(RagiumMaterials.IRON),
        NITER(RagiumMaterials.NITER),
        SULFUR(RagiumMaterials.SULFUR),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_dust"))
        override val enPattern: String = "%s Dust"
        override val jaPattern: String = "%sの粉"
        override val tagPrefix: HTTagPrefix = HTTagPrefixes.DUSTS
    }

    //    Gems    //

    enum class Gems(override val material: RagiumMaterials) : HTContent.Material<Item> {
        FLUORITE(RagiumMaterials.FLUORITE),
        RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL),
        RAGIUM(RagiumMaterials.RAGIUM),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id(name.lowercase()))
        override val enPattern: String = "%s"
        override val jaPattern: String = "%s"
        override val tagPrefix: HTTagPrefix = HTTagPrefixes.GEMS
    }

    //    Ingots    //

    enum class Ingots(override val material: RagiumMaterials) : HTContent.Material<Item> {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        ALUMINUM(RagiumMaterials.ALUMINUM),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_ingot"))
        override val enPattern: String = "%s Ingot"
        override val jaPattern: String = "%sインゴット"
        override val tagPrefix: HTTagPrefix = HTTagPrefixes.INGOTS
    }

    //    Plates    //

    enum class Plates(override val material: RagiumMaterials) : HTContent.Material<Item> {
        // tier1
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        IRON(RagiumMaterials.IRON),
        COPPER(RagiumMaterials.COPPER),

        // tier2
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        ALUMINUM(RagiumMaterials.ALUMINUM),
        GOLD(RagiumMaterials.GOLD),
        PLASTIC(RagiumMaterials.PLASTIC),
        SILICON(RagiumMaterials.SILICON),
        STEEL(RagiumMaterials.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ENGINEERING_PLASTIC(RagiumMaterials.ENGINEERING_PLASTIC),
        STELLA(RagiumMaterials.STELLA),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${name.lowercase()}_plate"))
        override val enPattern: String = "%s Plate"
        override val jaPattern: String = "%s板"
        override val tagPrefix: HTTagPrefix = HTTagPrefixes.PLATES
    }

    //    Raw Materials    //

    enum class RawMaterials(override val material: RagiumMaterials) : HTContent.Material<Item> {
        BAUXITE(RagiumMaterials.BAUXITE),
        CRUDE_RAGINITE(RagiumMaterials.CRUDE_RAGINITE),
        RAGINITE(RagiumMaterials.RAGINITE),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("raw_${name.lowercase()}"))
        override val enPattern: String = "Raw %s"
        override val jaPattern: String = "%sの原石"
        override val tagPrefix: HTTagPrefix = HTTagPrefixes.RAW_MATERIALS
    }

    //    Armors    //

    enum class Armors(val material: RegistryEntry<ArmorMaterial>, val armorType: HTArmorType, val multiplier: Int) :
        HTContent<Item> {
        STEEL_HELMET(RagiumMaterials.Armor.STEEL, HTArmorType.HELMET, 25),
        STEEL_CHESTPLATE(RagiumMaterials.Armor.STEEL, HTArmorType.CHESTPLATE, 25),
        STEEL_LEGGINGS(RagiumMaterials.Armor.STEEL, HTArmorType.LEGGINGS, 25),
        STEEL_BOOTS(RagiumMaterials.Armor.STEEL, HTArmorType.BOOTS, 25),
        STELLA_GOGGLE(RagiumMaterials.Armor.STELLA, HTArmorType.HELMET, 33),
        STELLA_JACKET(RagiumMaterials.Armor.STELLA, HTArmorType.CHESTPLATE, 33),
        STELLA_LEGGINGS(RagiumMaterials.Armor.STELLA, HTArmorType.LEGGINGS, 33),
        STELLA_BOOTS(RagiumMaterials.Armor.STELLA, HTArmorType.BOOTS, 33),
        RAGIUM_HELMET(RagiumMaterials.Armor.RAGIUM, HTArmorType.HELMET, 37),
        RAGIUM_CHESTPLATE(RagiumMaterials.Armor.RAGIUM, HTArmorType.CHESTPLATE, 37),
        RAGIUM_LEGGINGS(RagiumMaterials.Armor.RAGIUM, HTArmorType.LEGGINGS, 37),
        RAGIUM_BOOTS(RagiumMaterials.Armor.RAGIUM, HTArmorType.BOOTS, 37),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id(name.lowercase()))

        override fun getTranslation(type: HTLangType): String = throw UnsupportedOperationException()

        override val commonTagKey: TagKey<Item> = armorType.armorTag
    }

    //    Tools    //

    enum class Tools(val material: ToolMaterial, val toolType: HTToolType) :
        HTContent<Item>,
        HTTranslationFormatter by toolType {
        STEEL_AXE(RagiumMaterials.Tool.STEEL, HTToolType.AXE),
        STEEL_HOE(RagiumMaterials.Tool.STEEL, HTToolType.HOE),
        STEEL_PICKAXE(RagiumMaterials.Tool.STEEL, HTToolType.PICKAXE),
        STEEL_SHOVEL(RagiumMaterials.Tool.STEEL, HTToolType.SHOVEL),
        STEEL_SWORD(RagiumMaterials.Tool.STEEL, HTToolType.SWORD),
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id(name.lowercase()))

        override fun getTranslation(type: HTLangType): String = throw UnsupportedOperationException()

        override val commonTagKey: TagKey<Item> = toolType.toolTag
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

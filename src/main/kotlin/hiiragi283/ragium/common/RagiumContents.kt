package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.*
import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.HTTagPrefix
import hiiragi283.ragium.api.tags.HTTagPrefixes
import hiiragi283.ragium.common.block.HTPipeType
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.fluid.Fluid
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
import net.minecraft.util.Util
import java.awt.Color

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

    //    Foods    //

    enum class Foods : HTContent<Item> {
        BEE_WAX {
            override val commonTagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        BUTTER,
        CARAMEL,
        CHOCOLATE,
        CHOCOLATE_APPLE,
        CHOCOLATE_BREAD,
        FLOUR {
            override val commonTagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        DOUGH,
        MINCED_MEAT {
            override val commonTagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        PULP {
            override val commonTagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id(name.lowercase()))

        override fun getTranslation(type: HTLangType): String = throw UnsupportedOperationException()
    }

    //    Misc    //

    enum class Misc : HTContent<Item> {
        BACKPACK,
        BASALT_MESH,
        CRAFTER_HAMMER,
        DYNAMITE,
        EMPTY_FLUID_CUBE,
        FILLED_FLUID_CUBE,
        ENGINE,
        FORGE_HAMMER,
        HEART_OF_THE_NETHER,
        POLYMER_RESIN,
        PROCESSOR_SOCKET,
        RAGI_ALLOY_COMPOUND,
        RAGI_CRYSTAL_PROCESSOR,
        REMOVER_DYNAMITE,
        SOAP_INGOT,
        SOLAR_PANEL,
        TRADER_CATALOG,
        ;

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> =
            RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id(name.lowercase()))

        override fun getTranslation(type: HTLangType): String = throw UnsupportedOperationException()
    }

    //    Fluids    //

    enum class Fluids(val color: Color, override val enName: String, override val jaName: String) :
        HTRegistryContent<Fluid>,
        HTTranslationProvider {
        // Vanilla
        MILK(Color(0xffffff), "Milk", "牛乳"),
        HONEY(Color(0xffcc33), "Honey", "蜂蜜"),

        // Molten Materials

        // Organics
        TALLOW(Color(0xcc9933), "Tallow", "獣脂"),
        SEED_OIL(Color(0x99cc33), "Seed Oil", "種油"),
        GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),

        // Foods
        BATTER(Color(0xffcc66), "Batter", "バッター液"),
        CHOCOLATE(Color(0x663300), "Chocolate", "チョコレート"),
        STARCH_SYRUP(Color(0x99ffff), "Starch Syrup", "水あめ"),
        SWEET_BERRIES(Color(0x990000), "Sweet Berries", "スイートベリー"),

        // Natural Resources
        SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
        CRUDE_OIL(Color(0x000000), "Crude Oil", "原油"),

        // Elements
        HYDROGEN(Color(0x0000cc), "Hydrogen", "水素"),
        NITROGEN(Color(0x66cccc), "Nitrogen", "窒素"),
        OXYGEN(Color(0x99ccff), "Oxygen", "酸素"),
        CHLORINE(Color(0xccff33), "Chlorine", "塩素"),

        // Non-organic Chemical Compounds
        NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
        SODIUM_HYDROXIDE(Color(0x000099), "Sodium Hydroxide Solution", "水酸化ナトリウム水溶液"),
        SULFURIC_ACID(Color(0xff3300), "Sulfuric Acid", "硫酸"),
        MIXTURE_ACID(Color(0xff3300), "Mixture Acid", "混酸"),
        ALUMINA_SOLUTION(Color(0xcccccc), "Alumina Solution", "アルミナ溶液"),

        // Oil products
        REFINED_GAS(Color(0xcccccc), "Refined Gas", "精製ガス"),
        NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
        RESIDUAL_OIL(Color(0x000033), "Residual Oil", "残渣油"),

        // METHANE(Color(0xcc0099), "Methane", "メタン"),
        // METHANOL(Color(0xcc00ff), "Methanol", "メタノール"),
        // LPG(Color(0xffff33), "LPG", "LGP"),
        // ETHYLENE(Color(0x999999), "Ethylene", "エチレン"),
        ALCOHOL(Color(0xccffff), "Alcohol", "アルコール"),

        AROMATIC_COMPOUNDS(Color(0x666699), "Aromatic Compounds", "芳香族化合物"),

        // LUBRICANT(Color(0x996633), "Lubricant", "潤滑油"),
        ASPHALT(Color(0x000066), "Asphalt", "アスファルト"),

        // Fuels
        BIO_FUEL(Color(0x99ff00), "Bio Fuel", "バイオ燃料"),
        FUEL(Color(0xcc6633), "Fuel", "燃料"),
        NITRO_FUEL(Color(0xff33333), "Nitro Fuel", "ニトロ燃料"),

        // Explodes
        NITRO_GLYCERIN(Color(0x99cc66), "Nitroglycerin", "ニトログリセリン"),
        TRINITROTOLUENE(Color(0x666699), "Trinitrotoluene", "トリニトロトルエン"),
        ;

        override val registry: Registry<Fluid> = Registries.FLUID
        override val key: RegistryKey<Fluid> = RegistryKey.of(RegistryKeys.FLUID, RagiumAPI.id(name.lowercase()))

        val translationKey: String = Util.createTranslationKey("fluid", id)
    }
}

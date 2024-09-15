package hiiragi283.ragium.common.init

import hiiragi283.ragium.client.data.RagiumModels
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.item.HTForgeHammerItem
import hiiragi283.ragium.common.recipe.HTMachineTier
import hiiragi283.ragium.common.registry.HTItemRegister
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.item.Item
import net.minecraft.util.Identifier

object RagiumItems {
    @JvmField
    val REGISTER: HTItemRegister = HTItemRegister(Ragium.MOD_ID).apply(::registerBlockItems)

    @JvmField
    val POWER_METER: Item =
        REGISTER.registerSimple(
            "power_meter",
            Item.Settings().component(RagiumComponentTypes.DISABLE_CYCLE_POWER, Unit),
        ) {
            putEnglishLang("Power Meter")
            putJapaneseLang("パワー測定器")
        }

    @JvmField
    val FORGE_HAMMER: Item =
        REGISTER.register("forge_hammer", HTForgeHammerItem) {
            putEnglishLang("Forge Hammer")
            putJapaneseLang("鍛造ハンマー")
        }

    @JvmField
    val PLATE_SHAPE: Item =
        REGISTER.registerSimple("plate_shape") {
            putEnglishLang("Plate Shape")
            putJapaneseLang("板の型枠")
        }

    @JvmField
    val ROD_SHAPE: Item =
        REGISTER.registerSimple("rod_shape") {
            putEnglishLang("Rod Shape")
            putJapaneseLang("棒の型枠")
        }

    // tier1
    @JvmField
    val RAW_RAGINITE: Item =
        REGISTER.registerSimple("raw_raginite") {
            putEnglishLang("Raw Raginite")
            putJapaneseLang("ラギナイトの原石")
        }

    @JvmField
    val RAW_RAGINITE_DUST: Item =
        REGISTER.registerSimple("raw_raginite_dust") {
            putEnglishLang("Raw Raginite Dust")
            putJapaneseLang("ラギナイトの原石の粉")
        }

    @JvmField
    val RAGINITE_DUST: Item =
        REGISTER.registerSimple("raginite_dust") {
            putEnglishLang("Raginite Dust")
            putJapaneseLang("ラギナイトの粉")
        }

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item =
        REGISTER.registerSimple("ragi_alloy_compound") {
            putEnglishLang("Ragi-Alloy Compound")
            putJapaneseLang("ラギ合金混合物")
        }

    @JvmField
    val RAGI_ALLOY_INGOT: Item =
        REGISTER.registerSimple("ragi_alloy_ingot") {
            putEnglishLang("Ragi-Alloy Ingot")
            putJapaneseLang("ラギ合金インゴット")
        }

    @JvmField
    val RAGI_ALLOY_PLATE: Item =
        REGISTER.registerSimple("ragi_alloy_plate") {
            putEnglishLang("Ragi-Alloy Plate")
            putJapaneseLang("ラギ合金の板")
        }

    @JvmField
    val RAGI_ALLOY_ROD: Item =
        REGISTER.registerSimple("ragi_alloy_rod") {
            putEnglishLang("Ragi-Alloy Rod")
            putJapaneseLang("ラギ合金の棒")
        }

    // tier2
    @JvmField
    val RAGI_STEEL_INGOT: Item =
        REGISTER.registerSimple("ragi_steel_ingot") {
            putEnglishLang("Ragi-Steel Ingot")
            putJapaneseLang("ラギスチールインゴット")
        }

    @JvmField
    val RAGI_STEEL_PLATE: Item =
        REGISTER.registerSimple("ragi_steel_plate") {
            putEnglishLang("Ragi-Steel Plate")
            putJapaneseLang("ラギスチールの板")
        }

    // tier3
    @JvmField
    val REFINED_RAGI_STEEL_INGOT: Item =
        REGISTER.registerSimple("refined_ragi_steel_ingot") {
            putEnglishLang("Refined Ragi-Steel Ingot")
            putJapaneseLang("精製ラギスチールインゴット")
        }

    @JvmField
    val REFINED_RAGI_STEEL_PLATE: Item =
        REGISTER.registerSimple("refined_ragi_steel_plate") {
            putEnglishLang("Refined Ragi-Steel Plate")
            putJapaneseLang("精製ラギスチールの板")
        }

    // tier4

    // tier5

    // fluid cubes
    @JvmField
    val OIL_CUBE: Item =
        REGISTER.registerSimple("oil_cube") {
            putEnglishLang("Fluid Cube (Oil)")
            putJapaneseLang("液体キューブ（石油）")
            generateModel {
                RagiumModels.FLUID_CUBE.upload(
                    ModelIds.getItemModelId(item),
                    TextureMap()
                        .put(TextureKey.ALL, Identifier.of("block/glass"))
                        .put(TextureKey.INSIDE, Identifier.of("block/white_concrete")),
                    it.writer,
                )
            }
        }

    @JvmStatic
    private fun registerBlockItems(register: HTItemRegister) {
        register.registerBlockItem("raginite_ore", RagiumBlocks.RAGINITE_ORE)
        register.registerBlockItem("deepslate_raginite_ore", RagiumBlocks.DEEPSLATE_RAGINITE_ORE)
        register.registerBlockItem("creative_source", RagiumBlocks.CREATIVE_SOURCE)
        // tier1
        register.registerBlockItem(
            "ragi_alloy_block",
            RagiumBlocks.RAGI_ALLOY_BLOCK,
            Item.Settings().component(RagiumComponentTypes.TIER, HTMachineTier.HEAT),
        )
        register.registerBlockItem(
            "ragi_alloy_hull",
            RagiumBlocks.RAGI_ALLOY_HULL,
            Item.Settings().component(RagiumComponentTypes.TIER, HTMachineTier.HEAT),
        )
        register.registerBlockItem("manual_grinder", RagiumBlocks.MANUAL_GRINDER)
        register.registerBlockItem("water_collector", RagiumBlocks.WATER_COLLECTOR)
        register.registerBlockItem("burning_box", RagiumBlocks.BURNING_BOX)
        // tier2
        register.registerBlockItem(
            "ragi_steel_block",
            RagiumBlocks.RAGI_STEEL_BLOCK,
            Item.Settings().component(RagiumComponentTypes.TIER, HTMachineTier.KINETIC),
        )
        register.registerBlockItem(
            "ragi_steel_hull",
            RagiumBlocks.RAGI_STEEL_HULL,
            Item.Settings().component(RagiumComponentTypes.TIER, HTMachineTier.KINETIC),
        )
        // tier3
        register.registerBlockItem(
            "refined_ragi_steel_block",
            RagiumBlocks.REFINED_RAGI_STEEL_BLOCK,
            Item.Settings().component(RagiumComponentTypes.TIER, HTMachineTier.ELECTRIC),
        )
        register.registerBlockItem(
            "refined_ragi_steel_hull",
            RagiumBlocks.REFINED_RAGI_STEEL_HULL,
            Item.Settings().component(RagiumComponentTypes.TIER, HTMachineTier.ELECTRIC),
        )
        // tier4
        // tier5
    }

    /*private fun <T : Item> register(name: String, item: T): T =
        Registry.register(Registries.ITEM, Ragium.id(name), item)

    private fun register(name: String, builder: Item.Settings.() -> Unit = {}): Item =
        register(name, Item(Item.Settings().apply(builder)))

    private fun register(block: Block, builder: Item.Settings.() -> Unit = {}): Item =
        Registries.BLOCK.getKey(block).map {
            Registry.register(Registries.ITEM, it.value, BlockItem(block, Item.Settings().apply(builder)))
        }.orElseThrow()*/
}

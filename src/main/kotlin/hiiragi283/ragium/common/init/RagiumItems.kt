package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.item.HTBackpackItem
import hiiragi283.ragium.common.item.HTEnderBundleItem
import hiiragi283.ragium.common.item.HTFluidCubeItem
import hiiragi283.ragium.common.item.HTForgeHammerItem
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.registry.HTItemRegister
import hiiragi283.ragium.common.util.*
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.TextureMap
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Rarity
import java.awt.Color

object RagiumItems {
    @JvmField
    val REGISTER: HTItemRegister = HTItemRegister(Ragium.MOD_ID)
        .apply(::initBlockItems)
        .apply(::initMachines)
        .apply(::initDusts)
        .apply(::initIngots)
        .apply(::initPlates)
        .apply(::initElements)
        .apply(::initFluids)

    //    Tools    //

    @JvmField
    val FORGE_HAMMER: Item =
        REGISTER.register("forge_hammer", HTForgeHammerItem) {
            putEnglish("Forge Hammer")
            putEnglishTips("Used to make Ingot to Plate")
            putJapanese("鍛造ハンマー")
            putJapaneseTips("インゴットを板に加工するための道具")
        }

    @JvmField
    val BACKPACK: Item =
        REGISTER.register("backpack", HTBackpackItem.NORMAL) {
            putEnglish("Backpack")
            putEnglishTips("Same size as Chest")
            putJapanese("バックパック")
            putJapaneseTips("チェストと同じ容量")
        }

    @JvmField
    val LARGE_BACKPACK: Item =
        REGISTER.register("large_backpack", HTBackpackItem.LARGE) {
            putEnglish("Large Backpack")
            putEnglishTips("Same size as Large Chest")
            putJapanese("大型パックパック")
            putJapaneseTips("ラージチェストと同じ容量")
        }

    @JvmField
    val ENDER_BUNDLE: Item =
        REGISTER.register("ender_bundle", HTEnderBundleItem) {
            putEnglish("Ender Bundle")
            putEnglishTips("Open your Ender Chest anywhere")
            putJapanese("エンダーバンドル")
            putJapaneseTips("どこでもエンダーチェスト")
        }

    @JvmField
    val STEEL_SWORD: Item =
        REGISTER.registerSword(
            "steel_sword",
            RagiumToolMaterials.STEEL,
        ) {
            putEnglish("Steel Sword")
            putJapanese("鋼鉄の剣")
            registerTags(ItemTags.SWORDS)
        }

    @JvmField
    val STEEL_SHOVEL: Item =
        REGISTER.registerShovel(
            "steel_shovel",
            RagiumToolMaterials.STEEL,
        ) {
            putEnglish("Steel Shovel")
            putJapanese("鋼鉄のショベル")
            registerTags(ItemTags.SHOVELS)
        }

    @JvmField
    val STEEL_PICKAXE: Item =
        REGISTER.registerPickaxe(
            "steel_pickaxe",
            RagiumToolMaterials.STEEL,
        ) {
            putEnglish("Steel Pickaxe")
            putJapanese("鋼鉄のツルハシ")
            registerTags(ItemTags.PICKAXES)
        }

    @JvmField
    val STEEL_AXE: Item =
        REGISTER.registerAxe(
            "steel_axe",
            RagiumToolMaterials.STEEL,
        ) {
            putEnglish("Steel Axe")
            putJapanese("鋼鉄の斧")
            registerTags(ItemTags.AXES)
        }

    @JvmField
    val STEEL_HOE: Item =
        REGISTER.registerHoe(
            "steel_hoe",
            RagiumToolMaterials.STEEL,
        ) {
            putEnglish("Steel Hoe")
            putJapanese("鋼鉄のクワ")
            registerTags(ItemTags.HOES)
        }

    //    Ingredients    //

    @JvmField
    val RAW_RAGINITE: Item =
        REGISTER.registerSimple("raw_raginite") {
            putEnglish("Raw Raginite")
            putEnglishTips("Contains fine elements...")
            putJapanese("ラギナイトの原石")
            putJapaneseTips("5種類の元素を含んでいる...")
        }

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item =
        REGISTER.registerSimple("ragi_alloy_compound") {
            putEnglish("Ragi-Alloy Compound")
            putEnglishTips("Just an intermediate ingredient")
            putJapanese("ラギ合金混合物")
            putJapaneseTips("ただの中間素材")
        }

    @JvmField
    val EMPTY_FLUID_CUBE: Item =
        REGISTER.registerSimple("empty_fluid_cube") {
            putEnglish("Fluid Cube (Empty)")
            putEnglishTips("Holdable fluid for 1 Bucket")
            putJapanese("液体キューブ（なし）")
            putJapaneseTips("1バケツ分の液体を保持できる")
            setCustomModel()
        }

    @JvmField
    val SOAP_INGOT: Item =
        REGISTER.registerSimple("soap_ingot") {
            putEnglish("Soap Ingot")
            putEnglishTips("How about a Soap Sandwich?")
            putJapanese("石鹸インゴット")
            putJapaneseTips("石鹸サンドイッチはいかが？")
        }

    // tier2
    @JvmField
    val TALLOW_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("tallow", "Tallow", "獣脂", Color(0xcc9933))

    @JvmField
    val SEED_OIL_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("seed_oil", "Seed Oil", "種油", Color(0x99cc33))

    @JvmField
    val GLYCEROL_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("glycerol", "Glycerol", "グリセロール", Color(0x99cc66))

    // tier3
    @JvmField
    val HYDROGEN_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("hydrogen", "Hydrogen", "水素", Color(0x0000cc))

    @JvmField
    val NITROGEN_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("nitrogen", "Nitrogen", "窒素", Color(0x66cccc))

    @JvmField
    val OXYGEN_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("oxygen", "Oxygen", "酸素", Color(0x99ccff))

    @JvmField
    val FLUORINE_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("fluorine", "Chlorine", "塩素", Color(0x66cc99))

    @JvmField
    val CHLORINE_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("chlorine", "Chlorine", "塩素", Color(0xccff33))

    @JvmField
    val REFINED_GAS_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("refined_gas", "Refined Gas", "精製ガス", Color(0xcccccc))

    @JvmField
    val NAPHTHA_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("naphtha", "Naphtha", "ナフサ", Color(0xff9900))

    @JvmField
    val TAR_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("tar", "Tar", "タール", Color(0x000033))

    @JvmField
    val METHANE_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("methane", "Methane", "メタン", Color(0xcc0099))

    @JvmField
    val METHANOL_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("methanol", "Methanol", "メタノール", Color(0xcc00ff))

    @JvmField
    val LPG_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("lpg", "LPG", "LGP", Color(0xffff33))

    @JvmField
    val HELIUM_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("helium", "Helium", "ヘリウム", Color(0xffff99))

    @JvmField
    val ETHYLENE_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("ethylene", "Ethylene", "エチレン", Color(0x999999))

    @JvmField
    val VC_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("vinyl_chloride", "Vinyl Chloride", "塩化ビニル", Color(0x99cc99))

    @JvmField
    val TFE_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("tetra_fluoro_ethylene", "Tetra Fluoro Ethylene", "テトラフルオロエチレン", Color(0x669999))

    @JvmField
    val DIESEL_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("diesel", "Diesel", "ディーゼル", Color(0xcccc00))

    @JvmField
    val BENZENE_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("benzene", "Benzene", "ベンゼン", Color(0x000066))

    @JvmField
    val TOLUENE_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("toluene", "Toluene", "トルエン", Color(0x666699))

    @JvmField
    val PHENOL_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("phenol", "Phenol", "フェノール", Color(0x996633))

    //    Blocks    //

    @JvmStatic
    private fun initBlockItems(register: HTItemRegister) {
        // ores
        register.registerBlockItem(RagiumBlocks.RAGINITE_ORE) {
            registerTags(RagiumItemTags.RAGINITE_ORES)
        }
        register.registerBlockItem(RagiumBlocks.DEEPSLATE_RAGINITE_ORE) {
            registerTags(RagiumItemTags.RAGINITE_ORES)
        }
        // blocks
        RagiumBlocks.StorageBlocks.entries.forEach { block: RagiumBlocks.StorageBlocks ->
            register.registerBlockItem(block.block, itemSettings().tier(block.material.tier))
        }
        // hulls
        RagiumBlocks.Hulls.entries.forEach { hull: RagiumBlocks.Hulls ->
            register.registerBlockItem(hull.block, itemSettings().tier(hull.material.tier))
        }
        // machines
        register.registerBlockItem(RagiumBlocks.CREATIVE_SOURCE)
        register.registerBlockItem(RagiumBlocks.MANUAL_GRINDER)
        register.registerBlockItem(RagiumBlocks.WATER_COLLECTOR)
        register.registerBlockItem(RagiumBlocks.BURNING_BOX)
        register.registerBlockItem(RagiumBlocks.WATER_GENERATOR)
        register.registerBlockItem(RagiumBlocks.WIND_GENERATOR)
        register.registerBlockItem(RagiumBlocks.SHAFT)
        register.registerBlockItem(RagiumBlocks.GEAR_BOX)
        register.registerBlockItem(RagiumBlocks.BLAZING_BOX)
        register.registerBlockItem(
            RagiumBlocks.ALCHEMICAL_INFUSER,
            itemSettings().rarity(Rarity.EPIC),
        )
        register.registerBlockItem(RagiumBlocks.ITEM_DISPLAY)
    }

    //    Machines    //

    @JvmStatic
    private fun initMachines(register: HTItemRegister) {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            // BlockItem
            register.registerBlockItem(
                type.block,
                itemSettings().tier(type.tier).disableTooltips(),
            )
        }
    }

    //    Elements    //

    @JvmStatic
    private fun initElements(register: HTItemRegister) {
        RagiElement.entries.forEach { element: RagiElement ->
            register.registerBlockItem(element.buddingBlock)
            register.registerBlockItem(element.clusterBlock)
            // item
            register.register("${element.asString()}_dust", element.dustItem) {
                putEnglish("${element.getTranslatedName(HTLangType.EN_US)} Dust")
                putJapanese("${element.getTranslatedName(HTLangType.JA_JP)}の粉")
            }
        }
    }

    @JvmStatic
    private fun registerFluidCube(
        name: String,
        englishName: String,
        japaneseName: String,
        color: Color,
    ): HTFluidCubeItem = REGISTER.register("${name}_fluid_cube", HTFluidCubeItem.create(name)) {
        putEnglish("Fluid Cube ($englishName)")
        putJapanese("液体キューブ（$japaneseName）")
        setColor(color)
        generateModel {
            RagiumModels.FILLED_FLUID_CUBE.upload(
                ModelIds.getItemModelId(item),
                TextureMap(),
                it.writer,
            )
        }
    }

    //    Dusts    //

    enum class Dusts(val enName: String, val jaName: String) : ItemConvertible {
        RAW_RAGINITE("Raw Raginite Dust", "未加工のラギナイトの粉"),
        RAGINITE("Raginite Dust", "ラギナイトの粉"),
        REFINED_RAGINITE("Refined Raginite Dust", "精製ラギナイトの粉"),
        ASH("Ash Dust", "灰"),
        ;

        private val dust = Item(itemSettings())

        override fun asItem(): Item = dust
    }

    @JvmStatic
    private fun initDusts(register: HTItemRegister) {
        Dusts.entries.forEach { dust: Dusts ->
            register.register("${dust.name.lowercase()}_dust", dust.asItem())
        }
    }

    //    Ingots    //

    enum class Ingots(val material: RagiumMaterials) :
        ItemConvertible,
        HTTranslationFormatter {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        STEEL(RagiumMaterials.STEEL),
        TWILIGHT_METAL(RagiumMaterials.TWILIGHT_METAL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        private val ingot = Item(itemSettings())

        override fun asItem(): Item = ingot

        override val enPattern: String = "%s Ingot"
        override val jaPattern: String = "%sインゴット"
    }

    @JvmStatic
    private fun initIngots(register: HTItemRegister) {
        Ingots.entries.forEach { ingot: Ingots ->
            register.register("${ingot.name.lowercase()}_ingot", ingot.asItem())
        }
    }

    //    Plates    //

    enum class Plates(val material: RagiumMaterials) :
        ItemConvertible,
        HTTranslationFormatter {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        STEEL(RagiumMaterials.STEEL),
        TWILIGHT(RagiumMaterials.TWILIGHT_METAL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        PE(RagiumMaterials.PE),
        PVC(RagiumMaterials.PVC),
        PTFE(RagiumMaterials.PTFE),
        ;

        private val plate = Item(itemSettings())

        override fun asItem(): Item = plate

        override val enPattern: String = "%s Plate"
        override val jaPattern: String = "%s板"
    }

    @JvmStatic
    private fun initPlates(register: HTItemRegister) {
        Plates.entries.forEach { plate: Plates ->
            register.register("${plate.name.lowercase()}_plate", plate.asItem())
        }
    }

    //    Fluids    //

    enum class Fluids(val color: Color, override val enName: String, override val jaName: String) :
        ItemConvertible,
        HTTranslationProvider {
        // tier1
        WATER(Color(0x0033ff), "Water", "水"),
        LAVA(Color(0xff6600), "Lava", "溶岩"),
        MILK(Color(0xffffff), "Milk", "牛乳"),
        HONEY(Color(0xffcc33), "Honey", "蜂蜜"),

        // tier2
        // tier3
        SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
        OIL(Color(0x000000), "Oil", "石油"),
        ;

        val fluidName: String = name.lowercase()

        private val item: HTFluidCubeItem = HTFluidCubeItem.create(fluidName)

        override fun asItem(): Item = item
    }

    @JvmStatic
    private fun initFluids(register: HTItemRegister) {
        Fluids.entries.forEach { fluid: Fluids ->
            register.register("${fluid.fluidName}_fluid_cube", fluid.asItem()) {
                setColor(fluid.color)
                generateModel {
                    RagiumModels.FILLED_FLUID_CUBE.upload(
                        ModelIds.getItemModelId(item),
                        TextureMap(),
                        it.writer,
                    )
                }
            }
        }
    }
}

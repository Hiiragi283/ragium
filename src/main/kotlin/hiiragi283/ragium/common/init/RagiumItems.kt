package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.component.HTTooltipsComponent
import hiiragi283.ragium.common.item.HTEnderBundleItem
import hiiragi283.ragium.common.item.HTFluidCubeItem
import hiiragi283.ragium.common.item.HTForgeHammerItem
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.registry.HTItemRegister
import hiiragi283.ragium.common.util.itemSettings
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.ComponentMap
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.TextureMap
import net.minecraft.item.Item
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Formatting
import java.awt.Color

object RagiumItems {
    @JvmField
    val REGISTER: HTItemRegister = HTItemRegister(Ragium.MOD_ID).apply(::registerBlockItems)

    //    Tools    //

    /*@JvmField
    val POWER_METER: Item =
        REGISTER.registerSimple(
            "power_meter",
            itemSettings().component(RagiumComponentTypes.DISABLE_CYCLE_POWER, Unit),
        ) {
            putEnglish("Power Meter")
            putJapanese("パワー測定器")
        }*/

    @JvmField
    val FORGE_HAMMER: Item =
        REGISTER.register("forge_hammer", HTForgeHammerItem) {
            putEnglish("Forge Hammer")
            putEnglishTips("Used to make Ingot to Plate")
            putJapanese("鍛造ハンマー")
            putJapaneseTips("インゴットを板に加工するための道具")
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

    //    Fluid Cubes    //

    @JvmField
    val EMPTY_FLUID_CUBE: Item =
        REGISTER.registerSimple("empty_fluid_cube") {
            putEnglish("Fluid Cube (Empty)")
            putEnglishTips("Holdable fluid for 1 Bucket")
            putJapanese("液体キューブ（なし）")
            putJapaneseTips("1バケツ分の液体を保持できる")
            setCustomModel()
        }

    //    Dusts    //

    @JvmField
    val RAW_RAGINITE_DUST: Item =
        REGISTER.registerSimple("raw_raginite_dust") {
            putEnglish("Raw Raginite Dust")
            putEnglishTips("Impure and rough raginite dust")
            putJapanese("ラギナイトの原石の粉")
            putJapaneseTips("不純で粗いラギナイトの粉")
        }

    @JvmField
    val RAGINITE_DUST: Item =
        REGISTER.registerSimple("raginite_dust") {
            putEnglish("Raginite Dust")
            putEnglishTips("Obtained from washing Raw Raginite Dust by Cauldron")
            putJapanese("ラギナイトの粉")
            putJapaneseTips("ラギナイトの原石の粉を大釜で洗うと手に入る")
        }

    @JvmField
    val REFINED_RAGINITE_DUST: Item =
        REGISTER.registerSimple("refined_raginite_dust") {
            putEnglish("Refined Raginite Dust")
            putEnglishTips("Purified and fine raginite dust")
            putJapanese("精製ラギナイトの粉")
            putJapaneseTips("純粋で細かなラギナイトの粉")
        }

    @JvmField
    val ASH_DUST: Item =
        REGISTER.registerSimple("ash_dust") {
            putEnglish("Ash Dust")
            putEnglishTips("Obtained from Burning Box")
            putJapanese("灰の粉")
            putJapaneseTips("燃焼室から手に入る")
        }

    //    Ingots    //

    @JvmField
    val RAGI_ALLOY_INGOT: Item =
        REGISTER.registerSimple("ragi_alloy_ingot") {
            putEnglish("Ragi-Alloy Ingot")
            putEnglishTips("Tier 1 - Metal")
            putJapanese("ラギ合金インゴット")
            putJapaneseTips("Tier 1 - 金属")
        }

    @JvmField
    val RAGI_STEEL_INGOT: Item =
        REGISTER.registerSimple("ragi_steel_ingot") {
            putEnglish("Ragi-Steel Ingot")
            putEnglishTips("Tier 2 - Metal")
            putJapanese("ラギスチールインゴット")
            putJapaneseTips("Tier 2 - 金属")
        }

    @JvmField
    val REFINED_RAGI_STEEL_INGOT: Item =
        REGISTER.registerSimple("refined_ragi_steel_ingot") {
            putEnglish("Refined Ragi-Steel Ingot")
            putEnglishTips("Tier 3 - Metal")
            putJapanese("精製ラギスチールインゴット")
            putJapaneseTips("Tier 3 - 金属")
        }

    @JvmField
    val STEEL_INGOT: Item =
        REGISTER.registerSimple("steel_ingot") {
            putEnglish("Steel Ingot")
            putEnglishTips("Harder than Iron")
            putJapanese("鋼鉄インゴット")
            putJapaneseTips("鉄より固い鋼鉄の仮面")
            registerTags(RagiumItemTags.STEEL_INGOTS)
        }

    @JvmField
    val TWILIGHT_METAL_INGOT: Item =
        REGISTER.registerSimple("twilight_metal_ingot") {
            putEnglish("Twilight Metal Ingot")
            putEnglishTips("-- Nostalgia --")
            putJapanese("黄昏金属インゴット")
            putJapaneseTips("-- 郷愁 --")
        }

    @JvmField
    val SOAP_INGOT: Item =
        REGISTER.registerSimple("soap_ingot") {
            putEnglish("Soap Ingot")
            putEnglishTips("How about a Soap Sandwich?")
            putJapanese("石鹸インゴット")
            putJapaneseTips("石鹸サンドイッチはいかが？")
        }

    //    Plates    //

    @JvmField
    val RAGI_ALLOY_PLATE: Item =
        REGISTER.registerSimple("ragi_alloy_plate") {
            putEnglish("Ragi-Alloy Plate")
            putEnglishTips("Tier 1 - Metal Plate")
            putJapanese("ラギ合金の板")
            putJapaneseTips("Tier 1 - 金属板")
        }

    @JvmField
    val RAGI_STEEL_PLATE: Item =
        REGISTER.registerSimple("ragi_steel_plate") {
            putEnglish("Ragi-Steel Plate")
            putEnglishTips("Tier 2 - Metal Plate")
            putJapanese("ラギスチールの板")
            putJapaneseTips("Tier 2 - 金属板")
        }

    @JvmField
    val REFINED_RAGI_STEEL_PLATE: Item =
        REGISTER.registerSimple("refined_ragi_steel_plate") {
            putEnglish("Refined Ragi-Steel Plate")
            putEnglishTips("Tier 3 - Metal Plate")
            putJapanese("精製ラギスチールの板")
            putJapaneseTips("Tier 3 - 金属板")
        }

    @JvmField
    val PE_PLATE: Item =
        REGISTER.registerSimple("pe_plate") {
            putEnglish("Poly Ethylene Plate")
            putEnglishTips("Tier 1 - Plastic Plate")
            putJapanese("ポリエチレンの板")
            putJapaneseTips("Tier 1 - プラスチック板")
        }

    @JvmField
    val PVC_PLATE: Item =
        REGISTER.registerSimple("pvc_plate") {
            putEnglish("Poly Vinyl Chloride Plate")
            putEnglishTips("Tier 2 - Plastic Plate")
            putJapanese("ポリ塩化ビニルの板")
            putJapaneseTips("Tier 2 - プラスチック板")
        }

    @JvmField
    val PTFE_PLATE: Item =
        REGISTER.registerSimple("ptfe_plate") {
            putEnglish("PTFE Plate")
            putEnglishTips("Tier 3 - Plastic Plate")
            putJapanese("PTFEの板")
            putJapaneseTips("Tier 3 - プラスチック板")
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

    // vanilla
    @JvmField
    val WATER_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("water", "Water", "水", Color(0x0033ff))

    @JvmField
    val LAVA_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("lava", "Lava", "溶岩", Color(0xff6600))

    @JvmField
    val MILK_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("milk", "Milk", "牛乳", Color(0xffffff))

    @JvmField
    val HONEY_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("honey", "Honey", "蜂蜜", Color(0xffcc33))

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
    val SALT_WATER_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("salt_water", "Salt Water", "塩水", Color(0x003399))

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
    val OIL_FLUID_CUBE: HTFluidCubeItem =
        registerFluidCube("oil", "Oil", "石油", Color(0x000000))

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

    @JvmStatic
    private fun registerBlockItems(register: HTItemRegister) {
        // ores
        register.registerBlockItem("raginite_ore", RagiumBlocks.RAGINITE_ORE) {
            registerTags(RagiumItemTags.RAGINITE_ORES)
        }
        register.registerBlockItem("deepslate_raginite_ore", RagiumBlocks.DEEPSLATE_RAGINITE_ORE) {
            registerTags(RagiumItemTags.RAGINITE_ORES)
        }
        // blocks
        register.registerBlockItem(
            "ragi_alloy_block",
            RagiumBlocks.RAGI_ALLOY_BLOCK,
            itemSettings().component(RagiumComponentTypes.TIER, HTMachineTier.HEAT),
        )
        register.registerBlockItem(
            "ragi_steel_block",
            RagiumBlocks.RAGI_STEEL_BLOCK,
            itemSettings().component(RagiumComponentTypes.TIER, HTMachineTier.KINETIC),
        )
        register.registerBlockItem(
            "refined_ragi_steel_block",
            RagiumBlocks.REFINED_RAGI_STEEL_BLOCK,
            itemSettings().component(RagiumComponentTypes.TIER, HTMachineTier.ELECTRIC),
        )
        // hulls
        register.registerBlockItem(
            "ragi_alloy_hull",
            RagiumBlocks.RAGI_ALLOY_HULL,
            itemSettings().component(RagiumComponentTypes.TIER, HTMachineTier.HEAT),
        )
        register.registerBlockItem(
            "ragi_steel_hull",
            RagiumBlocks.RAGI_STEEL_HULL,
            itemSettings().component(RagiumComponentTypes.TIER, HTMachineTier.KINETIC),
        )
        register.registerBlockItem(
            "refined_ragi_steel_hull",
            RagiumBlocks.REFINED_RAGI_STEEL_HULL,
            itemSettings().component(RagiumComponentTypes.TIER, HTMachineTier.ELECTRIC),
        )
        // machines
        register.registerBlockItem("creative_source", RagiumBlocks.CREATIVE_SOURCE)
        register.registerBlockItem("manual_grinder", RagiumBlocks.MANUAL_GRINDER)
        register.registerBlockItem("water_collector", RagiumBlocks.WATER_COLLECTOR)
        register.registerBlockItem("burning_box", RagiumBlocks.BURNING_BOX)
        register.registerBlockItem("water_generator", RagiumBlocks.WATER_GENERATOR)
        register.registerBlockItem("wind_generator", RagiumBlocks.WIND_GENERATOR)
        register.registerBlockItem("shaft", RagiumBlocks.SHAFT)
        register.registerBlockItem("gear_box", RagiumBlocks.GEAR_BOX)
        register.registerBlockItem("blazing_box", RagiumBlocks.BLAZING_BOX)

        registerMachines(register)
    }

    @JvmStatic
    private fun registerMachines(register: HTItemRegister) {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            // BlockItem
            register.registerBlockItem(
                type.id.path,
                type.block,
                itemSettings().component(RagiumComponentTypes.DISABLE_TOOLTIPS, Unit),
            )
        }
    }

    /*@JvmStatic
    private fun registerFluidCube(
        name: String,
        englishName: String,
        japaneseName: String,
        texBlock: Block,
    ): HTFluidCubeItem = registerFluidCube(name, englishName, japaneseName, TextureMap.getId(texBlock))

    @JvmStatic
    private fun registerFluidCube(
        name: String,
        englishName: String,
        japaneseName: String,
        texId: Identifier,
    ): HTFluidCubeItem = REGISTER.register("${name}_fluid_cube", HTFluidCubeItem.create(name)) {
        putEnglish("Fluid Cube ($englishName)")
        putJapanese("液体キューブ（$japaneseName）")
        generateModel {
            RagiumModels.FILLED_FLUID_CUBE.upload(
                ModelIds.getItemModelId(item),
                TextureMap().put(TextureKey.INSIDE, texId),
                it.writer,
            )
        }
    }*/

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

    //    Event    //

    @JvmStatic
    fun registerEvents() {
        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            customTooltip(context, TWILIGHT_METAL_INGOT, Formatting.GRAY, Formatting.ITALIC)
            customTooltip(context, SOAP_INGOT, Formatting.GRAY, Formatting.ITALIC)
            customTooltip(context, RAW_RAGINITE, Formatting.GRAY, Formatting.OBFUSCATED)
            // auto setting
            context.modify(::canSetTooltip) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.TOOLTIPS,
                    HTTooltipsComponent.fromItem(item, Formatting.GRAY),
                )
            }
        }
    }

    @JvmStatic
    private fun customTooltip(context: DefaultItemComponentEvents.ModifyContext, item: Item, vararg formattings: Formatting) {
        context.modify(item) { builder: ComponentMap.Builder ->
            builder.add(
                RagiumComponentTypes.TOOLTIPS,
                HTTooltipsComponent.fromItem(item, *formattings),
            )
        }
    }

    @JvmStatic
    private fun canSetTooltip(item: Item): Boolean = when {
        item !in REGISTER -> false
        item.components.contains(RagiumComponentTypes.TOOLTIPS) -> false
        item.components.contains(RagiumComponentTypes.DISABLE_TOOLTIPS) -> false
        else -> true
    }
}

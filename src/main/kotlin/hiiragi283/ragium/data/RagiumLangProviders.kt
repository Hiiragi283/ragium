package hiiragi283.ragium.data

import hiiragi283.ragium.api.content.HTRegistryContent
import hiiragi283.ragium.api.content.HTTranslationFormatter
import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.extension.splitWith
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

object RagiumLangProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumLangProviders::EnglishLang)
        pack.addProvider(RagiumLangProviders::JapaneseLang)
    }

    @JvmName("addBlock")
    fun TranslationBuilder.add(entry: HTRegistryContent<Block>, value: String, desc: String? = null) {
        val block: Block = entry.value
        add(block, value)
        desc?.let { add("${block.translationKey}.description", it) }
    }

    @JvmName("addItem")
    fun TranslationBuilder.add(entry: HTRegistryContent<Item>, value: String) {
        add(entry.value, value)
    }

    fun TranslationBuilder.add(enchantment: RegistryKey<Enchantment>, value: String) {
        add("enchantment.${enchantment.value.splitWith('.')}", value)
    }

    fun TranslationBuilder.add(tier: HTMachineTier, name: String, prefix: String) {
        add(tier.translationKey, name)
        add(tier.prefixKey, prefix)
    }

    fun TranslationBuilder.add(key: HTMachineKey, value: String, desc: String? = null) {
        add(key.translationKey, value)
        desc?.let { add(key.descriptionKey, it) }
    }

    fun TranslationBuilder.add(key: HTMaterialKey, value: String) {
        add(key.translationKey, value)
    }

    fun TranslationBuilder.add(prefix: HTTagPrefix, value: String) {
        add(prefix.translationKey, value)
    }

    @JvmStatic
    private fun translateContents(builder: TranslationBuilder, type: HTLangType) {
        // blocks
        RagiumContents.Grates.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Grate"
                        HTLangType.JA_JP -> "%s格子"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        RagiumContents.Casings.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Casing"
                        HTLangType.JA_JP -> "%s外装"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        RagiumContents.Hulls.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Hull"
                        HTLangType.JA_JP -> "%s筐体"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        RagiumContents.Coils.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Coil"
                        HTLangType.JA_JP -> "%sコイル"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        RagiumContents.Exporters.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Exporter"
                        HTLangType.JA_JP -> "%s搬出機"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        RagiumContents.Drums.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Drum"
                        HTLangType.JA_JP -> "%sドラム"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        // items
        RagiumContents.CircuitBoards.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Circuit Board"
                        HTLangType.JA_JP -> "%s回路基板"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        RagiumContents.Circuits.entries.forEach {
            builder.add(
                it,
                HTTranslationFormatter { type ->
                    when (type) {
                        HTLangType.EN_US -> "%s Circuit"
                        HTLangType.JA_JP -> "%s回路"
                    }
                }.getTranslation(type, it.tier),
            )
        }
        /*HTCrafterHammerItem.Behavior.entries.forEach {
            builder.add(it, object : HTTranslationFormatter {
                override val enPattern: String = "Hammer Module (%s)"
                override val jaPattern: String = "%s回路"
            }.getTranslation(type, ))
        }*/
        // fluids
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            builder.add(
                fluid.translationKey,
                fluid.getTranslation(type),
            )
        }
    }

    //    English    //

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            translateContents(builder, HTLangType.EN_US)
            // Advancements
            builder.add(RagiumTranslationKeys.ADVANCEMENT_BUJIN, "Tycoon the Racoon")
            builder.add(RagiumTranslationKeys.ADVANCEMENT_STELLA_SUIT, "Synthetically Treated External Lightweight-Layered Augment")
            // Blocks
            builder.add(RagiumBlocks.POROUS_NETHERRACK, "Porous Netherrack")

            builder.add(RagiumBlocks.ASPHALT, "Asphalt")

            builder.add(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

            builder.add(RagiumBlocks.AUTO_ILLUMINATOR, "Auto Illuminator")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "Creative Power Source")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "Item Display")
            builder.add(RagiumBlocks.LARGE_PROCESSOR, "Large Processor")
            builder.add(RagiumBlocks.MANUAL_FORGE, "Ragi-Anvil")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "Ragi-Grinder")
            builder.add(RagiumBlocks.MANUAL_MIXER, "Ragi-Basin")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.SHAFT, "Shaft")
            builder.add(RagiumBlocks.TELEPORT_ANCHOR, "Teleport Anchor")
            builder.add(RagiumBlocks.TRASH_BOX, "Trash Box")

            builder.add(RagiumContents.Pipes.IRON, "Iron Pipe")
            builder.add(RagiumContents.Pipes.WOODEN, "Wooden Pipe")
            builder.add(RagiumContents.Pipes.STEEL, "Steel Pipe")
            builder.add(RagiumContents.Pipes.COPPER, "Copper Pipe")
            builder.add(RagiumContents.Pipes.UNIVERSAL, "Universal Pipe")

            builder.add(RagiumTranslationKeys.DRUM_AMOUNT, "Amount: %s Unit")
            builder.add(RagiumTranslationKeys.DRUM_CAPACITY, "Capacity: %s Unit")
            builder.add(RagiumTranslationKeys.DRUM_FLUID, "Fluid: %s")

            builder.add(RagiumTranslationKeys.TRANSPORTER_FLUID_SPEED, "液体速度: %s ユニット/秒")
            builder.add(RagiumTranslationKeys.TRANSPORTER_ITEM_SPEED, "アイテム速度: %s個/秒")
            // Enchantment
            builder.add(RagiumEnchantments.SMELTING, "Smelting")
            builder.add(RagiumEnchantments.SLEDGE_HAMMER, "Sledge Hammer")
            builder.add(RagiumEnchantments.BUZZ_SAW, "Buzz Saw")
            // Items
            builder.add(RagiumItems.STEEL_HELMET, "Steel Helmet")
            builder.add(RagiumItems.STEEL_CHESTPLATE, "Steel Chestplate")
            builder.add(RagiumItems.STEEL_LEGGINGS, "Steel Leggings")
            builder.add(RagiumItems.STEEL_BOOTS, "Steel Boots")
            builder.add(RagiumItems.STELLA_GOGGLE, "S.T.E.L.L.A. Goggles")
            builder.add(RagiumItems.STELLA_JACKET, "S.T.E.L.L.A. Jacket")
            builder.add(RagiumItems.STELLA_LEGGINGS, "S.T.E.L.L.A. Leggings")
            builder.add(RagiumItems.STELLA_BOOTS, "S.T.E.L.L.A. Boots")

            builder.add(RagiumItems.STEEL_AXE, "Steel Axe")
            builder.add(RagiumItems.STEEL_HOE, "Steel Hoe")
            builder.add(RagiumItems.STEEL_PICKAXE, "Steel Pickaxe")
            builder.add(RagiumItems.STEEL_SHOVEL, "Steel Shovel")
            builder.add(RagiumItems.STEEL_SWORD, "Steel Sword")
            builder.add(RagiumItems.BUJIN, "Bujin")

            builder.add(RagiumItems.BEE_WAX, "Bee Wax")
            builder.add(RagiumItems.BUTTER, "Butter")
            builder.add(RagiumItems.CARAMEL, "Caramel")
            builder.add(RagiumItems.CHOCOLATE, "Chocolate")
            builder.add(RagiumItems.CHOCOLATE_APPLE, "Chocolate Apple")
            builder.add(RagiumItems.CHOCOLATE_BREAD, "Chocolate Bread")
            builder.add(RagiumItems.FLOUR, "Flour")
            builder.add(RagiumItems.DOUGH, "Dough")
            builder.add(RagiumItems.MINCED_MEAT, "Minced Meat")
            builder.add(RagiumItems.PULP, "Pulp")

            builder.add(RagiumItems.BACKPACK, "Backpack")
            builder.add(RagiumItems.BASALT_MESH, "Basalt Mesh")
            builder.add(RagiumItems.CRAFTER_HAMMER, "Crafter's Hammer")
            builder.add(RagiumItems.CRIMSON_CRYSTAL, "Crimson Crystal")
            builder.add(RagiumItems.DEEPANT, "Deepant")
            builder.add(RagiumItems.DYNAMITE, "Dynamite")
            builder.add(RagiumItems.EMPTY_FLUID_CUBE, "Fluid Cube (Empty)")
            builder.add(RagiumItems.FILLED_FLUID_CUBE, "Fluid Cube (%s)")
            builder.add(RagiumItems.ENGINE, "V8 Engine")
            builder.add(RagiumItems.FORGE_HAMMER, "Forge Hammer")
            builder.add(RagiumItems.HEART_OF_THE_NETHER, "Heart of the Nether")
            builder.add(RagiumItems.LASER_EMITTER, "Laser Emitter")
            builder.add(RagiumItems.POLYMER_RESIN, "Polymer Resin")
            builder.add(RagiumItems.PROCESSOR_SOCKET, "Processor Socket")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
            builder.add(RagiumItems.RAGI_CRYSTAL_PROCESSOR, "Ragi-Crystal Processor")
            builder.add(RagiumItems.REMOVER_DYNAMITE, "Remover Dynamite")
            builder.add(RagiumItems.RESIDUAL_COKE, "Residual Coke")
            builder.add(RagiumItems.SLAG, "Slag")
            builder.add(RagiumItems.SOAP_INGOT, "Soap Ingot")
            builder.add(RagiumItems.SOLAR_PANEL, "Solar Panel")
            builder.add(RagiumItems.TRADER_CATALOG, "Trader Catalog")
            builder.add(RagiumItems.WARPED_CRYSTAL, "Warped Crystal")

            builder.add(RagiumTranslationKeys.CRAFTER_HAMMER_MODULE, "Module: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_DESTROY, "Destroy: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_POWER, "Power: %s")
            builder.add(RagiumTranslationKeys.REMOVER_DYNAMITE_MODE, "Mode: %s")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - Items")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - Machines")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "Name: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "Tier: %s")
            builder.add(RagiumTranslationKeys.MACHINE_FLUID_AMOUNT, "Amount: %s Units")
            builder.add(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, "Network Energy: %s Units")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "Recipe cost: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_SHOW_PREVIEW, "Show preview: %s")
            builder.add(RagiumTranslationKeys.MACHINE_SLOT_COUNTS, "Input/Output Slots: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TANK_CAPACITY, "Each Tank Capacity: %s Buckets")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The structure is valid!")
            // Machine Tier
            builder.add(HTMachineTier.PRIMITIVE, "Primitive", "Primitive %s")
            builder.add(HTMachineTier.BASIC, "Basic", "Basic %s")
            builder.add(HTMachineTier.ADVANCED, "Advanced", "Advanced %s")
            // Machine Type
            builder.add(
                RagiumMachineKeys.BIOMASS_FERMENTER,
                "Biomass Fermenter",
                "Produce Biomass from Composter inputs",
            )
            builder.add(
                RagiumMachineKeys.CANNING_MACHINE,
                "Canning Machine",
                "Insert or Extract fluids from Fluid Cube",
            )
            builder.add(
                RagiumMachineKeys.DRAIN,
                "Drain",
                "Drains fluids from each side",
            )
            builder.add(RagiumMachineKeys.FLUID_DRILL, "Fluid Drill", "Pump up fluids from specified biomes")

            builder.add(
                RagiumMachineKeys.COMBUSTION_GENERATOR,
                "Combustion Generator",
                "Generate energy from liquid fuels",
            )
            builder.add(
                RagiumMachineKeys.SOLAR_PANEL,
                "Solar Generator",
                "Generate energy in daytime",
            )
            builder.add(
                RagiumMachineKeys.STEAM_GENERATOR,
                "Steam Generator",
                "Generate energy from water and below heat source",
            )
            builder.add(
                RagiumMachineKeys.THERMAL_GENERATOR,
                "Thermal Generator",
                "Generate energy from hot fluids",
            )
            // builder.add(RagiumMachineKeys.WATER_GENERATOR, "Water Generator")

            builder.add(
                RagiumMachineKeys.ALLOY_FURNACE,
                "Alloy Furnace",
                "Smelt two ingredients into one",
            )
            builder.add(RagiumMachineKeys.ASSEMBLER, "Assembler", "Dr.Doom, Assemble!")
            builder.add(
                RagiumMachineKeys.BLAST_FURNACE,
                "Large Blast Furnace",
                "Smelt multiple ingredients into one",
            )
            builder.add(RagiumMachineKeys.CHEMICAL_REACTOR, "Chemical Reactor", "Are You Ready?")
            builder.add(RagiumMachineKeys.COMPRESSOR, "Compressor")
            builder.add(
                RagiumMachineKeys.DISTILLATION_TOWER,
                "Distillation Tower",
                "Process Crude Oil",
            )
            builder.add(RagiumMachineKeys.ELECTROLYZER, "Electrolyzer", "Elek On")
            builder.add(RagiumMachineKeys.EXTRACTOR, "Extractor", "Extract")
            builder.add(RagiumMachineKeys.GRINDER, "Grinder", "Grind ingredients")
            builder.add(RagiumMachineKeys.GROWTH_CHAMBER, "Growth Chamber")
            builder.add(RagiumMachineKeys.LASER_TRANSFORMER, "Laser Transformer")
            builder.add(RagiumMachineKeys.METAL_FORMER, "Metal Former", "It's High Quality.")
            builder.add(RagiumMachineKeys.MIXER, "Mixer", "Genomix")
            builder.add(RagiumMachineKeys.MULTI_SMELTER, "Multi Smelter", "Smelt multiple items at once")
            builder.add(RagiumMachineKeys.ROCK_GENERATOR, "Rock Generator")
            builder.add(
                RagiumMachineKeys.SAW_MILL,
                "Saw Mill",
                "Process Logs more efficiently",
            )
            // Material
            builder.add(RagiumMaterialKeys.CRUDE_RAGINITE, "Crude Raginite")
            builder.add(RagiumMaterialKeys.RAGI_ALLOY, "Ragi-Alloy")
            builder.add(RagiumMaterialKeys.ALKALI, "Alkali")
            builder.add(RagiumMaterialKeys.ASH, "Ash")
            builder.add(RagiumMaterialKeys.COPPER, "Copper")
            builder.add(RagiumMaterialKeys.IRON, "Iron")
            builder.add(RagiumMaterialKeys.NITER, "Niter")
            builder.add(RagiumMaterialKeys.SALT, "Salt")
            builder.add(RagiumMaterialKeys.SULFUR, "Sulfur")
            builder.add(RagiumMaterialKeys.WOOD, "Wood")

            builder.add(RagiumMaterialKeys.RAGINITE, "Raginite")
            builder.add(RagiumMaterialKeys.RAGI_STEEL, "Ragi-Steel")
            builder.add(RagiumMaterialKeys.FLUORITE, "Fluorite")
            builder.add(RagiumMaterialKeys.GOLD, "Gold")
            builder.add(RagiumMaterialKeys.PLASTIC, "Plastic")
            builder.add(RagiumMaterialKeys.SILICON, "Silicon")
            builder.add(RagiumMaterialKeys.STEEL, "Steel")

            builder.add(RagiumMaterialKeys.RAGI_CRYSTAL, "Ragi-Crystal")
            builder.add(RagiumMaterialKeys.REFINED_RAGI_STEEL, "Refined Ragi-Steel")
            builder.add(RagiumMaterialKeys.ALUMINUM, "Aluminum")
            builder.add(RagiumMaterialKeys.BAUXITE, "Bauxite")
            builder.add(RagiumMaterialKeys.CRYOLITE, "Cryolite")
            builder.add(RagiumMaterialKeys.DEEP_STEEL, "Deep Steel")
            builder.add(RagiumMaterialKeys.ENGINEERING_PLASTIC, "Engineering Plastic")
            builder.add(RagiumMaterialKeys.STELLA, "S.T.E.L.L.A.")

            builder.add(RagiumMaterialKeys.RAGIUM, "Ragium")

            builder.add(RagiumMaterialKeys.COAL, "Coal")
            builder.add(RagiumMaterialKeys.DIAMOND, "Diamond")
            builder.add(RagiumMaterialKeys.EMERALD, "Emerald")
            builder.add(RagiumMaterialKeys.PERIDOT, "Peridot")
            builder.add(RagiumMaterialKeys.QUARTZ, "Quartz")
            builder.add(RagiumMaterialKeys.SAPPHIRE, "Sapphire")
            builder.add(RagiumMaterialKeys.RUBY, "Ruby")

            builder.add(RagiumMaterialKeys.IRIDIUM, "Iridium")
            builder.add(RagiumMaterialKeys.LEAD, "Lead")
            builder.add(RagiumMaterialKeys.NICKEL, "Nickel")
            builder.add(RagiumMaterialKeys.PLATINUM, "Platinum")
            builder.add(RagiumMaterialKeys.SILVER, "Silver")
            builder.add(RagiumMaterialKeys.TIN, "Tin")
            builder.add(RagiumMaterialKeys.TUNGSTEN, "Tungsten")
            builder.add(RagiumMaterialKeys.ZINC, "Zinc")
            // Tag Prefix
            builder.add(HTTagPrefix.DEEP_ORE, "Deepslate %s Ore")
            builder.add(HTTagPrefix.END_ORE, "End %s Ore")
            builder.add(HTTagPrefix.DUST, "%s Dust")
            builder.add(HTTagPrefix.GEAR, "%s Gear")
            builder.add(HTTagPrefix.GEM, "%s")
            builder.add(HTTagPrefix.INGOT, "%s Ingot")
            builder.add(HTTagPrefix.NETHER_ORE, "Nether %s Ore")
            builder.add(HTTagPrefix.NUGGET, "%s Nugget")
            builder.add(HTTagPrefix.ORE, "%s Ore")
            builder.add(HTTagPrefix.PLATE, "%s Plate")
            builder.add(HTTagPrefix.RAW_MATERIAL, "Raw %s")
            builder.add(HTTagPrefix.ROD, "%s Rod")
            builder.add(HTTagPrefix.STORAGE_BLOCK, "Block of %s")
        }
    }

    //    Japanese    //

    private class JapaneseLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, "ja_jp", registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            translateContents(builder, HTLangType.JA_JP)
            // Advancements
            builder.add(RagiumTranslationKeys.ADVANCEMENT_BUJIN, "タイクーン将軍")
            builder.add(RagiumTranslationKeys.ADVANCEMENT_STELLA_SUIT, "ｽｺﾞｲ ﾂﾖｸﾃ ｴｹﾞﾂﾅｲｸﾗｲ Love-Loveﾅ ｱｰﾏｰ")
            // Blocks
            builder.add(RagiumBlocks.POROUS_NETHERRACK, "多孔質ネザーラック")

            builder.add(RagiumBlocks.ASPHALT, "アスファルト")

            builder.add(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

            builder.add(RagiumBlocks.AUTO_ILLUMINATOR, "光源置き太郎")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "アイテムティスプレイ")
            builder.add(RagiumBlocks.LARGE_PROCESSOR, "大型処理装置")
            builder.add(RagiumBlocks.MANUAL_FORGE, "らぎ金床")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "らぎ臼")
            builder.add(RagiumBlocks.MANUAL_MIXER, "らぎ釜")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.SHAFT, "シャフト")
            builder.add(RagiumBlocks.TELEPORT_ANCHOR, "テレポートアンカー")
            builder.add(RagiumBlocks.TRASH_BOX, "ゴミ箱")

            builder.add(RagiumItems.STEEL_HELMET, "スチールのヘルメット")
            builder.add(RagiumItems.STEEL_CHESTPLATE, "スチールのチェストプレート")
            builder.add(RagiumItems.STEEL_LEGGINGS, "スチールのレギンス")
            builder.add(RagiumItems.STEEL_BOOTS, "スチールのブーツ")
            builder.add(RagiumItems.STELLA_GOGGLE, "S.T.E.L.L.A.ゴーグル")
            builder.add(RagiumItems.STELLA_JACKET, "S.T.E.L.L.A.ジャケット")
            builder.add(RagiumItems.STELLA_LEGGINGS, "S.T.E.L.L.A.レギンス")
            builder.add(RagiumItems.STELLA_BOOTS, "S.T.E.L.L.A.ブーツ")

            builder.add(RagiumItems.STEEL_AXE, "スチールの斧")
            builder.add(RagiumItems.STEEL_HOE, "スチールのクワ")
            builder.add(RagiumItems.STEEL_PICKAXE, "スチールのツルハシ")
            builder.add(RagiumItems.STEEL_SHOVEL, "スチールのショベル")
            builder.add(RagiumItems.STEEL_SWORD, "スチールの剣")
            builder.add(RagiumItems.BUJIN, "武刃")

            builder.add(RagiumContents.Pipes.IRON, "鉄パイプ")
            builder.add(RagiumContents.Pipes.WOODEN, "木製パイプ")
            builder.add(RagiumContents.Pipes.STEEL, "鋼鉄パイプ")
            builder.add(RagiumContents.Pipes.COPPER, "銅パイプ")
            builder.add(RagiumContents.Pipes.UNIVERSAL, "万能パイプ")

            builder.add(RagiumTranslationKeys.DRUM_AMOUNT, "液体量: %s ユニット")
            builder.add(RagiumTranslationKeys.DRUM_CAPACITY, "容量: %s ユニット")
            builder.add(RagiumTranslationKeys.DRUM_FLUID, "液体: %s")
            // Enchantment
            builder.add(RagiumEnchantments.SMELTING, "精錬")
            builder.add(RagiumEnchantments.SLEDGE_HAMMER, "粉砕")
            builder.add(RagiumEnchantments.BUZZ_SAW, "製材")
            // Items
            builder.add(RagiumItems.BEE_WAX, "蜜蠟")
            builder.add(RagiumItems.BUTTER, "バター")
            builder.add(RagiumItems.CARAMEL, "キャラメル")
            builder.add(RagiumItems.CHOCOLATE, "チョコレート")
            builder.add(RagiumItems.CHOCOLATE_APPLE, "チョコリンゴ")
            builder.add(RagiumItems.CHOCOLATE_BREAD, "チョコパン")
            builder.add(RagiumItems.FLOUR, "小麦粉")
            builder.add(RagiumItems.DOUGH, "生地")
            builder.add(RagiumItems.MINCED_MEAT, "ひき肉")
            builder.add(RagiumItems.PULP, "パルプ")

            builder.add(RagiumItems.BACKPACK, "バックパック")
            builder.add(RagiumItems.BASALT_MESH, "玄武岩メッシュ")
            builder.add(RagiumItems.CRAFTER_HAMMER, "クラフターズ・ハンマー")
            builder.add(RagiumItems.CRIMSON_CRYSTAL, "深紅の結晶")
            builder.add(RagiumItems.DEEPANT, "ディーパント")
            builder.add(RagiumItems.DYNAMITE, "ダイナマイト")
            builder.add(RagiumItems.EMPTY_FLUID_CUBE, "液体キューブ（なし）")
            builder.add(RagiumItems.FILLED_FLUID_CUBE, "液体キューブ（%s）")
            builder.add(RagiumItems.ENGINE, "V8エンジン")
            builder.add(RagiumItems.FORGE_HAMMER, "鍛造ハンマー")
            builder.add(RagiumItems.HEART_OF_THE_NETHER, "地獄の心臓")
            builder.add(RagiumItems.LASER_EMITTER, "レーザーエミッタ")
            builder.add(RagiumItems.POLYMER_RESIN, "高分子樹脂")
            builder.add(RagiumItems.PROCESSOR_SOCKET, "プロセッサソケット")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumItems.RAGI_CRYSTAL_PROCESSOR, "ラギクリスタリルプロセッサ")
            builder.add(RagiumItems.REMOVER_DYNAMITE, "削除用ダイナマイト")
            builder.add(RagiumItems.RESIDUAL_COKE, "残渣油コークス")
            builder.add(RagiumItems.SLAG, "スラグ")
            builder.add(RagiumItems.SOAP_INGOT, "石鹸インゴット")
            builder.add(RagiumItems.SOLAR_PANEL, "太陽光パネル")
            builder.add(RagiumItems.TRADER_CATALOG, "行商人カタログ")
            builder.add(RagiumItems.WARPED_CRYSTAL, "歪んだ結晶")

            builder.add(RagiumTranslationKeys.CRAFTER_HAMMER_MODULE, "モジュール: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_DESTROY, "地形破壊: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_POWER, "威力: %s")
            builder.add(RagiumTranslationKeys.REMOVER_DYNAMITE_MODE, "モード: %s")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - アイテム")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - 機械")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "名称: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "ティア: %s")
            builder.add(RagiumTranslationKeys.MACHINE_FLUID_AMOUNT, "液体量: %s ユニット")
            builder.add(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, "ネットワーク上のエネルギー量: %s ユニット")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "処理コスト: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_SHOW_PREVIEW, "プレビューの表示: %s")
            builder.add(RagiumTranslationKeys.MACHINE_SLOT_COUNTS, "入力/出力スロット数: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TANK_CAPACITY, "各液体タンクの容量: %s バケツ")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません; %s (座標 %s)")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "構造物は有効です！")
            // Machine Tier
            builder.add(HTMachineTier.PRIMITIVE, "簡易", "簡易型%s")
            builder.add(HTMachineTier.BASIC, "基本", "基本型%s")
            builder.add(HTMachineTier.ADVANCED, "発展", "発展型%s")
            // Machine Type
            builder.add(
                RagiumMachineKeys.BIOMASS_FERMENTER,
                "バイオマス発酵槽",
                "コンポスターに入れられるアイテムからバイオマスを生産する",
            )
            builder.add(RagiumMachineKeys.CANNING_MACHINE, "缶詰機", "液体キューブに液体を出し入れできる")
            builder.add(RagiumMachineKeys.DRAIN, "排水溝", "各面から液体を吸い取る")
            builder.add(RagiumMachineKeys.FLUID_DRILL, "液体採掘機", "特定のバイオームから液体を汲み上げる")

            builder.add(RagiumMachineKeys.COMBUSTION_GENERATOR, "燃焼発電機", "液体燃料から発電する")
            builder.add(RagiumMachineKeys.SOLAR_PANEL, "太陽光発電機", "日中に発電する")
            builder.add(RagiumMachineKeys.STEAM_GENERATOR, "蒸気発電機", "水と下部の熱源から発電する")
            builder.add(RagiumMachineKeys.THERMAL_GENERATOR, "地熱発電機", "高温の液体から発電する")
            // builder.add(RagiumMachineKeys.WATER_GENERATOR, "水力発電機")

            builder.add(RagiumMachineKeys.ALLOY_FURNACE, "合金かまど", "二つの素材を一つに焼き上げる")
            builder.add(RagiumMachineKeys.ASSEMBLER, "組立機", "悪魔博士，アッセンブル！")
            builder.add(RagiumMachineKeys.BLAST_FURNACE, "大型高炉", "複数の素材を一つに焼き上げる")
            builder.add(RagiumMachineKeys.CHEMICAL_REACTOR, "化学反応槽", "Are You Ready?")
            builder.add(RagiumMachineKeys.COMPRESSOR, "圧縮機")
            builder.add(RagiumMachineKeys.DISTILLATION_TOWER, "蒸留塔", "原油を処理する")
            builder.add(RagiumMachineKeys.ELECTROLYZER, "電解槽", "エレキ オン")
            builder.add(RagiumMachineKeys.EXTRACTOR, "抽出器")
            builder.add(RagiumMachineKeys.GRINDER, "粉砕機")
            builder.add(RagiumMachineKeys.GROWTH_CHAMBER, "成長チャンバー")
            builder.add(RagiumMachineKeys.LASER_TRANSFORMER, "レーザー変換機")
            builder.add(RagiumMachineKeys.METAL_FORMER, "金属加工機", "It's High Quality.")
            builder.add(RagiumMachineKeys.MULTI_SMELTER, "並列精錬機", "複数のアイテムを一度に製錬する")
            builder.add(RagiumMachineKeys.MIXER, "ミキサー", "ゲノミクス")
            builder.add(RagiumMachineKeys.ROCK_GENERATOR, "岩石生成機", "岩石を生成する")
            builder.add(RagiumMachineKeys.SAW_MILL, "製材機", "より効率的に原木を加工する")
            // Material
            builder.add(RagiumMaterialKeys.CRUDE_RAGINITE, "粗製ラギナイト")
            builder.add(RagiumMaterialKeys.RAGI_ALLOY, "ラギ合金")
            builder.add(RagiumMaterialKeys.ALKALI, "アルカリ")
            builder.add(RagiumMaterialKeys.ASH, "灰")
            builder.add(RagiumMaterialKeys.COPPER, "銅")
            builder.add(RagiumMaterialKeys.IRON, "鉄")
            builder.add(RagiumMaterialKeys.NITER, "硝石")
            builder.add(RagiumMaterialKeys.SULFUR, "硫黄")
            builder.add(RagiumMaterialKeys.WOOD, "木材")

            builder.add(RagiumMaterialKeys.RAGINITE, "ラギナイト")
            builder.add(RagiumMaterialKeys.RAGI_STEEL, "ラギスチール")
            builder.add(RagiumMaterialKeys.FLUORITE, "蛍石")
            builder.add(RagiumMaterialKeys.GOLD, "金")
            builder.add(RagiumMaterialKeys.PLASTIC, "プラスチック")
            builder.add(RagiumMaterialKeys.SILICON, "シリコン")
            builder.add(RagiumMaterialKeys.STEEL, "スチール")

            builder.add(RagiumMaterialKeys.RAGI_CRYSTAL, "ラギクリスタリル")
            builder.add(RagiumMaterialKeys.REFINED_RAGI_STEEL, "精製ラギスチール")
            builder.add(RagiumMaterialKeys.ALUMINUM, "アルミニウム")
            builder.add(RagiumMaterialKeys.BAUXITE, "ボーキサイト")
            builder.add(RagiumMaterialKeys.CRYOLITE, "氷晶石")
            builder.add(RagiumMaterialKeys.DEEP_STEEL, "深層鋼")
            builder.add(RagiumMaterialKeys.ENGINEERING_PLASTIC, "エンジニアリングプラスチック")
            builder.add(RagiumMaterialKeys.STELLA, "S.T.E.L.L.A.")

            builder.add(RagiumMaterialKeys.RAGIUM, "ラギウム")

            builder.add(RagiumMaterialKeys.COAL, "石炭")
            builder.add(RagiumMaterialKeys.DIAMOND, "ダイアモンド")
            builder.add(RagiumMaterialKeys.EMERALD, "エメラルド")
            builder.add(RagiumMaterialKeys.PERIDOT, "ペリドット")
            builder.add(RagiumMaterialKeys.QUARTZ, "水晶")
            builder.add(RagiumMaterialKeys.SAPPHIRE, "サファイア")
            builder.add(RagiumMaterialKeys.RUBY, "ルビー")

            builder.add(RagiumMaterialKeys.IRIDIUM, "イリジウム")
            builder.add(RagiumMaterialKeys.LEAD, "鉛")
            builder.add(RagiumMaterialKeys.NICKEL, "ニッケル")
            builder.add(RagiumMaterialKeys.PLATINUM, "白金")
            builder.add(RagiumMaterialKeys.SILVER, "銀")
            builder.add(RagiumMaterialKeys.TIN, "スズ")
            builder.add(RagiumMaterialKeys.TUNGSTEN, "タングステン")
            builder.add(RagiumMaterialKeys.ZINC, "亜鉛")
            // Tag Prefix
            builder.add(HTTagPrefix.DEEP_ORE, "深層%s鉱石")
            builder.add(HTTagPrefix.END_ORE, "エンド%s鉱石")
            builder.add(HTTagPrefix.DUST, "%sの粉")
            builder.add(HTTagPrefix.GEAR, "%sの歯車")
            builder.add(HTTagPrefix.GEM, "%s")
            builder.add(HTTagPrefix.INGOT, "%sインゴット")
            builder.add(HTTagPrefix.NUGGET, "%sのナゲット")
            builder.add(HTTagPrefix.NETHER_ORE, "ネザー%s鉱石")
            builder.add(HTTagPrefix.ORE, "%s鉱石")
            builder.add(HTTagPrefix.PLATE, "%s板")
            builder.add(HTTagPrefix.RAW_MATERIAL, "%sの原石")
            builder.add(HTTagPrefix.ROD, "%s棒")
            builder.add(HTTagPrefix.STORAGE_BLOCK, "%sブロック")
        }
    }
}

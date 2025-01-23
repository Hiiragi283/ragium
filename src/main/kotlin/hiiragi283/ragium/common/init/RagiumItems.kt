package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.component.HTRadioactiveComponent
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.item.HTAmbrosiaItem
import hiiragi283.ragium.common.item.HTCraftingToolItem
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumItems {
    @JvmField
    val REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    init {
        registerBlockItems()

        Circuits.entries
        Plastics.entries
        Radioactives.entries
    }

    @JvmStatic
    private fun registerBlockItems() {
        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            REGISTER.registerSimpleBlockItem(
                ore.id.path,
                ore,
                itemProperty().name(ore.oreVariant.createText(ore.material)),
            )
        }

        buildList {
            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Coils.entries)

            addAll(RagiumBlocks.Drums.entries)

            addAll(RagiumBlocks.Decorations.entries)
            addAll(RagiumBlocks.LEDBlocks.entries)
        }.map(HTBlockContent::holder)
            .forEach(REGISTER::registerSimpleBlockItem)

        buildList {
            add(RagiumBlocks.SHAFT)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)

            add(RagiumBlocks.MANUAL_GRINDER)

            add(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
        }.forEach(REGISTER::registerSimpleBlockItem)
    }

    //    Materials    //

    enum class Dusts(override val material: HTMaterialKey, override val parentPrefix: HTTagPrefix? = null) :
        HTItemContent.Material {
        // Vanilla
        COPPER(RagiumMaterialKeys.COPPER, HTTagPrefix.INGOT),
        IRON(RagiumMaterialKeys.IRON, HTTagPrefix.INGOT),
        LAPIS(RagiumMaterialKeys.LAPIS, HTTagPrefix.GEM),
        QUARTZ(RagiumMaterialKeys.QUARTZ, HTTagPrefix.GEM),
        GOLD(RagiumMaterialKeys.GOLD, HTTagPrefix.INGOT),
        DIAMOND(RagiumMaterialKeys.DIAMOND, HTTagPrefix.GEM),
        EMERALD(RagiumMaterialKeys.EMERALD, HTTagPrefix.GEM),

        // Ragium
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL, HTTagPrefix.GEM),

        // Other
        WOOD(RagiumMaterialKeys.WOOD),
        ASH(RagiumMaterialKeys.ASH),
        CARBON(RagiumMaterialKeys.CARBON),
        ALKALI(RagiumMaterialKeys.ALKALI),

        NITER(RagiumMaterialKeys.NITER),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        BAUXITE(RagiumMaterialKeys.BAUXITE),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM, HTTagPrefix.INGOT),
        ;

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem("${name.lowercase()}_dust")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.DUST
    }

    @JvmField
    val BEE_WAX: DeferredItem<Item> = REGISTER.registerItem("bee_wax", ::HoneycombItem, itemProperty())

    @JvmField
    val DEEPANT: DeferredItem<Item> = REGISTER.registerSimpleItem("deepant")

    @JvmField
    val LUMINESCENCE_DUST: DeferredItem<Item> = REGISTER.registerSimpleItem("luminescence_dust")

    @JvmField
    val OTHER_DUSTS: List<DeferredItem<Item>> = listOf(
        BEE_WAX,
        DEEPANT,
        LUMINESCENCE_DUST,
    )

    enum class Gears(override val material: HTMaterialKey, override val parentPrefix: HTTagPrefix = HTTagPrefix.INGOT) :
        HTItemContent.Material {
        // Vanilla
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        GOLD(RagiumMaterialKeys.GOLD),
        DIAMOND(RagiumMaterialKeys.DIAMOND, HTTagPrefix.GEM),
        EMERALD(RagiumMaterialKeys.EMERALD, HTTagPrefix.GEM),

        // Ragium
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),

        // Steel
        STEEL(RagiumMaterialKeys.STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        DRAGONIUM(RagiumMaterialKeys.DRAGONIUM),
        ;

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem("${name.lowercase()}_gear")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEAR
    }

    enum class RawResources(override val tagPrefix: HTTagPrefix, override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        RAW_CRUDE_RAGINITE(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.CRUDE_RAGINITE),

        // tier2
        RAW_RAGINITE(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.RAGINITE),

        // tier 3
        RAGI_CRYSTAL(HTTagPrefix.GEM, RagiumMaterialKeys.RAGI_CRYSTAL),
        CRYOLITE(HTTagPrefix.GEM, RagiumMaterialKeys.CRYOLITE),
        FLUORITE(HTTagPrefix.GEM, RagiumMaterialKeys.FLUORITE),
        ;

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem(name.lowercase())
    }

    @JvmField
    val GLASS_SHARD: DeferredItem<Item> = REGISTER.registerSimpleItem("glass_shard")

    @JvmField
    val CRIMSON_CRYSTAL: DeferredItem<Item> = REGISTER.registerSimpleItem("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: DeferredItem<Item> = REGISTER.registerSimpleItem("warped_crystal")

    @JvmField
    val OBSIDIAN_TEAR: DeferredItem<Item> = REGISTER.registerSimpleItem("obsidian_tear")

    @JvmField
    val SLAG: DeferredItem<Item> = REGISTER.registerSimpleItem("slag")

    @JvmField
    val COAL_CHIP: DeferredItem<Item> = REGISTER.registerSimpleItem("coal_chip")

    @JvmField
    val RESIDUAL_COKE: DeferredItem<Item> = REGISTER.registerSimpleItem("residual_coke")

    @JvmField
    val CALCIUM_CARBIDE: DeferredItem<Item> = REGISTER.registerSimpleItem("calcium_carbide")

    @JvmField
    val OTHER_RESOURCES: List<DeferredItem<Item>> = listOf(
        GLASS_SHARD,
        CRIMSON_CRYSTAL,
        WARPED_CRYSTAL,
        OBSIDIAN_TEAR,
        SLAG,
        COAL_CHIP,
        RESIDUAL_COKE,
        CALCIUM_CARBIDE,
    )

    enum class Ingots(override val material: HTMaterialKey) : HTItemContent.Material {
        // Vanilla
        // Ragium
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        RAGIUM(RagiumMaterialKeys.RAGIUM),

        // Steel
        STEEL(RagiumMaterialKeys.STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        DRAGONIUM(RagiumMaterialKeys.DRAGONIUM),

        // Other
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),

        ECHORIUM(RagiumMaterialKeys.ECHORIUM),
        FIERIUM(RagiumMaterialKeys.FIERIUM),
        ;

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem("${name.lowercase()}_ingot")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val SOAP: DeferredItem<Item> = REGISTER.registerSimpleItem("soap")

    @JvmField
    val OTHER_INGOTS: List<DeferredItem<Item>> = listOf(
        RAGI_ALLOY_COMPOUND,
        SOAP,
    )

    enum class Rods(override val material: HTMaterialKey) : HTItemContent.Material {
        // Vanilla
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        GOLD(RagiumMaterialKeys.GOLD),

        // Steel
        STEEL(RagiumMaterialKeys.STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        DRAGONIUM(RagiumMaterialKeys.DRAGONIUM),
        ;

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem("${name.lowercase()}_rod")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.ROD
        override val parentPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    @JvmField
    val MATERIALS: List<HTItemContent.Material> = buildList {
        addAll(Dusts.entries)
        addAll(Gears.entries)
        addAll(RawResources.entries)
        addAll(Ingots.entries)
        addAll(Rods.entries)
    }

    //    Foods    //

    @JvmStatic
    private fun registerFood(name: String, foodProperties: FoodProperties): DeferredItem<Item> =
        REGISTER.registerSimpleItem(name, itemProperty().food(foodProperties))

    @JvmField
    val SWEET_BERRIES_CAKE_PIECE: DeferredItem<Item> =
        registerFood("sweet_berries_cake_piece", RagiumFoods.SWEET_BERRIES_CAKE)

    @JvmField
    val MELON_PIE: DeferredItem<Item> = registerFood("melon_pie", RagiumFoods.MELON_PIE)

    @JvmField
    val BUTTER: DeferredItem<Item> = registerFood("butter", Foods.APPLE)

    @JvmField
    val CARAMEL: DeferredItem<Item> = registerFood("caramel", Foods.DRIED_KELP)

    @JvmField
    val DOUGH: DeferredItem<Item> = REGISTER.registerSimpleItem("dough")

    @JvmField
    val FLOUR: DeferredItem<Item> = REGISTER.registerSimpleItem("flour")

    @JvmField
    val CHOCOLATE: DeferredItem<Item> = registerFood("chocolate", RagiumFoods.CHOCOLATE)

    @JvmField
    val CHOCOLATE_APPLE: DeferredItem<Item> = registerFood("chocolate_apple", Foods.COOKED_CHICKEN)

    @JvmField
    val CHOCOLATE_BREAD: DeferredItem<Item> = registerFood("chocolate_bread", Foods.COOKED_BEEF)

    @JvmField
    val CHOCOLATE_COOKIE: DeferredItem<Item> = registerFood("chocolate_cookie", Foods.COOKIE)

    @JvmField
    val CINNAMON_STICK: DeferredItem<Item> = REGISTER.registerSimpleItem("cinnamon_stick")

    @JvmField
    val CINNAMON_POWDER: DeferredItem<Item> = REGISTER.registerSimpleItem("cinnamon_powder")

    @JvmField
    val CINNAMON_ROLL: DeferredItem<Item> = registerFood("cinnamon_roll", Foods.COOKED_BEEF)

    @JvmField
    val TALLOW: DeferredItem<Item> = REGISTER.registerSimpleItem("tallow")

    @JvmField
    val MINCED_MEAT: DeferredItem<Item> = REGISTER.registerSimpleItem("minced_meat")

    @JvmField
    val MEAT_INGOT: DeferredItem<Item> = registerFood("meat_ingot", Foods.BEEF)

    @JvmField
    val COOKED_MEAT_INGOT: DeferredItem<Item> = registerFood("cooked_meat_ingot", Foods.COOKED_BEEF)

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<Item> = registerFood("canned_cooked_meat", RagiumFoods.CANNED_COOKED_MEAT)

    @JvmField
    val AMBROSIA: DeferredItem<Item> =
        REGISTER.registerItem(
            "ambrosia",
            ::HTAmbrosiaItem,
            itemProperty().food(RagiumFoods.AMBROSIA).rarity(Rarity.EPIC),
        )

    @JvmField
    val FOODS: List<DeferredItem<Item>> = listOf(
        // cake
        SWEET_BERRIES_CAKE_PIECE,
        MELON_PIE,
        // ingredient
        BUTTER,
        CARAMEL,
        DOUGH,
        FLOUR,
        // chocolate
        CHOCOLATE,
        CHOCOLATE_APPLE,
        CHOCOLATE_BREAD,
        CHOCOLATE_COOKIE,
        // cinnamon
        CINNAMON_STICK,
        CINNAMON_POWDER,
        CINNAMON_ROLL,
        // meat
        TALLOW,
        MINCED_MEAT,
        MEAT_INGOT,
        COOKED_MEAT_INGOT,
        CANNED_COOKED_MEAT,
        // end-contents
        AMBROSIA,
    )

    //    Tools    //

    @JvmField
    val FORGE_HAMMER: DeferredItem<HTCraftingToolItem> =
        REGISTER.registerItem("forge_hammer", ::HTCraftingToolItem, itemProperty().durability(63))

    @JvmField
    val SLOT_LOCK: DeferredItem<Item> =
        REGISTER.registerSimpleItem("slot_lock")

    //    Circuits    //

    enum class Circuits(override val machineTier: HTMachineTier) : HTItemContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem("${name.lowercase()}_circuit")
    }

    //    Press Molds    //

    @JvmField
    val GEAR_PRESS_MOLD: DeferredItem<Item> = REGISTER.registerSimpleItem("gear_press_mold")

    @JvmField
    val PLATE_PRESS_MOLD: DeferredItem<Item> = REGISTER.registerSimpleItem("plate_press_mold")

    @JvmField
    val ROD_PRESS_MOLD: DeferredItem<Item> = REGISTER.registerSimpleItem("rod_press_mold")

    @JvmField
    val PRESS_MOLDS: List<DeferredItem<Item>> = listOf(GEAR_PRESS_MOLD, PLATE_PRESS_MOLD, ROD_PRESS_MOLD)

    //    Catalysts    //

    @JvmField
    val HEATING_CATALYST: DeferredItem<Item> = REGISTER.registerSimpleItem("heating_catalyst")

    @JvmField
    val COOLING_CATALYST: DeferredItem<Item> = REGISTER.registerSimpleItem("cooling_catalyst")

    @JvmField
    val OXIDIZATION_CATALYST: DeferredItem<Item> = REGISTER.registerSimpleItem("oxidization_catalyst")

    @JvmField
    val REDUCTION_CATALYST: DeferredItem<Item> = REGISTER.registerSimpleItem("reduction_catalyst")

    @JvmField
    val DEHYDRATION_CATALYST: DeferredItem<Item> = REGISTER.registerSimpleItem("dehydration_catalyst")

    @JvmField
    val CATALYSTS: List<DeferredItem<Item>> = listOf(
        HEATING_CATALYST,
        COOLING_CATALYST,
        OXIDIZATION_CATALYST,
        REDUCTION_CATALYST,
        DEHYDRATION_CATALYST,
    )

    //    Plastics    //

    enum class Plastics(override val machineTier: HTMachineTier) : HTItemContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem("${name.lowercase()}_plastic")
    }

    //    Ingredients    //

    @JvmField
    val CIRCUIT_BOARD: DeferredItem<Item> = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val ENGINE: DeferredItem<Item> = REGISTER.registerSimpleItem("engine")

    @JvmField
    val LASER_EMITTER: DeferredItem<Item> = REGISTER.registerSimpleItem("laser_emitter")

    @JvmField
    val LED: DeferredItem<Item> = REGISTER.registerSimpleItem("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = REGISTER.registerSimpleItem("solar_panel")

    @JvmField
    val STELLA_PLATE: DeferredItem<Item> = REGISTER.registerSimpleItem("stella_plate")

    @JvmField
    val RAGI_TICKET: DeferredItem<Item> = REGISTER.registerSimpleItem("ragi_ticket", itemProperty().rarity(Rarity.EPIC))

    @JvmField
    val INGREDIENTS: List<DeferredItem<Item>> = buildList {
        // parts
        add(CIRCUIT_BOARD)
        add(ENGINE)
        add(LASER_EMITTER)
        add(LED)
        add(SOLAR_PANEL)
        add(STELLA_PLATE)
        // misc
        add(RAGI_TICKET)
    }

    //    Radioactives    //

    enum class Radioactives(val level: HTRadioactiveComponent) : HTItemContent {
        URANIUM_FUEL(HTRadioactiveComponent.MEDIUM) {
            override fun getProperty(): Item.Properties = itemProperty().durability(1024)
        },
        PLUTONIUM_FUEL(HTRadioactiveComponent.HIGH) {
            override fun getProperty(): Item.Properties = itemProperty().durability(1024)
        },
        YELLOW_CAKE(HTRadioactiveComponent.MEDIUM),
        YELLOW_CAKE_PIECE(HTRadioactiveComponent.LOW) {
            override fun getProperty(): Item.Properties = itemProperty().food(RagiumFoods.YELLOW_CAKE_PIECE)
        },
        NUCLEAR_WASTE(HTRadioactiveComponent.LOW),
        ;

        protected open fun getProperty(): Item.Properties = itemProperty()

        override val holder: DeferredItem<out Item> = REGISTER.registerSimpleItem(name.lowercase(), getProperty())
    }
}

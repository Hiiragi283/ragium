package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.common.item.HTAmbrosiaItem
import hiiragi283.ragium.common.item.HTCraftingToolItem
import hiiragi283.ragium.common.item.HTDynamiteItem
import hiiragi283.ragium.common.item.HTSilkyPickaxeItem
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.*
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumItems {
    @JvmField
    val REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    @JvmStatic
    private fun register(name: String, properties: Item.Properties = itemProperty()): DeferredItem<Item> =
        REGISTER.registerSimpleItem(name, properties)

    @JvmStatic
    private fun <T : Item> register(
        name: String,
        factory: (Item.Properties) -> T,
        properties: Item.Properties = itemProperty(),
    ): DeferredItem<T> = REGISTER.registerItem(name, factory, properties)

    @JvmField
    val MATERIAL_ITEMS: HTTable<HTTagPrefix, HTMaterialKey, DeferredItem<out Item>> = buildTable {
        fun register(prefix: HTTagPrefix, material: HTMaterialKey) {
            put(
                prefix,
                material,
                register(
                    prefix.createPath(material),
                    itemProperty().name(prefix.createText(material)),
                ),
            )
        }

        // Dusts
        register(HTTagPrefix.DUST, VanillaMaterials.COPPER)
        register(HTTagPrefix.DUST, VanillaMaterials.IRON)
        register(HTTagPrefix.DUST, VanillaMaterials.LAPIS)
        register(HTTagPrefix.DUST, VanillaMaterials.QUARTZ)
        register(HTTagPrefix.DUST, VanillaMaterials.GOLD)
        register(HTTagPrefix.DUST, VanillaMaterials.DIAMOND)
        register(HTTagPrefix.DUST, VanillaMaterials.EMERALD)

        register(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
        register(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL)
        register(HTTagPrefix.DUST, CommonMaterials.FLUORITE)

        register(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
        register(HTTagPrefix.DUST, CommonMaterials.ALUMINUM)
        register(HTTagPrefix.DUST, CommonMaterials.ASH)
        register(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
        register(HTTagPrefix.DUST, CommonMaterials.SALTPETER)
        register(HTTagPrefix.DUST, CommonMaterials.WOOD)
        // Raws
        register(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)

        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.BAUXITE)
        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.SALTPETER)
        register(HTTagPrefix.RAW_MATERIAL, VanillaMaterials.REDSTONE)
        // Ingots
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)

        register(HTTagPrefix.INGOT, CommonMaterials.STEEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)

        register(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
        register(HTTagPrefix.INGOT, RagiumMaterials.ECHORIUM)
        // Gems
        register(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
        register(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
        register(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)

        register(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL)
        // Gears
        register(HTTagPrefix.GEAR, VanillaMaterials.COPPER)
        register(HTTagPrefix.GEAR, VanillaMaterials.IRON)
        register(HTTagPrefix.GEAR, VanillaMaterials.GOLD)
        register(HTTagPrefix.GEAR, VanillaMaterials.DIAMOND)
        register(HTTagPrefix.GEAR, VanillaMaterials.EMERALD)

        register(HTTagPrefix.GEAR, CommonMaterials.STEEL)
        register(HTTagPrefix.GEAR, RagiumMaterials.DEEP_STEEL)
        register(HTTagPrefix.GEAR, VanillaMaterials.NETHERITE)
        // Rods
        register(HTTagPrefix.ROD, VanillaMaterials.COPPER)
        register(HTTagPrefix.ROD, VanillaMaterials.IRON)
        register(HTTagPrefix.ROD, VanillaMaterials.GOLD)

        register(HTTagPrefix.ROD, CommonMaterials.STEEL)
        register(HTTagPrefix.ROD, RagiumMaterials.DEEP_STEEL)
        register(HTTagPrefix.ROD, VanillaMaterials.NETHERITE)
    }

    @JvmStatic
    fun getMaterialMap(prefix: HTTagPrefix): Map<HTMaterialKey, DeferredItem<out Item>> = MATERIAL_ITEMS.row(prefix)

    @JvmStatic
    fun getMaterialItems(prefix: HTTagPrefix): Collection<DeferredItem<out Item>> = getMaterialMap(prefix).values

    @JvmStatic
    fun getMaterialItem(prefix: HTTagPrefix, material: HTMaterialKey): DeferredItem<out Item> =
        getMaterialMap(prefix)[material] ?: error("Unregistered material item: ${prefix.createPath(material)}")

    init {
        registerBlockItems()
    }

    @JvmStatic
    private fun registerBlockItems() {
        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            REGISTER.registerSimpleBlockItem(
                ore,
                itemProperty().name(variant.createText(key)),
            )
        }

        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, storage: DeferredBlock<Block>) ->
            REGISTER.registerSimpleBlockItem(
                storage,
                itemProperty().name(HTTagPrefix.STORAGE_BLOCK.createText(key)),
            )
        }

        buildList {
            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Burners.entries)

            addAll(RagiumBlocks.Drums.entries)
        }.forEach { content: HTBlockContent.Tier ->
            REGISTER.registerSimpleBlockItem(
                content.holder,
                itemProperty().name(content.machineTier.createPrefixedText(content.translationKey)),
            )
        }

        buildList {
            addAll(RagiumBlocks.Decorations.entries)
            addAll(RagiumBlocks.LEDBlocks.entries)
        }.map(HTBlockContent::holder)
            .forEach(REGISTER::registerSimpleBlockItem)

        buildList {
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SLAG_BLOCK)

            add(RagiumBlocks.SHAFT)
            addAll(RagiumBlocks.GLASSES)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)

            add(RagiumBlocks.MANUAL_GRINDER)
            add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

            addAll(RagiumBlocks.ADDONS)
        }.forEach(REGISTER::registerSimpleBlockItem)
    }

    //    Materials    //

    @JvmField
    val BEE_WAX: DeferredItem<Item> = register("bee_wax", ::HoneycombItem, itemProperty())

    @JvmField
    val OTHER_DUSTS: List<DeferredItem<Item>> = listOf(
        BEE_WAX,
    )

    @JvmField
    val SILKY_CRYSTAL: DeferredItem<Item> = register("silky_crystal")

    @JvmField
    val CRIMSON_CRYSTAL: DeferredItem<Item> = register("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: DeferredItem<Item> = register("warped_crystal")

    @JvmField
    val OBSIDIAN_TEAR: DeferredItem<Item> = register("obsidian_tear")

    @JvmField
    val OTHER_GEMS: List<DeferredItem<Item>> = listOf(
        SILKY_CRYSTAL,
        CRIMSON_CRYSTAL,
        WARPED_CRYSTAL,
        OBSIDIAN_TEAR,
    )

    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("ragi_alloy_compound")

    @JvmField
    val SOAP: DeferredItem<Item> = register("soap")

    @JvmField
    val SLAG: DeferredItem<Item> = register("slag")

    @JvmField
    val OTHER_RESOURCES: List<DeferredItem<Item>> = listOf(
        RAGI_ALLOY_COMPOUND,
        SOAP,
        SLAG,
    )

    //    Foods    //

    @JvmStatic
    private fun registerFood(name: String, foodProperties: FoodProperties): DeferredItem<Item> =
        register(name, itemProperty().food(foodProperties))

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
    val DOUGH: DeferredItem<Item> = register("dough")

    @JvmField
    val FLOUR: DeferredItem<Item> = register("flour")

    @JvmField
    val CHOCOLATE: DeferredItem<Item> = registerFood("chocolate", RagiumFoods.CHOCOLATE)

    @JvmField
    val CHOCOLATE_APPLE: DeferredItem<Item> = registerFood("chocolate_apple", Foods.COOKED_CHICKEN)

    @JvmField
    val CHOCOLATE_BREAD: DeferredItem<Item> = registerFood("chocolate_bread", Foods.COOKED_BEEF)

    @JvmField
    val CHOCOLATE_COOKIE: DeferredItem<Item> = registerFood("chocolate_cookie", Foods.COOKIE)

    @JvmField
    val CINNAMON_STICK: DeferredItem<Item> = register("cinnamon_stick")

    @JvmField
    val CINNAMON_POWDER: DeferredItem<Item> = register("cinnamon_powder")

    @JvmField
    val CINNAMON_ROLL: DeferredItem<Item> = registerFood("cinnamon_roll", Foods.COOKED_BEEF)

    @JvmField
    val MINCED_MEAT: DeferredItem<Item> = register("minced_meat")

    @JvmField
    val MEAT_INGOT: DeferredItem<Item> = registerFood("meat_ingot", Foods.BEEF)

    @JvmField
    val COOKED_MEAT_INGOT: DeferredItem<Item> = registerFood("cooked_meat_ingot", Foods.COOKED_BEEF)

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<Item> = registerFood("canned_cooked_meat", RagiumFoods.CANNED_COOKED_MEAT)

    @JvmField
    val AMBROSIA: DeferredItem<Item> =
        register(
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
        register("forge_hammer", ::HTCraftingToolItem, itemProperty().durability(63))

    @JvmField
    val SILKY_PICKAXE: DeferredItem<HTSilkyPickaxeItem> =
        register("silky_pickaxe", ::HTSilkyPickaxeItem)

    @JvmField
    val DYNAMITE: DeferredItem<HTDynamiteItem> =
        register("dynamite", ::HTDynamiteItem)

    @JvmField
    val SLOT_LOCK: DeferredItem<Item> =
        register("slot_lock")

    //    Circuits    //

    val BASIC_CIRCUIT: DeferredItem<Item> = register(
        "basic_circuit",
        itemProperty().name(HTMachineTier.BASIC.createPrefixedText(RagiumTranslationKeys.CIRCUIT)),
    )

    val ADVANCED_CIRCUIT: DeferredItem<Item> = register(
        "advanced_circuit",
        itemProperty().name(HTMachineTier.ADVANCED.createPrefixedText(RagiumTranslationKeys.CIRCUIT)),
    )

    val ELITE_CIRCUIT: DeferredItem<Item> = register(
        "elite_circuit",
        itemProperty().name(HTMachineTier.ELITE.createPrefixedText(RagiumTranslationKeys.CIRCUIT)),
    )

    val ULTIMATE_CIRCUIT: DeferredItem<Item> = register(
        "ultimate_circuit",
        itemProperty().name(HTMachineTier.ULTIMATE.createPrefixedText(RagiumTranslationKeys.CIRCUIT)),
    )

    @JvmField
    val CIRCUITS: List<DeferredItem<Item>> = listOf(
        BASIC_CIRCUIT,
        ADVANCED_CIRCUIT,
        ELITE_CIRCUIT,
        ULTIMATE_CIRCUIT,
    )

    //    Press Molds    //

    @JvmField
    val GEAR_PRESS_MOLD: DeferredItem<Item> = register("gear_press_mold")

    @JvmField
    val PLATE_PRESS_MOLD: DeferredItem<Item> = register("plate_press_mold")

    @JvmField
    val ROD_PRESS_MOLD: DeferredItem<Item> = register("rod_press_mold")

    @JvmField
    val WIRE_PRESS_MOLD: DeferredItem<Item> = register("wire_press_mold")

    @JvmField
    val PRESS_MOLDS: List<DeferredItem<Item>> =
        listOf(GEAR_PRESS_MOLD, PLATE_PRESS_MOLD, ROD_PRESS_MOLD, WIRE_PRESS_MOLD)

    //    Reagents    //

    @JvmField
    val ALKALI_REAGENT: DeferredItem<Item> = register("alkali_reagent")

    @JvmField
    val BLAZE_REAGENT: DeferredItem<Item> = register("blaze_reagent")

    @JvmField
    val CREEPER_REAGENT: DeferredItem<Item> = register("creeper_reagent")

    @JvmField
    val DEEPANT_REAGENT: DeferredItem<Item> = register("deepant_reagent")

    @JvmField
    val ENDER_REAGENT: DeferredItem<Item> = register("ender_reagent")

    @JvmField
    val GLOW_REAGENT: DeferredItem<Item> = register("glow_reagent")

    @JvmField
    val PRISMARINE_REAGENT: DeferredItem<Item> = register("prismarine_reagent")

    @JvmField
    val RAGIUM_REAGENT: DeferredItem<Item> = register("ragium_reagent")

    @JvmField
    val SCULK_REAGENT: DeferredItem<Item> = register("sculk_reagent")

    @JvmField
    val SOUL_REAGENT: DeferredItem<Item> = register("soul_reagent")

    @JvmField
    val WITHER_REAGENT: DeferredItem<Item> = register("wither_reagent")

    @JvmField
    val REAGENTS: List<DeferredItem<Item>> = listOf(
        ALKALI_REAGENT,
        BLAZE_REAGENT,
        CREEPER_REAGENT,
        DEEPANT_REAGENT,
        ENDER_REAGENT,
        GLOW_REAGENT,
        PRISMARINE_REAGENT,
        RAGIUM_REAGENT,
        SCULK_REAGENT,
        SOUL_REAGENT,
        WITHER_REAGENT,
    )

    //    Ingredients    //

    @JvmField
    val CRUDE_OIL_BUCKET: DeferredItem<BucketItem> = register(
        "crude_oil_bucket",
        { properties: Item.Properties -> BucketItem(RagiumFluids.CRUDE_OIL.get(), properties) },
        itemProperty().craftRemainder(Items.BUCKET).stacksTo(1),
    )

    @JvmField
    val POLYMER_RESIN: DeferredItem<Item> = register("polymer_resin")

    @JvmField
    val PLASTIC_PLATE: DeferredItem<Item> = register("plastic_plate")

    @JvmField
    val CIRCUIT_BOARD: DeferredItem<Item> = register("circuit_board")

    @JvmField
    val ENGINE: DeferredItem<Item> = register("engine")

    @JvmField
    val LED: DeferredItem<Item> = register("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = register("solar_panel")

    @JvmField
    val RAGI_TICKET: DeferredItem<Item> = register("ragi_ticket", itemProperty().rarity(Rarity.EPIC))

    @JvmField
    val INGREDIENTS: List<DeferredItem<out Item>> = buildList {
        // bucket
        add(CRUDE_OIL_BUCKET)
        // parts
        add(POLYMER_RESIN)
        add(PLASTIC_PLATE)
        add(CIRCUIT_BOARD)
        add(ENGINE)
        add(LED)
        add(SOLAR_PANEL)
        // misc
        add(RAGI_TICKET)
    }

    //    Radioactives    //

    val YELLOW_CAKE: DeferredItem<Item> =
        register("yellow_cake")

    val YELLOW_CAKE_PIECE: DeferredItem<Item> =
        registerFood("yellow_cake_piece", RagiumFoods.YELLOW_CAKE_PIECE)

    val URANIUM_FUEL: DeferredItem<Item> =
        register("uranium_fuel", itemProperty().durability(255))

    val NUCLEAR_WASTE: DeferredItem<Item> =
        register("nuclear_waste")

    val PLUTONIUM_FUEL: DeferredItem<Item> =
        register("plutonium_fuel", itemProperty().durability(1023))

    @JvmField
    val RADIOACTIVES: List<DeferredItem<Item>> = listOf(
        YELLOW_CAKE,
        YELLOW_CAKE_PIECE,
        URANIUM_FUEL,
        NUCLEAR_WASTE,
        PLUTONIUM_FUEL,
    )
}

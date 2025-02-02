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
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumItems {
    @JvmField
    val REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    @JvmField
    val MATERIAL_ITEMS: HTTable<HTTagPrefix, HTMaterialKey, DeferredItem<out Item>> = buildTable {
        fun register(prefix: HTTagPrefix, material: HTMaterialKey) {
            put(
                prefix,
                material,
                REGISTER.registerSimpleItem(
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

        register(HTTagPrefix.DUST, RagiumMaterials.CRUDE_RAGINITE)
        register(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
        register(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL)

        register(HTTagPrefix.DUST, CommonMaterials.ALUMINUM)
        register(HTTagPrefix.DUST, CommonMaterials.CHROMIUM)
        register(HTTagPrefix.DUST, CommonMaterials.NICKEL)

        register(HTTagPrefix.DUST, CommonMaterials.ASH)
        register(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
        register(HTTagPrefix.DUST, CommonMaterials.CARBON)
        register(HTTagPrefix.DUST, CommonMaterials.NITER)
        register(HTTagPrefix.DUST, CommonMaterials.SALT)
        register(HTTagPrefix.DUST, CommonMaterials.SULFUR)
        register(HTTagPrefix.DUST, CommonMaterials.WOOD)
        // Raws
        register(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.CRUDE_RAGINITE)
        register(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)

        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.BAUXITE)
        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.NITER)
        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.SALT)
        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.SULFUR)
        register(HTTagPrefix.RAW_MATERIAL, VanillaMaterials.REDSTONE)
        // Ingots
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.REFINED_RAGI_STEEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)

        register(HTTagPrefix.INGOT, CommonMaterials.STEEL)
        register(HTTagPrefix.INGOT, CommonMaterials.STAINLESS_STEEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)

        register(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
        register(HTTagPrefix.INGOT, CommonMaterials.CHROMIUM)
        register(HTTagPrefix.INGOT, CommonMaterials.NICKEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.ECHORIUM)
        // Gems
        register(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
        register(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
        register(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)

        register(HTTagPrefix.GEM, RagiumMaterials.SLAG)
        register(HTTagPrefix.GEM, RagiumMaterials.RESIDUAL_COKE)
        register(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL)
        // Gears
        register(HTTagPrefix.GEAR, VanillaMaterials.COPPER)
        register(HTTagPrefix.GEAR, VanillaMaterials.IRON)
        register(HTTagPrefix.GEAR, VanillaMaterials.GOLD)
        register(HTTagPrefix.GEAR, VanillaMaterials.DIAMOND)
        register(HTTagPrefix.GEAR, VanillaMaterials.EMERALD)

        register(HTTagPrefix.GEAR, CommonMaterials.STEEL)
        register(HTTagPrefix.GEAR, CommonMaterials.STAINLESS_STEEL)
        register(HTTagPrefix.GEAR, RagiumMaterials.DEEP_STEEL)
        register(HTTagPrefix.GEAR, VanillaMaterials.NETHERITE)
        // Rods
        register(HTTagPrefix.ROD, VanillaMaterials.COPPER)
        register(HTTagPrefix.ROD, VanillaMaterials.IRON)
        register(HTTagPrefix.ROD, VanillaMaterials.GOLD)

        register(HTTagPrefix.ROD, CommonMaterials.STEEL)
        register(HTTagPrefix.ROD, CommonMaterials.STAINLESS_STEEL)
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
                storage.id.path,
                storage,
                itemProperty().name(HTTagPrefix.STORAGE_BLOCK.createText(key)),
            )
        }

        RagiumBlocks.CASINGS.forEach { (key: HTMaterialKey, casing: DeferredBlock<Block>) ->
            REGISTER.registerSimpleBlockItem(
                casing.id.path,
                casing,
                itemProperty().name(HTTagPrefix.CASING.createText(key)),
            )
        }

        buildList {
            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Coils.entries)
            addAll(RagiumBlocks.Burners.entries)

            addAll(RagiumBlocks.Drums.entries)
        }.forEach { content: HTBlockContent.Tier ->
            REGISTER.registerSimpleBlockItem(
                content.id.path,
                content,
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

            add(RagiumBlocks.SHAFT)
            add(RagiumBlocks.CHEMICAL_GLASS)

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

    @JvmField
    val SILKY_CRYSTAL: DeferredItem<Item> = REGISTER.registerSimpleItem("silky_crystal")

    @JvmField
    val CRIMSON_CRYSTAL: DeferredItem<Item> = REGISTER.registerSimpleItem("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: DeferredItem<Item> = REGISTER.registerSimpleItem("warped_crystal")

    @JvmField
    val OBSIDIAN_TEAR: DeferredItem<Item> = REGISTER.registerSimpleItem("obsidian_tear")

    @JvmField
    val OTHER_GEMS: List<DeferredItem<Item>> = listOf(
        SILKY_CRYSTAL,
        CRIMSON_CRYSTAL,
        WARPED_CRYSTAL,
        OBSIDIAN_TEAR,
    )

    @JvmField
    val CALCIUM_CARBIDE: DeferredItem<Item> = REGISTER.registerSimpleItem("calcium_carbide")

    @JvmField
    val OTHER_RESOURCES: List<DeferredItem<Item>> = listOf(
        CALCIUM_CARBIDE,
    )

    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val SOAP: DeferredItem<Item> = REGISTER.registerSimpleItem("soap")

    @JvmField
    val OTHER_INGOTS: List<DeferredItem<Item>> = listOf(
        RAGI_ALLOY_COMPOUND,
        SOAP,
    )

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
    val SILKY_PICKAXE: DeferredItem<HTSilkyPickaxeItem> =
        REGISTER.registerItem("silky_pickaxe", ::HTSilkyPickaxeItem)

    @JvmField
    val DYNAMITE: DeferredItem<HTDynamiteItem> =
        REGISTER.registerItem("dynamite", ::HTDynamiteItem)

    @JvmField
    val SLOT_LOCK: DeferredItem<Item> =
        REGISTER.registerSimpleItem("slot_lock")

    //    Circuits    //

    val BASIC_CIRCUIT: DeferredItem<Item> = REGISTER.registerSimpleItem(
        "basic_circuit",
        itemProperty().name(HTMachineTier.BASIC.createPrefixedText(RagiumTranslationKeys.CIRCUIT)),
    )

    val ADVANCED_CIRCUIT: DeferredItem<Item> = REGISTER.registerSimpleItem(
        "advanced_circuit",
        itemProperty().name(HTMachineTier.ADVANCED.createPrefixedText(RagiumTranslationKeys.CIRCUIT)),
    )

    val ELITE_CIRCUIT: DeferredItem<Item> = REGISTER.registerSimpleItem(
        "elite_circuit",
        itemProperty().name(HTMachineTier.ELITE.createPrefixedText(RagiumTranslationKeys.CIRCUIT)),
    )

    val ULTIMATE_CIRCUIT: DeferredItem<Item> = REGISTER.registerSimpleItem(
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
    val GEAR_PRESS_MOLD: DeferredItem<Item> = REGISTER.registerSimpleItem("gear_press_mold")

    @JvmField
    val PLATE_PRESS_MOLD: DeferredItem<Item> = REGISTER.registerSimpleItem("plate_press_mold")

    @JvmField
    val ROD_PRESS_MOLD: DeferredItem<Item> = REGISTER.registerSimpleItem("rod_press_mold")

    @JvmField
    val PRESS_MOLDS: List<DeferredItem<Item>> = listOf(GEAR_PRESS_MOLD, PLATE_PRESS_MOLD, ROD_PRESS_MOLD)

    //    Ingredients    //

    @JvmField
    val ALKALI_REAGENT: DeferredItem<Item> = REGISTER.registerSimpleItem("alkali_reagent")

    @JvmField
    val PLASTIC_PLATE: DeferredItem<Item> = REGISTER.registerSimpleItem("plastic_plate")

    @JvmField
    val CIRCUIT_BOARD: DeferredItem<Item> = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val ENGINE: DeferredItem<Item> = REGISTER.registerSimpleItem("engine")

    @JvmField
    val LED: DeferredItem<Item> = REGISTER.registerSimpleItem("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = REGISTER.registerSimpleItem("solar_panel")

    @JvmField
    val RAGI_TICKET: DeferredItem<Item> = REGISTER.registerSimpleItem("ragi_ticket", itemProperty().rarity(Rarity.EPIC))

    @JvmField
    val INGREDIENTS: List<DeferredItem<Item>> = buildList {
        // compound
        add(ALKALI_REAGENT)
        // parts
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
        REGISTER.registerSimpleItem("yellow_cake")

    val YELLOW_CAKE_PIECE: DeferredItem<Item> =
        registerFood("yellow_cake_piece", RagiumFoods.YELLOW_CAKE_PIECE)

    val URANIUM_FUEL: DeferredItem<Item> =
        REGISTER.registerSimpleItem("uranium_fuel", itemProperty().durability(255))

    val NUCLEAR_WASTE: DeferredItem<Item> =
        REGISTER.registerSimpleItem("nuclear_waste")

    val PLUTONIUM_FUEL: DeferredItem<Item> =
        REGISTER.registerSimpleItem("plutonium_fuel", itemProperty().durability(1023))

    @JvmField
    val RADIOACTIVES: List<DeferredItem<Item>> = listOf(
        YELLOW_CAKE,
        YELLOW_CAKE_PIECE,
        URANIUM_FUEL,
        NUCLEAR_WASTE,
        PLUTONIUM_FUEL,
    )
}

package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.lore
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.item.*
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.*
import net.minecraft.world.item.enchantment.Enchantments
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

    //    Natural Resources    //

    @JvmField
    val CRUDE_OIL_BUCKET: DeferredItem<BucketItem> = register(
        "crude_oil_bucket",
        { properties: Item.Properties -> BucketItem(RagiumFluids.CRUDE_OIL.get(), properties) },
        itemProperty().craftRemainder(Items.BUCKET).stacksTo(1),
    )

    //    Materials    //

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
        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.SULFUR)
        register(HTTagPrefix.RAW_MATERIAL, VanillaMaterials.REDSTONE)
        // Ingots
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)

        register(HTTagPrefix.INGOT, CommonMaterials.STEEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)

        register(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
        register(HTTagPrefix.INGOT, RagiumMaterials.ECHORIUM)
        // Gems
        register(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
        register(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)

        register(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL)
        // Coils
        register(HTTagPrefix.COIL, VanillaMaterials.COPPER)
        register(HTTagPrefix.COIL, VanillaMaterials.GOLD)
        register(HTTagPrefix.COIL, CommonMaterials.ALUMINUM)
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
        // register(HTTagPrefix.ROD, VanillaMaterials.COPPER)
        // register(HTTagPrefix.ROD, VanillaMaterials.IRON)
        // register(HTTagPrefix.ROD, VanillaMaterials.GOLD)

        // register(HTTagPrefix.ROD, CommonMaterials.STEEL)
        // register(HTTagPrefix.ROD, RagiumMaterials.DEEP_STEEL)
        // register(HTTagPrefix.ROD, VanillaMaterials.NETHERITE)
        // Mekanism
        register(HTTagPrefix.DIRTY_DUST, RagiumMaterials.RAGINITE)
        register(HTTagPrefix.CLUMP, RagiumMaterials.RAGINITE)
        register(HTTagPrefix.SHARD, RagiumMaterials.RAGINITE)
        register(HTTagPrefix.CRYSTAL, RagiumMaterials.RAGINITE)
    }

    @JvmStatic
    fun getMaterialMap(prefix: HTTagPrefix): Map<HTMaterialKey, DeferredItem<out Item>> = MATERIAL_ITEMS.row(prefix)

    @JvmStatic
    fun getMaterialItems(prefix: HTTagPrefix): Collection<DeferredItem<out Item>> = getMaterialMap(prefix).values

    @JvmStatic
    fun getMaterialItem(prefix: HTTagPrefix, material: HTMaterialKey): DeferredItem<out Item> =
        getMaterialMap(prefix)[material] ?: error("Unregistered material item: ${prefix.createPath(material)}")

    @JvmField
    val BEE_WAX: DeferredItem<HoneycombItem> = register(
        "bee_wax",
        ::HoneycombItem,
        itemProperty().lore(RagiumTranslationKeys.BEE_WAX),
    )

    @JvmField
    val SLAG: DeferredItem<Item> = register("slag")

    @JvmField
    val SILKY_CRYSTAL: DeferredItem<Item> = register("silky_crystal")

    @JvmField
    val CRIMSON_CRYSTAL: DeferredItem<Item> = register("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: DeferredItem<Item> = register("warped_crystal")

    @JvmField
    val OBSIDIAN_TEAR: DeferredItem<Item> = register("obsidian_tear")

    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = register("ragi_alloy_compound")

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
    val MINCED_MEAT: DeferredItem<Item> = register("minced_meat")

    @JvmField
    val MEAT_INGOT: DeferredItem<Item> = registerFood("meat_ingot", Foods.BEEF)

    @JvmField
    val COOKED_MEAT_INGOT: DeferredItem<Item> = registerFood("cooked_meat_ingot", Foods.COOKED_BEEF)

    @JvmField
    val CANNED_COOKED_MEAT: DeferredItem<Item> = registerFood("canned_cooked_meat", RagiumFoods.CANNED_COOKED_MEAT)

    @JvmField
    val AMBROSIA: DeferredItem<HTAmbrosiaItem> =
        register(
            "ambrosia",
            ::HTAmbrosiaItem,
            itemProperty().food(RagiumFoods.AMBROSIA).rarity(Rarity.EPIC).lore(RagiumTranslationKeys.AMBROSIA),
        )

    @JvmField
    val FOODS: List<DeferredItem<out Item>> = listOf(
        // cake
        SWEET_BERRIES_CAKE_PIECE,
        MELON_PIE,
        // ingredient
        BUTTER,
        DOUGH,
        FLOUR,
        // chocolate
        CHOCOLATE,
        CHOCOLATE_APPLE,
        CHOCOLATE_BREAD,
        CHOCOLATE_COOKIE,
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
    val FORGE_HAMMER: DeferredItem<HTCraftingToolItem> = register("forge_hammer", ::HTCraftingToolItem)

    @JvmField
    val SILKY_PICKAXE: DeferredItem<HTSingleEnchantmentPickaxeItem> = register(
        "silky_pickaxe",
        { properties: Item.Properties -> HTSingleEnchantmentPickaxeItem(Enchantments.SILK_TOUCH, 1, properties) },
        itemProperty().lore(RagiumTranslationKeys.SILKY_PICKAXE),
    )

    @JvmField
    val DEFOLIANT: DeferredItem<HTDefoliantItem> = register(
        "defoliant",
        ::HTDefoliantItem,
        itemProperty().lore(RagiumTranslationKeys.SILKY_PICKAXE),
    )

    @JvmField
    val DYNAMITE: DeferredItem<HTDynamiteItem> = register("dynamite", ::HTDynamiteItem)

    @JvmField
    val MAGNET: DeferredItem<HTMagnetItem> = register(
        "magnet",
        ::HTMagnetItem,
        itemProperty().lore(RagiumTranslationKeys.SILKY_PICKAXE),
    )

    @JvmField
    val SOAP: DeferredItem<HTSoapItem> = register(
        "soap",
        ::HTSoapItem,
        itemProperty().lore(RagiumTranslationKeys.SOAP),
    )

    @JvmField
    val ALUMINUM_CAN: DeferredItem<Item> = register("aluminum_can")

    @JvmField
    val POTION_CAN: DeferredItem<HTPotionCanItem> = register("potion_can", ::HTPotionCanItem)

    @JvmField
    val PRESS_MOLDS: Map<HTTagPrefix, DeferredItem<HTCatalystItem>> =
        listOf(
            HTTagPrefix.GEAR,
            HTTagPrefix.PLATE,
            HTTagPrefix.ROD,
            HTTagPrefix.WIRE,
        ).associateWith { prefix: HTTagPrefix ->
            register("${prefix.serializedName}_press_mold", ::HTCatalystItem)
        }

    @JvmStatic
    fun getPressMold(prefix: HTTagPrefix): DeferredItem<HTCatalystItem> = PRESS_MOLDS[prefix] ?: error("Unregistered with $prefix")

    @JvmField
    val REDSTONE_LENS: DeferredItem<HTCatalystItem> = register("redstone_lens", ::HTCatalystItem)

    @JvmField
    val GLOW_LENS: DeferredItem<HTCatalystItem> = register("glow_lens", ::HTCatalystItem)

    @JvmField
    val PRISMARINE_LENS: DeferredItem<HTCatalystItem> = register("prismarine_lens", ::HTCatalystItem)

    @JvmField
    val MAGICAL_LENS: DeferredItem<HTCatalystItem> = register("magical_lens", ::HTCatalystItem)

    //    Circuits    //

    @JvmField
    val POLYMER_RESIN: DeferredItem<Item> = register("polymer_resin")

    @JvmField
    val PLASTIC_PLATE: DeferredItem<Item> = register("plastic_plate")

    @JvmField
    val CIRCUIT_BOARD: DeferredItem<Item> = register("circuit_board")

    @JvmField
    val BASIC_CIRCUIT: DeferredItem<Item> = register("basic_circuit")

    @JvmField
    val ADVANCED_CIRCUIT: DeferredItem<Item> = register("advanced_circuit")

    @JvmField
    val ELITE_CIRCUIT: DeferredItem<Item> = register("elite_circuit")

    @JvmField
    val ULTIMATE_CIRCUIT: DeferredItem<Item> = register("ultimate_circuit")

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
    val MAGICAL_REAGENT: DeferredItem<Item> = register("magical_reagent")

    @JvmField
    val PRISMARINE_REAGENT: DeferredItem<Item> = register("prismarine_reagent")

    @JvmField
    val SCULK_REAGENT: DeferredItem<Item> = register("sculk_reagent")

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
        MAGICAL_REAGENT,
        PRISMARINE_REAGENT,
        SCULK_REAGENT,
        WITHER_REAGENT,
    )

    //    Machine Parts    //

    @JvmField
    val MACHINE_CASING: DeferredItem<Item> = register("machine_casing")

    @JvmField
    val CHEMICAL_MACHINE_CASING: DeferredItem<Item> = register("chemical_machine_casing")

    @JvmField
    val PRECISION_MACHINE_CASING: DeferredItem<Item> = register("precision_machine_casing")

    @JvmField
    val ENGINE: DeferredItem<Item> = register("engine")

    @JvmField
    val LED: DeferredItem<Item> = register("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = register("solar_panel")

    val YELLOW_CAKE: DeferredItem<Item> =
        register("yellow_cake")

    val YELLOW_CAKE_PIECE: DeferredItem<Item> =
        registerFood("yellow_cake_piece", RagiumFoods.YELLOW_CAKE_PIECE)

    @JvmField
    val RAGI_TICKET: DeferredItem<Item> = register("ragi_ticket", itemProperty().rarity(Rarity.EPIC))

    @JvmField
    val BROKEN_SPAWNER: DeferredItem<Item> = register("broken_spawner")
}

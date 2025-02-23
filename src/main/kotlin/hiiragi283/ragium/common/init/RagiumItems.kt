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
import hiiragi283.ragium.api.util.HTArmorSets
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.HTToolSets
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.item.*
import hiiragi283.ragium.common.item.armor.HTDivingGoggleItem
import hiiragi283.ragium.common.item.armor.HTJetpackItem
import hiiragi283.ragium.common.item.tool.HTCraftingToolItem
import hiiragi283.ragium.common.item.tool.HTSingleEnchantmentPickaxeItem
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

        register(HTTagPrefix.DUST, CommonMaterials.ALUMINUM)
        register(HTTagPrefix.DUST, CommonMaterials.TIN)
        register(HTTagPrefix.DUST, CommonMaterials.NIOBIUM)

        register(HTTagPrefix.DUST, CommonMaterials.ASH)
        register(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
        register(HTTagPrefix.DUST, CommonMaterials.CALCITE)
        register(HTTagPrefix.DUST, CommonMaterials.WOOD)
        // Raws
        register(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)

        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.TIN)
        register(HTTagPrefix.RAW_MATERIAL, CommonMaterials.NIOBIUM)
        // Ingots
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
        register(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)

        register(HTTagPrefix.INGOT, CommonMaterials.STEEL)
        register(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)

        register(HTTagPrefix.INGOT, CommonMaterials.TIN)
        register(HTTagPrefix.INGOT, CommonMaterials.NIOBIUM)
        register(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
        register(HTTagPrefix.INGOT, RagiumMaterials.ECHORIUM)
        // Gems
        register(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
        register(HTTagPrefix.GEM, RagiumMaterials.CRIMSON_CRYSTAL)
        register(HTTagPrefix.GEM, RagiumMaterials.WARPED_CRYSTAL)
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
    val WARPED_WART: DeferredItem<HTWarpedWartItem> = register(
        "warped_wart",
        ::HTWarpedWartItem,
        itemProperty().food(RagiumFoods.WARPED_WART).lore(RagiumTranslationKeys.WARPED_WART),
    )

    @JvmField
    val AMBROSIA: DeferredItem<HTAmbrosiaItem> = register(
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
        // wart
        WARPED_WART,
        // end-contents
        AMBROSIA,
    )

    //    Armors    //

    @JvmField
    val DIVING_GOGGLE: DeferredItem<HTDivingGoggleItem> = register("diving_goggles", ::HTDivingGoggleItem)

    @JvmField
    val JETPACK: DeferredItem<HTJetpackItem> = register("jetpack", ::HTJetpackItem)

    @JvmField
    val STEEL_ARMORS = HTArmorSets(REGISTER, RagiumArmorMaterials.STEEL, "steel")

    //    Tools    //

    @JvmField
    val FORGE_HAMMER: DeferredItem<HTCraftingToolItem> = register("forge_hammer", ::HTCraftingToolItem)

    @JvmField
    val RAGI_LANTERN: DeferredItem<HTRagiLanternItem> = register("ragi_lantern", ::HTRagiLanternItem)

    @JvmField
    val FEVER_PICKAXE: DeferredItem<HTSingleEnchantmentPickaxeItem> = register(
        "fever_pickaxe",
        { properties: Item.Properties ->
            HTSingleEnchantmentPickaxeItem(
                Enchantments.FORTUNE,
                5,
                Tiers.GOLD,
                properties,
            )
        },
        itemProperty().lore(RagiumTranslationKeys.FEVER_PICKAXE),
    )

    @JvmField
    val SILKY_PICKAXE: DeferredItem<HTSingleEnchantmentPickaxeItem> = register(
        "silky_pickaxe",
        { properties: Item.Properties ->
            HTSingleEnchantmentPickaxeItem(
                Enchantments.SILK_TOUCH,
                1,
                Tiers.GOLD,
                properties,
            )
        },
        itemProperty().lore(RagiumTranslationKeys.SILKY_PICKAXE),
    )

    @JvmField
    val STEEL_TOOLS = HTToolSets(REGISTER, RagiumToolMaterials.STEEL, "steel")

    @JvmField
    val STEEL_SHEARS: DeferredItem<ShearsItem> = register("steel_shears", ::ShearsItem)

    //    Utilities    //

    @JvmField
    val DEFOLIANT: DeferredItem<HTDefoliantItem> = register(
        "defoliant",
        ::HTDefoliantItem,
        itemProperty().lore(RagiumTranslationKeys.DEFOLIANT),
    )

    @JvmField
    val DYNAMITE: DeferredItem<HTDynamiteItem> = register("dynamite", ::HTDynamiteItem)

    @JvmField
    val MAGNET: DeferredItem<HTMagnetItem> = register(
        "magnet",
        ::HTMagnetItem,
        itemProperty().lore(RagiumTranslationKeys.MAGNET),
    )

    @JvmField
    val POTION_BUNDLE: DeferredItem<HTPotionBundleItem> = register(
        "potion_bundle",
        ::HTPotionBundleItem,
        itemProperty().lore(RagiumTranslationKeys.POTION_BUNDLE, RagiumTranslationKeys.POTION_BUNDLE_1),
    )

    @JvmField
    val SOAP: DeferredItem<HTSoapItem> = register(
        "soap",
        ::HTSoapItem,
        itemProperty().lore(RagiumTranslationKeys.SOAP),
    )

    //    Molds    //

    @JvmField
    val BLANK_PRESS_MOLD: DeferredItem<HTCatalystItem> = register("blank_press_mold", ::HTCatalystItem)

    @JvmField
    val BALL_PRESS_MOLD: DeferredItem<HTCatalystItem> = register("ball_press_mold", ::HTCatalystItem)

    @JvmField
    val GEAR_PRESS_MOLD: DeferredItem<HTCatalystItem> = register("gear_press_mold", ::HTCatalystItem)

    @JvmField
    val PLATE_PRESS_MOLD: DeferredItem<HTCatalystItem> = register("plate_press_mold", ::HTCatalystItem)

    @JvmField
    val ROD_PRESS_MOLD: DeferredItem<HTCatalystItem> = register("rod_press_mold", ::HTCatalystItem)

    @JvmField
    val WIRE_PRESS_MOLD: DeferredItem<HTCatalystItem> = register("wire_press_mold", ::HTCatalystItem)

    //    Lens    //

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
    val MAGICAL_REAGENT: DeferredItem<Item> = register("magical_reagent")

    @JvmField
    val PRISMARINE_REAGENT: DeferredItem<Item> = register("prismarine_reagent")

    @JvmField
    val SCULK_REAGENT: DeferredItem<Item> = register("sculk_reagent")

    @JvmField
    val WITHER_REAGENT: DeferredItem<Item> = register("wither_reagent")

    @JvmField
    val REAGENTS: List<DeferredItem<Item>> = listOf(
        MAGICAL_REAGENT,
        PRISMARINE_REAGENT,
        SCULK_REAGENT,
        WITHER_REAGENT,
    )

    //    Fluid Cube    //

    @JvmField
    val EMPTY_FLUID_CUBE: DeferredItem<Item> = register("empty_fluid_cube")

    @JvmField
    val WATER_FLUID_CUBE: DeferredItem<Item> = register("water_fluid_cube")

    @JvmField
    val LAVA_FLUID_CUBE: DeferredItem<Item> = register("lava_fluid_cube")

    @JvmField
    val FLUID_CUBES: List<DeferredItem<Item>> = listOf(
        WATER_FLUID_CUBE,
        LAVA_FLUID_CUBE,
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
}

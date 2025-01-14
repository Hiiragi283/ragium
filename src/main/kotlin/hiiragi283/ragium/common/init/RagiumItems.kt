package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.component.HTRadioactiveComponent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.item.HTAmbrosiaItem
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumItems {
    @JvmField
    val REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    //    Materials    //

    enum class Dusts(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        ALKALI(RagiumMaterialKeys.ALKALI),
        ASH(RagiumMaterialKeys.ASH),
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        LAPIS(RagiumMaterialKeys.LAPIS),
        LEAD(RagiumMaterialKeys.LEAD),
        NITER(RagiumMaterialKeys.NITER),
        QUARTZ(RagiumMaterialKeys.QUARTZ),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        // tier 2
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        GOLD(RagiumMaterialKeys.GOLD),
        SILVER(RagiumMaterialKeys.SILVER),

        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_dust")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.DUST
    }

    enum class Gears(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        IRON(RagiumMaterialKeys.IRON),

        // tier 2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        GOLD(RagiumMaterialKeys.GOLD),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier 3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_gear")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEAR
    }

    enum class Gems(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        CINNABAR(RagiumMaterialKeys.CINNABAR),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder(name.lowercase())
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEM
    }

    enum class Ingots(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),

        // tier 2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier 3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),

        // tier 4
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ECHORIUM(RagiumMaterialKeys.ECHORIUM),
        FIERIUM(RagiumMaterialKeys.FIERIUM),
        DRAGONIUM(RagiumMaterialKeys.DRAGONIUM),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_ingot")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    enum class RawMaterials(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        NITER(RagiumMaterialKeys.NITER),
        REDSTONE(RagiumMaterialKeys.REDSTONE),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        // tier2
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        GALENA(RagiumMaterialKeys.GALENA),
        PYRITE(RagiumMaterialKeys.PYRITE),
        SPHALERITE(RagiumMaterialKeys.SPHALERITE),

        // tier 3
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("raw_${name.lowercase()}")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.RAW_MATERIAL
    }

    @JvmField
    val MATERIALS: List<HTItemContent.Material> = buildList {
        addAll(Dusts.entries)
        addAll(Gears.entries)
        addAll(Gems.entries)
        addAll(Ingots.entries)
        addAll(RawMaterials.entries)
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
        MINCED_MEAT,
        MEAT_INGOT,
        COOKED_MEAT_INGOT,
        CANNED_COOKED_MEAT,
        // end-contents
        AMBROSIA,
    )

    //    Parts    //

    enum class Circuits(override val machineTier: HTMachineTier) : HTItemContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_circuit")
    }

    enum class PressMolds : HTItemContent {
        GEAR,
        PIPE,
        PLATE,
        ROD,
        WIRE,
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_press_mold")
    }

    enum class Catalysts : HTItemContent {
        HEATING,
        COOLING,
        OXIDIZATION,
        REDUCTION,
        DEHYDRATION,
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_catalyst")
    }

    enum class FluidCubes(val containment: DeferredHolder<Fluid, out Fluid>) : HTItemContent {
        WATER(DeferredHolder.create(Registries.FLUID, ResourceLocation.withDefaultNamespace("fluid"))),
        LAVA(DeferredHolder.create(Registries.FLUID, ResourceLocation.withDefaultNamespace("lava"))),
        MILK(NeoForgeMod.MILK),
        HONEY(RagiumFluids.HONEY.holder),
        ;

        companion object {
            @JvmStatic
            fun fromFluid(fluid: Fluid): FluidCubes? = when (fluid) {
                Fluids.WATER -> WATER
                Fluids.LAVA -> LAVA
                NeoForgeMod.MILK.get() -> MILK
                RagiumFluids.HONEY.get() -> HONEY
                else -> null
            }
        }

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_cube")
    }

    @JvmField
    val BEE_WAX: DeferredItem<Item> = REGISTER.registerItem("bee_wax", ::HoneycombItem, itemProperty())

    @JvmField
    val COAL_CHIP: DeferredItem<Item> = REGISTER.registerSimpleItem("coal_chip")

    @JvmField
    val PULP: DeferredItem<Item> = REGISTER.registerSimpleItem("pulp")

    @JvmField
    val RESIDUAL_COKE: DeferredItem<Item> = REGISTER.registerSimpleItem("residual_coke")

    @JvmField
    val DEEPANT: DeferredItem<Item> = REGISTER.registerSimpleItem("deepant")

    @JvmField
    val GLASS_SHARD: DeferredItem<Item> = REGISTER.registerSimpleItem("glass_shard")

    @JvmField
    val LUMINESCENCE_DUST: DeferredItem<Item> = REGISTER.registerSimpleItem("luminescence_dust")

    @JvmField
    val RAGI_ALLOY_COMPOUND: DeferredItem<Item> = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val SLAG: DeferredItem<Item> = REGISTER.registerSimpleItem("slag")

    @JvmField
    val SOAP: DeferredItem<Item> = REGISTER.registerSimpleItem("soap")

    @JvmField
    val POLYMER_RESIN: DeferredItem<Item> = REGISTER.registerSimpleItem("polymer_resin")

    @JvmField
    val STELLA_PLATE: DeferredItem<Item> = REGISTER.registerSimpleItem("stella_plate")

    @JvmField
    val CRUDE_SILICON: DeferredItem<Item> = REGISTER.registerSimpleItem("crude_silicon")

    @JvmField
    val SILICON: DeferredItem<Item> = REGISTER.registerSimpleItem("silicon")

    @JvmField
    val REFINED_SILICON: DeferredItem<Item> = REGISTER.registerSimpleItem("refined_silicon")

    @JvmField
    val CRIMSON_CRYSTAL: DeferredItem<Item> = REGISTER.registerSimpleItem("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: DeferredItem<Item> = REGISTER.registerSimpleItem("warped_crystal")

    @JvmField
    val OBSIDIAN_TEAR: DeferredItem<Item> = REGISTER.registerSimpleItem("obsidian_tear")

    @JvmField
    val CARBON_ELECTRODE: DeferredItem<Item> = REGISTER.registerSimpleItem("carbon_electrode")

    @JvmField
    val BLAZING_CARBON_ELECTRODE: DeferredItem<Item> = REGISTER.registerSimpleItem("blazing_carbon_electrode")

    @JvmField
    val CHARGED_CARBON_ELECTRODE: DeferredItem<Item> = REGISTER.registerSimpleItem("charged_carbon_electrode")

    @JvmField
    val ENGINE: DeferredItem<Item> = REGISTER.registerSimpleItem("engine")

    @JvmField
    val LASER_EMITTER: DeferredItem<Item> = REGISTER.registerSimpleItem("laser_emitter")

    @JvmField
    val LED: DeferredItem<Item> = REGISTER.registerSimpleItem("led")

    @JvmField
    val SOLAR_PANEL: DeferredItem<Item> = REGISTER.registerSimpleItem("solar_panel")

    @JvmField
    val RAGI_TICKET: DeferredItem<Item> = REGISTER.registerSimpleItem("ragi_ticket", itemProperty().rarity(Rarity.EPIC))

    @JvmField
    val INGREDIENTS: List<DeferredItem<Item>> = buildList {
        // organic
        add(BEE_WAX)
        add(COAL_CHIP)
        add(PULP)
        add(RESIDUAL_COKE)
        // inorganic
        add(DEEPANT)
        add(GLASS_SHARD)
        add(LUMINESCENCE_DUST)
        add(RAGI_ALLOY_COMPOUND)
        add(SLAG)
        add(SOAP)
        // plastic
        add(POLYMER_RESIN)
        add(STELLA_PLATE)
        // silicon
        add(CRUDE_SILICON)
        add(SILICON)
        add(REFINED_SILICON)
        // magical
        add(CRIMSON_CRYSTAL)
        add(WARPED_CRYSTAL)
        add(OBSIDIAN_TEAR)
        // parts
        add(CARBON_ELECTRODE)
        add(BLAZING_CARBON_ELECTRODE)
        add(CHARGED_CARBON_ELECTRODE)
        add(ENGINE)
        add(LASER_EMITTER)
        add(LED)
        // add(PROCESSOR_SOCKET)
        add(SOLAR_PANEL)

        add(RAGI_TICKET)
    }

    enum class Radioactives(val level: HTRadioactiveComponent) : HTItemContent {
        URANIUM_FUEL(HTRadioactiveComponent.MEDIUM),
        PLUTONIUM_FUEL(HTRadioactiveComponent.HIGH),
        YELLOW_CAKE(HTRadioactiveComponent.MEDIUM),
        YELLOW_CAKE_PIECE(HTRadioactiveComponent.LOW),
        NUCLEAR_WASTE(HTRadioactiveComponent.LOW),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder(name.lowercase())
    }

    //    Register    //

    @JvmStatic
    internal fun register(bus: IEventBus) {
        fun DeferredRegister.Items.registerSimple(content: HTItemContent) {
            content.registerSimpleItem(this)
        }

        buildList {
            addAll(MATERIALS)

            addAll(Circuits.entries)
            addAll(PressMolds.entries)
            addAll(Catalysts.entries)
            addAll(FluidCubes.entries)
        }.forEach(REGISTER::registerSimple)

        Radioactives.entries.forEach { radioactive: Radioactives ->
            REGISTER.registerSimpleItem(
                radioactive.id.path,
                when (radioactive) {
                    Radioactives.URANIUM_FUEL -> itemProperty().durability(1024)
                    Radioactives.PLUTONIUM_FUEL -> itemProperty().durability(1024)
                    Radioactives.YELLOW_CAKE -> itemProperty()
                    Radioactives.YELLOW_CAKE_PIECE -> itemProperty().food(RagiumFoods.YELLOW_CAKE_PIECE)
                    Radioactives.NUCLEAR_WASTE -> itemProperty()
                },
            )
        }

        REGISTER.register(bus)
    }
}

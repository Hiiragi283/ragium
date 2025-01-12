package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.component.HTRadioactiveComponent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTArmorType
import hiiragi283.ragium.api.util.HTToolType
import hiiragi283.ragium.common.item.*
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object RagiumItems {
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

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_dust")
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

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_gear")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEAR
    }

    enum class Gems(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        CINNABAR(RagiumMaterialKeys.CINNABAR),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey(name.lowercase())
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

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_ingot")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    enum class Plates(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        LAPIS(RagiumMaterialKeys.LAPIS),

        // tier2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        GOLD(RagiumMaterialKeys.GOLD),
        QUARTZ(RagiumMaterialKeys.QUARTZ),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),

        // tier4
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ECHORIUM(RagiumMaterialKeys.ECHORIUM),
        FIERIUM(RagiumMaterialKeys.FIERIUM),
        DRAGONIUM(RagiumMaterialKeys.DRAGONIUM),
        NETHERITE(RagiumMaterialKeys.NETHERITE),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_plate")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.PLATE
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

        override val key: RegistryKey<Item> = HTContent.itemKey("raw_${name.lowercase()}")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.RAW_MATERIAL
    }

    //    Armors    //

    enum class SteelArmors(val armorType: HTArmorType) : HTItemContent {
        HELMET(HTArmorType.HELMET),
        CHESTPLATE(HTArmorType.CHESTPLATE),
        LEGGINGS(HTArmorType.LEGGINGS),
        BOOTS(HTArmorType.BOOTS),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("steel_${name.lowercase()}")
    }

    enum class DeepSteelArmors(val armorType: HTArmorType) : HTItemContent {
        HELMET(HTArmorType.HELMET),
        CHESTPLATE(HTArmorType.CHESTPLATE),
        LEGGINGS(HTArmorType.LEGGINGS),
        BOOTS(HTArmorType.BOOTS),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("deep_steel_${name.lowercase()}")
    }

    enum class StellaSuits(val armorType: HTArmorType) : HTItemContent {
        GOGGLE(HTArmorType.HELMET),
        JACKET(HTArmorType.CHESTPLATE),
        LEGGINGS(HTArmorType.LEGGINGS),
        BOOTS(HTArmorType.BOOTS),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("stella_${name.lowercase()}")
    }

    @JvmField
    val DRAGONIC_ELYTRA: HTItemContent = HTContent.ofItem("dragonic_elytra")

    //    Tools    //

    enum class SteelTools(val toolType: HTToolType) : HTItemContent {
        AXE(HTToolType.AXE),
        HOE(HTToolType.HOE),
        PICKAXE(HTToolType.PICKAXE),
        SHOVEL(HTToolType.SHOVEL),
        SWORD(HTToolType.SWORD),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("steel_${name.lowercase()}")
    }

    enum class DeepSteelTools(val toolType: HTToolType) : HTItemContent {
        AXE(HTToolType.AXE),
        HOE(HTToolType.HOE),
        PICKAXE(HTToolType.PICKAXE),
        SHOVEL(HTToolType.SHOVEL),
        SWORD(HTToolType.SWORD),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("deep_steel_${name.lowercase()}")
    }

    enum class Dynamites(name: String) : HTItemContent {
        SIMPLE(""),
        ANVIL("anvil_"),
        BLAZING("blazing_"),
        BEDROCK("bedrock_"),
        FLATTENING("flattening_"),
        FROSTING("frosting_"),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey(name + "dynamite")
    }

    @JvmField
    val BACKPACK: HTItemContent = HTContent.ofItem("backpack")

    @JvmField
    val EMPTY_FLUID_CUBE: HTItemContent = HTContent.ofItem("empty_fluid_cube")

    @JvmField
    val FILLED_FLUID_CUBE: HTItemContent = HTContent.ofItem("filled_fluid_cube")

    @JvmField
    val FORGE_HAMMER: HTItemContent = HTContent.ofItem("forge_hammer")

    @JvmField
    val GIGANT_HAMMER: HTItemContent = HTContent.ofItem("gigant_hammer")

    @JvmField
    val ECHO_BULLET: HTItemContent = HTContent.ofItem("echo_bullet")

    @JvmField
    val RAGI_WRENCH: HTItemContent = HTContent.ofItem("ragi_wrench")

    @JvmField
    val STELLA_SABER: HTItemContent = HTContent.ofItem("stella_saber")

    @JvmField
    val RAGIUM_SABER: HTItemContent = HTContent.ofItem("ragium_saber")

    @JvmField
    val FLUID_FILTER: HTItemContent = HTContent.ofItem("fluid_filter")

    @JvmField
    val ITEM_FILTER: HTItemContent = HTContent.ofItem("item_filter")

    @JvmField
    val TRADER_CATALOG: HTItemContent = HTContent.ofItem("trader_catalog")

    //    Foods    //

    @JvmField
    val SWEET_BERRIES_CAKE_PIECE: HTItemContent = HTContent.ofItem("sweet_berries_cake_piece")

    @JvmField
    val MELON_PIE: HTItemContent = HTContent.ofItem("melon_pie")

    @JvmField
    val BUTTER: HTItemContent = HTContent.ofItem("butter")

    @JvmField
    val CARAMEL: HTItemContent = HTContent.ofItem("caramel")

    @JvmField
    val DOUGH: HTItemContent = HTContent.ofItem("dough")

    @JvmField
    val FLOUR: HTItemContent = HTContent.ofItem("flour")

    @JvmField
    val CHOCOLATE: HTItemContent = HTContent.ofItem("chocolate")

    @JvmField
    val CHOCOLATE_APPLE: HTItemContent = HTContent.ofItem("chocolate_apple")

    @JvmField
    val CHOCOLATE_BREAD: HTItemContent = HTContent.ofItem("chocolate_bread")

    @JvmField
    val CHOCOLATE_COOKIE: HTItemContent = HTContent.ofItem("chocolate_cookie")

    @JvmField
    val CINNAMON_STICK: HTItemContent = HTContent.ofItem("cinnamon_stick")

    @JvmField
    val CINNAMON_POWDER: HTItemContent = HTContent.ofItem("cinnamon_powder")

    @JvmField
    val CINNAMON_ROLL: HTItemContent = HTContent.ofItem("cinnamon_roll")

    @JvmField
    val MINCED_MEAT: HTItemContent = HTContent.ofItem("minced_meat")

    @JvmField
    val MEAT_INGOT: HTItemContent = HTContent.ofItem("meat_ingot")

    @JvmField
    val COOKED_MEAT_INGOT: HTItemContent = HTContent.ofItem("cooked_meat_ingot")

    @JvmField
    val CANNED_COOKED_MEAT: HTItemContent = HTContent.ofItem("canned_cooked_meat")

    @JvmField
    val AMBROSIA: HTItemContent = HTContent.ofItem("ambrosia")

    @JvmField
    val FOODS: List<HTItemContent> = listOf(
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

    //    Ingredients    //

    enum class Plastics(override val tier: HTMachineTier) :
        HTItemContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_plastic")

        val tagKey: TagKey<Item> = itemTagKey(commonId("plastics/${name.lowercase()}"))
    }

    enum class CircuitBoards(override val tier: HTMachineTier) :
        HTItemContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_circuit_board")

        fun getCircuit(): Circuits = when (this) {
            PRIMITIVE -> Circuits.PRIMITIVE
            BASIC -> Circuits.BASIC
            ADVANCED -> Circuits.ADVANCED
        }
    }

    enum class Circuits(override val tier: HTMachineTier) :
        HTItemContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_circuit")
    }

    enum class Processors(val material: HTMaterialKey) : HTItemContent {
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),
        NETHER_STAR(RagiumMaterialKeys.NETHER_STAR),
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_processor")
    }

    enum class PressMolds : HTItemContent {
        GEAR,
        PIPE,
        PLATE,
        ROD,
        WIRE,
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_press_mold")
    }

    enum class Radioactives(val level: HTRadioactiveComponent) : HTItemContent {
        URANIUM_FUEL(HTRadioactiveComponent.MEDIUM),
        PLUTONIUM_FUEL(HTRadioactiveComponent.HIGH),
        YELLOW_CAKE(HTRadioactiveComponent.MEDIUM),
        YELLOW_CAKE_PIECE(HTRadioactiveComponent.LOW),
        NUCLEAR_WASTE(HTRadioactiveComponent.LOW),
        ;

        override val key: RegistryKey<Item> = HTContent.itemKey(name.lowercase())
    }

    enum class FluidCubes(val fluid: HTFluidContent) : HTItemContent {
        WATER(HTContent.fromFluid(Fluids.WATER)),
        LAVA(HTContent.fromFluid(Fluids.LAVA)),
        MILK(RagiumFluids.MILK),
        HONEY(RagiumFluids.HONEY),
        ;

        companion object {
            @JvmStatic
            fun fromFluid(fluid: Fluid): FluidCubes? = when (fluid) {
                Fluids.WATER -> WATER
                Fluids.LAVA -> LAVA
                RagiumFluids.MILK.get() -> MILK
                RagiumFluids.HONEY.get() -> HONEY
                else -> null
            }
        }

        override val key: RegistryKey<Item> = HTContent.itemKey("${name.lowercase()}_cube")
    }

    @JvmField
    val BEE_WAX: HTItemContent = HTContent.ofItem("bee_wax")

    @JvmField
    val COAL_CHIP: HTItemContent = HTContent.ofItem("coal_chip")

    @JvmField
    val PULP: HTItemContent = HTContent.ofItem("pulp")

    @JvmField
    val RESIDUAL_COKE: HTItemContent = HTContent.ofItem("residual_coke")

    @JvmField
    val DEEPANT: HTItemContent = HTContent.ofItem("deepant")

    @JvmField
    val GLASS_SHARD: HTItemContent = HTContent.ofItem("glass_shard")

    @JvmField
    val LUMINESCENCE_DUST: HTItemContent = HTContent.ofItem("luminescence_dust")

    @JvmField
    val RAGI_ALLOY_COMPOUND: HTItemContent = HTContent.ofItem("ragi_alloy_compound")

    @JvmField
    val SLAG: HTItemContent = HTContent.ofItem("slag")

    @JvmField
    val SOAP: HTItemContent = HTContent.ofItem("soap")

    @JvmField
    val POLYMER_RESIN: HTItemContent = HTContent.ofItem("polymer_resin")

    @JvmField
    val STELLA_PLATE: HTItemContent = HTContent.ofItem("stella_plate")

    @JvmField
    val CRUDE_SILICON: HTItemContent = HTContent.ofItem("crude_silicon")

    @JvmField
    val SILICON: HTItemContent = HTContent.ofItem("silicon")

    @JvmField
    val REFINED_SILICON: HTItemContent = HTContent.ofItem("refined_silicon")

    @JvmField
    val CRIMSON_CRYSTAL: HTItemContent = HTContent.ofItem("crimson_crystal")

    @JvmField
    val WARPED_CRYSTAL: HTItemContent = HTContent.ofItem("warped_crystal")

    @JvmField
    val OBSIDIAN_TEAR: HTItemContent = HTContent.ofItem("obsidian_tear")

    @JvmField
    val CARBON_ELECTRODE: HTItemContent = HTContent.ofItem("carbon_electrode")

    @JvmField
    val BLAZING_CARBON_ELECTRODE: HTItemContent = HTContent.ofItem("blazing_carbon_electrode")

    @JvmField
    val CHARGED_CARBON_ELECTRODE: HTItemContent = HTContent.ofItem("charged_carbon_electrode")

    @JvmField
    val ENGINE: HTItemContent = HTContent.ofItem("engine")

    @JvmField
    val LASER_EMITTER: HTItemContent = HTContent.ofItem("laser_emitter")

    @JvmField
    val LED: HTItemContent = HTContent.ofItem("led")

    @JvmField
    val PROCESSOR_SOCKET: HTItemContent = HTContent.ofItem("processor_socket")

    @JvmField
    val SOLAR_PANEL: HTItemContent = HTContent.ofItem("solar_panel")

    @JvmField
    val INGREDIENTS: List<HTItemContent> = buildList {
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
        add(PROCESSOR_SOCKET)
        add(SOLAR_PANEL)
    }

    //    Misc    //

    @JvmField
    val RAGI_TICKET: HTItemContent = HTContent.ofItem("ragi_ticket")

    @JvmField
    val MISC: List<HTItemContent> = listOf(
        RAGI_TICKET,
    )

    //    Register    //

    private fun registerItem(content: HTItemContent, parent: Item.Settings = itemSettings(), item: (Item.Settings) -> Item = ::Item): Item =
        Registry.register(Registries.ITEM, content.key, item(parent))

    private fun registerArmor(
        content: HTItemContent,
        armorType: HTArmorType,
        material: RegistryEntry<ArmorMaterial>,
        multiplier: Int,
        parent: Item.Settings = itemSettings(),
    ) {
        registerItem(content, parent) { armorType.createItem(material, multiplier, it) }
    }

    @JvmStatic
    internal fun register() {
        // material
        buildList {
            addAll(Dusts.entries)
            addAll(Gears.entries)
            addAll(Gems.entries)
            addAll(Ingots.entries)
            addAll(Plates.entries)
            addAll(RawMaterials.entries)
        }.forEach { content ->
            registerItem(content) { Item(it.material(content.material, content.tagPrefix)) }
        }
        Plastics.entries.forEach { plastic: Plastics ->
            registerItem(plastic, itemSettings().tieredText(RagiumTranslationKeys.PLASTIC, plastic.tier))
        }
        CircuitBoards.entries.forEach { board: CircuitBoards ->
            registerItem(board, itemSettings().tieredText(RagiumTranslationKeys.CIRCUIT_BOARD, board.tier))
        }
        Circuits.entries.forEach { circuit: Circuits ->
            registerItem(circuit, itemSettings().tieredText(RagiumTranslationKeys.CIRCUIT, circuit.tier))
        }
        Processors.entries.forEach(::registerItem)
        PressMolds.entries.forEach(::registerItem)
        // armor
        SteelArmors.entries.forEach {
            registerArmor(it, it.armorType, RagiumArmorMaterials.STEEL, 25)
        }
        DeepSteelArmors.entries.forEach {
            registerArmor(it, it.armorType, RagiumArmorMaterials.DEEP_STEEL, 33)
        }
        registerArmor(
            StellaSuits.GOGGLE,
            HTArmorType.HELMET,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings().rarity(Rarity.EPIC),
        )
        registerArmor(
            StellaSuits.JACKET,
            HTArmorType.CHESTPLATE,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings()
                .rarity(Rarity.EPIC)
                .attributeModifiers(
                    createArmorAttribute(RagiumArmorMaterials.STELLA, ArmorItem.Type.CHESTPLATE)
                        .add(
                            EntityAttributes.PLAYER_MINING_EFFICIENCY,
                            EntityAttributeModifier(
                                Identifier.of("armor.chestplate"),
                                2.0,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                            ),
                            AttributeModifierSlot.CHEST,
                        ).add(
                            EntityAttributes.GENERIC_MAX_HEALTH,
                            EntityAttributeModifier(
                                Identifier.of("armor.chestplate"),
                                20.0,
                                EntityAttributeModifier.Operation.ADD_VALUE,
                            ),
                            AttributeModifierSlot.CHEST,
                        ).build(),
                ),
        )
        registerArmor(
            StellaSuits.LEGGINGS,
            HTArmorType.LEGGINGS,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings()
                .rarity(Rarity.EPIC)
                .attributeModifiers(
                    createArmorAttribute(RagiumArmorMaterials.STELLA, ArmorItem.Type.LEGGINGS)
                        .add(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED,
                            EntityAttributeModifier(
                                Identifier.of("armor.leggings"),
                                0.4,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                            ),
                            AttributeModifierSlot.LEGS,
                        ).add(
                            EntityAttributes.GENERIC_JUMP_STRENGTH,
                            EntityAttributeModifier(
                                Identifier.of("armor.leggings"),
                                1.0,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                            ),
                            AttributeModifierSlot.LEGS,
                        ).build(),
                ),
        )
        registerArmor(
            StellaSuits.BOOTS,
            HTArmorType.BOOTS,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings()
                .rarity(Rarity.EPIC)
                .attributeModifiers(
                    createArmorAttribute(RagiumArmorMaterials.STELLA, ArmorItem.Type.BOOTS)
                        .add(
                            EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER,
                            EntityAttributeModifier(
                                Identifier.of("armor.boots"),
                                -100.0,
                                EntityAttributeModifier.Operation.ADD_VALUE,
                            ),
                            AttributeModifierSlot.FEET,
                        ).add(
                            EntityAttributes.GENERIC_STEP_HEIGHT,
                            EntityAttributeModifier(
                                Identifier.of("armor.boots"),
                                1.0,
                                EntityAttributeModifier.Operation.ADD_VALUE,
                            ),
                            AttributeModifierSlot.FEET,
                        ).build(),
                ),
        )
        registerItem(DRAGONIC_ELYTRA, item = ::HTDragonicElytraItem)
        // steel tool
        SteelTools.entries.forEach { tool: SteelTools ->
            registerItem(tool) {
                tool.toolType.createToolItem(RagiumToolMaterials.STEEL, it)
            }
        }
        // deep steel tool
        DeepSteelTools.entries.forEach { tool: DeepSteelTools ->
            registerItem(tool) {
                tool.toolType.createToolItem(RagiumToolMaterials.DEEP_STEEL, it)
            }
        }
        // dynamite
        registerItem(Dynamites.SIMPLE, item = ::HTDynamiteItem)
        registerItem(Dynamites.ANVIL, item = ::HTAnvilDynamiteItem)
        registerItem(Dynamites.BLAZING, item = ::HTBlazingDynamiteItem)
        registerItem(Dynamites.BEDROCK, item = ::HTBedrockDynamiteItem)
        registerItem(Dynamites.FLATTENING, item = ::HTFlatteningDynamiteItem)
        registerItem(Dynamites.FROSTING, item = ::HTFrostingDynamiteItem)

        registerItem(BACKPACK, item = ::HTBackpackItem)
        registerItem(EMPTY_FLUID_CUBE)
        registerItem(FILLED_FLUID_CUBE, item = ::HTFilledFluidCubeItem)
        registerItem(FORGE_HAMMER, itemSettings().maxDamage(63), ::HTForgeHammerItem)
        registerItem(
            GIGANT_HAMMER,
            itemSettings()
                .unbreakable()
                .rarity(Rarity.EPIC)
                .attributeModifiers(
                    createToolAttribute(ToolMaterials.NETHERITE, 15.0, -3.0)
                        .add(
                            EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                            EntityAttributeModifier(
                                RagiumAPI.id("gigant_hammer_range"),
                                12.0,
                                EntityAttributeModifier.Operation.ADD_VALUE,
                            ),
                            AttributeModifierSlot.MAINHAND,
                        ).add(
                            EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                            EntityAttributeModifier(
                                RagiumAPI.id("gigant_hammer_range"),
                                12.0,
                                EntityAttributeModifier.Operation.ADD_VALUE,
                            ),
                            AttributeModifierSlot.MAINHAND,
                        ).build(),
                ),
            ::HTGigantHammerItem,
        )
        registerItem(ECHO_BULLET, item = ::HTEchoBulletItem)
        registerItem(
            RAGI_WRENCH,
            itemSettings()
                .maxCount(1)
                .descriptions(RagiumTranslationKeys.RAGI_WRENCH),
        )
        registerItem(STELLA_SABER, itemSettings().rarity(Rarity.RARE)) {
            HTToolType.SWORD.createToolItem(RagiumToolMaterials.STELLA, it)
        }
        registerItem(
            RAGIUM_SABER,
            itemSettings()
                .rarity(Rarity.EPIC)
                .attributeModifiers(createToolAttribute(RagiumToolMaterials.STELLA, 7.0, 0.0).build()),
        ) {
            HTToolType.SWORD.createToolItem(RagiumToolMaterials.STELLA, it)
        }

        registerItem(
            FLUID_FILTER,
            itemSettings()
                .maxCount(1)
                .descriptions(RagiumTranslationKeys.FILTER)
                .component(RagiumComponentTypes.FLUID_FILTER, RegistryEntryList.empty()),
        )
        registerItem(
            ITEM_FILTER,
            itemSettings()
                .maxCount(1)
                .descriptions(RagiumTranslationKeys.FILTER)
                .component(RagiumComponentTypes.ITEM_FILTER, RegistryEntryList.empty()),
        )
        registerItem(TRADER_CATALOG, itemSettings().maxCount(1).descriptions(RagiumTranslationKeys.TRADER_CATALOG), ::HTTraderCatalogItem)
        // food
        registerItem(SWEET_BERRIES_CAKE_PIECE, itemSettings().food(RagiumFoodComponents.SWEET_BERRIES_CAKE))
        registerItem(MELON_PIE, itemSettings().food(RagiumFoodComponents.MELON_PIE))
        registerItem(BUTTER, itemSettings().food(FoodComponents.APPLE))
        registerItem(CARAMEL, itemSettings().food(FoodComponents.DRIED_KELP))
        registerItem(DOUGH)
        registerItem(FLOUR)
        registerItem(CHOCOLATE, itemSettings().food(RagiumFoodComponents.CHOCOLATE))
        registerItem(CHOCOLATE_APPLE, itemSettings().food(FoodComponents.COOKED_CHICKEN))
        registerItem(CHOCOLATE_BREAD, itemSettings().food(FoodComponents.COOKED_BEEF))
        registerItem(CHOCOLATE_COOKIE, itemSettings().food(FoodComponents.COOKIE))
        registerItem(CINNAMON_STICK, itemSettings())
        registerItem(CINNAMON_POWDER, itemSettings())
        registerItem(CINNAMON_ROLL, itemSettings().food(FoodComponents.COOKED_BEEF))
        registerItem(MINCED_MEAT)
        registerItem(MEAT_INGOT, itemSettings().food(FoodComponents.BEEF))
        registerItem(COOKED_MEAT_INGOT, itemSettings().food(FoodComponents.COOKED_BEEF))
        registerItem(CANNED_COOKED_MEAT, itemSettings().food(RagiumFoodComponents.CANNED_COOKED_MEAT))
        registerItem(AMBROSIA, itemSettings().rarity(Rarity.EPIC).food(RagiumFoodComponents.AMBROSIA), ::HTAmbrosiaItem)
        // ingredients
        Radioactives.entries.forEach { radioactive: Radioactives ->
            registerItem(
                radioactive,
                when (radioactive) {
                    Radioactives.URANIUM_FUEL -> itemSettings().maxDamage(1024)
                    Radioactives.PLUTONIUM_FUEL -> itemSettings().maxDamage(1024)
                    Radioactives.YELLOW_CAKE -> itemSettings()
                    Radioactives.YELLOW_CAKE_PIECE -> itemSettings().food(RagiumFoodComponents.YELLOW_CAKE_PIECE)
                    Radioactives.NUCLEAR_WASTE -> itemSettings()
                }.radioactive(radioactive.level),
            )
        }
        FluidCubes.entries.forEach(::registerItem)

        INGREDIENTS.forEach { ingredient: HTItemContent ->
            when (ingredient) {
                BEE_WAX -> registerItem(ingredient, item = ::HoneycombItem)
                WARPED_CRYSTAL -> registerItem(
                    ingredient,
                    itemSettings().descriptions(RagiumTranslationKeys.WARPED_CRYSTAL),
                    ::HTWarpedCrystalItem,
                )
                else -> registerItem(ingredient)
            }
        }
        // misc
        registerItem(RAGI_TICKET, itemSettings().rarity(Rarity.EPIC))
    }
}

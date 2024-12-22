package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.createArmorAttribute
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.material
import hiiragi283.ragium.api.extension.tieredText
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTArmorType
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object RagiumItemsNew {
    //    Materials    //

    enum class Dusts(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        ALKALI(RagiumMaterialKeys.ALKALI),
        ASH(RagiumMaterialKeys.ASH),
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        LAPIS(RagiumMaterialKeys.LAPIS),
        NITER(RagiumMaterialKeys.NITER),
        QUARTZ(RagiumMaterialKeys.QUARTZ),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        // tier 2
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        GOLD(RagiumMaterialKeys.GOLD),

        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),
        ;

        override val delegated: HTContent<Item> = HTContent.ofItem("${name.lowercase()}_dust")
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

        override val delegated: HTContent<Item> = HTContent.ofItem("${name.lowercase()}_gear")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEAR
    }

    enum class Gems(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        FLUORITE(RagiumMaterialKeys.FLUORITE),

        // tier 4
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val delegated: HTContent<Item> = HTContent.ofItem(name.lowercase())
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
        ;

        override val delegated: HTContent<Item> = HTContent.ofItem("${name.lowercase()}_ingot")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    enum class Plates(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        LAPIS(RagiumMaterialKeys.LAPIS),
        WOOD(RagiumMaterialKeys.WOOD),

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
        NETHERITE(RagiumMaterialKeys.NETHERITE),
        ;

        override val delegated: HTContent<Item> = HTContent.ofItem("${name.lowercase()}_plate")
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

        // tier 3
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        ;

        override val delegated: HTContent<Item> = HTContent.ofItem("raw_${name.lowercase()}")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.RAW_MATERIAL
    }

    //    Armors    //

    @JvmField
    val STEEL_HELMET: HTItemContent = HTContent.ofItem("steel_helmet")

    @JvmField
    val STEEL_CHESTPLATE: HTItemContent = HTContent.ofItem("steel_chestplate")

    @JvmField
    val STEEL_LEGGINGS: HTItemContent = HTContent.ofItem("steel_leggings")

    @JvmField
    val STEEL_BOOTS: HTItemContent = HTContent.ofItem("steel_boots")

    @JvmField
    val STELLA_GOGGLE: HTItemContent = HTContent.ofItem("stella_goggle")

    @JvmField
    val STELLA_JACKET: HTItemContent = HTContent.ofItem("stella_jacket")

    @JvmField
    val STELLA_LEGGINGS: HTItemContent = HTContent.ofItem("stella_leggings")

    @JvmField
    val STELLA_BOOTS: HTItemContent = HTContent.ofItem("stella_boots")

    @JvmField
    val ARMORS: List<HTItemContent> = listOf(
        STEEL_HELMET,
        STEEL_CHESTPLATE,
        STEEL_LEGGINGS,
        STEEL_BOOTS,
        STELLA_GOGGLE,
        STELLA_JACKET,
        STELLA_LEGGINGS,
        STELLA_BOOTS,
    )

    //    Tools    //

    /*@JvmField
    val ANVIL_DYNAMITE= HTDynamiteItem(
        { entity: HTDynamiteEntity, result: HitResult ->
            val world: World = entity.world
            when (result) {
                is BlockHitResult -> {
                    world.setBlockState(result.blockPos.offset(result.side), Blocks.ANVIL.defaultState)
                }
                is EntityHitResult -> {
                    result.entity.damage(world.damageSources.fallingAnvil(entity), 10f)
                }
                else -> {}
            }
        },
        itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.ANVIL_DYNAMITE)),
    )

    @JvmField
    val BACKPACK: Item = HTBackpackItem

    @JvmField
    val BEDROCK_DYNAMITE: Item = HTDynamiteItem(
        { entity: HTDynamiteEntity, result: HitResult ->
            if (result is BlockHitResult) {
                val world: World = entity.world
                val bottomY: Int = world.bottomY
                ChunkPos(result.blockPos).forEach(bottomY + 1..bottomY + 5) { pos ->
                    if (world.getBlockState(pos).isOf(Blocks.BEDROCK)) {
                        world.removeBlock(pos, false)
                    }
                }
            }
        },
        itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.BEDROCK_DYNAMITE)),
    )

    @JvmField
    val DYNAMITE: Item = HTDynamiteItem(
        { entity: HTDynamiteEntity, result: HitResult ->
            val pos: Vec3d = result.pos
            entity.stack
                .getOrDefault(RagiumComponentTypes.DYNAMITE, HTDynamiteItem.Component.DEFAULT)
                .createExplosion(entity.world, entity, pos.x, pos.y, pos.z)
        },
        itemSettings().component(RagiumComponentTypes.DYNAMITE, HTDynamiteItem.Component.DEFAULT),
    )

    @JvmField
    val EMPTY_FLUID_CUBE: Item = Item(itemSettings())

    @JvmField
    val FILLED_FLUID_CUBE: Item = HTFilledFluidCubeItem

    @JvmField
    val FLATTENING_DYNAMITE: Item = HTDynamiteItem(
        { entity: HTDynamiteEntity, result: HitResult ->
            if (result is BlockHitResult) {
                val world: World = entity.world
                val pos: BlockPos = result.blockPos
                val hitY: Int = pos.y
                val minY: Int = when (result.side) {
                    Direction.UP -> hitY + 1
                    else -> hitY
                }
                ChunkPos(pos).forEach(minY..world.height) { pos: BlockPos ->
                    world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
                }
            }
        },
        itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.FLATTENING_DYNAMITE)),
    )

    @JvmField
    val FLUID_FILTER: Item = Item(
        itemSettings()
            .maxCount(1)
            .descriptions(Text.translatable(RagiumTranslationKeys.FILTER)),
    )

    @JvmField
    val FORGE_HAMMER: Item = HTForgeHammerItem

    @JvmField
    val GUIDE_BOOK: Item = HTGuideBookItem

    @JvmField
    val ITEM_FILTER: Item = Item(
        itemSettings()
            .maxCount(1)
            .descriptions(Text.translatable(RagiumTranslationKeys.FILTER)),
    )

    @JvmField
    val RAGI_WRENCH: Item = Item(itemSettings().maxCount(1).descriptions(Text.translatable(RagiumTranslationKeys.RAGI_WRENCH)))

    @JvmField
    val STEEL_AXE: Item = HTToolType.AXE.createToolItem(RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_HOE: Item = HTToolType.HOE.createToolItem(RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_PICKAXE: Item = HTToolType.PICKAXE.createToolItem(RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_SHOVEL: Item = HTToolType.SHOVEL.createToolItem(RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_SWORD: Item = HTToolType.SWORD.createToolItem(RagiumToolMaterials.STEEL)

    @JvmField
    val STELLA_SABER: Item =
        HTToolType.SWORD.createToolItem(RagiumToolMaterials.STELLA, itemSettings().rarity(Rarity.RARE))

    @JvmField
    val RAGIUM_SABER: Item = SwordItem(
        RagiumToolMaterials.STELLA,
        itemSettings()
            .rarity(Rarity.EPIC)
            .attributeModifiers(createToolAttribute(RagiumToolMaterials.STELLA, 7.0, 0.0).build()),
    )

    @JvmField
    val GIGANT_HAMMER: Item = HTGigantHammerItem

    @JvmField
    val TRADER_CATALOG: Item = HTTraderCatalogItem

    @JvmField
    val TOOLS: List<Item> = listOf(
        // damageable tool
        FORGE_HAMMER,
        STEEL_AXE,
        STEEL_HOE,
        STEEL_PICKAXE,
        STEEL_SHOVEL,
        STEEL_SWORD,
        STELLA_SABER,
        RAGIUM_SABER,
        GIGANT_HAMMER,
        // dynamite
        DYNAMITE,
        ANVIL_DYNAMITE,
        BEDROCK_DYNAMITE,
        FLATTENING_DYNAMITE,
        // non-damageable tool
        BACKPACK,
        EMPTY_FLUID_CUBE,
        FILLED_FLUID_CUBE,
        FLUID_FILTER,
        GUIDE_BOOK,
        ITEM_FILTER,
        RAGI_WRENCH,
        TRADER_CATALOG,
    )*/

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
    val MINCED_MEAT: HTItemContent = HTContent.ofItem("minced_meat")

    @JvmField
    val MEAT_INGOT: HTItemContent = HTContent.ofItem("meat_ingot")

    @JvmField
    val COOKED_MEAT_INGOT: HTItemContent = HTContent.ofItem("cooked_meat_ingot")

    @JvmField
    val FOODS: List<HTItemContent> = listOf(
        SWEET_BERRIES_CAKE_PIECE,
        MELON_PIE,
        BUTTER,
        CARAMEL,
        DOUGH,
        FLOUR,
        CHOCOLATE,
        CHOCOLATE_APPLE,
        CHOCOLATE_BREAD,
        CHOCOLATE_COOKIE,
        MINCED_MEAT,
        MEAT_INGOT,
        COOKED_MEAT_INGOT,
    )

    //    Ingredients    //

    enum class CircuitBoards(override val tier: HTMachineTier) :
        HTItemContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val delegated: HTContent<Item> = HTContent.ofItem("${name.lowercase()}_circuit_board")

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

        override val delegated: HTContent<Item> = HTContent.ofItem("${name.lowercase()}_circuit")
    }

    enum class PressMolds : HTItemContent {
        GEAR,
        PIPE,
        PLATE,
        ROD,
        ;

        override val delegated: HTContent<Item> = HTContent.ofItem("${name.lowercase()}_press_mold")
    }

    // organic
    /*@JvmField
    val BEE_WAX: Item = Item(itemSettings())

    @JvmField
    val PULP: Item = Item(itemSettings())

    @JvmField
    val RESIDUAL_COKE: Item = Item(itemSettings())

    // inorganic
    @JvmField
    val DEEPANT: Item = Item(itemSettings())

    @JvmField
    val LUMINESCENCE_DUST = Item(itemSettings())

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item = Item(itemSettings())

    @JvmField
    val SLAG: Item = Item(itemSettings())

    @JvmField
    val SOAP_INGOT: Item = Item(itemSettings())

    // plastic
    @JvmField
    val POLYMER_RESIN: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val PLASTIC_PLATE: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val ENGINEERING_PLASTIC_PLATE: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val STELLA_PLATE: Item = Item(itemSettings())

    // silicon
    @JvmField
    val CRUDE_SILICON: Item = Item(itemSettings())

    @JvmField
    val SILICON: Item = Item(itemSettings())

    @JvmField
    val REFINED_SILICON: Item = Item(itemSettings())

    // magical
    @JvmField
    val CRIMSON_CRYSTAL: Item = Item(itemSettings())

    @JvmField
    val WARPED_CRYSTAL: Item = HTWarpedCrystalItem

    @JvmField
    val OBSIDIAN_TEAR = Item(itemSettings())

    // parts
    @JvmField
    val BASALT_MESH: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val BLAZING_CARBON_ELECTRODE: Item = Item(itemSettings())

    @JvmField
    val CARBON_ELECTRODE: Item = Item(itemSettings())

    @JvmField
    val CHARGED_CARBON_ELECTRODE: Item = Item(itemSettings())

    @JvmField
    val ENGINE: Item = Item(itemSettings())

    @JvmField
    val LASER_EMITTER: Item = Item(itemSettings())

    @JvmField
    val LED: Item = Item(itemSettings())

    @JvmField
    val PROCESSOR_SOCKET: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val RAGI_CRYSTAL_PROCESSOR: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val SOLAR_PANEL: Item = Item(itemSettings())

    // nuclear
    @JvmField
    val URANIUM_FUEL = Item(itemSettings().maxDamage(1024))

    @JvmField
    val PLUTONIUM_FUEL = Item(itemSettings().maxDamage(2048))

    @JvmField
    val YELLOW_CAKE: Item = Item(itemSettings())

    @JvmField
    val YELLOW_CAKE_PIECE: Item = Item(
        itemSettings().food(
            FoodComponent
                .Builder()
                .statusEffect(StatusEffectInstance(StatusEffects.WITHER, -1, 1), 1f)
                .build(),
        ),
    )

    @JvmField
    val NUCLEAR_WASTE: Item = Item(itemSettings())

    @JvmField
    val INGREDIENTS: List<Item> = listOf(
        // organic
        BEE_WAX,
        PULP,
        RESIDUAL_COKE,
        // inorganic
        DEEPANT,
        LUMINESCENCE_DUST,
        RAGI_ALLOY_COMPOUND,
        SLAG,
        SOAP_INGOT,
        // plastic
        POLYMER_RESIN,
        PLASTIC_PLATE,
        ENGINEERING_PLASTIC_PLATE,
        STELLA_PLATE,
        // silicon
        CRUDE_SILICON,
        SILICON,
        REFINED_SILICON,
        // magical
        CRIMSON_CRYSTAL,
        WARPED_CRYSTAL,
        OBSIDIAN_TEAR,
        // parts
        BASALT_MESH,
        BLAZING_CARBON_ELECTRODE,
        CARBON_ELECTRODE,
        CHARGED_CARBON_ELECTRODE,
        ENGINE,
        LASER_EMITTER,
        LED,
        PROCESSOR_SOCKET,
        RAGI_CRYSTAL_PROCESSOR,
        SOLAR_PANEL,
        // nuclear
        URANIUM_FUEL,
        PLUTONIUM_FUEL,
        YELLOW_CAKE,
        YELLOW_CAKE_PIECE,
        NUCLEAR_WASTE,
    )*/

    //    Misc    //

    @JvmField
    val RAGI_TICKET: HTItemContent = HTContent.ofItem("ragi_ticket")

    @JvmField
    val MISC: List<HTItemContent> = listOf(
        RAGI_TICKET,
    )

    //    Register    //

    private fun registerItem(name: String, item: Item): Item = Registry.register(Registries.ITEM, RagiumAPI.id(name), item)

    private fun registerItem(content: HTItemContent, parent: Item.Settings = itemSettings(), item: (Item.Settings) -> Item = ::Item): Item =
        Registry.register(Registries.ITEM, content.id, item(parent))

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
        RagiumItemsNew.CircuitBoards.entries.forEach { board: RagiumItemsNew.CircuitBoards ->
            registerItem(board) {
                Item(
                    it.tieredText(
                        RagiumTranslationKeys.CIRCUIT_BOARD,
                        board.tier,
                    ),
                )
            }
        }
        RagiumItemsNew.Circuits.entries.forEach { circuit: RagiumItemsNew.Circuits ->
            registerItem(circuit) {
                Item(
                    it.tieredText(
                        RagiumTranslationKeys.CIRCUIT,
                        circuit.tier,
                    ),
                )
            }
        }
        RagiumItemsNew.PressMolds.entries.forEach(::registerItem)
        // armor
        registerArmor(STEEL_HELMET, HTArmorType.HELMET, RagiumArmorMaterials.STEEL, 25)
        registerArmor(STEEL_CHESTPLATE, HTArmorType.CHESTPLATE, RagiumArmorMaterials.STEEL, 25)
        registerArmor(STEEL_LEGGINGS, HTArmorType.LEGGINGS, RagiumArmorMaterials.STEEL, 25)
        registerArmor(STEEL_BOOTS, HTArmorType.BOOTS, RagiumArmorMaterials.STEEL, 25)
        registerArmor(
            STELLA_GOGGLE,
            HTArmorType.HELMET,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings().rarity(Rarity.EPIC),
        )
        registerArmor(
            STELLA_JACKET,
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
            STELLA_LEGGINGS,
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
            STELLA_BOOTS,
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
        // food
        registerItem(
            SWEET_BERRIES_CAKE_PIECE,
            itemSettings().food(
                FoodComponent
                    .Builder()
                    .nutrition(2)
                    .saturationModifier(0.1f)
                    .build(),
            ),
        )
        registerItem(
            MELON_PIE,
            itemSettings().food(
                FoodComponent
                    .Builder()
                    .nutrition(8)
                    .saturationModifier(0.3f)
                    .usingConvertsTo(Items.MELON_SEEDS)
                    .build(),
            ),
        )
        registerItem(BUTTER, itemSettings().food(FoodComponents.APPLE))
        registerItem(CARAMEL, itemSettings().food(FoodComponents.DRIED_KELP))
        registerItem(DOUGH)
        registerItem(FLOUR)
        registerItem(
            CHOCOLATE,
            itemSettings().food(
                FoodComponent
                    .Builder()
                    .nutrition(3)
                    .saturationModifier(0.3f)
                    .statusEffect(
                        StatusEffectInstance(StatusEffects.STRENGTH, 10 * 20, 0),
                        1.0f,
                    ).snack()
                    .alwaysEdible()
                    .build(),
            ),
        )
        registerItem(CHOCOLATE_APPLE, itemSettings().food(FoodComponents.COOKED_CHICKEN))
        registerItem(CHOCOLATE_BREAD, itemSettings().food(FoodComponents.COOKED_BEEF))
        registerItem(CHOCOLATE_COOKIE, itemSettings().food(FoodComponents.COOKIE))
        registerItem(MINCED_MEAT)
        registerItem(MEAT_INGOT, itemSettings().food(FoodComponents.BEEF))
        registerItem(COOKED_MEAT_INGOT, itemSettings().food(FoodComponents.COOKED_BEEF))
        // misc
        registerItem(RAGI_TICKET, itemSettings().rarity(Rarity.EPIC))

        registerItem("forge_hammer", RagiumItems.FORGE_HAMMER)
        registerItem("steel_axe", RagiumItems.STEEL_AXE)
        registerItem("steel_hoe", RagiumItems.STEEL_HOE)
        registerItem("steel_pickaxe", RagiumItems.STEEL_PICKAXE)
        registerItem("steel_shovel", RagiumItems.STEEL_SHOVEL)
        registerItem("steel_sword", RagiumItems.STEEL_SWORD)
        registerItem("stella_saber", RagiumItems.STELLA_SABER)
        registerItem("ragium_saber", RagiumItems.RAGIUM_SABER)
        registerItem("gigant_hammer", RagiumItems.GIGANT_HAMMER)

        registerItem("dynamite", RagiumItems.DYNAMITE)
        registerItem("anvil_dynamite", RagiumItems.ANVIL_DYNAMITE)
        registerItem("bedrock_dynamite", RagiumItems.BEDROCK_DYNAMITE)
        registerItem("flattening_dynamite", RagiumItems.FLATTENING_DYNAMITE)

        registerItem("backpack", RagiumItems.BACKPACK)
        registerItem("empty_fluid_cube", RagiumItems.EMPTY_FLUID_CUBE)
        registerItem("filled_fluid_cube", RagiumItems.FILLED_FLUID_CUBE)
        registerItem("fluid_filter", RagiumItems.FLUID_FILTER)
        registerItem("guide_book", RagiumItems.GUIDE_BOOK)
        registerItem("item_filter", RagiumItems.ITEM_FILTER)
        registerItem("ragi_wrench", RagiumItems.RAGI_WRENCH)
        registerItem("trader_catalog", RagiumItems.TRADER_CATALOG)

        registerItem("bee_wax", RagiumItems.BEE_WAX)
        registerItem("pulp", RagiumItems.PULP)
        registerItem("residual_coke", RagiumItems.RESIDUAL_COKE)

        registerItem("deepant", RagiumItems.DEEPANT)
        registerItem("luminescence_dust", RagiumItems.LUMINESCENCE_DUST)
        registerItem("ragi_alloy_compound", RagiumItems.RAGI_ALLOY_COMPOUND)
        registerItem("slag", RagiumItems.SLAG)
        registerItem("soap_ingot", RagiumItems.SOAP_INGOT)

        registerItem("polymer_resin", RagiumItems.POLYMER_RESIN)
        registerItem("plastic_plate", RagiumItems.PLASTIC_PLATE)
        registerItem("engineering_plastic_plate", RagiumItems.ENGINEERING_PLASTIC_PLATE)
        registerItem("stella_plate", RagiumItems.STELLA_PLATE)

        registerItem("crude_silicon", RagiumItems.CRUDE_SILICON)
        registerItem("silicon", RagiumItems.SILICON)
        registerItem("refined_silicon", RagiumItems.REFINED_SILICON)

        registerItem("crimson_crystal", RagiumItems.CRIMSON_CRYSTAL)
        registerItem("warped_crystal", RagiumItems.WARPED_CRYSTAL)
        registerItem("obsidian_tear", RagiumItems.OBSIDIAN_TEAR)

        registerItem("basalt_mesh", RagiumItems.BASALT_MESH)
        registerItem("blazing_carbon_electrode", RagiumItems.BLAZING_CARBON_ELECTRODE)
        registerItem("carbon_electrode", RagiumItems.CARBON_ELECTRODE)
        registerItem("charged_carbon_electrode", RagiumItems.CHARGED_CARBON_ELECTRODE)
        registerItem("engine", RagiumItems.ENGINE)
        registerItem("laser_emitter", RagiumItems.LASER_EMITTER)
        registerItem("led", RagiumItems.LED)
        registerItem("processor_socket", RagiumItems.PROCESSOR_SOCKET)
        registerItem("ragi_crystal_processor", RagiumItems.RAGI_CRYSTAL_PROCESSOR)
        registerItem("solar_panel", RagiumItems.SOLAR_PANEL)

        registerItem("uranium_fuel", RagiumItems.URANIUM_FUEL)
        registerItem("plutonium_fuel", RagiumItems.PLUTONIUM_FUEL)
        registerItem("yellow_cake", RagiumItems.YELLOW_CAKE)
        registerItem("yellow_cake_piece", RagiumItems.YELLOW_CAKE_PIECE)
        registerItem("nuclear_waste", RagiumItems.NUCLEAR_WASTE)
    }
}

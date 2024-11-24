package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTArmorType
import hiiragi283.ragium.api.content.HTToolType
import hiiragi283.ragium.api.extension.createArmorAttribute
import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.item.*
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

object RagiumItems {
    //    Armors    //

    @JvmField
    val STEEL_HELMET: Item = HTArmorType.HELMET.createItem(RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STEEL_CHESTPLATE: Item = HTArmorType.CHESTPLATE.createItem(RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STEEL_LEGGINGS: Item = HTArmorType.LEGGINGS.createItem(RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STEEL_BOOTS: Item = HTArmorType.BOOTS.createItem(RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STELLA_GOGGLE: Item =
        HTArmorType.HELMET.createItem(RagiumArmorMaterials.STELLA, 33, itemSettings().rarity(Rarity.EPIC))

    @JvmField
    val STELLA_JACKET: Item =
        HTArmorType.CHESTPLATE.createItem(
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

    @JvmField
    val STELLA_LEGGINGS: Item =
        HTArmorType.LEGGINGS.createItem(
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

    @JvmField
    val STELLA_BOOTS: Item =
        HTArmorType.BOOTS.createItem(
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

    @JvmField
    val ARMORS: List<Item> = listOf(
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

    @JvmField
    val ANVIL_DYNAMITE: Item = HTDynamiteItem(
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
    val BUJIN: Item = HTBujinItem

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
        itemSettings().descriptions(
            Text.translatable(RagiumTranslationKeys.FILTER),
            Text.translatable(RagiumTranslationKeys.FILTER_ID_FORMAT),
            Text.translatable(RagiumTranslationKeys.FILTER_TAG_FORMAT),
        ),
    )

    @JvmField
    val FORGE_HAMMER: Item = HTForgeHammerItem

    @JvmField
    val GIGANT_HAMMER: Item = HTGigantHammerItem

    @JvmField
    val GUIDE_BOOK: Item = HTGuideBookItem

    @JvmField
    val ITEM_FILTER: Item = Item(
        itemSettings().descriptions(
            Text.translatable(RagiumTranslationKeys.FILTER),
            Text.translatable(RagiumTranslationKeys.FILTER_ID_FORMAT),
            Text.translatable(RagiumTranslationKeys.FILTER_TAG_FORMAT),
        ),
    )

    @JvmField
    val RAGI_WRENCH: Item = Item(itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.RAGI_WRENCH)))

    @JvmField
    val ROPE_DYNAMITE: Item = HTDynamiteItem(
        { entity: HTDynamiteEntity, result: HitResult ->
            val world: World = entity.world
            if (result is BlockHitResult) {
                var pos: BlockPos = result.blockPos.offset(result.side)
                while (world.isAir(pos)) {
                    world.setBlockState(pos, Blocks.WEEPING_VINES.defaultState)
                    pos = pos.down()
                }
            }
        },
        itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit),
    )

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
        BUJIN,
        GIGANT_HAMMER,
        // dynamite
        DYNAMITE,
        ANVIL_DYNAMITE,
        BEDROCK_DYNAMITE,
        FLATTENING_DYNAMITE,
        ROPE_DYNAMITE,
        // non-damageable tool
        BACKPACK,
        EMPTY_FLUID_CUBE,
        FILLED_FLUID_CUBE,
        FLUID_FILTER,
        GUIDE_BOOK,
        ITEM_FILTER,
        RAGI_WRENCH,
        TRADER_CATALOG,
    )

    //    Foods    //

    @JvmField
    val BUTTER: Item = Item(itemSettings().food(FoodComponents.APPLE))

    @JvmField
    val CARAMEL: Item = Item(itemSettings().food(FoodComponents.DRIED_KELP))

    @JvmField
    val DOUGH: Item = Item(itemSettings())

    @JvmField
    val FLOUR: Item = Item(itemSettings())

    @JvmField
    val CHOCOLATE: Item = Item(
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

    @JvmField
    val CHOCOLATE_APPLE: Item = Item(itemSettings().food(FoodComponents.COOKED_CHICKEN))

    @JvmField
    val CHOCOLATE_BREAD: Item = Item(itemSettings().food(FoodComponents.COOKED_BEEF))

    @JvmField
    val MINCED_MEAT: Item = Item(itemSettings())

    @JvmField
    val MEAT_INGOT: Item = Item(itemSettings().food(FoodComponents.BEEF))

    @JvmField
    val COOKED_MEAT_INGOT: Item = Item(itemSettings().food(FoodComponents.COOKED_BEEF))

    @JvmField
    val SWEET_BERRIES_CAKE_PIECE: Item = Item(
        itemSettings().food(
            FoodComponent
                .Builder()
                .nutrition(2)
                .saturationModifier(0.1f)
                .build(),
        ),
    )

    @JvmField
    val FOODS: List<Item> = listOf(
        SWEET_BERRIES_CAKE_PIECE,
        BUTTER,
        CARAMEL,
        DOUGH,
        FLOUR,
        CHOCOLATE,
        CHOCOLATE_APPLE,
        CHOCOLATE_BREAD,
        MINCED_MEAT,
        MEAT_INGOT,
        COOKED_MEAT_INGOT,
    )

    //    Ingredients    //

    // organic
    @JvmField
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
    val PROCESSOR_SOCKET: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val RAGI_CRYSTAL_PROCESSOR: Item = Item(itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit))

    @JvmField
    val SOLAR_PANEL: Item = Item(itemSettings())

    // nuclear
    @JvmField
    val URANIUM_FUEL = Item(itemSettings().maxDamage(1024))

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
        // parts
        BASALT_MESH,
        BLAZING_CARBON_ELECTRODE,
        CARBON_ELECTRODE,
        CHARGED_CARBON_ELECTRODE,
        ENGINE,
        LASER_EMITTER,
        PROCESSOR_SOCKET,
        RAGI_CRYSTAL_PROCESSOR,
        SOLAR_PANEL,
        // nuclear
        URANIUM_FUEL,
        YELLOW_CAKE,
        YELLOW_CAKE_PIECE,
    )

    //    Misc    //

    @JvmField
    val RAGI_TICKET: Item = Item(itemSettings().rarity(Rarity.EPIC))

    @JvmField
    val MISC: List<Item> = listOf(
        RAGI_TICKET,
    )
}

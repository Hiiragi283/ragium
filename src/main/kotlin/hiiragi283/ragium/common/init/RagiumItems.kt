package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.createToolAttribute
import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.util.HTToolType
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.item.*
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.UnbreakableComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.text.Text
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
    val BACKPACK: Item = HTBackpackItem(itemSettings())

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
    val FILLED_FLUID_CUBE: Item = HTFilledFluidCubeItem(itemSettings())

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
    val FORGE_HAMMER: Item = HTForgeHammerItem(itemSettings().maxDamage(63))

    @JvmField
    val GUIDE_BOOK: Item =
        HTGuideBookItem(itemSettings().maxCount(1).component(RagiumComponentTypes.REWORK_TARGET, Unit))

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
    val GIGANT_HAMMER: Item = HTGigantHammerItem(
        itemSettings()
            .component(DataComponentTypes.UNBREAKABLE, UnbreakableComponent(true))
            .maxCount(1)
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
    )

    @JvmField
    val TRADER_CATALOG: Item = HTTraderCatalogItem(
        itemSettings().maxCount(1).descriptions(Text.translatable(RagiumTranslationKeys.TRADER_CATALOG)),
    )

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
    val WARPED_CRYSTAL: Item =
        HTWarpedCrystalItem(itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.WARPED_CRYSTAL)))

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
    )
}

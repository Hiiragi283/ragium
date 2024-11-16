package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTArmorType
import hiiragi283.ragium.api.content.HTToolType
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.component.HTDynamiteComponent
import hiiragi283.ragium.common.component.HTRemoverDynamiteBehaviors
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.item.*
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.util.Rarity
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d

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
        HTArmorType.CHESTPLATE.createItem(RagiumArmorMaterials.STELLA, 33, itemSettings().rarity(Rarity.EPIC))

    @JvmField
    val STELLA_LEGGINGS: Item =
        HTArmorType.LEGGINGS.createItem(RagiumArmorMaterials.STELLA, 33, itemSettings().rarity(Rarity.EPIC))

    @JvmField
    val STELLA_BOOTS: Item =
        HTArmorType.BOOTS.createItem(RagiumArmorMaterials.STELLA, 33, itemSettings().rarity(Rarity.EPIC))

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
    val BACKPACK: Item = HTBackpackItem

    @JvmField
    val BEDROCK_DYNAMITE: Item = HTDynamiteItem(HTRemoverDynamiteBehaviors.BEDROCK::onBlockHit, itemSettings())

    @JvmField
    val BUJIN: Item = HTBujinItem

    @JvmField
    val DYNAMITE: Item = HTDynamiteItem(
        { entity: HTDynamiteEntity, result: HitResult ->
            val pos: Vec3d = result.pos
            entity.stack
                .getOrDefault(RagiumComponentTypes.DYNAMITE, HTDynamiteComponent.DEFAULT)
                .createExplosion(entity.world, entity, pos.x, pos.y, pos.z)
        },
        itemSettings().component(RagiumComponentTypes.DYNAMITE, HTDynamiteComponent.DEFAULT),
    )

    @JvmField
    val EMPTY_FLUID_CUBE: Item = Item(itemSettings())

    @JvmField
    val FILLED_FLUID_CUBE: Item = HTFilledFluidCubeItem

    @JvmField
    val FLATTENING_DYNAMITE: Item = HTDynamiteItem(HTRemoverDynamiteBehaviors.FLATTEN::onBlockHit, itemSettings())

    @JvmField
    val FORGE_HAMMER: Item = HTForgeHammerItem

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
        STEEL_AXE,
        STEEL_HOE,
        STEEL_PICKAXE,
        STEEL_SHOVEL,
        STEEL_SWORD,
        FORGE_HAMMER,
        // non-damageable tool
        BACKPACK,
        BEDROCK_DYNAMITE,
        DYNAMITE,
        EMPTY_FLUID_CUBE,
        FILLED_FLUID_CUBE,
        TRADER_CATALOG,
        BUJIN,
    )

    //    Foods    //

    @JvmField
    val BEE_WAX: Item = Item(itemSettings())

    @JvmField
    val BUTTER: Item = Item(itemSettings().food(FoodComponents.APPLE))

    @JvmField
    val CARAMEL: Item = Item(itemSettings().food(FoodComponents.DRIED_KELP))

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
    val FLOUR: Item = Item(itemSettings())

    @JvmField
    val DOUGH: Item = Item(itemSettings())

    @JvmField
    val MINCED_MEAT: Item = Item(itemSettings())

    @JvmField
    val PULP: Item = Item(itemSettings())

    @JvmField
    val FOODS: List<Item> = listOf(
        BEE_WAX,
        BUTTER,
        CARAMEL,
        CHOCOLATE,
        CHOCOLATE_APPLE,
        CHOCOLATE_BREAD,
        FLOUR,
        DOUGH,
        MINCED_MEAT,
        PULP,
    )

    //    Misc    //

    @JvmField
    val BASALT_MESH: Item = Item(itemSettings())

    @JvmField
    val CRIMSON_CRYSTAL: Item = Item(itemSettings())

    @JvmField
    val CRUDE_SILICON: Item = Item(itemSettings())

    @JvmField
    val DEEPANT: Item = Item(itemSettings())

    @JvmField
    val ENGINE: Item = Item(itemSettings())

    @JvmField
    val ENGINEERING_PLASTIC_PLATE: Item = Item(itemSettings())

    @JvmField
    val HEART_OF_THE_NETHER: Item = Item(itemSettings().rarity(Rarity.UNCOMMON))

    @JvmField
    val LASER_EMITTER: Item = Item(itemSettings())

    @JvmField
    val PLASTIC_PLATE: Item = Item(itemSettings())

    @JvmField
    val POLYMER_RESIN: Item = Item(itemSettings())

    @JvmField
    val PROCESSOR_SOCKET: Item = Item(itemSettings())

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item = Item(itemSettings())

    @JvmField
    val RAGI_CRYSTAL_PROCESSOR: Item = Item(itemSettings())

    @JvmField
    val REFINED_SILICON: Item = Item(itemSettings())

    @JvmField
    val RESIDUAL_COKE: Item = Item(itemSettings())

    @JvmField
    val SILICON: Item = Item(itemSettings())

    @JvmField
    val SLAG: Item = Item(itemSettings())

    @JvmField
    val SOAP_INGOT: Item = Item(itemSettings())

    @JvmField
    val SOLAR_PANEL: Item = Item(itemSettings())

    @JvmField
    val STELLA_PLATE: Item = Item(itemSettings())

    @JvmField
    val WARPED_CRYSTAL: Item = HTWarpedCrystalItem

    @JvmField
    val MISC: List<Item> = listOf(
        BASALT_MESH,
        CRIMSON_CRYSTAL,
        CRUDE_SILICON,
        DEEPANT,
        ENGINE,
        ENGINEERING_PLASTIC_PLATE,
        FLATTENING_DYNAMITE,
        HEART_OF_THE_NETHER,
        LASER_EMITTER,
        POLYMER_RESIN,
        PROCESSOR_SOCKET,
        RAGI_ALLOY_COMPOUND,
        RAGI_CRYSTAL_PROCESSOR,
        REFINED_SILICON,
        RESIDUAL_COKE,
        SILICON,
        SLAG,
        SOAP_INGOT,
        SOLAR_PANEL,
        STELLA_PLATE,
        WARPED_CRYSTAL,
    )
}

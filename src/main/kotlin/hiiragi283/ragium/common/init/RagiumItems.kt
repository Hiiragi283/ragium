package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTArmorType
import hiiragi283.ragium.api.content.HTContentRegister
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

object RagiumItems : HTContentRegister {
    //    Armors    //

    @JvmField
    val STEEL_HELMET: Item =
        registerArmorItem("steel_helmet", HTArmorType.HELMET, RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STEEL_CHESTPLATE: Item =
        registerArmorItem("steel_chestplate", HTArmorType.CHESTPLATE, RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STEEL_LEGGINGS: Item =
        registerArmorItem("steel_leggings", HTArmorType.LEGGINGS, RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STEEL_BOOTS: Item =
        registerArmorItem("steel_boots", HTArmorType.BOOTS, RagiumArmorMaterials.STEEL, 25)

    @JvmField
    val STELLA_GOGGLE: Item =
        registerArmorItem(
            "stella_goggle",
            HTArmorType.HELMET,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings().rarity(Rarity.EPIC),
        )

    @JvmField
    val STELLA_JACKET: Item =
        registerArmorItem(
            "stella_jacket",
            HTArmorType.CHESTPLATE,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings().rarity(Rarity.EPIC),
        )

    @JvmField
    val STELLA_LEGGINGS: Item =
        registerArmorItem(
            "stella_leggings",
            HTArmorType.LEGGINGS,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings().rarity(Rarity.EPIC),
        )

    @JvmField
    val STELLA_BOOTS: Item =
        registerArmorItem(
            "stella_boots",
            HTArmorType.BOOTS,
            RagiumArmorMaterials.STELLA,
            33,
            itemSettings().rarity(Rarity.EPIC),
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
    val BACKPACK: Item = registerItem("backpack", HTBackpackItem)

    @JvmField
    val BEDROCK_DYNAMITE: Item = registerItem(
        "bedrock_dynamite",
        HTDynamiteItem(HTRemoverDynamiteBehaviors.BEDROCK::onBlockHit, itemSettings()),
    )

    @JvmField
    val BUJIN: Item = registerItem("bujin", HTBujinItem)

    @JvmField
    val CRAFTER_HAMMER: Item = registerItem("crafter_hammer", HTCrafterHammerItem)

    @JvmField
    val DYNAMITE: Item = registerItem(
        "dynamite",
        HTDynamiteItem(
            { entity: HTDynamiteEntity, result: HitResult ->
                val pos: Vec3d = result.pos
                entity.stack
                    .getOrDefault(RagiumComponentTypes.DYNAMITE, HTDynamiteComponent.DEFAULT)
                    .createExplosion(entity.world, entity, pos.x, pos.y, pos.z)
            },
            itemSettings().component(RagiumComponentTypes.DYNAMITE, HTDynamiteComponent.DEFAULT),
        ),
    )

    @JvmField
    val EMPTY_FLUID_CUBE: Item = registerItem("empty_fluid_cube")

    @JvmField
    val FILLED_FLUID_CUBE: Item = registerItem("filled_fluid_cube", HTFilledFluidCubeItem)

    @JvmField
    val FLATTENING_DYNAMITE: Item = registerItem(
        "flattening_dynamite",
        HTDynamiteItem(HTRemoverDynamiteBehaviors.FLATTEN::onBlockHit, itemSettings()),
    )

    @JvmField
    val FORGE_HAMMER: Item = registerItem("forge_hammer", HTForgeHammerItem)

    @JvmField
    val STEEL_AXE: Item = registerToolItem("steel_axe", HTToolType.AXE, RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_HOE: Item = registerToolItem("steel_hoe", HTToolType.HOE, RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_PICKAXE: Item = registerToolItem("steel_pickaxe", HTToolType.PICKAXE, RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_SHOVEL: Item = registerToolItem("steel_shovel", HTToolType.SHOVEL, RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_SWORD: Item = registerToolItem("steel_sword", HTToolType.SWORD, RagiumToolMaterials.STEEL)

    @JvmField
    val TRADER_CATALOG: Item = registerItem("trader_catalog", HTTraderCatalogItem)

    @JvmField
    val TOOLS: List<Item> = listOf(
        // damageable tool
        STEEL_AXE,
        STEEL_HOE,
        STEEL_PICKAXE,
        STEEL_SHOVEL,
        STEEL_SWORD,
        CRAFTER_HAMMER,
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
    val BEE_WAX: Item = registerItem("bee_wax")

    @JvmField
    val BUTTER: Item = registerItem("butter", itemSettings().food(FoodComponents.APPLE))

    @JvmField
    val CARAMEL: Item = registerItem("caramel", itemSettings().food(FoodComponents.DRIED_KELP))

    @JvmField
    val CHOCOLATE: Item = registerItem(
        "chocolate",
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
    val CHOCOLATE_APPLE: Item = registerItem("chocolate_apple", itemSettings().food(FoodComponents.COOKED_CHICKEN))

    @JvmField
    val CHOCOLATE_BREAD: Item = registerItem("chocolate_bread", itemSettings().food(FoodComponents.COOKED_BEEF))

    @JvmField
    val FLOUR: Item = registerItem("flour")

    @JvmField
    val DOUGH: Item = registerItem("dough")

    @JvmField
    val MINCED_MEAT: Item = registerItem("minced_meat")

    @JvmField
    val PULP: Item = registerItem("pulp")

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
    val BASALT_MESH: Item = registerItem("basalt_mesh")

    @JvmField
    val CRIMSON_CRYSTAL: Item = registerItem("crimson_crystal")

    @JvmField
    val CRUDE_SILICON: Item = registerItem("crude_silicon")

    @JvmField
    val DEEPANT: Item = registerItem("deepant")

    @JvmField
    val ENGINE: Item = registerItem("engine")

    @JvmField
    val ENGINEERING_PLASTIC_PLATE: Item = registerItem("engineering_plastic_plate")

    @JvmField
    val HEART_OF_THE_NETHER: Item = registerItem("heart_of_the_nether", itemSettings().rarity(Rarity.UNCOMMON))

    @JvmField
    val LASER_EMITTER: Item = registerItem("laser_emitter")

    @JvmField
    val PLASTIC_PLATE: Item = registerItem("plastic_plate")

    @JvmField
    val POLYMER_RESIN: Item = registerItem("polymer_resin")

    @JvmField
    val PROCESSOR_SOCKET: Item = registerItem("processor_socket")

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item = registerItem("ragi_alloy_compound")

    @JvmField
    val RAGI_CRYSTAL_PROCESSOR: Item = registerItem("ragi_crystal_processor")

    @JvmField
    val REFINED_SILICON: Item = registerItem("refined_silicon")

    @JvmField
    val RESIDUAL_COKE: Item = registerItem("residual_coke")

    @JvmField
    val SILICON: Item = registerItem("silicon")

    @JvmField
    val SLAG: Item = registerItem("slag")

    @JvmField
    val SOAP_INGOT: Item = registerItem("soap_ingot")

    @JvmField
    val SOLAR_PANEL: Item = registerItem("solar_panel")

    @JvmField
    val STELLA_PLATE: Item = registerItem("stella_plate")

    @JvmField
    val WARPED_CRYSTAL: Item = registerItem("warped_crystal", HTWarpedCrystalItem)

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

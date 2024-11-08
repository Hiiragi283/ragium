package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTArmorType
import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.content.HTToolType
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.item.*
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.util.Rarity

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
        registerArmorItem("stella_goggle", HTArmorType.HELMET, RagiumArmorMaterials.STELLA, 33)

    @JvmField
    val STELLA_JACKET: Item =
        registerArmorItem("stella_jacket", HTArmorType.CHESTPLATE, RagiumArmorMaterials.STELLA, 33)

    @JvmField
    val STELLA_LEGGINGS: Item =
        registerArmorItem("stella_leggings", HTArmorType.LEGGINGS, RagiumArmorMaterials.STELLA, 33)

    @JvmField
    val STELLA_BOOTS: Item =
        registerArmorItem("stella_boots", HTArmorType.BOOTS, RagiumArmorMaterials.STELLA, 33)

    @JvmField
    val RAGIUM_HELMET: Item =
        registerArmorItem("ragium_helmet", HTArmorType.HELMET, RagiumArmorMaterials.RAGIUM, 37)

    @JvmField
    val RAGIUM_CHESTPLATE: Item =
        registerArmorItem("ragium_chestplate", HTArmorType.CHESTPLATE, RagiumArmorMaterials.RAGIUM, 37)

    @JvmField
    val RAGIUM_LEGGINGS: Item =
        registerArmorItem("ragium_leggings", HTArmorType.LEGGINGS, RagiumArmorMaterials.RAGIUM, 37)

    @JvmField
    val RAGIUM_BOOTS: Item =
        registerArmorItem("ragium_boots", HTArmorType.BOOTS, RagiumArmorMaterials.RAGIUM, 37)

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
        RAGIUM_HELMET,
        RAGIUM_CHESTPLATE,
        RAGIUM_LEGGINGS,
        RAGIUM_BOOTS,
    )

    //    Tools    //

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
    val TOOLS: List<Item> = listOf(
        STEEL_AXE,
        STEEL_HOE,
        STEEL_PICKAXE,
        STEEL_SHOVEL,
        STEEL_SWORD,
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
    val BACKPACK: Item = registerItem("backpack", HTBackpackItem)

    @JvmField
    val BASALT_MESH: Item = registerItem("basalt_mesh")

    @JvmField
    val CRAFTER_HAMMER: Item = registerItem("crafter_hammer", HTCrafterHammerItem)

    @JvmField
    val DYNAMITE: Item = registerItem("dynamite", HTDynamiteItem)

    @JvmField
    val EMPTY_FLUID_CUBE: Item = registerItem("empty_fluid_cube")

    @JvmField
    val FILLED_FLUID_CUBE: Item = registerItem("filled_fluid_cube", HTFilledFluidCubeItem)

    @JvmField
    val ENGINE: Item = registerItem("engine")

    @JvmField
    val FORGE_HAMMER: Item = registerItem("forge_hammer", HTForgeHammerItem)

    @JvmField
    val HEART_OF_THE_NETHER: Item = registerItem("heart_of_the_nether", itemSettings().rarity(Rarity.UNCOMMON))

    @JvmField
    val LASER_EMITTER: Item = registerItem("laser_emitter")

    @JvmField
    val POLYMER_RESIN: Item = registerItem("polymer_resin")

    @JvmField
    val PROCESSOR_SOCKET: Item = registerItem("processor_socket")

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item = registerItem("ragi_alloy_compound")

    @JvmField
    val RAGI_CRYSTAL_PROCESSOR: Item = registerItem("ragi_crystal_processor")

    @JvmField
    val REMOVER_DYNAMITE: Item = registerItem("remover_dynamite", HTRemoverDynamiteItem)

    @JvmField
    val SLAG: Item = registerItem("slag")

    @JvmField
    val SOAP_INGOT: Item = registerItem("soap_ingot")

    @JvmField
    val SOLAR_PANEL: Item = registerItem("solar_panel")

    @JvmField
    val TRADER_CATALOG: Item = registerItem("trader_catalog", HTTraderCatalogItem)

    @JvmField
    val MISC: List<Item> = listOf(
        BACKPACK,
        BASALT_MESH,
        CRAFTER_HAMMER,
        DYNAMITE,
        EMPTY_FLUID_CUBE,
        FILLED_FLUID_CUBE,
        ENGINE,
        FORGE_HAMMER,
        HEART_OF_THE_NETHER,
        LASER_EMITTER,
        POLYMER_RESIN,
        PROCESSOR_SOCKET,
        RAGI_ALLOY_COMPOUND,
        RAGI_CRYSTAL_PROCESSOR,
        REMOVER_DYNAMITE,
        SLAG,
        SOAP_INGOT,
        SOLAR_PANEL,
        TRADER_CATALOG,
    )
}

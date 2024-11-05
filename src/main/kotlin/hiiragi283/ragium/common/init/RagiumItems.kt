package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.item.*
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.util.Rarity

object RagiumItems : HTContentRegister {
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
    val SOAP_INGOT: Item = registerItem("soap_ingot")

    @JvmField
    val SOLAR_PANEL: Item = registerItem("solar_panel")

    @JvmField
    val TRADER_CATALOG: Item = registerItem("trader_catalog")

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
        POLYMER_RESIN,
        PROCESSOR_SOCKET,
        RAGI_ALLOY_COMPOUND,
        RAGI_CRYSTAL_PROCESSOR,
        REMOVER_DYNAMITE,
        SOAP_INGOT,
        SOLAR_PANEL,
        TRADER_CATALOG,
    )
}

package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.item.HTWarpedCrystalItem
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.HoneycombItem
import net.minecraft.item.Item
import net.minecraft.text.Text

object RagiumItems {
    //    Ingredients    //

    // organic
    @JvmField
    val BEE_WAX: Item = HoneycombItem(itemSettings())

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

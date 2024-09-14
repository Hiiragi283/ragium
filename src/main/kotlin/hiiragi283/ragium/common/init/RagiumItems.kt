package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.item.HTForgeHammerItem
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumItems {

    @JvmField
    val POWER_METER: Item = register("power_meter") {
        component(RagiumComponentTypes.DISABLE_CYCLE_POWER, Unit)
    }

    @JvmField
    val FORGE_HAMMER: Item = register("forge_hammer", HTForgeHammerItem)

    // tier1
    @JvmField
    val RAW_RAGINITE: Item = register("raw_raginite")

    @JvmField
    val RAW_RAGINITE_DUST: Item = register("raw_raginite_dust")

    @JvmField
    val RAGINITE_DUST: Item = register("raginite_dust")

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item = register("ragi_alloy_compound")

    @JvmField
    val RAGI_ALLOY_INGOT: Item = register("ragi_alloy_ingot")

    @JvmField
    val RAGI_ALLOY_PLATE: Item = register("ragi_alloy_plate")

    @JvmField
    val RAGI_ALLOY_ROD: Item = register("ragi_alloy_rod")

    // tier2
    @JvmField
    val RAGI_STEEL_INGOT: Item = register("ragi_steel_ingot")

    @JvmField
    val RAGI_STEEL_PLATE: Item = register("ragi_steel_plate")

    // tier3
    @JvmField
    val REFINED_RAGI_STEEL_INGOT: Item = register("refined_ragi_steel_ingot")

    @JvmField
    val REFINED_RAGI_STEEL_PLATE: Item = register("refined_ragi_steel_plate")

    // tier4

    // tier5

    private fun <T : Item> register(name: String, item: T): T =
        Registry.register(Registries.ITEM, Ragium.id(name), item)

    private fun register(name: String, builder: Item.Settings.() -> Unit = {}): Item =
        register(name, Item(Item.Settings().apply(builder)))

    /*private fun register(block: Block, builder: Item.Settings.() -> Unit = {}): Item =
        Registries.BLOCK.getKey(block).map {
            Registry.register(Registries.ITEM, it.value, BlockItem(block, Item.Settings().apply(builder)))
        }.orElseThrow()*/

    private fun registerFluidCell(fluid: Fluid) {

    }

}
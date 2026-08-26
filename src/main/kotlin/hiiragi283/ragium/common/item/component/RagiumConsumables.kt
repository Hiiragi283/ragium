package hiiragi283.ragium.common.item.component

import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.Consumables

data object RagiumConsumables {
    @JvmField
    val FLUID_BUCKET: Consumable = Consumables.defaultDrink().consumeSeconds(6.4f).build()
}

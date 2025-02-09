package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * Ragiumが追加する[DataMapType]
 */
object RagiumDataMaps {
    @JvmField
    val EXECUTIONER_DROPS: DataMapType<EntityType<*>, Holder<Item>> = AdvancedDataMapType
        .builder(
            RagiumAPI.id("executioner_drops"),
            Registries.ENTITY_TYPE,
            ItemStack.ITEM_NON_AIR_CODEC,
        ).synced(ItemStack.ITEM_NON_AIR_CODEC, false)
        .build()
}

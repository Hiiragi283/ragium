package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * Ragiumが追加する[DataMapType]
 */
object RagiumDataMaps {
    /**
     * 枯葉剤でのブロック置き換えで参照します。
     */
    @JvmField
    val DEFOLIANT: DataMapType<Block, HTDefoliant> = DataMapType
        .builder(
            RagiumAPI.id("defoliant"),
            Registries.BLOCK,
            HTDefoliant.CODEC,
        ).synced(HTDefoliant.CODEC, false)
        .build()

    /**
     * エンチャント「処刑人」のドロップテーブルで参照します。
     */
    @JvmField
    val EXECUTIONER_DROPS: DataMapType<EntityType<*>, Holder<Item>> = DataMapType
        .builder(
            RagiumAPI.id("executioner_drops"),
            Registries.ENTITY_TYPE,
            ItemStack.ITEM_NON_AIR_CODEC,
        ).synced(ItemStack.ITEM_NON_AIR_CODEC, false)
        .build()
}

package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.RagiumConst
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player

class HTSimpleMagnetItem(properties: Properties) : HTMagnetItem<ItemEntity>(properties) {
    override val entityClass: Class<ItemEntity> = ItemEntity::class.java

    override fun forEachEntity(entity: ItemEntity, player: Player) {
        // IEのコンベヤ上にいるアイテムは無視する
        if (entity.persistentData.getBoolean(RagiumConst.PREVENT_ITEM_MAGNET)) return
        if (entity.isAlive && !entity.hasPickUpDelay()) {
            entity.playerTouch(player)
        }
    }
}

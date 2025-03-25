package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.HTMagnetItem
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import kotlin.jvm.java

class HTSimpleMagnetItem(properties: Properties) : HTMagnetItem<ItemEntity>(properties) {
    override val entityClass: Class<ItemEntity> = ItemEntity::class.java

    override fun forEachEntity(entity: ItemEntity, player: Player) {
        // Exclude items on IE Conveyor
        if (entity.persistentData.getBoolean("PreventRemoteMovement")) return
        if (entity.isAlive && !entity.hasPickUpDelay()) {
            entity.playerTouch(player)
        }
    }
}

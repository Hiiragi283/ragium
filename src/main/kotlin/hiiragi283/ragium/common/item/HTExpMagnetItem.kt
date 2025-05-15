package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.HTMagnetItem
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player

class HTExpMagnetItem(properties: Properties) : HTMagnetItem<ExperienceOrb>(properties) {
    override val entityClass: Class<ExperienceOrb> = ExperienceOrb::class.java

    override fun forEachEntity(entity: ExperienceOrb, player: Player) {
        if (entity.value > 0) {
            entity.playerTouch(player)
        }
    }
}

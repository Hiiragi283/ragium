package hiiragi283.ragium.common.item.magnet

import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player

class HTExpMagnetItem(properties: Properties) : HTMagnetItem<ExperienceOrb>(properties.stacksTo(1)) {
    override val entityClass: Class<ExperienceOrb> = ExperienceOrb::class.java

    override fun forEachEntity(entity: ExperienceOrb, player: Player) {
        if (entity.value > 0) {
            entity.playerTouch(player)
        }
    }
}

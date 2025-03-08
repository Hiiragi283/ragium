package hiiragi283.ragium.common.item.dynamite

import hiiragi283.ragium.common.entity.HTDynamite
import hiiragi283.ragium.common.entity.HTFlattenDynamite
import hiiragi283.ragium.common.item.HTThrowableItem
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTFlattenDynamiteItem(properties: Properties) : HTThrowableItem(properties) {
    override fun throwProjectile(level: Level, player: Player, stack: ItemStack): Projectile =
        HTDynamite.withItem(level, player, stack, ::HTFlattenDynamite)
}

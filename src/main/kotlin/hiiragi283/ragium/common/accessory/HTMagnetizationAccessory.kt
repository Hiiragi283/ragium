package hiiragi283.ragium.common.accessory

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.common.util.HTItemHelper
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate

class HTMagnetizationAccessory<T : Entity>(val entityClass: Class<T>, val interaction: BiPredicate<T, Player>) : Accessory {
    companion object {
        @JvmStatic
        inline fun <reified T : Entity> create(interaction: BiPredicate<T, Player>): HTMagnetizationAccessory<T> =
            HTMagnetizationAccessory(T::class.java, interaction)
    }

    override fun tick(stack: ItemStack, reference: SlotReference) {
        super.tick(stack, reference)
        val player: Player = reference.entity() as? Player ?: return
        val level: ServerLevel = player.level() as? ServerLevel ?: return
        val range: Double = HTItemHelper.processCollectorRange(level, stack)
        val entitiesInRange: List<T> = level.getEntitiesOfClass(
            entityClass,
            player.position().getRangedAABB(range),
        )
        for (entity: T in entitiesInRange) {
            if (interaction.test(entity, player)) {
                entity.playerTouch(player)
            }
        }
    }
}

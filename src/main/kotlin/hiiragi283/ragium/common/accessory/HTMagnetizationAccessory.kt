package hiiragi283.ragium.common.accessory

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.config.RagiumConfig
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.function.BiConsumer

class HTMagnetizationAccessory<T : Entity>(val entityClass: Class<T>, val interaction: BiConsumer<T, Player>) : Accessory {
    companion object {
        @JvmStatic
        inline fun <reified T : Entity> create(interaction: BiConsumer<T, Player>): HTMagnetizationAccessory<T> =
            HTMagnetizationAccessory(T::class.java, interaction)
    }

    override fun tick(stack: ItemStack, reference: SlotReference) {
        super.tick(stack, reference)
        val player: Player = reference.entity() as? Player ?: return
        val level: Level = player.level()
        val range: Double = RagiumConfig.CONFIG.deviceCollectorEntityRange.asDouble * 2.0
        val entitiesInRange: List<T> = level.getEntitiesOfClass(
            entityClass,
            player.position().getRangedAABB(range),
        )
        for (entity: T in entitiesInRange) {
            interaction.accept(entity, player)
        }
    }
}

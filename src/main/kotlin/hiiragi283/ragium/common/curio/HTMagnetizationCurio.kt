package hiiragi283.ragium.common.curio

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem
import java.util.function.BiConsumer

class HTMagnetizationCurio<T : Entity>(val entityClass: Class<T>, val interaction: BiConsumer<T, Player>) : ICurioItem {
    companion object {
        @JvmStatic
        inline fun <reified  T: Entity> create(interaction: BiConsumer<T, Player>): HTMagnetizationCurio<T> = HTMagnetizationCurio(T::class.java, interaction)
    }
    
    override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
        super.curioTick(slotContext, stack)
        val player: Player = slotContext.entity as? Player ?: return
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

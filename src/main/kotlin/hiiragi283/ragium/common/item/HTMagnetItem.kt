package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.common.item.base.HTActivatableItem
import hiiragi283.ragium.common.util.HTItemHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.function.DoubleSupplier

class HTMagnetItem(private val range: DoubleSupplier, properties: Properties) : HTActivatableItem(properties) {
    override fun onTick(
        stack: ItemStack,
        level: Level,
        player: Player,
        slotId: Int,
        isSelected: Boolean,
    ) {
        val level: ServerLevel = level as? ServerLevel ?: return
        val range: Double = HTItemHelper.processCollectorRange(level, stack, range.asDouble)

        val entitiesInRange: List<Entity> = level.getEntities(
            player,
            player.position().getRangedAABB(range),
        ) { entityIn: Entity? ->
            when (entityIn) {
                is ItemEntity -> !entityIn.persistentData.getBoolean(RagiumConst.PREVENT_ITEM_MAGNET)
                is ExperienceOrb -> true
                else -> false
            }
        }
        for (entityIn: Entity in entitiesInRange) {
            entityIn.playerTouch(player)
        }
    }
}

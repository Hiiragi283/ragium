package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.chat.Component
import net.minecraft.util.Unit
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

abstract class HTMagnetItem<T : Entity>(properties: Properties) : Item(properties.stacksTo(1)) {
    protected val isActive: DataComponentType<Unit> get() = RagiumAPI.getInstance().getActiveComponent()

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!level.isClientSide && player.isShiftKeyDown) {
            val stack: ItemStack = player.getItemInHand(usedHand)
            when (stack.has(isActive)) {
                true -> stack.remove(isActive)
                false -> stack.set(isActive, Unit.INSTANCE)
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
        }
        return super.use(level, player, usedHand)
    }

    override fun inventoryTick(
        stack: ItemStack,
        level: Level,
        entity: Entity,
        slotId: Int,
        isSelected: Boolean,
    ) {
        val player: Player = entity as? Player ?: return
        if (!stack.has(isActive)) return
        val range: Int = RagiumAPI.getInstance().getEffectRange(stack)
        val entitiesInRange: List<T> = level.getEntitiesOfClass(
            entityClass,
            AABB(
                player.x - range,
                player.y - range,
                player.z - range,
                player.x + range,
                player.y + range,
                player.z + range,
            ),
        )
        for (entity: T in entitiesInRange) {
            forEachEntity(entity, player)
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        tooltips.add(RagiumAPI.getInstance().createRangeText(stack))
    }

    override fun isFoil(stack: ItemStack): Boolean = stack.has(isActive) || super.isFoil(stack)

    protected abstract val entityClass: Class<T>

    protected abstract fun forEachEntity(entity: T, player: Player)
}

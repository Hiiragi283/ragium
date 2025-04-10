package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.extension.getHighestLevel
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.tag.RagiumEnchantmentTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
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
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

abstract class HTMagnetItem<T : Entity>(properties: Properties) : Item(properties.stacksTo(1)) {
    protected fun getRange(stack: ItemStack, level: Level?): Int {
        val enchLookup: HolderLookup.RegistryLookup<Enchantment> = level?.registryAccess()?.enchLookup() ?: return 0
        val enchLevel: Int = stack.getAllEnchantments(enchLookup).getHighestLevel(RagiumEnchantmentTags.RANGE)
        return (enchLevel + 1) * 5
    }

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
        val range: Int = getRange(stack, level)
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
        tooltips.add(
            Component
                .translatable(
                    RagiumTranslationKeys.TEXT_EFFECT_RANGE,
                    intText(getRange(stack, context.level())).withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
    }

    override fun isFoil(stack: ItemStack): Boolean = stack.has(isActive)

    override fun supportsEnchantment(stack: ItemStack, enchantment: Holder<Enchantment>): Boolean =
        super.supportsEnchantment(stack, enchantment) || enchantment.`is`(RagiumEnchantmentTags.RANGE)

    protected abstract val entityClass: Class<T>

    protected abstract fun forEachEntity(entity: T, player: Player)
}

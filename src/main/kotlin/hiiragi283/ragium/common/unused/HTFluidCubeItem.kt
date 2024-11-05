package hiiragi283.ragium.common.unused

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

open class HTFluidCubeItem(settings: Settings = itemSettings()) : Item(settings.recipeRemainder(RagiumItems.EMPTY_FLUID_CUBE)) {
    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        onDrink(stack, world, user)
        dropStackAt(
            user,
            RagiumItems.EMPTY_FLUID_CUBE.defaultStack,
        )
        stack.decrementUnlessCreative(1, user)
        return stack
    }

    open fun onDrink(stack: ItemStack, world: World, user: LivingEntity) {}

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.DRINK

    override fun getMaxUseTime(stack: ItemStack, user: LivingEntity): Int = 32

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
        ItemUsage.consumeHeldItem(world, user, hand)
}

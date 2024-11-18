package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.WanderingTraderEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object HTTraderCatalogItem : Item(itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.TRADER_CATALOG))) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack: ItemStack = user.getStackInHand(hand)
        user.itemCooldownManager.set(this, 20 * 1 * 60)
        return if (stack.isOf(this)) {
            WanderingTraderEntity(EntityType.WANDERING_TRADER, world).interactMob(user, Hand.MAIN_HAND)
            TypedActionResult.success(stack, world.isClient)
        } else {
            TypedActionResult.pass(stack)
        }
    }
}

package hiiragi283.ragium.api.data.interaction

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.state.BlockState
import java.util.*

data class HTBlockInteraction(
    val ingredient: Ingredient,
    val predicate: Optional<StatePropertiesPredicate>,
    val actions: List<HTBlockAction>,
) {
    companion object {
        @JvmField
        val CODEC: Codec<HTBlockInteraction> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTBlockInteraction::ingredient),
                    StatePropertiesPredicate.CODEC.optionalFieldOf("predicate").forGetter(HTBlockInteraction::predicate),
                    HTBlockAction.CODEC
                        .listOf()
                        .fieldOf("actions")
                        .forGetter(HTBlockInteraction::actions),
                ).apply(instance, ::HTBlockInteraction)
        }
    }

    fun canPerformActions(stack: ItemStack, state: BlockState): Boolean =
        ingredient.test(stack) && (predicate.isEmpty || predicate.get().matches(state))

    fun applyActions(context: UseOnContext, player: Player) {
        for (action: HTBlockAction in actions) {
            action.applyAction(context)
        }

        val stack: ItemStack = context.itemInHand
        val hand: InteractionHand = context.hand
        if (stack.hasCraftingRemainingItem()) {
            val newStack: ItemStack = stack.craftingRemainingItem
            player.setItemInHand(hand, newStack)
        } else if (stack.isDamageableItem) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))
        } else {
            stack.consume(1, player)
        }
    }
}

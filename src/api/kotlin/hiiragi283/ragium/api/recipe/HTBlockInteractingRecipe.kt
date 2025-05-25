package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import net.minecraft.core.HolderLookup
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface HTBlockInteractingRecipe : Recipe<HTInteractRecipeInput> {
    val ingredient: Ingredient
    val actions: List<HTBlockAction>

    fun matchState(state: BlockState): Boolean

    fun applyActions(context: UseOnContext) {
        for (action: HTBlockAction in actions) {
            action.applyAction(context)
        }

        val stack: ItemStack = context.itemInHand
        val hand: InteractionHand = context.hand
        val player: Player? = context.player
        if (player != null) {
            if (stack.hasCraftingRemainingItem()) {
                val newStack: ItemStack = stack.craftingRemainingItem
                player.setItemInHand(hand, newStack)
            } else if (stack.isDamageableItem) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))
            } else {
                stack.consume(1, player)
            }
        } else {
            stack.shrink(1)
        }
    }

    //    Recipe    //

    override fun matches(input: HTInteractRecipeInput, level: Level): Boolean {
        // ブロックが一致しているか判定
        val state: BlockState = level.getBlockState(input.pos)
        if (!matchState(state)) return false
        // 材料が一致してるか判定
        return ingredient.test(input.item)
    }

    override fun assemble(input: HTInteractRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getType(): RecipeType<*> = RagiumAPI.getInstance().getBlockInteractingRecipeType()
}

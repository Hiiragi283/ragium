package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

interface HTItemOutputRecipeBuilder<B : HTItemOutputRecipeBuilder<B>> : HTRecipeBuilder {
    fun itemOutput(item: ItemLike, count: Int = 1, chance: Float = 1f): B = itemOutput(ItemStack(item, count), chance)

    fun itemOutput(stack: ItemStack, chance: Float = 1f): B {
        validateStack(stack)
        return itemOutput(stack.itemHolder.idOrThrow, stack.count, stack.componentsPatch, chance)
    }

    fun itemOutput(
        id: ResourceLocation,
        count: Int = 1,
        component: DataComponentPatch = DataComponentPatch.EMPTY,
        chance: Float = 1f,
    ): B

    fun itemOutput(
        tagKey: TagKey<Item>,
        count: Int = 1,
        chance: Float = 1f,
        appendCondition: Boolean = false,
    ): B

    fun validateStack(stack: ItemStack) {
        if (stack.isEmpty) {
            error("Empty ItemStack is not allowed for HTItemOutput!")
        }
    }

    fun validateChance(chance: Float) {
        if (chance !in (0f..1f)) {
            error("Chance must be in 0f to 1f!")
        }
    }
}

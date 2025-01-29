package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.block.addon.HTCatalystAddonBlockEntity
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level

data class HTProcessorCatalystCondition(override val itemIngredient: Ingredient) : HTMachineRecipeCondition.ItemBased {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTProcessorCatalystCondition> = Ingredient.CODEC_NONEMPTY
            .fieldOf("ingredient")
            .xmap(::HTProcessorCatalystCondition, HTProcessorCatalystCondition::itemIngredient)
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent =
        Component
            .translatable(RagiumTranslationKeys.CATALYST_CONDITION)
            .withStyle(ChatFormatting.GREEN)

    override fun test(level: Level, pos: BlockPos): Boolean {
        for (direction: Direction in Direction.entries) {
            val posTo: BlockPos = pos.relative(direction)
            val catalystStack: ItemStack =
                (level.getBlockEntity(posTo) as? HTCatalystAddonBlockEntity)?.catalystStack ?: continue
            if (itemIngredient.test(catalystStack)) {
                return true
            }
        }
        return false
    }
}

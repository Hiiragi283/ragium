package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level

data class HTProcessorCatalystCondition(val ingredient: Ingredient) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTProcessorCatalystCondition> = Ingredient.CODEC_NONEMPTY
            .fieldOf("ingredient")
            .xmap(::HTProcessorCatalystCondition, HTProcessorCatalystCondition::ingredient)
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent =
        Component
            .translatable(RagiumTranslationKeys.CATALYST_CONDITION)
            .withStyle(ChatFormatting.GREEN)

    override fun test(level: Level, pos: BlockPos): Boolean {
        val propertyHolder: HTPropertyHolder = level.getMachineEntity(pos)?.machineKey?.getProperty() ?: return false
        val catalystSlot: Int = propertyHolder[HTMachinePropertyKeys.CATALYST_SLOT] ?: return false
        val stackIn: ItemStack =
            level.getMachineEntity(pos)?.getItemHandler(null)?.getStackInSlot(catalystSlot) ?: return false
        return ingredient.test(stackIn)
    }
}

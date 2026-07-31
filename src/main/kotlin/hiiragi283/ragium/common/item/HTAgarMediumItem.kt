package hiiragi283.ragium.common.item

import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class HTAgarMediumItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val lookupContext: HTRecipeLookup.Context = HTRecipeLookup.Context.create(level)
        // TODO
        return super.useOn(context)
    }
}

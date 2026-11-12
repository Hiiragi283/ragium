package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.EnchantingTableBlock

class HTEnchantmentWidget(val power: () -> Float, x: Int, y: Int) : HTAbstractWidget(x, y, 18, 18) {
    companion object {
        @JvmStatic
        fun collectPower(level: Level?, pos: BlockPos): Float {
            val level: Level = level ?: return 0f
            return EnchantingTableBlock.BOOKSHELF_OFFSETS
                .filter { EnchantingTableBlock.isValidBookShelf(level, pos, it) }
                .map {
                    val fixedPos: BlockPos = pos.offset(it)
                    level.getBlockState(fixedPos).getEnchantPowerBonus(level, fixedPos)
                }.sum()
        }
    }

    override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.ENCHANTMENT.get()
}

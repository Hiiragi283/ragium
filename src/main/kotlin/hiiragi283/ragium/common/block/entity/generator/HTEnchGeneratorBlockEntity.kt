package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.isOf
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.block.state.BlockState

class HTEnchGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(RagiumBlocks.ENCHANTMENT_GENERATOR, pos, state) {
    override fun getFuelValue(stack: ImmutableItemStack): Int {
        if (!stack.isOf(Items.ENCHANTED_BOOK)) return 0
        return EnchantmentHelper
            .getEnchantmentsForCrafting(stack.stack)
            .entrySet()
            .sumOf { (holder: Holder<Enchantment>, level: Int) ->
                val amount: Int = this.level?.registryAccess()?.let { access: RegistryAccess ->
                    RagiumDataMaps.INSTANCE.getEnchBasedValue(access, holder, level)
                } ?: level
                amount * 100
            }
    }

    override fun getFuelStack(value: Int): ImmutableFluidStack = RagiumFluidContents.EXPERIENCE.toStorageStack(value)

    override fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack): Int =
        if (RagiumFluidContents.EXPERIENCE.isOf(stack)) 10 else 0
}

package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.text.Text
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block

class HTImitationSpawnerBlockItem(block: Block, properties: Properties) :
    BlockItem(block, properties),
    HTSubCreativeTabContents {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {
        super.appendHoverText(stack, context, tooltips, flag)
        stack
            .get(RagiumDataComponents.SPAWNER_MOB)
            ?.let(HTSpawnerMob::getText)
            ?.let(Text::copy)
            ?.withStyle(ChatFormatting.GRAY)
            ?.let(tooltips::add)
    }

    override fun addItems(baseItem: Holder<Item>, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output) {
        parameters.holders()
            .lookupOrThrow(Registries.ENTITY_TYPE)
            .filterElements(HTImitationSpawnerBlock::filterEntityType)
            .listElements()
            .map { holder: Holder<EntityType<*>> ->
                createItemStack(
                    baseItem.value(),
                    RagiumDataComponents.SPAWNER_MOB,
                    HTSpawnerMob.of(holder),
                )
            }.forEach(output::accept)
    }
}

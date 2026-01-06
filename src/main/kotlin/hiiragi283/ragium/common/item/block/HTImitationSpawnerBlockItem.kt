package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import java.util.function.Consumer

class HTImitationSpawnerBlockItem(block: HTImitationSpawnerBlock, properties: Properties) :
    HTBlockItem<HTImitationSpawnerBlock>(block, properties),
    HTSubCreativeTabContents {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        super.appendHoverText(stack, context, tooltips, flag)
        stack
            .get(RagiumDataComponents.SPAWNER_MOB)
            ?.let(HTSpawnerMob::getText)
            ?.let(Component::copy)
            ?.withStyle(ChatFormatting.GRAY)
            ?.let(tooltips::add)
    }

    override fun addItems(baseItem: HTItemHolderLike<*>, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>) {
        parameters
            .holders()
            .lookupOrThrow(Registries.ENTITY_TYPE)
            .filterElements(HTImitationSpawnerBlock::filterEntityType)
            .listElements()
            .forEach { holder: Holder<EntityType<*>> ->
                createItemStack(
                    baseItem,
                    RagiumDataComponents.SPAWNER_MOB,
                    HTSpawnerMob(holder),
                ).let(consumer::accept)
            }
    }
}

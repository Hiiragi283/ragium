package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block
import java.util.function.Consumer

class HTImitationSpawnerBlockItem(block: Block, properties: Properties) :
    HTBlockItem<Block>(block, properties),
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
            ?.let(HTSpawnerMob::entityType)
            ?.let(EntityType<*>::getDescription)
            ?.let(Component::copy)
            ?.withStyle(ChatFormatting.GRAY)
            ?.let(tooltips::add)
    }

    override fun addItems(baseItem: HTItemHolderLike, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>) {
        parameters
            .holders()
            .lookupOrThrow(Registries.ENTITY_TYPE)
            .filterElements { entityType: EntityType<*> -> SpawnEggItem.byId(entityType) != null }
            .listElements()
            .forEach { holder: Holder<EntityType<*>> ->
                consumer.accept(
                    baseItem.toStack(
                        components = DataComponentPatch
                            .builder()
                            .set(RagiumDataComponents.SPAWNER_MOB, HTSpawnerMob(holder))
                            .build(),
                    ),
                )
            }
    }
}

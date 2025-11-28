package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.HolderLookup
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.function.Consumer

class HTUniversalBundleItem(properties: Properties) :
    Item(properties.stacksTo(1)),
    HTSubCreativeTabContents {
    companion object {
        @JvmStatic
        fun createBundle(color: DyeColor): ImmutableItemStack =
            ImmutableItemStack.of(RagiumItems.UNIVERSAL_BUNDLE).plus(RagiumDataComponents.COLOR, color)

        @JvmStatic
        fun openBundle(level: Level, player: Player, stack: ItemStack): InteractionResultHolder<ItemStack> {
            val color: DyeColor = stack.get(RagiumDataComponents.COLOR) ?: return InteractionResultHolder.fail(stack)
            if (level is ServerLevel) {
                val handler: HTItemHandler = RagiumPlatform.INSTANCE.getUniversalBundle(level.server, color)
                RagiumMenuTypes.UNIVERSAL_BUNDLE.openMenu(player, stack.hoverName, handler) {}
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
        }
    }

    override fun getDefaultInstance(): ItemStack = createBundle(DyeColor.WHITE).unwrap()

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> =
        openBundle(level, player, player.getItemInHand(usedHand))

    override fun addItems(baseItem: HTItemHolderLike, provider: HolderLookup.Provider, consumer: Consumer<ItemStack>) {
        DyeColor.entries
            .map(::createBundle)
            .map(ImmutableItemStack::unwrap)
            .forEach(consumer)
    }

    override fun shouldAddDefault(): Boolean = false
}

package hiiragi283.ragium.api.inventory.container.type

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.network.IContainerFactory

/**
 * @see [mekanism.common.inventory.container.type.MekanismContainerType]
 */
class HTMenuType<MENU : AbstractContainerMenu, C>(val factory: HTContainerFactory<MENU, C>, constructor: IContainerFactory<MENU>) :
    MenuType<MENU>(constructor, FeatureFlags.VANILLA_SET),
    HTContainerFactory<MENU, C> by factory {
    constructor(
        factory: HTContainerFactory<MENU, C>,
        decoder: (RegistryFriendlyByteBuf?) -> C,
    ) : this(
        factory,
        { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf? ->
            factory.create(containerId, inventory, decoder(buf))
        },
    )
}

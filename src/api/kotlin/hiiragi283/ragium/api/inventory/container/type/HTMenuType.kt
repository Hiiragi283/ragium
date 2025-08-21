package hiiragi283.ragium.api.inventory.container.type

import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamDecoder
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.network.IContainerFactory

/**
 * @see [mekanism.common.inventory.container.type.MekanismContainerType]
 */
class HTMenuType<MENU : AbstractContainerMenu, C>(val factory: HTContainerFactory<MENU, C>, constructor: IContainerFactory<MENU>) :
    MenuType<MENU>(constructor, FeatureFlags.VANILLA_SET),
    HTContainerFactory<MENU, C> by factory {
    companion object {
        @JvmStatic
        fun <MENU : AbstractContainerMenu, C> create(
            factory: HTContainerFactory<MENU, C>,
            decoder: StreamDecoder<in RegistryFriendlyByteBuf, C>,
        ): HTMenuType<MENU, C> = HTMenuType(factory) { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf ->
            factory.create(containerId, inventory, decoder.decode(buf))
        }

        @JvmStatic
        inline fun <MENU : AbstractContainerMenu, reified BE : BlockEntity> blockEntity(
            factory: HTContainerFactory<MENU, BE>,
        ): HTMenuType<MENU, BE> = create(factory, ::getBlockEntityFromBuf)

        /**
         * @see [mekanism.common.inventory.container.type.MekanismContainerType.getTileFromBuf]
         */
        @JvmStatic
        inline fun <reified BE : BlockEntity> getBlockEntityFromBuf(buf: FriendlyByteBuf?): BE {
            checkNotNull(buf)
            if (!FMLEnvironment.dist.isClient) error("Only supported on client side")
            return Minecraft.getInstance().level?.getBlockEntity(buf.readBlockPos()) as? BE
                ?: error("Failed to find block entity on client side")
        }
    }
}

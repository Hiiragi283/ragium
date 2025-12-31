package hiiragi283.ragium.setup

import hiiragi283.core.api.inventory.container.type.HTContainerFactory
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.core.common.registry.register.HTDeferredMenuTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.entity.processing.HTDryerBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTPyrolyzerBlockEntity
import hiiragi283.ragium.common.inventory.container.HTComplexContainerMenu
import hiiragi283.ragium.common.inventory.container.HTMelterContainerMenu
import hiiragi283.ragium.common.inventory.container.HTPyrolyzerContainerMenu
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.loading.FMLEnvironment

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTDeferredMenuTypeRegister(RagiumAPI.MOD_ID)

    //    Processor    //

    @JvmField
    val DRYER: HTDeferredMenuType.WithContext<HTComplexContainerMenu<HTDryerBlockEntity>, HTDryerBlockEntity> =
        registerBE(RagiumConst.DRYER, HTComplexContainerMenu.Companion::dryer)

    @JvmField
    val MELTER: HTDeferredMenuType.WithContext<HTMelterContainerMenu, HTMelterBlockEntity> =
        registerBE(RagiumConst.MELTER, ::HTMelterContainerMenu)

    @JvmField
    val PYROLYZER: HTDeferredMenuType.WithContext<HTPyrolyzerContainerMenu, HTPyrolyzerBlockEntity> =
        registerBE(RagiumConst.PYROLYZER, ::HTPyrolyzerContainerMenu)

    //    Extensions    //

    /**
     * @see mekanism.common.inventory.container.type.MekanismContainerType.getTileFromBuf
     */
    @JvmStatic
    inline fun <reified BE : BlockEntity> getBlockEntityFromBuf(buf: FriendlyByteBuf?): BE {
        checkNotNull(buf)
        check(FMLEnvironment.dist.isClient) { "Only supported on client side" }
        val level: Level = checkNotNull(Minecraft.getInstance().level) { "Failed to find client level" }
        val pos: BlockPos = buf.readBlockPos()
        return checkNotNull(level.getTypedBlockEntity<BE>(pos)) { "No block entity is present at $pos" }
    }

    @JvmStatic
    inline fun <reified BE : HTBlockEntity, MENU : HTBlockEntityContainerMenu<BE>> registerBE(
        name: String,
        factory: HTContainerFactory<MENU, BE>,
    ): HTDeferredMenuType.WithContext<MENU, BE> = REGISTER.registerType(name, factory, ::getBlockEntityFromBuf)
}

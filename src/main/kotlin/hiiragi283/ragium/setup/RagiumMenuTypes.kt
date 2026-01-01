package hiiragi283.ragium.setup

import hiiragi283.core.api.inventory.container.type.HTContainerFactory
import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.core.common.registry.register.HTDeferredMenuTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.entity.processing.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTDryerBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTPyrolyzerBlockEntity
import hiiragi283.ragium.common.inventory.HTComplexMenu
import hiiragi283.ragium.common.inventory.HTCrusherMenu
import hiiragi283.ragium.common.inventory.HTCuttingMachineMenu
import hiiragi283.ragium.common.inventory.HTMelterMenu
import hiiragi283.ragium.common.inventory.HTPyrolyzerMenu
import hiiragi283.ragium.common.inventory.HTUniversalChestMenu
import hiiragi283.ragium.common.item.tool.HTUniversalChestManager
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
    val CRUSHER: HTDeferredMenuType.WithContext<HTCrusherMenu, HTCrusherBlockEntity> =
        registerBE(RagiumConst.CRUSHER, ::HTCrusherMenu)

    @JvmField
    val CUTTING_MACHINE: HTDeferredMenuType.WithContext<HTCuttingMachineMenu, HTCuttingMachineBlockEntity> =
        registerBE(RagiumConst.CUTTING_MACHINE, ::HTCuttingMachineMenu)

    @JvmField
    val DRYER: HTDeferredMenuType.WithContext<HTComplexMenu<HTDryerBlockEntity>, HTDryerBlockEntity> =
        registerBE(RagiumConst.DRYER, HTComplexMenu.Companion::dryer)

    @JvmField
    val MELTER: HTDeferredMenuType.WithContext<HTMelterMenu, HTMelterBlockEntity> =
        registerBE(RagiumConst.MELTER, ::HTMelterMenu)

    @JvmField
    val MIXER: HTDeferredMenuType.WithContext<HTComplexMenu<HTMixerBlockEntity>, HTMixerBlockEntity> =
        registerBE(RagiumConst.MIXER, HTComplexMenu.Companion::mixer)

    @JvmField
    val PYROLYZER: HTDeferredMenuType.WithContext<HTPyrolyzerMenu, HTPyrolyzerBlockEntity> =
        registerBE(RagiumConst.PYROLYZER, ::HTPyrolyzerMenu)

    //    Storages    //

    @JvmField
    val UNIVERSAL_CHEST: HTDeferredMenuType.WithContext<HTUniversalChestMenu, HTItemHandler> =
        REGISTER.registerType(RagiumConst.UNIVERSAL_CHEST, ::HTUniversalChestMenu) {
            HTItemHandler { HTUniversalChestManager.createSlots() }
        }

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

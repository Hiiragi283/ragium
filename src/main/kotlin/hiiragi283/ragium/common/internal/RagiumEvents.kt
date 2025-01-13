package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumCapabilities
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.extension.material
import hiiragi283.ragium.api.extension.tieredText
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import org.slf4j.Logger

internal object RagiumEvents {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    fun register(eventBus: IEventBus) {
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::modifyComponents)
        eventBus.addListener(::registerCapabilities)
        eventBus.addListener(::buildCreativeTabs)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {}

    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : ItemLike> modifyAll(items: Collection<T>, patch: (DataComponentPatch.Builder, T) -> Unit) {
            items.forEach { itemLike: T ->
                event.modify(itemLike.asItem()) { builder: DataComponentPatch.Builder -> patch(builder, itemLike) }
            }
        }

        fun tieredText(translationKey: String): (DataComponentPatch.Builder, HTMachineTierProvider) -> Unit =
            { builder: DataComponentPatch.Builder, provider: HTMachineTierProvider ->
                builder.tieredText(translationKey, provider.tier)
            }

        modifyAll(RagiumBlocks.StorageBlocks.entries, DataComponentPatch.Builder::material)
        modifyAll(RagiumBlocks.Grates.entries, tieredText(RagiumTranslationKeys.GRATE))
        modifyAll(RagiumBlocks.Casings.entries, tieredText(RagiumTranslationKeys.CASING))
        modifyAll(RagiumBlocks.Hulls.entries, tieredText(RagiumTranslationKeys.HULL))
        modifyAll(RagiumBlocks.Coils.entries, tieredText(RagiumTranslationKeys.COIL))

        modifyAll(RagiumBlocks.Drums.entries, tieredText(RagiumTranslationKeys.DRUM))

        LOGGER.info("Modified item components!")
    }

    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        fun <T : Any, C> registerForBlocks(capability: BlockCapability<T, C>, provider: IBlockCapabilityProvider<T, C>) {
            for (block: Block in BuiltInRegistries.BLOCK) {
                event.registerBlock(
                    capability,
                    provider,
                    block,
                )
            }
        }

        registerForBlocks(
            RagiumCapabilities.CONTROLLER_HOLDER,
        ) { _: Level, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, _: Direction -> blockEntity as? HTControllerHolder }

        registerForBlocks(
            RagiumCapabilities.MACHINE_TIER,
        ) { _: Level, _: BlockPos, state: BlockState, blockEntity: BlockEntity?, _: Void? ->
            (blockEntity as? HTMachineTierProvider)?.tier ?: state.machineTier
        }

        LOGGER.info("Registered capabilities!")
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
    }
}

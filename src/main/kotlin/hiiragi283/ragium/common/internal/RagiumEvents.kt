package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumCapabilities
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.extension.material
import hiiragi283.ragium.api.extension.tieredText
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.resource.HTRuntimeDatapack
import hiiragi283.ragium.common.init.*
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.repository.Pack
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.event.AddPackFindersEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import org.slf4j.Logger
import java.util.function.Consumer
import java.util.function.Supplier

internal object RagiumEvents {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    fun register(eventBus: IEventBus) {
        eventBus.addListener(::createRegistry)

        eventBus.addListener(::commonSetup)

        eventBus.addListener(::addRuntimePack)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerCapabilities)
        eventBus.addListener(::modifyComponents)
    }

    private fun createRegistry(event: NewRegistryEvent) {
        event.register(RagiumRegistries.MULTIBLOCK_COMPONENT_TYPE)

        LOGGER.info("Registered new registries!")
    }

    private fun addRuntimePack(event: AddPackFindersEvent) {
        if (event.packType != PackType.SERVER_DATA) return
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, Items.DIAMOND)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', Items.DIRT)
            .define('B', ItemTags.STONE_CRAFTING_MATERIALS)
            .unlockedBy("has_item", CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance()))
            .save(HTRuntimeDatapack.RUNTIME_OUTPUT, RagiumAPI.id("runtime_test"))

        event.addRepositorySource { consumer: Consumer<Pack> ->
            consumer.accept(HTRuntimeDatapack.PACK)
            LOGGER.info("Registered runtime datapack!")
        }
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("Loaded common setup!")
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            event.registerFluidType(fluid, fluid.typeHolder)
        }

        LOGGER.info("Registered client extensions!")
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

        fun <T> registerHandlers(supplier: Supplier<BlockEntityType<T>>) where T : BlockEntity, T : HTBlockEntityHandlerProvider {
            val type: BlockEntityType<T> = supplier.get()
            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getItemHandler,
            )
            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getFluidHandler,
            )
            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getEnergyStorage,
            )
        }

        registerHandlers(RagiumBlockEntityTypes.DRUM)

        LOGGER.info("Registered capabilities!")
    }

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

        modifyAll(RagiumItems.MATERIALS, DataComponentPatch.Builder::material)

        LOGGER.info("Modified item components!")
    }
}

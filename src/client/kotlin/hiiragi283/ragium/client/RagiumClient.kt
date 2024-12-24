package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.render.HTMultiblockMachineBlockEntityRenderer
import hiiragi283.ragium.api.render.HTMultiblockPatternRendererRegistry
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTItemVariantStack
import hiiragi283.ragium.client.gui.HTFluidFilterScreen
import hiiragi283.ragium.client.gui.HTItemFilterScreen
import hiiragi283.ragium.client.gui.machine.*
import hiiragi283.ragium.client.machine.HTBlockTagPatternRenderer
import hiiragi283.ragium.client.machine.HTSimpleBlockPatternRenderer
import hiiragi283.ragium.client.machine.HTTieredBlockPatternRenderer
import hiiragi283.ragium.client.model.HTFluidCubeModel
import hiiragi283.ragium.client.model.HTProcessorMachineModel
import hiiragi283.ragium.client.renderer.*
import hiiragi283.ragium.common.block.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.machine.HTBlockTagPattern
import hiiragi283.ragium.common.machine.HTSimpleBlockPattern
import hiiragi283.ragium.common.machine.HTTieredBlockPattern
import hiiragi283.ragium.common.network.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.particle.ParticleType
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerBlocks()
        registerEntities()
        registerFluids()
        registerItems()
        registerScreens()
        registerEvents()
        registerNetworks()
        registerPattern()

        RagiumAPI.LOGGER.info("Ragium-Client initialized!")
    }

    //    Blocks    //

    @JvmStatic
    private fun registerBlocks() {
        // cutout
        buildList {
            add(RagiumBlocks.Creatives.CRATE)
            add(RagiumBlocks.Creatives.EXPORTER)
            addAll(RagiumBlocks.WhiteLines.entries)
            addAll(RagiumBlocks.Glasses.entries)

            addAll(RagiumBlocks.Ores.entries)
            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Exporters.entries)
            addAll(RagiumBlocks.Pipes.entries)
            addAll(RagiumBlocks.CrossPipes.entries)
            addAll(RagiumBlocks.PipeStations.entries)
            addAll(RagiumBlocks.FilteringPipes.entries)
            addAll(RagiumBlocks.Crates.entries)
        }.map(HTBlockContent::get).forEach(::registerCutoutMipped)

        RagiumAPI
            .getInstance()
            .machineRegistry.blocks
            .forEach(::registerCutoutMipped)

        registerCutoutMipped(
            RagiumBlocks.ITEM_DISPLAY,
            RagiumBlocks.POROUS_NETHERRACK,
        )
        // block entity renderer
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.BEDROCK_MINER) { HTBedrockMinerBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.CRATE, ::HTCrateBlockEntityRenderer)
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.CREATIVE_CRATE) { HTCreativeCrateBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.EXTENDED_PROCESSOR) { HTExtendedProcessorBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ITEM_DISPLAY) { HTItemDisplayBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.MANUAL_FORGE) { HTManualForgeBlockEntityRenderer }

        registerMachineRenderer(RagiumBlockEntityTypes.DISTILLATION_TOWER)
        registerMachineRenderer(RagiumBlockEntityTypes.FLUID_DRILL)
        registerMachineRenderer(RagiumBlockEntityTypes.LARGE_PROCESSOR)
        registerMachineRenderer(RagiumBlockEntityTypes.MULTI_SMELTER)

        ColorProviderRegistry.BLOCK.register({ state: BlockState, _: BlockRenderView?, _: BlockPos?, _: Int ->
            state.getOrNull(RagiumBlockProperties.COLOR)?.fireworkColor ?: -1
        }, RagiumBlocks.BACKPACK_INTERFACE.get())
    }

    @JvmStatic
    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
    }

    @JvmStatic
    private fun registerCutoutMipped(vararg contents: HTBlockContent) {
        contents.map(HTBlockContent::get).forEach { block: Block ->
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
        }
    }

    @JvmStatic
    private fun <T : HTMachineBlockEntityBase> registerMachineRenderer(type: BlockEntityType<T>) {
        BlockEntityRendererFactories.register(type) { HTMultiblockMachineBlockEntityRenderer }
    }

    //    Entities    //

    @JvmStatic
    private fun registerEntities() {
        RagiumEntityTypes.DYNAMITES.forEach { entityType: EntityType<out ThrownItemEntity> ->
            EntityRendererRegistry.register(entityType, ::FlyingItemEntityRenderer)
        }
    }

    //    Fluids    //

    @JvmStatic
    private fun registerFluids() {
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            val type: RagiumFluids.TextureType = fluid.type
            FluidRenderHandlerRegistry.INSTANCE.register(
                fluid.get(),
                SimpleFluidRenderHandler(
                    type.stillTex,
                    type.floatingTex,
                    type.overTex,
                    fluid.color.rgb,
                ),
            )
        }
    }

    //    Items    //

    @JvmStatic
    private fun registerItems() {
        ColorProviderRegistry.ITEM.register({ stack: ItemStack, _: Int ->
            stack.get(RagiumComponentTypes.COLOR)?.entityColor ?: -1
        }, RagiumItems.BACKPACK.get())
    }

    //    Screens    //

    @JvmStatic
    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.CHEMICAL_MACHINE, ::HTChemicalMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.DISTILLATION_TOWER, ::HTDistillationTowerScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.LARGE_MACHINE, ::HTLargeMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.SIMPLE_MACHINE, ::HTSimpleMachineScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.SMALL_MACHINE, ::HTSmallMachineScreen)
    }

    //    Events    //

    @JvmStatic
    private fun registerEvents() {
        UseItemCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (world.isClient) {
                if (HTItemFilterScreen.openScreen(stack, world)) {
                    return@register TypedActionResult.success(stack, true)
                }
                if (HTFluidFilterScreen.openScreen(stack, world)) {
                    return@register TypedActionResult.success(stack, true)
                }
            }
            TypedActionResult.pass(stack)
        }

        ItemTooltipCallback.EVENT.register {
                stack: ItemStack,
                context: Item.TooltipContext,
                tooltipType: TooltipType,
                lines: MutableList<Text>,
            ->
            stack.get(RagiumComponentTypes.ITEM_FILTER)?.let(::itemFilterText)?.let(lines::add)
            stack.get(RagiumComponentTypes.FLUID_FILTER)?.let(::fluidFilterText)?.let(lines::add)
        }

        ModelLoadingPlugin.register { context: ModelLoadingPlugin.Context ->
            // register block state resolver
            /*RagiumAPI.getInstance().machineRegistry.entryMap.forEach { (_: HTMachineKey, entry: HTMachineRegistry.Entry) ->
                val block: HTMachineBlock = entry.block
                context.registerBlockStateResolver(block) { context: BlockStateResolver.Context ->
                    Properties.HORIZONTAL_FACING.values.forEach { direction: Direction ->
                        RagiumBlockProperties.ACTIVE.values.forEach { isActive: Boolean ->
                            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                                val state: BlockState = block.getTierState(tier)
                                    .with(Properties.HORIZONTAL_FACING, direction)
                                    .with(RagiumBlockProperties.ACTIVE, isActive)
                                val modelId: Identifier = when (isActive) {
                                    true -> HTMachinePropertyKeys.ACTIVE_MODEL_ID
                                    false -> HTMachinePropertyKeys.MODEL_ID
                                }.let(entry::getOrDefault)
                                val variant = ModelVariant(
                                    modelId,
                                    ModelRotation
                                        .get(
                                            0,
                                            when (direction) {
                                                Direction.SOUTH -> 180
                                                Direction.WEST -> 270
                                                Direction.EAST -> 90
                                                else -> 0
                                            },
                                        ).rotation,
                                    false,
                                    1,
                                )
                                val model = WeightedUnbakedModel(listOf(variant))
                                context.setModel(state, model)
                            }
                        }
                    }
                }
            }*/
            // register item model resolver
            context.modifyModelOnLoad().register onLoad@{ original: UnbakedModel, _: ModelModifier.OnLoad.Context ->
                when {
                    RagiumAPI.id("block/dynamic_processor") in original.modelDependencies -> HTProcessorMachineModel.INACTIVE
                    RagiumAPI.id("block/active_dynamic_processor") in original.modelDependencies -> HTProcessorMachineModel.ACTIVE
                    RagiumAPI.id("item/fluid_cube") in original.modelDependencies -> HTFluidCubeModel
                    else -> original
                }
            }
            RagiumAPI.LOGGER.info("Loaded runtime models!")
        }
        ItemTooltipCallback.EVENT.register(
            RagiumAPI.id("description"),
        ) { stack: ItemStack, _: Item.TooltipContext, _: TooltipType, tooltips: MutableList<Text> ->
            // rework warning
            if (stack.contains(RagiumComponentTypes.REWORK_TARGET)) {
                tooltips.add(Text.literal("This content may be updated or REMOVED!").formatted(Formatting.DARK_RED))
            }
            // descriptions
            val texts: List<Text> = buildList {
                stack.get(RagiumComponentTypes.DESCRIPTION)?.let(this::addAll)
                ContainerItemContext.withConstant(stack).find(EnergyStorage.ITEM)?.let { storage: EnergyStorage ->
                    add(Text.literal("- Stored Energy: ${longText(storage.amount).string} E (${storage.energyPercent} %)"))
                }
            }
            if (texts.isEmpty()) return@register
            if (Screen.hasControlDown()) {
                texts.map(Text::copy).map { it.formatted(Formatting.AQUA) }.forEach(tooltips::add)
            } else {
                tooltips.add(Text.translatable(RagiumTranslationKeys.PRESS_CTRL).formatted(Formatting.YELLOW))
            }
        }
    }

    //    Networks    //

    @JvmStatic
    private fun registerNetworks() {
        RagiumNetworks.CRATE_PREVIEW.registerClientReceiver { payload: HTCratePreviewPayload, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, stack: HTItemVariantStack) = payload
            (context.getBlockEntity(pos) as? HTCrateBlockEntity)?.itemStorage?.variantStack = stack
        }

        RagiumNetworks.FLOATING_ITEM.registerClientReceiver { payload: HTFloatingItemPayload, context: ClientPlayNetworking.Context ->
            val (stack: ItemStack, particle: RegistryEntry<ParticleType<*>>, soundEvent: RegistryEntry<SoundEvent>) = payload
            val client: MinecraftClient = context.client()
            val player: ClientPlayerEntity = context.player()
            (particle.value() as? SimpleParticleType)?.let { type: SimpleParticleType ->
                client.particleManager.addEmitter(player, type, 30)
            }
            context.world?.playSound(
                player.x,
                player.y,
                player.z,
                soundEvent.value(),
                player.soundCategory,
                1.0F,
                1.0F,
                false,
            )
            client.gameRenderer.showFloatingItem(stack)
        }

        RagiumNetworks.FLUID_SYNC.registerClientReceiver { payload: HTFluidSyncPayload, context: ClientPlayNetworking.Context ->
            val (index: Int, stack: HTFluidVariantStack) = payload
            val screen: Screen = context.client().currentScreen ?: return@registerClientReceiver
            if (screen is HTMachineScreenBase<*>) {
                screen.fluidCache[index] = stack.variant
                screen.amountCache[index] = stack.amount
            }
        }

        RagiumNetworks.ITEM_SYNC.registerClientReceiver { payload: HTItemSyncPayload, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int, stack: ItemStack) = payload
            val inventory: Inventory = (context.getBlockEntity(pos) as? HTMachineBlockEntityBase)?.asInventory()
                ?: return@registerClientReceiver
            when {
                stack.isEmpty -> inventory.removeStack(slot)
                else -> inventory.setStack(slot, stack)
            }
        }

        RagiumNetworks.MACHINE_SYNC.registerClientReceiver { payload: HTMachineKeySyncPayload, context: ClientPlayNetworking.Context ->
            (context.getBlockEntity(payload.pos) as? HTMachineBlockEntityBase)?.onPacketReceived(payload)
        }
    }

    //    Machine    //

    private fun registerPattern() {
        HTMultiblockPatternRendererRegistry.register(
            HTSimpleBlockPattern::class.java,
            HTSimpleBlockPatternRenderer,
        )
        HTMultiblockPatternRendererRegistry.register(
            HTBlockTagPattern::class.java,
            HTBlockTagPatternRenderer,
        )
        HTMultiblockPatternRendererRegistry.register(
            HTTieredBlockPattern::class.java,
            HTTieredBlockPatternRenderer,
        )
    }
}

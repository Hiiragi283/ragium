package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.energyPercent
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.longText
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.client.extension.getBlockEntity
import hiiragi283.ragium.client.extension.registerClientReceiver
import hiiragi283.ragium.client.extension.world
import hiiragi283.ragium.client.gui.HTFluidFilterScreen
import hiiragi283.ragium.client.gui.HTItemFilterScreen
import hiiragi283.ragium.client.gui.machine.*
import hiiragi283.ragium.client.model.HTFluidCubeModel
import hiiragi283.ragium.client.model.HTProcessorMachineModel
import hiiragi283.ragium.client.renderer.*
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTFluidSyncPayload
import hiiragi283.ragium.common.network.HTInventoryPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
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
import net.minecraft.client.render.model.ModelRotation
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.ModelVariant
import net.minecraft.client.render.model.json.WeightedUnbakedModel
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.particle.ParticleType
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
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

        RagiumAPI.LOGGER.info("Ragium-Client initialized!")
    }

    //    Blocks    //

    @JvmStatic
    private fun registerBlocks() {
        // cutout
        buildList {
            addAll(RagiumContents.Ores.entries)
            addAll(RagiumContents.Grates.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Exporters.entries)
            addAll(RagiumContents.Pipes.entries)
            addAll(RagiumContents.CrossPipes.entries)
            addAll(RagiumContents.PipeStations.entries)
            addAll(RagiumContents.FilteringPipe.entries)
        }.map(HTContent<Block>::value).forEach(::registerCutoutMipped)

        RagiumAPI
            .getInstance()
            .machineRegistry.blocks
            .forEach(::registerCutoutMipped)

        registerCutoutMipped(RagiumBlocks.CREATIVE_EXPORTER)
        registerCutoutMipped(RagiumBlocks.CROSS_WHITE_LINE)
        registerCutoutMipped(RagiumBlocks.ITEM_DISPLAY)
        registerCutoutMipped(RagiumBlocks.POROUS_NETHERRACK)
        registerCutoutMipped(RagiumBlocks.RAGIUM_GLASS)
        registerCutoutMipped(RagiumBlocks.STEEL_GLASS)
        registerCutoutMipped(RagiumBlocks.T_WHITE_LINE)
        registerCutoutMipped(RagiumBlocks.WHITE_LINE)
        // block entity renderer
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.BEDROCK_MINER) { HTBedrockMinerBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.MANUAL_FORGE) { HTManualForgeBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ITEM_DISPLAY) { HTItemDisplayBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.LARGE_PROCESSOR) { HTLargeProcessorBlockEntityRenderer }

        registerMachineRenderer(RagiumBlockEntityTypes.BLAST_FURNACE)
        registerMachineRenderer(RagiumBlockEntityTypes.DISTILLATION_TOWER)
        registerMachineRenderer(RagiumBlockEntityTypes.FLUID_DRILL)
        registerMachineRenderer(RagiumBlockEntityTypes.MULTI_SMELTER)
        registerMachineRenderer(RagiumBlockEntityTypes.SAW_MILL)

        ColorProviderRegistry.BLOCK.register({ state: BlockState, _: BlockRenderView?, _: BlockPos?, _: Int ->
            state.getOrNull(RagiumBlockProperties.COLOR)?.fireworkColor ?: -1
        }, RagiumBlocks.BACKPACK_INTERFACE)
    }

    @JvmStatic
    private fun registerCutout(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
    }

    @JvmStatic
    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
    }

    @JvmStatic
    private fun <T : HTMachineBlockEntityBase> registerMachineRenderer(type: BlockEntityType<T>) {
        BlockEntityRendererFactories.register(type) { HTMultiblockMachineBlockEntityRenderer }
    }

    //    Entities    //

    @JvmStatic
    private fun registerEntities() {
        EntityRendererRegistry.register(RagiumEntityTypes.DYNAMITE, ::FlyingItemEntityRenderer)
    }

    //    Fluids    //

    @JvmStatic
    private fun registerFluids() {
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            FluidRenderHandlerRegistry.INSTANCE.register(
                fluid.value,
                SimpleFluidRenderHandler(
                    Identifier.of("block/white_concrete"),
                    Identifier.of("block/white_concrete"),
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
        }, RagiumItems.BACKPACK)
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

        ModelLoadingPlugin.register { context: ModelLoadingPlugin.Context ->
            // register block state resolver
            RagiumAPI.getInstance().machineRegistry.entryMap.forEach { (_: HTMachineKey, entry: HTMachineRegistry.Entry) ->
                entry.blocks.forEach { block: HTMachineBlock ->
                    context.registerBlockStateResolver(block) { context: BlockStateResolver.Context ->
                        Properties.HORIZONTAL_FACING.values.forEach { direction: Direction ->
                            RagiumBlockProperties.ACTIVE.values.forEach { isActive: Boolean ->
                                val state: BlockState = block.defaultState
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
            }
            // register item model resolver
            context.resolveModel().register { context1: ModelResolver.Context ->
                val id: Identifier = context1.id() ?: return@register null
                if (id.namespace != RagiumAPI.MOD_ID) return@register null
                val item: Item = Registries.ITEM.get(id.withPath { it.removePrefix("item/") })
                item.components
                    .get(HTMachineKey.COMPONENT_TYPE)
                    ?.let(HTMachineKey::entry)
                    ?.getOrDefault(HTMachinePropertyKeys.MODEL_ID)
                    ?.let { modelId: Identifier ->
                        when (modelId) {
                            RagiumAPI.id("block/dynamic_processor") -> HTProcessorMachineModel.INACTIVE
                            RagiumAPI.id("block/active_dynamic_processor") -> HTProcessorMachineModel.ACTIVE
                            else -> context1.getOrLoadModel(modelId)
                        }
                    }
            }
            context.modifyModelOnLoad().register onLoad@{ original: UnbakedModel, _: ModelModifier.OnLoad.Context ->
                when {
                    RagiumAPI.id("block/dynamic_processor") in original.modelDependencies -> HTProcessorMachineModel.INACTIVE
                    RagiumAPI.id("block/active_dynamic_processor") in original.modelDependencies -> HTProcessorMachineModel.ACTIVE
                    RagiumAPI.id("item/fluid_cube") in original.modelDependencies -> HTFluidCubeModel
                    else -> original
                }
            }
            context.addModels(
                RagiumAPI.id("block/generator"),
                RagiumAPI.id("block/solar_generator"),
            )
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
            val (index: Int, variant: FluidVariant, amount: Long) = payload
            val screen: Screen = context.client().currentScreen ?: return@registerClientReceiver
            if (screen is HTMachineScreenBase<*>) {
                screen.fluidCache[index] = variant
                screen.amountCache[index] = amount
            }
        }

        RagiumNetworks.ITEM_SYNC.registerClientReceiver { payload: HTInventoryPayload, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int, stack: ItemStack) = payload
            val inventory: Inventory = (context.getBlockEntity(pos) as? HTMachineBlockEntityBase)?.asInventory()
                ?: return@registerClientReceiver
            when {
                stack.isEmpty -> inventory.removeStack(slot)
                else -> inventory.setStack(slot, stack)
            }
        }

        RagiumNetworks.MACHINE_SYNC.registerClientReceiver { payload: HTMachinePacket, context: ClientPlayNetworking.Context ->
            (context.getBlockEntity(payload.pos) as? HTMachineBlockEntityBase)?.onPacketReceived(payload)
        }
    }
}

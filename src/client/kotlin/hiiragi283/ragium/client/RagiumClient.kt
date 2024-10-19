package hiiragi283.ragium.client

import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.isModLoaded
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.client.gui.HTGeneratorScreen
import hiiragi283.ragium.client.gui.HTGenericScreen
import hiiragi283.ragium.client.gui.HTProcessorScreen
import hiiragi283.ragium.client.integration.accessories.RagiumAccessoriesInit
import hiiragi283.ragium.client.integration.patchouli.RagiumPatchouliInit
import hiiragi283.ragium.client.integration.rei.INPUT_ENTRIES
import hiiragi283.ragium.client.model.HTMachineModel
import hiiragi283.ragium.client.renderer.HTAlchemicalInfuserBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTItemDisplayBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTMetaMachineBlockEntityRenderer
import hiiragi283.ragium.client.renderer.HTOblivionCubeEntityRenderer
import hiiragi283.ragium.client.util.getBlockEntity
import hiiragi283.ragium.client.util.getMachineEntity
import hiiragi283.ragium.client.util.registerClient
import hiiragi283.ragium.client.util.registerClientReceiver
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.machine.HTHeatGeneratorMachineEntity
import hiiragi283.ragium.common.machine.HTProcessorMachineEntity
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTInventoryPayload
import hiiragi283.ragium.common.network.HTMachineRecipePayload
import hiiragi283.ragium.common.network.HTOpenBackpackPayload
import io.wispforest.accessories.api.AccessoriesCapability
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.SpawnEggItem
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import net.minecraft.world.biome.Biome

@Environment(EnvType.CLIENT)
object RagiumClient : ClientModInitializer, HTMachineTypeInitializer {
    override fun onInitializeClient() {
        RagiumKeyBinds

        registerBlocks()
        registerEntities()
        registerItems()
        registerScreens()
        registerEvents()
        registerNetworks()

        RagiumAccessoriesInit.init()
        RagiumPatchouliInit.init()

        RagiumAPI.log { info("Ragium-Client initialized!") }
    }

    //    Blocks    //

    private fun registerBlocks() {
        // cutout
        registerCutout(RagiumBlocks.ITEM_DISPLAY)
        RagiumContents.Crops.entries
            .map(RagiumContents.Crops::cropBlock)
            .forEach(::registerCutout)

        RagiumContents.Element.entries
            .map(RagiumContents.Element::clusterBlock)
            .forEach(::registerCutout)
        // cutout mipped
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutoutMipped(),
            RagiumBlocks.POROUS_NETHERRACK,
            RagiumBlocks.OBLIVION_CLUSTER,
            RagiumBlocks.META_MACHINE,
        )

        RagiumContents.Ores
            .entries
            .map(RagiumContents.Ores::value)
            .forEach(::registerCutoutMipped)

        RagiumFluids.PETROLEUM.registerClient(Identifier.of("block/black_concrete"))

        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ALCHEMICAL_INFUSER) { HTAlchemicalInfuserBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.ITEM_DISPLAY) { HTItemDisplayBlockEntityRenderer }
        BlockEntityRendererFactories.register(RagiumBlockEntityTypes.META_MACHINE) { HTMetaMachineBlockEntityRenderer }

        ColorProviderRegistry.BLOCK.register({ state: BlockState, _: BlockRenderView?, _: BlockPos?, _: Int ->
            state.getOrNull(RagiumBlockProperties.COLOR)?.mapColor?.color ?: -1
        }, RagiumBlocks.BACKPACK_INTERFACE)
    }

    private fun registerCutout(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
    }

    private fun registerCutoutMipped(block: Block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped())
    }

    //    Entities    //

    private fun registerEntities() {
        EntityRendererRegistry.register(RagiumEntityTypes.REMOVER_DYNAMITE, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(RagiumEntityTypes.DYNAMITE, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(RagiumEntityTypes.OBLIVION_CUBE, ::HTOblivionCubeEntityRenderer)

        EntityModelLayerRegistry.registerModelLayer(HTOblivionCubeEntityRenderer.ENTITY_MODEL_LAYER) {
            val modelData = ModelData()
            modelData.root.addChild(
                EntityModelPartNames.CUBE,
                ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F),
                ModelTransform.pivot(0F, 0F, 0F),
            )
            TexturedModelData.of(modelData, 64, 64)
        }
    }

    //    Items    //

    private fun registerItems() {
        RagiumContents.Fluids.entries.forEach { fluid: RagiumContents.Fluids ->
            ColorProviderRegistry.ITEM.register({ _: ItemStack, _: Int -> fluid.color.rgb }, fluid)
        }

        ColorProviderRegistry.ITEM.register({ stack: ItemStack, _: Int ->
            stack.get(RagiumComponentTypes.COLOR)?.entityColor ?: -1
        }, RagiumContents.Misc.BACKPACK)
    }

    //    Screens    //

    private fun registerScreens() {
        HandledScreens.register(RagiumScreenHandlerTypes.GENERATOR, ::HTGeneratorScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.PROCESSOR, ::HTProcessorScreen)
        HandledScreens.register(RagiumScreenHandlerTypes.ALCHEMICAL_INFUSER, ::HTGenericScreen)
    }

    //    Events    //

    private fun registerEvents() {
        ModelLoadingPlugin.register { context: ModelLoadingPlugin.Context ->
            // register item model resolver
            context.modifyModelOnLoad().register onLoad@{ original: UnbakedModel, _: ModelModifier.OnLoad.Context ->
                when (HTMachineModel.MODEL_ID) {
                    in original.modelDependencies -> HTMachineModel
                    else -> original
                }
            }
        }

        ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
            while (RagiumKeyBinds.OPEN_BACKPACK.wasPressed()) {
                val capability: AccessoriesCapability = client.player?.accessoriesCapability() ?: break
                if (capability.isEquipped(RagiumContents.Misc.BACKPACK.asItem())) {
                    ClientPlayNetworking.send(HTOpenBackpackPayload.ENDER)
                }
            }
        }
    }

    //    Networks    //

    private fun registerNetworks() {
        RagiumNetworks.FLOATING_ITEM.registerClientReceiver { payload: HTFloatingItemPayload, context: ClientPlayNetworking.Context ->
            context.client().gameRenderer.showFloatingItem(payload.stack)
        }

        RagiumNetworks.MACHINE_RECIPE.registerClientReceiver { payload: HTMachineRecipePayload, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, recipe: HTMachineRecipe) = payload
            (context.getMachineEntity(pos) as? HTProcessorMachineEntity)?.currentRecipe = recipe
            RagiumAPI.log { info("Received recipe!") }
        }

        RagiumNetworks.SET_STACK.registerClientReceiver { payload: HTInventoryPayload.Setter, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int, stack: ItemStack) = payload
            (context.getBlockEntity(pos) as? Inventory)?.setStack(slot, stack)
        }

        RagiumNetworks.REMOVE_STACK.registerClientReceiver { payload: HTInventoryPayload.Remover, context: ClientPlayNetworking.Context ->
            val (pos: BlockPos, slot: Int) = payload
            (context.getBlockEntity(pos) as? Inventory)?.removeStack(slot)
        }
    }

    //    HTMachineTypeInitializer    //

    override val priority: Int = -100

    override fun modifyProperties(helper: HTMachineTypeInitializer.Helper) {
        helper.modify(RagiumMachineTypes.HEAT_GENERATOR) {
            set(HTMachinePropertyKeys.DYNAMIC_FRONT_TEX) { machine: HTMachineEntity ->
                val generator: HTHeatGeneratorMachineEntity? = machine as? HTHeatGeneratorMachineEntity
                RagiumAPI.log { info("Burning Time; ${generator?.burningTime}") }
                when ((machine as? HTHeatGeneratorMachineEntity)?.isBurning == true) {
                    true -> "block/heat_generator_front_active"
                    false -> "block/heat_generator_front"
                }.let(RagiumAPI.Companion::id)
            }
        }

        if (!isModLoaded("roughlyenoughitems")) return
        helper.modify(RagiumMachineTypes.FLUID_DRILL) {
            set(INPUT_ENTRIES) { recipe: HTMachineRecipe ->
                recipe
                    .get(HTRecipeComponentTypes.BIOME)
                    ?.let { biome: RegistryKey<Biome> ->
                        EntryStacks.of(Items.COMPASS).tooltip {
                            listOf(
                                Text
                                    .translatable(RagiumTranslationKeys.REI_RECIPE_BIOME, biome.value)
                                    .formatted(Formatting.YELLOW),
                            )
                        }
                    }?.let(EntryIngredient::of)
                    ?.let(::listOf)
                    ?: emptyList()
            }
        }
        helper.modify(RagiumMachineTypes.MOB_EXTRACTOR) {
            set(INPUT_ENTRIES) { recipe: HTMachineRecipe ->
                recipe
                    .get(HTRecipeComponentTypes.ENTITY_TYPE)
                    ?.let(SpawnEggItem::forEntity)
                    ?.let(EntryIngredients::of)
                    ?.let(::listOf)
                    ?: emptyList()
            }
        }
    }
}

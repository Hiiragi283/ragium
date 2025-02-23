package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.extension.asServerLevel
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMoltenFluidIngredient
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.common.block.machine.HTMachineBlock
import hiiragi283.ragium.common.capability.fluid.HTDivingGoggleFluidHandler
import hiiragi283.ragium.common.capability.fluid.HTFluidCubeFluidHandler
import hiiragi283.ragium.common.capability.fluid.HTJetpackFluidHandler
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.network.HTPotionBundlePacket
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import org.slf4j.Logger
import java.util.function.Supplier

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
internal object RagiumModEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(CommonMaterials.ALUMINUM, HTMaterialType.METAL)
        event.register(CommonMaterials.ANTIMONY, HTMaterialType.METAL)
        event.register(CommonMaterials.BERYLLIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.ASH, HTMaterialType.DUST)
        event.register(CommonMaterials.BAUXITE, HTMaterialType.MINERAL)
        event.register(CommonMaterials.BRASS, HTMaterialType.ALLOY)
        event.register(CommonMaterials.BRONZE, HTMaterialType.ALLOY)
        event.register(CommonMaterials.CADMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.CALCITE, HTMaterialType.DUST)
        event.register(CommonMaterials.CARBON, HTMaterialType.DUST)
        event.register(CommonMaterials.CHROMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.COAL_COKE, HTMaterialType.GEM)
        event.register(CommonMaterials.CRYOLITE, HTMaterialType.GEM)
        event.register(CommonMaterials.ELECTRUM, HTMaterialType.ALLOY)
        event.register(CommonMaterials.FLUORITE, HTMaterialType.GEM)
        event.register(CommonMaterials.INVAR, HTMaterialType.ALLOY)
        event.register(CommonMaterials.IRIDIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.LEAD, HTMaterialType.METAL)
        event.register(CommonMaterials.NICKEL, HTMaterialType.METAL)
        event.register(CommonMaterials.NIOBIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.OSMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PERIDOT, HTMaterialType.GEM)
        event.register(CommonMaterials.PLATINUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PLUTONIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.RUBY, HTMaterialType.GEM)
        event.register(CommonMaterials.SALT, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SALTPETER, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SAPPHIRE, HTMaterialType.GEM)
        event.register(CommonMaterials.SILICON, HTMaterialType.METAL)
        event.register(CommonMaterials.SILVER, HTMaterialType.METAL)
        event.register(CommonMaterials.STAINLESS_STEEL, HTMaterialType.ALLOY)
        event.register(CommonMaterials.STEEL, HTMaterialType.ALLOY)
        event.register(CommonMaterials.SULFUR, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SUPERCONDUCTOR, HTMaterialType.METAL)
        event.register(CommonMaterials.TIN, HTMaterialType.METAL)
        event.register(CommonMaterials.TITANIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.TUNGSTEN, HTMaterialType.METAL)
        event.register(CommonMaterials.URANIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.WOOD, HTMaterialType.DUST)
        event.register(CommonMaterials.ZINC, HTMaterialType.METAL)

        event.register(RagiumMaterials.CRIMSON_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.DEEP_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.ECHORIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.FIERY_COAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.RAGI_ALLOY, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.RAGI_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterials.RAGIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.WARPED_CRYSTAL, HTMaterialType.GEM)

        event.register(VanillaMaterials.AMETHYST, HTMaterialType.GEM)
        event.register(VanillaMaterials.COAL, HTMaterialType.GEM)
        event.register(VanillaMaterials.COPPER, HTMaterialType.METAL)
        event.register(VanillaMaterials.DIAMOND, HTMaterialType.GEM)
        event.register(VanillaMaterials.EMERALD, HTMaterialType.GEM)
        event.register(VanillaMaterials.GOLD, HTMaterialType.METAL)
        event.register(VanillaMaterials.IRON, HTMaterialType.METAL)
        event.register(VanillaMaterials.LAPIS, HTMaterialType.GEM)
        event.register(VanillaMaterials.NETHERITE, HTMaterialType.ALLOY)
        event.register(VanillaMaterials.NETHERITE_SCRAP, HTMaterialType.GEM)
        event.register(VanillaMaterials.QUARTZ, HTMaterialType.GEM)
        event.register(VanillaMaterials.REDSTONE, HTMaterialType.MINERAL)

        event.register(IntegrationMaterials.BLACK_QUARTZ, HTMaterialType.GEM)

        event.register(IntegrationMaterials.COPPER_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.ENERGETIC_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.VIBRANT_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.REDSTONE_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.CONDUCTIVE_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.PULSATING_ALLOY, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.DARK_STEEL, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.SOULARIUM, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.END_STEEL, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.PULSATING_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.VIBRANT_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.ENDER_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.ENTICING_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.WEATHER_CRYSTAL, HTMaterialType.GEM)
        event.register(IntegrationMaterials.PRESCIENT_CRYSTAL, HTMaterialType.GEM)

        event.register(IntegrationMaterials.DARK_GEM, HTMaterialType.GEM)

        event.register(IntegrationMaterials.REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        event.register(IntegrationMaterials.CARMINITE, HTMaterialType.GEM)
        event.register(IntegrationMaterials.FIERY_METAL, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.IRONWOOD, HTMaterialType.METAL)
        event.register(IntegrationMaterials.KNIGHTMETAL, HTMaterialType.METAL)
        event.register(IntegrationMaterials.STEELEAF, HTMaterialType.METAL)
    }

    @JvmStatic
    internal lateinit var blockMap: Map<HTMachineType, DeferredBlock<*>>

    @SubscribeEvent
    fun registerMachineBlocks(event: RegisterEvent) {
        // Block
        event.register(Registries.BLOCK) { helper: RegisterEvent.RegisterHelper<Block> ->
            blockMap = HTMachineType.entries.associateWith { type: HTMachineType ->
                val block = HTMachineBlock(
                    type,
                    blockProperty()
                        .mapColor(MapColor.STONE)
                        .strength(2f)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
                        .noOcclusion(),
                )
                val id: ResourceLocation = RagiumAPI.id(type.serializedName)
                helper.register(id, block)
                DeferredBlock.createBlock<Block>(id)
            }
            LOGGER.info("Registered machine blocks!")
        }
        // Item
        event.register(Registries.ITEM) { helper: RegisterEvent.RegisterHelper<Item> ->
            blockMap.forEach { (_, holder: DeferredBlock<*>) ->
                helper.register(holder.id, BlockItem(holder.get(), itemProperty()))
            }
        }
        // Recipe Serializer
        event.register(Registries.RECIPE_SERIALIZER) { helper: RegisterEvent.RegisterHelper<RecipeSerializer<*>> ->
            HTRecipeTypes.ALL_TYPES.forEach { type: HTRecipeType<out HTMachineRecipeBase> ->
                helper.register(RagiumAPI.id(type.toString()), type.serializer)
            }
        }
        // Recipe Type
        event.register(Registries.RECIPE_TYPE) { helper: RegisterEvent.RegisterHelper<RecipeType<*>> ->
            HTRecipeTypes.ALL_TYPES.forEach { type: HTRecipeType<out HTMachineRecipeBase> ->
                helper.register(RagiumAPI.id(type.toString()), type)
            }
            LOGGER.info("Added machine recipe types!")
        }

        // Fluid Ingredient
        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper: RegisterEvent.RegisterHelper<FluidIngredientType<*>> ->
            helper.register(RagiumAPI.id("molten_metal"), HTMoltenFluidIngredient.TYPE)
        }
    }

    @SubscribeEvent
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        // from HTBlockEntityHandlerProvider
        fun <T> registerHandlers(supplier: Supplier<BlockEntityType<T>>) where T : BlockEntity, T : HTHandlerBlockEntity {
            val type: BlockEntityType<T> = supplier.get()
            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                type,
                HTHandlerBlockEntity::getItemHandler,
            )
            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                type,
                HTHandlerBlockEntity::getFluidHandler,
            )
            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                HTHandlerBlockEntity::getEnergyStorage,
            )
        }

        registerHandlers(RagiumBlockEntityTypes.MANUAL_GRINDER)
        registerHandlers(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE)

        registerHandlers(RagiumBlockEntityTypes.FISHER)

        registerHandlers(RagiumBlockEntityTypes.COMBUSTION_GENERATOR)
        registerHandlers(RagiumBlockEntityTypes.STIRLING_GENERATOR)
        registerHandlers(RagiumBlockEntityTypes.THERMAL_GENERATOR)

        registerHandlers(RagiumBlockEntityTypes.ASSEMBLER)
        registerHandlers(RagiumBlockEntityTypes.BLAST_FURNACE)
        registerHandlers(RagiumBlockEntityTypes.COMPRESSOR)
        registerHandlers(RagiumBlockEntityTypes.EXTRACTOR)
        registerHandlers(RagiumBlockEntityTypes.GRINDER)
        registerHandlers(RagiumBlockEntityTypes.INFUSER)
        registerHandlers(RagiumBlockEntityTypes.LASER_ASSEMBLY)
        registerHandlers(RagiumBlockEntityTypes.MIXER)
        registerHandlers(RagiumBlockEntityTypes.MULTI_SMELTER)
        registerHandlers(RagiumBlockEntityTypes.REFINERY)

        registerHandlers(RagiumBlockEntityTypes.CRATE)
        registerHandlers(RagiumBlockEntityTypes.DRUM)
        registerHandlers(RagiumBlockEntityTypes.SLAG_COLLECTOR)

        // Other
        event.registerBlock(
            Capabilities.EnergyStorage.BLOCK,
            { level: Level, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction ->
                level.asServerLevel()?.let(RagiumAPI.getInstance().getEnergyNetwork())
            },
            RagiumBlocks.ENERGY_NETWORK_INTERFACE.get(),
        )

        LOGGER.info("Registered Block Capabilities!")
    }

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        fun registerFluid(vararg items: ItemLike, transform: (ItemStack, Int) -> IFluidHandlerItem?) {
            event.registerItem(
                Capabilities.FluidHandler.ITEM,
                { stack: ItemStack, _: Void? ->
                    val enchLevel: Int =
                        stack.getLevel(RagiumAPI.getInstance().getCurrentLookup(), RagiumEnchantments.CAPACITY)
                    transform(stack, RagiumAPI.getInstance().getTankCapacityWithEnch(enchLevel))
                },
                *items,
            )
        }

        registerFluid(RagiumBlocks.COPPER_DRUM) { stack: ItemStack, capacity: Int ->
            FluidHandlerItemStack(RagiumComponentTypes.FLUID_CONTENT, stack, capacity)
        }

        registerFluid(RagiumItems.DIVING_GOGGLE, transform = ::HTDivingGoggleFluidHandler)
        registerFluid(RagiumItems.JETPACK, transform = ::HTJetpackFluidHandler)

        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            ::HTFluidCubeFluidHandler,
            RagiumItems.EMPTY_FLUID_CUBE,
            RagiumItems.WATER_FLUID_CUBE,
            RagiumItems.LAVA_FLUID_CUBE,
        )

        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? -> FluidBucketWrapper(stack) },
            RagiumItems.CRUDE_OIL_BUCKET,
            RagiumItems.HONEY_BUCKET,
        )

        LOGGER.info("Registered Item Capabilities!")
    }

    @SubscribeEvent
    fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMaps.DEFOLIANT)
        event.register(RagiumDataMaps.SOAP)

        LOGGER.info("Registered Data Map Types!")
    }

    @SubscribeEvent
    fun registerNetworking(event: RegisterPayloadHandlersEvent) {
        val register: PayloadRegistrar = event.registrar(RagiumAPI.MOD_ID)
        register.playToServer(
            HTPotionBundlePacket.TYPE,
            HTPotionBundlePacket.STREAM_CODEC,
            HTPotionBundlePacket::onReceived,
        )

        LOGGER.info("Registered C2S Networking!")
    }
}

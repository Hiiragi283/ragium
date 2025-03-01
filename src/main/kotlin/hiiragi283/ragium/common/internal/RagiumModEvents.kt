package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.asServerLevel
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.network.HTPotionBundlePacket
import hiiragi283.ragium.common.storage.fluid.HTDivingGoggleFluidHandler
import hiiragi283.ragium.common.storage.fluid.HTFluidCubeFluidHandler
import hiiragi283.ragium.common.storage.fluid.HTJetpackFluidHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import org.slf4j.Logger
import java.util.function.Supplier

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
internal object RagiumModEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun onRegister(event: RegisterEvent) {
        // Recipe Serializer
        event.register(Registries.RECIPE_SERIALIZER) { helper: RegisterEvent.RegisterHelper<RecipeSerializer<*>> ->
            HTRecipeTypes.ALL_TYPES.forEach { type: HTRecipeType<out HTMachineRecipe> ->
                helper.register(RagiumAPI.id(type.toString()), type.serializer)
            }
        }
        // Recipe Type
        event.register(Registries.RECIPE_TYPE) { helper: RegisterEvent.RegisterHelper<RecipeType<*>> ->
            HTRecipeTypes.ALL_TYPES.forEach { type: HTRecipeType<out HTMachineRecipe> ->
                helper.register(RagiumAPI.id(type.toString()), type)
            }
            LOGGER.info("Added machine recipe types!")
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
        registerHandlers(RagiumBlockEntityTypes.BREWERY)
        registerHandlers(RagiumBlockEntityTypes.COMPRESSOR)
        registerHandlers(RagiumBlockEntityTypes.CRUSHER)
        registerHandlers(RagiumBlockEntityTypes.EXTRACTOR)
        registerHandlers(RagiumBlockEntityTypes.GRINDER)
        registerHandlers(RagiumBlockEntityTypes.INFUSER)
        registerHandlers(RagiumBlockEntityTypes.LASER_ASSEMBLY)
        registerHandlers(RagiumBlockEntityTypes.MIXER)
        registerHandlers(RagiumBlockEntityTypes.MULTI_SMELTER)
        registerHandlers(RagiumBlockEntityTypes.REFINERY)
        registerHandlers(RagiumBlockEntityTypes.SOLIDIFIER)

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
        fun <T : Any> withCapacity(capability: ItemCapability<T, Void?>, vararg items: ItemLike, transform: (ItemStack, Int) -> T?) {
            event.registerItem(
                capability,
                { stack: ItemStack, _: Void? ->
                    val enchLevel: Int =
                        stack.getLevel(RagiumAPI.getInstance().getRegistryAccess(), RagiumEnchantments.CAPACITY)
                    transform(stack, RagiumAPI.getInstance().getTankCapacityWithEnch(enchLevel))
                },
                *items,
            )
        }

        // Fluid
        // withCapacity(Capabilities.FluidHandler.ITEM, RagiumBlocks.COPPER_DRUM, transform = ::HTItemFluidHandler)
        withCapacity(
            Capabilities.FluidHandler.ITEM,
            RagiumItems.DIVING_GOGGLE,
            transform = ::HTDivingGoggleFluidHandler,
        )
        withCapacity(Capabilities.FluidHandler.ITEM, RagiumItems.JETPACK, transform = ::HTJetpackFluidHandler)

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

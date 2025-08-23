package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.container.type.HTMenuType
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTDeferredMenuTypeRegister
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEngraverBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSmelterBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotConfigurationMenu
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.resources.ResourceLocation

typealias DeferredBEMenu<BE> = HTDeferredMenuType<HTBlockEntityContainerMenu<BE>, BE>

typealias DeferredBEMenu1 = HTDeferredMenuType<HTBlockEntityContainerMenu<HTMachineBlockEntity>, HTMachineBlockEntity>

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTDeferredMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    inline fun <reified BE : HTBlockEntity> register(name: String): HTDeferredMenuType<HTBlockEntityContainerMenu<BE>, BE> {
        val holder: HTDeferredMenuType<HTBlockEntityContainerMenu<BE>, BE> =
            HTDeferredMenuType.createType(RagiumAPI.id(name))
        REGISTER.register(name) { _: ResourceLocation ->
            HTMenuType.blockEntity(HTBlockEntityContainerMenu.create(holder))
        }
        return holder
    }

    @JvmField
    val DRUM: DeferredBEMenu<HTDrumBlockEntity> = register("drum")

    @JvmField
    val SLOT_CONFIG: HTDeferredMenuType<HTSlotConfigurationMenu, HTMachineBlockEntity> =
        REGISTER.registerType("slot_configuration", ::HTSlotConfigurationMenu)

    //    Generator    //

    @JvmField
    val FUEL_GENERATOR: DeferredBEMenu<HTFuelGeneratorBlockEntity> = register("fuel_generator")

    //    Machine    //

    @JvmField
    val ALLOY_SMELTER: DeferredBEMenu<HTAlloySmelterBlockEntity> = register("alloy_smelter")

    @JvmField
    val COMPRESSOR: DeferredBEMenu<HTCompressorBlockEntity> = register("compressor")

    @JvmField
    val CRUSHER: DeferredBEMenu<HTCrusherBlockEntity> = register("crusher")

    @JvmField
    val ENERGY_NETWORK_ACCESS: DeferredBEMenu<HTEnergyNetworkAccessBlockEntity> = register("energy_network_access")

    @JvmField
    val ENGRAVER: DeferredBEMenu<HTEngraverBlockEntity> = register("engraver")

    @JvmField
    val EXTRACTOR: DeferredBEMenu<HTExtractorBlockEntity> = register("extractor")

    @JvmField
    val FLUID_COLLECTOR: DeferredBEMenu<HTFluidCollectorBlockEntity> = register("fluid_collector")

    @JvmField
    val INFUSER: DeferredBEMenu1 = register("infuser")

    @JvmField
    val ITEM_BUFFER: DeferredBEMenu<HTItemBufferBlockEntity> = register("item_buffer")

    @JvmField
    val MELTER: DeferredBEMenu<HTMelterBlockEntity> = register("melter")

    @JvmField
    val MIXER: DeferredBEMenu<HTMixerBlockEntity> = register("mixer")

    @JvmField
    val PULVERIZER: DeferredBEMenu<HTPulverizerBlockEntity> = register("pulverizer")

    @JvmField
    val REFINERY: DeferredBEMenu<HTRefineryBlockEntity> = register("refinery")

    @JvmField
    val SMELTER: DeferredBEMenu<HTSmelterBlockEntity> = register("smelter")

    @JvmField
    val SINGLE_ITEM: DeferredBEMenu<HTBlockBreakerBlockEntity> = register("single_item")

    @JvmField
    val SOLIDIFIER: DeferredBEMenu1 = register("solidifier")
}

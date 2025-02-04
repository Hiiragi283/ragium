package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTFluidContent
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.awt.Color

enum class RagiumFluids(
    val color: Color,
    val enName: String,
    val jaName: String,
    private val textureType: TextureType = TextureType.LIQUID,
) : HTFluidContent,
    IClientFluidTypeExtensions {
    // Vanilla
    HONEY("Honey", "蜂蜜", TextureType.HONEY),
    SNOW("Powder Snow", "粉雪", TextureType.SNOW),
    EXPERIENCE(Color(0x99cc00), "Liquid Experience", "液体経験値"),

    // Natural Resources
    AIR(Color(0xccffff), "Air", "空気", TextureType.GASEOUS),
    BRINE(Color(0x003399), "Brine", "塩水"),

    // Elements
    HYDROGEN(Color(0x0000cc), "Hydrogen", "水素", TextureType.GASEOUS),
    NITROGEN(Color(0x66cccc), "Nitrogen", "窒素", TextureType.GASEOUS),
    CHLORINE(Color(0xccff33), "Chlorine", "塩素", TextureType.GASEOUS),

    // Non-organic Chemical Compounds
    STEAM("Steam", "蒸気", TextureType.GASEOUS),

    CARBON_MONOXIDE(Color(0x6699cc), "Carbon Monoxide", "一酸化炭素", TextureType.GASEOUS),
    CARBON_DIOXIDE(Color(0x99ccff), "Carbon Dioxide", "二酸化炭素", TextureType.GASEOUS),

    LIQUID_NITROGEN(Color(0x66cccc), "Liquid Nitrogen", "液体窒素"),
    AMMONIA(Color(0x9999ff), "Ammonia", "アンモニア", TextureType.GASEOUS),
    NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
    MIXTURE_ACID(Color(0xff9900), "Mixture Acid", "混酸"),

    HYDROGEN_FLUORIDE(Color(0x33cccc), "Hydrogen Fluoride", "フッ化水素", TextureType.GASEOUS),
    HYDROFLUORIC_ACID(Color(0x33ccff), "Hydrofluoric Acid", "フッ化水素酸"),

    ALKALI_SOLUTION(Color(0x000099), "Alkali Solution", "アルカリ溶液"),
    SODIUM_SILICATE(Color(0x00cc99), "Sodium Silicate", "ケイ酸ナトリウム"),

    SULFURIC_ACID(Color(0xff3300), "Blaze Acid", "ブレイズ酸", TextureType.STICKY),

    HYDROGEN_CHLORIDE(Color(0xccff66), "Hydrogen Chloride", "塩化水素", TextureType.GASEOUS),
    HYDROCHLORIC_ACID(Color(0xccff99), "Hydrochloric Acid", "塩酸"),
    AQUA_REGIA(Color(0xffff00), "Aqua Regia", "王水"),

    ALUMINA_SOLUTION(Color(0xcccccc), "Alumina Solution", "アルミナ溶液"),

    CHEMICAL_SLUDGE(Color(0x333366), "Chemical Sludge", "化学汚泥", TextureType.STICKY),

    // Oil
    CRUDE_OIL(Color(0x000000), "Crude Oil", "原油", TextureType.STICKY),
    NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
    FUEL(Color(0xcc6633), "Fuel", "燃料"),
    ETHANOL(Color(0x99ffff), "Ethanol", "エタノール"),

    // Natural Gas
    NATURAL_GAS(Color(0xcccccc), "Natural Gas", "天然ガス", TextureType.GASEOUS),
    METHANE(Color(0x990099), "Methane", "メタン", TextureType.GASEOUS),
    ETHENE(Color(0xffccff), "Ethene", "エテン", TextureType.GASEOUS),
    ACETYLENE(Color(0xffffcc), "Acetylene", "アセチレン", TextureType.GASEOUS),

    // Bio
    PLANT_OIL(Color(0x99cc33), "Plant Oil", "植物油"),
    GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),
    BIOMASS(Color(0x006600), "Biomass", "バイオマス", TextureType.STICKY),
    BIODIESEL(Color(0x99ff00), "Biodiesel", "バイオディーゼル"),

    // Saps
    SAP(Color(0x996633), "Sap", "樹液", TextureType.STICKY),
    CRIMSON_SAP(Color(0x660000), "Crimson Sap", "深紅の樹液", TextureType.STICKY),
    WARPED_SAP(Color(0x006666), "Warped Sap", "歪んだ樹液", TextureType.STICKY),

    // Fuels
    NITRO_FUEL(Color(0xff33333), "Nitro Fuel", "ニトロ燃料"),

    // Ragium
    RAGIUM_SOLUTION(Color(0x330000), "Ragium Solution", "ラギウム溶液"),
    DISTILLED_RAGIUM_SOLUTION(Color(0x990033), "Distilled Ragium Solution", "蒸留ラギウム溶液"),
    REFINED_RAGIUM_SOLUTION(Color(0xff0033), "RefinedRagium Solution", "精製ラギウム溶液"),
    DESTABILIZED_RAGIUM_SOLUTION(Color(0xff6666), "Destabilized Ragium Solution", "励起ラギウム溶液"),
    ;

    constructor(enName: String, jaName: String, type: TextureType = TextureType.LIQUID) : this(
        Color.WHITE,
        enName,
        jaName,
        type,
    )

    companion object {
        @JvmField
        val REGISTER: DeferredRegister<Fluid> = DeferredRegister.create(Registries.FLUID, RagiumAPI.MOD_ID)

        @JvmField
        val TYPE_REGISTER: DeferredRegister<FluidType> =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, RagiumAPI.MOD_ID)

        @JvmStatic
        fun register(eventBus: IEventBus) {
            RagiumFluids.entries.forEach { fluid: RagiumFluids ->
                // Fluid Type
                TYPE_REGISTER.register(fluid.serializedName) { _: ResourceLocation -> FluidType(FluidType.Properties.create()) }
                // Fluid
                REGISTER.register(fluid.serializedName) { _: ResourceLocation ->
                    BaseFlowingFluid.Source(
                        BaseFlowingFluid.Properties(
                            fluid.typeHolder,
                            fluid.fluidHolder,
                            fluid.fluidHolder,
                        ),
                    )
                }
            }

            TYPE_REGISTER.register(eventBus)
            REGISTER.register(eventBus)
        }
    }

    private val id: ResourceLocation = RagiumAPI.id(serializedName)

    override val fluidHolder: DeferredHolder<Fluid, FlowingFluid> = DeferredHolder.create(Registries.FLUID, id)
    override val typeHolder: DeferredHolder<FluidType, FluidType> =
        DeferredHolder.create(NeoForgeRegistries.Keys.FLUID_TYPES, id)

    //    TextureType    //

    private enum class TextureType(
        val stillTex: ResourceLocation = ResourceLocation.withDefaultNamespace("block/bone_block_side"),
        val floatingTex: ResourceLocation = stillTex,
        val overTex: ResourceLocation? = null,
    ) {
        GASEOUS(ResourceLocation.withDefaultNamespace("block/white_concrete")),
        HONEY(ResourceLocation.withDefaultNamespace("block/honey_block_top")),
        LIQUID,
        SNOW(ResourceLocation.withDefaultNamespace("block/snow")),
        STICKY(ResourceLocation.withDefaultNamespace("block/quartz_block_bottom")),
    }

    //    ClientExtensions    //

    override fun getStillTexture(): ResourceLocation = textureType.stillTex

    override fun getFlowingTexture(): ResourceLocation = textureType.floatingTex

    override fun getOverlayTexture(): ResourceLocation? = textureType.overTex

    override fun getTintColor(): Int = color.rgb

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}

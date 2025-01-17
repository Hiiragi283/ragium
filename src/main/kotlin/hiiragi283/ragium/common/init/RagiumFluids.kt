package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidHolder
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.awt.Color
import java.util.function.Supplier

enum class RagiumFluids(
    val color: Color,
    val enName: String,
    val jaName: String,
    val type: TextureType = TextureType.LIQUID,
) : Supplier<FlowingFluid>,
    IClientFluidTypeExtensions,
    StringRepresentable {
    // Vanilla
    HONEY("Honey", "蜂蜜", TextureType.HONEY),
    EXPERIENCE(Color(0x99cc00), "Liquid Experience", "液体経験値"),

    // Organics
    PLANT_OIL(Color(0x99cc33), "Plant Oil", "植物油"),

    BIOMASS(Color(0x006600), "Biomass", "バイオマス", TextureType.STICKY),
    GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),

    SAP(Color(0x996633), "Sap", "樹液", TextureType.STICKY),
    CRIMSON_SAP(Color(0x660000), "Crimson Sap", "深紅の樹液", TextureType.STICKY),
    WARPED_SAP(Color(0x006666), "Warped Sap", "歪んだ樹液", TextureType.STICKY),

    // Natural Resources
    AIR(Color(0xccffff), "Air", "空気", TextureType.GASEOUS),
    BRINE(Color(0x003399), "Brine", "塩水"),
    CRUDE_OIL(Color(0x000000), "Crude Oil", "原油", TextureType.STICKY),

    // Elements
    HYDROGEN(Color(0x0000cc), "Hydrogen", "水素", TextureType.GASEOUS),
    NITROGEN(Color(0x66cccc), "Nitrogen", "窒素", TextureType.GASEOUS),

    CHLORINE(Color(0xccff33), "Chlorine", "塩素", TextureType.GASEOUS),

    // Non-organic Chemical Compounds
    CARBON_MONOXIDE(Color(0x99ccff), "Carbon Monoxide", "一酸化炭素", TextureType.GASEOUS),
    CARBON_DIOXIDE(Color(0x99ccff), "Carbon Dioxide", "二酸化炭素", TextureType.GASEOUS),

    LIQUID_NITROGEN(Color(0x66cccc), "Liquid Nitrogen", "液体窒素"),
    AMMONIA(Color(0x9999ff), "Ammonia", "アンモニア", TextureType.GASEOUS),
    NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
    MIXTURE_ACID(Color(0xff9900), "Mixture Acid", "混酸"),

    HYDROGEN_FLUORIDE(Color(0x33cccc), "Hydrogen Fluoride", "フッ化水素", TextureType.GASEOUS),

    ALKALI_SOLUTION(Color(0x000099), "Alkali Solution", "アルカリ溶液"),

    SULFUR_DIOXIDE(Color(0xff6600), "Sulfur Dioxide", "二酸化硫黄", TextureType.STICKY),
    SULFURIC_ACID(Color(0xff3300), "Sulfuric Acid", "硫酸", TextureType.STICKY),

    HYDROCHLORIC_ACID(Color(0xccff99), "Hydrochloric Acid", "塩酸"),
    HYDROGEN_CHLORIDE(Color(0xccff66), "Hydrogen Chloride", "塩化水素"),
    AQUA_REGIA(Color(0xffff00), "Aqua Regia", "王水"),

    ALUMINA_SOLUTION(Color(0xcccccc), "Alumina Solution", "アルミナ溶液"),

    CHEMICAL_SLUDGE(Color(0x333366), "Chemical Sludge", "化学汚泥", TextureType.STICKY),
    CHLOROSILANE(Color(0xcccccc), "Chlorosilane", "塩化ケイ素", TextureType.GASEOUS),

    // Oil products
    REFINED_GAS(Color(0xcccccc), "Refined Gas", "精製ガス", TextureType.GASEOUS),
    NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
    RESIDUAL_OIL(Color(0x663300), "Residual Oil", "残渣油", TextureType.STICKY),

    ALCOHOL(Color(0x99ffff), "Alcohol", "アルコール"),
    AROMATIC_COMPOUNDS(Color(0x666699), "Aromatic Compounds", "芳香族化合物"),
    NOBLE_GAS(Color(0xff00ff), "Noble Gas", "希ガス", TextureType.GASEOUS),

    // Fuels
    BIO_FUEL(Color(0x99ff00), "Bio Fuel", "バイオ燃料"),
    FUEL(Color(0xcc6633), "Fuel", "燃料"),
    NITRO_FUEL(Color(0xff33333), "Nitro Fuel", "ニトロ燃料"),

    // Explosives
    NITRO_GLYCERIN(Color(0x99cc66), "Nitroglycerin", "ニトログリセリン", TextureType.EXPLOSIVE),
    TRINITROTOLUENE(Color(0x666699), "Trinitrotoluene", "トリニトロトルエン", TextureType.EXPLOSIVE),

    // Radioactive
    URANIUM_HEXAFLUORIDE(Color(0x33ff00), "Uranium Hexafluoride", "六フッ化ウラン", TextureType.RADIOACTIVE),
    ENRICHED_URANIUM_HEXAFLUORIDE(Color(0x33ff00), "Enriched Uranium Hexafluoride", "濃縮六フッ化ウラン"),

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
                    BaseFlowingFluid.Source(fluid.property)
                }
                REGISTER.register("flowing_" + fluid.serializedName) { _: ResourceLocation ->
                    BaseFlowingFluid.Flowing(fluid.property)
                }
                // Block
                RagiumBlocks.REGISTER.registerBlock(fluid.serializedName) { prop: BlockBehaviour.Properties ->
                    LiquidBlock(
                        fluid.stillHolder.get(),
                        prop
                            .noCollission()
                            .strength(100f)
                            .noLootTable()
                            .replaceable()
                            .pushReaction(PushReaction.DESTROY)
                            .liquid(),
                    )
                }
            }

            TYPE_REGISTER.register(eventBus)
            REGISTER.register(eventBus)
        }
    }

    val id: ResourceLocation = RagiumAPI.id(serializedName)

    val typeHolder: DeferredHolder<FluidType, FluidType> =
        DeferredHolder.create(NeoForgeRegistries.Keys.FLUID_TYPES, id)

    val stillHolder: DeferredHolder<Fluid, FlowingFluid> = fluidHolder<FlowingFluid>(id)
    val flowingHolder: DeferredHolder<Fluid, FlowingFluid> = fluidHolder(id.withPrefix("flowing_"))

    val blockHolder: DeferredBlock<out LiquidBlock> = DeferredBlock.createBlock(id)

    val property: BaseFlowingFluid.Properties = BaseFlowingFluid
        .Properties(typeHolder, stillHolder, flowingHolder)
        .bucket(Items::AIR)
        .block(blockHolder)

    val commonTag: TagKey<Fluid> = fluidTagKey(commonId(serializedName))

    //    TextureType    //

    enum class TextureType(
        val stillTex: ResourceLocation = ResourceLocation.withDefaultNamespace("block/bone_block_side"),
        val floatingTex: ResourceLocation = stillTex,
        val overTex: ResourceLocation? = null,
    ) {
        EXPLOSIVE,
        GASEOUS(ResourceLocation.withDefaultNamespace("block/white_concrete")),
        HONEY(ResourceLocation.withDefaultNamespace("block/honey_block_top")),
        LIQUID,
        RADIOACTIVE,
        STICKY(ResourceLocation.withDefaultNamespace("block/quartz_block_bottom")),
    }

    override fun get(): FlowingFluid = stillHolder.get()

    //    ClientExtensions    //

    override fun getStillTexture(): ResourceLocation = type.stillTex

    override fun getFlowingTexture(): ResourceLocation = type.floatingTex

    override fun getOverlayTexture(): ResourceLocation? = type.overTex

    override fun getTintColor(): Int = color.rgb

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}

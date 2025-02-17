package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.awt.Color
import java.util.function.Supplier

enum class RagiumVirtualFluids(val color: Color, val textureType: TextureType = TextureType.LIQUID) :
    StringRepresentable,
    Supplier<Fluid> {
    MUSHROOM_STEW(Color(0xcc9966)),

    // Nitrogen
    NITROGEN(Color(0x33ccff), TextureType.GASEOUS),
    AMMONIA(Color(0x9999ff), TextureType.GASEOUS),
    NITRIC_ACID(Color(0xcc99ff)),
    MIXTURE_ACID(Color(0xff9900)),

    // Oxygen
    OXYGEN(Color(0x66ccff), TextureType.GASEOUS),

    // Sulfur
    SULFUR_DIOXIDE(Color(0xff6600), TextureType.GASEOUS),
    SULFUR_TRIOXIDE(Color(0xff6600), TextureType.GASEOUS),
    SULFURIC_ACID(Color(0xff3300), TextureType.STICKY),
    HYDROFLUORIC_ACID(Color(0xffcc33)),

    // Base
    LAPIS_SOLUTION(Color(0x3333ff)),

    // Oil
    NAPHTHA(Color(0xff9900)),
    FUEL(Color(0xcc6633)),
    NITRO_FUEL(Color(0xff33333)),

    AROMATIC_COMPOUND(Color(0xcc6633), TextureType.STICKY),

    // Bio
    PLANT_OIL(Color(0x99cc33)),
    BIOMASS(Color(0x006600), TextureType.STICKY),
    ETHANOL(Color(0x99ffff)),
    BIODIESEL(Color(0x99ff00)),

    // Saps
    SAP(Color(0x996633), TextureType.STICKY),
    CRIMSON_SAP(Color(0x660000), TextureType.STICKY),
    WARPED_SAP(Color(0x006666), TextureType.STICKY),
    LATEX(Color(0xcccccc), TextureType.STICKY),

    RAGIUM_SOLUTION(Color(0xff003f)),
    ;

    private val id: ResourceLocation = RagiumAPI.id(serializedName)

    val fluidHolder: DeferredHolder<Fluid, FlowingFluid> = DeferredHolder.create(Registries.FLUID, id)
    val typeHolder: DeferredHolder<FluidType, FluidType> =
        DeferredHolder.create(NeoForgeRegistries.Keys.FLUID_TYPES, id)

    //    TextureType    //

    enum class TextureType {
        GASEOUS,
        LIQUID,
        STICKY,
    }

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()

    override fun get(): Fluid = fluidHolder.get()
}

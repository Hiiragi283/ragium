package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredFluid
import hiiragi283.ragium.api.registry.HTDeferredFluidType
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import java.awt.Color
import java.util.function.Supplier

enum class RagiumVirtualFluids(val color: Color, val textureType: TextureType = TextureType.LIQUID) :
    StringRepresentable,
    Supplier<Fluid> {
    EXPERIENCE(Color(0x66ff33)),

    CHOCOLATE(Color(0x663333)),
    MUSHROOM_STEW(Color(0xcc9966)),

    AIR(Color.WHITE, TextureType.GASEOUS),

    // Hydrogen
    HYDROGEN(Color(0x3333ff), TextureType.GASEOUS),

    // Nitrogen
    NITROGEN(Color(0x33ccff), TextureType.GASEOUS),
    AMMONIA(Color(0x9999ff), TextureType.GASEOUS),
    NITRIC_ACID(Color(0xcc99ff)),
    MIXTURE_ACID(Color(0xff9900)),

    // Oxygen
    OXYGEN(Color(0x66ccff), TextureType.GASEOUS),
    ROCKET_FUEL(Color(0x0066ff), TextureType.GASEOUS),

    // Alkali
    ALKALI_SOLUTION(Color(0x0000cc)),

    // Sulfur
    SULFUR_DIOXIDE(Color(0xff6600), TextureType.GASEOUS),
    SULFUR_TRIOXIDE(Color(0xff6600), TextureType.GASEOUS),
    SULFURIC_ACID(Color(0xff3300), TextureType.STICKY),

    // Oil
    NAPHTHA(Color(0xff9900)),
    FUEL(Color(0xcc6633)),
    NITRO_FUEL(Color(0xff33333)),

    AROMATIC_COMPOUND(Color(0xcc6633), TextureType.STICKY),

    // Bio
    PLANT_OIL(Color(0x99cc33)),
    BIOMASS(Color(0x006600), TextureType.STICKY),
    ETHANOL(Color(0x99ffff)),

    CRUDE_BIODIESEL(Color(0x66cc00)),
    BIODIESEL(Color(0x99ff00)),
    GLYCEROL(Color(0xccff66)),
    NITROGLYCERIN(Color(0xff33333)),

    // Saps
    SAP(Color(0x996633), TextureType.STICKY),
    CRIMSON_SAP(Color(0x660000), TextureType.STICKY),
    WARPED_SAP(Color(0x006666), TextureType.STICKY),

    // Other
    RAGIUM_SOLUTION(Color(0xff003f)),
    ;

    private val id: ResourceLocation = RagiumAPI.id(serializedName)

    val fluidHolder: HTDeferredFluid<FlowingFluid> = HTDeferredFluid.createFluid<FlowingFluid>(id)
    val typeHolder: HTDeferredFluidType<FluidType> = HTDeferredFluidType.createType<FluidType>(id)

    val commonTag: TagKey<Fluid> get() = fluidHolder.commonTag

    //    TextureType    //

    enum class TextureType {
        GASEOUS,
        LIQUID,
        MOLTEN,
        STICKY,
    }

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()

    override fun get(): Fluid = fluidHolder.get()
}

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

enum class RagiumVirtualFluids(val color: Color, val isGaseous: Boolean = false) :
    StringRepresentable,
    Supplier<Fluid> {
    EXPERIENCE(Color(0x66ff33)),

    CONDENSED_MILK(Color(0xffcc99)),
    CHOCOLATE(Color(0x663333)),
    MUSHROOM_STEW(Color(0xcc9966)),

    AIR(Color.WHITE, true),

    // Hydrogen
    HYDROGEN(Color(0x3333ff), true),

    // Nitrogen
    NITROGEN(Color(0x33ccff), true),
    AMMONIA(Color(0x9999ff), true),
    NITRIC_ACID(Color(0xcc99ff)),
    MIXTURE_ACID(Color(0xff9900)),

    // Oxygen
    OXYGEN(Color(0x66ccff), true),
    ROCKET_FUEL(Color(0x0066ff), true),

    // Alkali
    ALKALI_SOLUTION(Color(0x0000cc)),

    // Sulfur
    SULFUR_DIOXIDE(Color(0xff6600), true),
    SULFUR_TRIOXIDE(Color(0xff6600), true),
    SULFURIC_ACID(Color(0xff3300)),

    // Oil
    NAPHTHA(Color(0xff9900)),
    FUEL(Color(0xcc6633)),
    NITRO_FUEL(Color(0xff33333)),

    AROMATIC_COMPOUND(Color(0xcc6633)),

    // Bio
    PLANT_OIL(Color(0x99cc33)),
    BIOMASS(Color(0x006600)),
    ETHANOL(Color(0x99ffff)),

    CRUDE_BIODIESEL(Color(0x66cc00)),
    BIODIESEL(Color(0x99ff00)),
    GLYCEROL(Color(0xccff66)),
    NITROGLYCERIN(Color(0xff33333)),

    // Saps
    SAP(Color(0x996633)),
    CRIMSON_SAP(Color(0x660000)),
    WARPED_SAP(Color(0x006666)),

    // Other
    RAGIUM_SOLUTION(Color(0xff003f)),
    ;

    private val id: ResourceLocation = RagiumAPI.id(serializedName)

    val fluidHolder: HTDeferredFluid<FlowingFluid> = HTDeferredFluid.createFluid<FlowingFluid>(id)
    val typeHolder: HTDeferredFluidType<FluidType> = HTDeferredFluidType.createType<FluidType>(id)

    val commonTag: TagKey<Fluid> get() = fluidHolder.commonTag

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()

    override fun get(): Fluid = fluidHolder.get()
}

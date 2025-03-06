package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredFluidType
import hiiragi283.ragium.api.registry.HTFluidTypeRegister
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

object RagiumFluidTypes {
    @JvmField
    val REGISTER = HTFluidTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(name: String, properties: FluidType.Properties): HTDeferredFluidType<FluidType> = REGISTER.registerSimpleType(
        name,
        properties
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY),
    )

    @JvmStatic
    private val DEFAULT_PROPERTY: FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)

    @JvmField
    val GLASS: HTDeferredFluidType<out FluidType> = REGISTER.register("glass") { _: ResourceLocation ->
        object : FluidType(DEFAULT_PROPERTY) {
            override fun getBlockForFluidState(getter: BlockAndTintGetter, pos: BlockPos, state: FluidState): BlockState =
                Blocks.GLASS.defaultBlockState()
        }
    }

    @JvmField
    val HONEY: HTDeferredFluidType<out FluidType> = REGISTER.register("honey") { _: ResourceLocation ->
        object : FluidType(DEFAULT_PROPERTY) {
            override fun getBlockForFluidState(getter: BlockAndTintGetter, pos: BlockPos, state: FluidState): BlockState =
                Blocks.HONEY_BLOCK.defaultBlockState()
        }
    }

    @JvmField
    val SNOW: HTDeferredFluidType<out FluidType> = REGISTER.register("snow") { _: ResourceLocation ->
        object : FluidType(DEFAULT_PROPERTY) {
            override fun getBucket(stack: FluidStack): ItemStack = ItemStack(Items.POWDER_SNOW_BUCKET)

            override fun getBlockForFluidState(getter: BlockAndTintGetter, pos: BlockPos, state: FluidState): BlockState =
                Blocks.POWDER_SNOW.defaultBlockState()
        }
    }

    @JvmField
    val CRUDE_OIL: HTDeferredFluidType<FluidType> = register(
        "crude_oil",
        FluidType.Properties
            .create()
            .descriptionId("block.ragium.crude_oil")
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000),
    )
}

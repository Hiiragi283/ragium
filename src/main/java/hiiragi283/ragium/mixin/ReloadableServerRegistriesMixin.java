package hiiragi283.ragium.mixin;

import hiiragi283.ragium.internal.data.RagiumDynamicServerResources;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ReloadableServerRegistries.class)
public abstract class ReloadableServerRegistriesMixin {
    @Inject(method = "createAndValidateFullContext", at = @At("RETURN"))
    private static void ragium$createAndValidateFullContext(
            LayeredRegistryAccess<RegistryLayer> contextLayers,
            HolderLookup.Provider contextLookupWithUpdatedTags,
            List<WritableRegistry<?>> newRegistries,
            CallbackInfoReturnable<ReloadableServerRegistries.LoadResult> cir) {
        HolderLookup.Provider registries = cir.getReturnValue().lookupWithUpdatedTags();
        RagiumDynamicServerResources.initialize(registries);
    }
}

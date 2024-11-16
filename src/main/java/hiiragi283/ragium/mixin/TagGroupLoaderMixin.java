package hiiragi283.ragium.mixin;

import hiiragi283.ragium.api.RagiumAPI;
import hiiragi283.ragium.api.material.HTMaterialKey;
import hiiragi283.ragium.api.material.HTMaterialRegistry;
import hiiragi283.ragium.api.material.HTTagPrefix;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(TagGroupLoader.class)
public abstract class TagGroupLoaderMixin {

    @Final
    @Shadow
    private String dataType;

    @Inject(method = "loadTags", at = @At("RETURN"), cancellable = true)
    private void ragium$injectMaterialTags(ResourceManager resourceManager, CallbackInfoReturnable<Map<Identifier, List<TagGroupLoader.TrackedEntry>>> cir) {
        if (dataType.endsWith("item")) {
            Map<Identifier, List<TagGroupLoader.TrackedEntry>> map = cir.getReturnValue();
            RagiumAPI.getInstance().getMaterialRegistry().getEntryMap().forEach((HTMaterialKey key, HTMaterialRegistry.Entry entry) -> {
                entry.getItemMap().forEach((HTTagPrefix prefix, Set<Item> items) -> {
                    TagKey<Item> tagKey = prefix.createTag(key);
                    // add tag entries
                    TagEntry tagEntry = TagEntry.createTag(tagKey.id());
                    map.computeIfAbsent(prefix.getCommonTagKey().id(), k -> new ArrayList<>()).add(new TagGroupLoader.TrackedEntry(tagEntry, RagiumAPI.MOD_NAME));
                    // add item entries
                    items.forEach((Item item) -> {
                        Identifier itemId = Registries.ITEM.getId(item);
                        TagEntry itemEntry = TagEntry.create(itemId);
                        map.computeIfAbsent(tagKey.id(), k -> new ArrayList<>()).add(new TagGroupLoader.TrackedEntry(itemEntry, RagiumAPI.MOD_NAME));
                    });
                });
            });
            cir.setReturnValue(map);
        }
    }

}
